package com.friendlywagerapp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Hex;
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
import org.json.JSONException;
import org.json.JSONObject;

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
	    final EditText usernameField = (EditText) findViewById(R.id.usernameText);
	    String username = usernameField.getText().toString();
	    final EditText passwordField = (EditText) findViewById(R.id.passwordText);
	    String password = passwordField.getText().toString();
	    // encrypt the password
        try {
        	MessageDigest cript = MessageDigest.getInstance("SHA-1");
            cript.reset();
			cript.update(password.getBytes("utf8"));
			String encryptedPassword = new String(Hex.encodeHex(cript.digest()));
			loginToDatabase(username, encryptedPassword);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
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
			result = httpclient.execute(httppost, responseHandler);
	        
			/* parse json data */
			try {
				JSONObject json_data = new JSONObject(result);
				String success = json_data.getString("success");
				if (success.equals("false")){
					String error = json_data.getString("error");
					error = error.substring(2, error.length()-2); // strip out array representation
					Log.i(TAG,error);
					Toast.makeText(FriendlyWager.this, error, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FriendlyWager.this, "Login successful", Toast.LENGTH_SHORT).show();
					Intent goToWelcomePage = new Intent(FriendlyWager.this, Welcome.class);
					startActivity(goToWelcomePage);
				}
			} catch (JSONException e) {
				Toast.makeText(FriendlyWager.this, e.toString(), Toast.LENGTH_SHORT).show();
				System.out.println("Error parsing data " + e.toString());
			}
		} catch (Exception e) {
			Toast.makeText(FriendlyWager.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
}