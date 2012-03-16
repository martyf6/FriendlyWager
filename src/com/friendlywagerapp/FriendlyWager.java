package com.friendlywagerapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FriendlyWager extends Activity {
	/** Called when the activity is first created. */

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
   }
   
   public void onLoginClicked(View v) {
	    final EditText emailField = (EditText) findViewById(R.id.emailText);
	    String email = emailField.getText().toString();
	    final EditText passwordField = (EditText) findViewById(R.id.passwordText);
	    String password = passwordField.getText().toString();
	    String msg = "email: " + email + "\npassword:" + password;
	    Toast.makeText(FriendlyWager.this, msg, Toast.LENGTH_SHORT).show();
	    loginToDatabase(email, password);
	}
   
   public void onRegisterClicked(View v) {
	   // For now just go to welcome screen
	   Intent goToWelcomePage = new Intent(FriendlyWager.this, Welcome.class);
	   startActivity(goToWelcomePage);
   }
   
	private void loginToDatabase(String username, String password) {
		String result = "";
		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://friendlywagerapp.com/login.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				result = sb.toString();
			} catch (Exception e) {
				System.out.println("Error converting result " + e.toString());
			}

			// parse json data
			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					System.out.println("id: " + json_data.getInt("id")
							+ ", name: " + json_data.getString("name")
							+ ", sex: " + json_data.getInt("sex")
							+ ", birthyear: " + json_data.getInt("birthyear"));
				}
			} catch (JSONException e) {
				System.out.println("Error parsing data " + e.toString());
			}
		} catch (Exception e) {
			System.out.println("Error in http connection " + e.toString());
		}
	}
}