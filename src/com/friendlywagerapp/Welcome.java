package com.friendlywagerapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Welcome extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	}
	
	public void onCreateWagerClicked(View v) {
	    Toast.makeText(Welcome.this, "Blah", Toast.LENGTH_SHORT).show();
	}
    
    public void onFindWagerClicked(View v) {
	    Toast.makeText(Welcome.this, "Blah", Toast.LENGTH_SHORT).show();
    }

    public void onLogoutClicked(View v) {
    	// Show logout message and return to Login screen
    	// TODO need to actually logout...
	    Toast.makeText(Welcome.this, "Logout Successful", Toast.LENGTH_SHORT).show();
	    finish();
    }
    
    public void onHelpClicked(View v) {
	    Toast.makeText(Welcome.this, "Blah", Toast.LENGTH_SHORT).show();
    }
}
