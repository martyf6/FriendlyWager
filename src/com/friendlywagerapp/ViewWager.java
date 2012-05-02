package com.friendlywagerapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewWager extends Activity {
	
private static String TAG = "ViewWager";
	
	protected String wagerName;
		
	protected int getTextField () {
		return R.id.viewWager;
	}
	protected int getLayout () {
		return R.layout.view_wager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(getLayout());
	    String vote = getIntent().getExtras().get("vote").toString();
	    wagerName = getIntent().getExtras().get("wagerName").toString();
	    TextView wagerNameLabel = (TextView) findViewById(R.id.viewWagerName);
	    wagerNameLabel.setText(wagerName);
	    String wagerLocation = getIntent().getExtras().get("wagerLocation").toString();
	    String wagerTime = getIntent().getExtras().get("wagerTime").toString();
	    TextView wagerDetails = (TextView) findViewById(R.id.viewWagerDetails);
	    wagerDetails.setText(wagerTime + ", in " + wagerLocation);
	    final EditText voteField = (EditText) findViewById(getTextField());
	    voteField.setText(vote);
	}
	
	public void onVoteClicked (View v){
		// do the voting
		final EditText voteField = (EditText) findViewById(getTextField());
	    String vote = voteField.getText().toString();
	    JSONObject result = FriendlyWagerServer.voteOnWager(wagerName, vote);
	    if (result == null) Toast.makeText(ViewWager.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
	    else {
	    	try {
	    		String success = result.getString("success");
				if (success.equals("false")){
					String error = result.getString("error");
					Log.i(TAG,error);
					Toast.makeText(ViewWager.this, error, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ViewWager.this, "Vote successful", Toast.LENGTH_SHORT).show();
					finish();
				}
	    	} catch (JSONException e) {
	    		Toast.makeText(ViewWager.this, "Cannot contact FriendlyWager server", Toast.LENGTH_SHORT).show();
	    		Log.e(TAG, "Error parsing FriendlyWager response.\n" + e.toString());
	    	}
	    }
		finishedVoting();
	}
	
	protected void finishedVoting() {
		finish();
	}
}
