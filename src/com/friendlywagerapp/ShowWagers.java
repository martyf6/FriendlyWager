package com.friendlywagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowWagers extends Activity {
	
	private static String TAG = "ShowWager";
	
	protected List<FriendlyWager> wagers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.show_wagers);
	    
	    List<FriendlyWager> wagers = getWagers();
	    if (wagers == null) return;
	    
	    List<String> wagerNames = new ArrayList<String>();
	    for (FriendlyWager wager : wagers) {
			wagerNames.add(wager.getName());
			Log.i(TAG,"Found wager: " + wager.getName() + " and is owner? " + wager.isOwner());
		}
		if (wagerNames.isEmpty()) wagerNames.add("No events found for user");
		else this.wagers = wagers;
	    ListView listView = (ListView) findViewById(R.id.showWagersList);

	    // First paramenter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the View to which the data is written
	    // Forth - the Array of data
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    	android.R.layout.simple_list_item_1, android.R.id.text1, wagerNames);

	    // Assign adapter to ListView
	    listView.setAdapter(adapter);
	    listView.setOnItemClickListener(new OnWagerClicked());
	}
	
	public void onDoneClicked(View v) {
    	finish();
    }
	
	private List<FriendlyWager> getWagers() {
    	try {
			JSONArray response = FriendlyWagerServer.getWagers();
			JSONObject result = response.getJSONObject(0);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(ShowWagers.this, error, Toast.LENGTH_SHORT).show();
			} else {
				JSONArray wagersObj = response.getJSONArray(1);
				return FriendlyWager.createWagers(wagersObj);
			}
		} catch (Exception e) {
			Toast.makeText(ShowWagers.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
    	return null;
    }
	
	private class OnWagerClicked implements OnItemClickListener {
		
		@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FriendlyWager selected = wagers.get(position);
			Intent goToViewWager;
			if (selected.isOwner()) goToViewWager = new Intent(ShowWagers.this, ViewWagerAdmin.class);
			else goToViewWager = new Intent(ShowWagers.this, ViewWager.class);
			String vote = "no vote yet";
			if (selected.getVote() != null) vote = selected.getVote().toString();
			goToViewWager.putExtra("vote",vote);
			goToViewWager.putExtra("wagerName",selected.getName().toString());
			startActivity(goToViewWager);
    	}
	}
}
