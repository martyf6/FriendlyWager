package com.friendlywagerapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ViewWagerAdmin extends ViewWager {
	
	private static String TAG = "ViewWagerAdmin";
	
	protected int getTextField () {
		return R.id.viewWagerAdmin;
	}
	protected int getLayout () {
		return R.layout.view_wager_admin;
	}
	
	public void onInviteClicked(View v) {
		final EditText usernameField = (EditText) findViewById(R.id.viewWagerInviteText);		
		String username = usernameField.getText().toString();
		JSONObject result = FriendlyWagerServer.inviteUser(wagerName, username);
	    if (result == null) Toast.makeText(ViewWagerAdmin.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
	    else {
	    	try {
	    		String success = result.getString("success");
				if (success.equals("false")){
					String error = result.getString("error");
					Log.i(TAG,error);
					Toast.makeText(ViewWagerAdmin.this, error, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ViewWagerAdmin.this, "Login successful", Toast.LENGTH_SHORT).show();
					finish();
				}
	    	} catch (JSONException e) {
	    		Toast.makeText(ViewWagerAdmin.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
	    		Log.e(TAG, "Error parsing FriendlyWager response.\n" + e.toString());
	    	}
	    }
    }


}
