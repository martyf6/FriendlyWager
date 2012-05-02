package com.friendlywagerapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ViewClosedWager extends Activity {
private static String TAG = "ViewClosedWager";
	
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
	    wagerDetails.setText(wagerLocation + ", at " + wagerTime);
	    final EditText voteField = (EditText) findViewById(getTextField());
	    voteField.setText(vote);
	}
	
	protected void onViewResponsesClicked() {
		// don't show responses until admin has updated the result
		// switch to page that shows responses in list
		// page should also display the winner at the top
	}
}
