package com.friendlywagerapp;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewWager extends Activity {
/** Called when the activity is first created. */
	
	private static String TAG = "NewWager";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.new_wager);
	}
	
	public void onFinishClicked(View v) {
		final EditText eventNameField = (EditText) findViewById(R.id.newWagerName);
	    String eventName = eventNameField.getText().toString();
	    final EditText locationField = (EditText) findViewById(R.id.newWagerLocation);
	    String location = locationField.getText().toString();
	    final EditText timeField = (EditText) findViewById(R.id.newWagerTime);
	    String time = timeField.getText().toString();
	    try {
			JSONObject result = FriendlyWagerServer.createWager(eventName, location, time);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(NewWager.this, error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(NewWager.this, "successful", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(NewWager.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	    finish();
	}
    
    public void onCancelClicked(View v) {
    	finish();
    }
}
