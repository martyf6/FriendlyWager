package com.friendlywagerapp;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		Intent goToNewWagerPage = new Intent(Welcome.this, NewWager.class);
		startActivity(goToNewWagerPage);
	}
    
    public void onFindWagerClicked(View v) {
    	Intent goToShowWagersPage = new Intent(Welcome.this, ShowWagers.class);
		startActivity(goToShowWagersPage);
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
}
