package com.friendlywagerapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FriendlyWager extends Activity {
	/** Called when the activity is first created. */
	
	private static final String TAG = "FriendlyWager";

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
	    Log.d(TAG,msg);
	    Toast.makeText(FriendlyWager.this, msg, Toast.LENGTH_SHORT).show();
	    loginToDatabase(email, password);
	}
   
   public void onRegisterClicked(View v) {
	   // For now just go to welcome screen
	   Intent goToWelcomePage = new Intent(FriendlyWager.this, Welcome.class);
	   startActivity(goToWelcomePage);
   }
   
   public static void main(String[] args){
	   //loginToDatabase("test","test");
   }
   
	private void loginToDatabase(String username, String password) {
		String result = "";
		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("submit", "Login"));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("remember","0"));

		// http post
		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpClient httpclient = new DefaultHttpClient(params);
			HttpPost httppost = new HttpPost("http://friendlywagerapp.com/session.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);
			Toast.makeText(FriendlyWager.this, response, Toast.LENGTH_SHORT).show();

			/* parse json data
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
			}*/
		} catch (Exception e) {
			Toast.makeText(FriendlyWager.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
}