package com.friendlywagerapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	/** Called when the activity is first created. */
	
	private static final String TAG = "FriendlyWager";

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
   }
   
   public void onLoginClicked(View v) {
	    final EditText usernameField = (EditText) findViewById(R.id.usernameText);
	    String username = usernameField.getText().toString();
	    final EditText passwordField = (EditText) findViewById(R.id.passwordText);
	    String password = passwordField.getText().toString();
	    if (username.length() == 0 || password.length() == 0){
	    	Toast.makeText(Login.this, "Both a username and password must be supplied", Toast.LENGTH_SHORT).show();
	    } else {
		    JSONObject result = FriendlyWagerServer.login(username, password);
		    if (result == null) Toast.makeText(Login.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
		    else {
		    	try {
		    		String success = result.getString("success");
					if (success.equals("false")){
						String error = result.getString("error");
						Log.i(TAG,error);
						Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
						Intent goToWelcomePage = new Intent(Login.this, Welcome.class);
						startActivity(goToWelcomePage);
					}
		    	} catch (JSONException e) {
		    		Toast.makeText(Login.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
		    		Log.e(TAG, "Error parsing FriendlyWager response.\n" + e.toString());
		    	}
		    }
	    }
	}
   
   public void onRegisterClicked(View v) {
	   Intent goToRegisterPage = new Intent(Login.this, Register.class);
	   startActivity(goToRegisterPage);
   }
}