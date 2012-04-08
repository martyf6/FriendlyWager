package com.friendlywagerapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	
	private static String TAG = "Register";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.register);
	}
	
	public void onRegisterButtonClicked(View v) {
		// validate the info
		final EditText usernameField = (EditText) findViewById(R.id.registerUsernameText);
	    String username = usernameField.getText().toString();
	    final EditText passwordField = (EditText) findViewById(R.id.registerPasswordText);
	    String password = passwordField.getText().toString();
	    final EditText passwordField2 = (EditText) findViewById(R.id.registerPassword2Text);
	    String password2 = passwordField2.getText().toString();
	    final EditText emailField = (EditText) findViewById(R.id.registerEmailText);
	    String email = emailField.getText().toString();
	    if (username.length() == 0 || password.length() == 0 || password2.length() == 0 || email.length() == 0){
	    	Toast.makeText(Register.this, "Fields may not be left blank", Toast.LENGTH_SHORT).show();
	    } else if (!password.equals(password2)) {
	    	Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();	
	    } else {
	    	Pattern p = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*\\.[a-zA-Z]*");
	    	Matcher m = p.matcher(email);
	    	if (!m.matches()) {
	    		Toast.makeText(Register.this, "A valid email address must be supplied", Toast.LENGTH_SHORT).show();	
	    	} else {
	    		// POST to register.php script
	    		JSONObject result = FriendlyWagerServer.register(username, password, email);
	    		if (result == null) Toast.makeText(Register.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
			    else {
			    	try {
			    		String success = result.getString("success");
						if (success.equals("false")){
							// alert if there are issues
							String error = result.getString("error");
							Log.i(TAG,error);
							Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
						} else {
							// return to login page if there are none (registration is successful)
							Toast.makeText(Register.this, "Registration was successful", Toast.LENGTH_SHORT).show();
							Intent goToLoginPage = new Intent(Register.this, Login.class);
							startActivity(goToLoginPage);
						}
			    	} catch (JSONException e) {
			    		Toast.makeText(Register.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
			    		Log.e(TAG, "Error parsing FriendlyWager response.\n" + e.toString());
			    	}
			    }
	    	}
	    }
	}

}
