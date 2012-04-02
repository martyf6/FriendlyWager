package com.friendlywagerapp;

import java.util.List;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Welcome extends Activity {
	/** Called when the activity is first created. */
	
	private static String TAG = "Welcome";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	}
	
	public void onCreateWagerClicked(View v) {
	    newWager();
	}
    
    public void onFindWagerClicked(View v) {
	    getWagers();
    }

    public void onLogoutClicked(View v) {
    	// create a new cookie store (overwriting the old one)
    	FriendlyWagerContext context = FriendlyWagerContext.getInstance();
    	context.getHttpContext().setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
    	finish();
    }
    
    public void onHelpClicked(View v) {
	    Toast.makeText(Welcome.this, "Blah", Toast.LENGTH_SHORT).show();
    }
    
    // test function for ensuring server-side scripting is working.
    private void newWager() {
		try {
			JSONObject result = FriendlyWagerServer.createWager("Will someone blackout on Thursday?", "Madison", "Thursday, May 3rd 2012");
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(Welcome.this, error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Welcome.this, "successful", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(Welcome.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
    
    private void getWagers() {
    	try {
			JSONArray response = FriendlyWagerServer.getWagers();
			JSONObject result = response.getJSONObject(0);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(Welcome.this, error, Toast.LENGTH_SHORT).show();
			} else {
				JSONArray wagersObj = response.getJSONArray(1);
				List<FriendlyWager> wagers = FriendlyWager.createWagers(wagersObj);
				String eventNames = "";
				for (FriendlyWager wager : wagers) {
					eventNames += wager.getName() + ":";
					Log.i(TAG,"Found wager: " + wager.getName());
				}
				if (eventNames.equals("")) eventNames = "No events found for user";
				Toast.makeText(Welcome.this, eventNames, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(Welcome.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
    }
}
