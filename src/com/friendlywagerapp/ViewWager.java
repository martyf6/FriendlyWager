package com.friendlywagerapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class ViewWager extends Activity {
	
private static String TAG = "ShowWager";
	
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
	    Log.i(TAG,(getLayout() == R.layout.view_wager_admin) + "");
	    setContentView(getLayout());
	    String vote = getIntent().getExtras().get("vote").toString();
	    wagerName = getIntent().getExtras().get("wagerName").toString();
	    final EditText voteField = (EditText) findViewById(getTextField());
	    voteField.setText(vote);
	}
}
