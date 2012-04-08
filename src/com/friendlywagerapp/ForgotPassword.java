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

public class ForgotPassword extends Activity {
	
	private static final String TAG = "ForgotPassword";

	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.forgot_password);
	   }
	   
	   public void onForgotPasswordClicked(View v) {
		    final EditText usernameField = (EditText) findViewById(R.id.forgotPasswordUsernameText);
		    String username = usernameField.getText().toString();
		    JSONObject result = FriendlyWagerServer.forgotPassword(username);
		    if (result == null) Toast.makeText(ForgotPassword.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
		    else {
		    	try {
		    		String success = result.getString("success");
					if (success.equals("false")){
						// alert if there are issues
						String error = result.getString("error");
						Log.i(TAG,error);
						Toast.makeText(ForgotPassword.this, error, Toast.LENGTH_SHORT).show();
					} else {
						// return to login page if there are none (registration is successful)
						Toast.makeText(ForgotPassword.this, "A new password has been sent to the email registered to your account", Toast.LENGTH_SHORT).show();
						Intent goToLoginPage = new Intent(ForgotPassword.this, Login.class);
						startActivity(goToLoginPage);
					}
		    	} catch (JSONException e) {
		    		Toast.makeText(ForgotPassword.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
		    		Log.e(TAG, "Error parsing FriendlyWager response.\n" + e.toString());
		    	}
		    }
	   }

}
