package com.friendlywagerapp;

import android.content.Intent;
import android.view.View;

public class ViewClosedWagerAdmin extends ViewClosedWager {
	
	private static String TAG = "ViewClosedWagerAdmin";
	
	protected int getLayout () {
		return R.layout.view_closed_wager_admin;
	}
	
	@Override
	public void onViewResponsesClicked(View v) {
		// don't show responses until admin has updated the result
		// switch to page that shows responses in list
		// page should also display the winner at the top
		
		Intent goToViewResponses =  new Intent(ViewClosedWagerAdmin.this, ViewResponses.class);
		goToViewResponses.putExtra("wagerName",wagerName);
		startActivity(goToViewResponses);
	}
}
