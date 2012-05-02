package com.friendlywagerapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Welcome extends Activity {
	/** Called when the activity is first created. */
	
	private static String TAG = "Welcome";
	
	protected List<FriendlyWager> openedWagers = new ArrayList<FriendlyWager>();
	protected List<FriendlyWager> closedWagers = new ArrayList<FriendlyWager>();
	
	private static final String defaultOpenedWager = "No opened events";
	private static final String defaultClosedWager = "No closed events";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	    createWagerLists();
	}
	
	private void createWagerLists() {
		List<FriendlyWager> wagers = getWagers();
	    if (wagers == null) return;
	    
	    List<String> openWagerNames = new ArrayList<String>();
	    List<String> closedWagerNames = new ArrayList<String>();
	    for (FriendlyWager wager : wagers) {
			if (wager.isClosed()){
				closedWagerNames.add(wager.getName());
				closedWagers.add(wager);
			}
			else {
				openWagerNames.add(wager.getName());
				openedWagers.add(wager);
			}
			Log.i(TAG,"Found wager: " + wager.getName() + " and is owner? " + wager.isOwner());
		}
		if (openWagerNames.isEmpty()) openWagerNames.add(defaultOpenedWager);
		if (closedWagerNames.isEmpty()) closedWagerNames.add(defaultClosedWager);
		ListView openWagersView = (ListView) findViewById(R.id.showOpenWagersList);
	    ListView closedWagersView = (ListView) findViewById(R.id.showClosedWagersList);
	    
	    
	    // First paramenter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the View to which the data is written
	    // Forth - the Array of data
	    ArrayAdapter<String> openWagersAdapter = new ArrayAdapter<String>(this,
	    	android.R.layout.simple_list_item_1, android.R.id.text1, openWagerNames);
	    ArrayAdapter<String> closedWagersAdapter = new ArrayAdapter<String>(this,
		    	android.R.layout.simple_list_item_1, android.R.id.text1, closedWagerNames);

	    // Assign adapter to ListView
	    openWagersView.setAdapter(openWagersAdapter);
	    openWagersView.setOnItemClickListener(new OnWagerClicked(openedWagers, defaultOpenedWager));
	    closedWagersView.setAdapter(closedWagersAdapter);
	    closedWagersView.setOnItemClickListener(new OnWagerClicked(closedWagers, defaultClosedWager));
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
    
    private List<FriendlyWager> getWagers() {
    	try {
			JSONArray response = FriendlyWagerServer.getWagers();
			JSONObject result = response.getJSONObject(0);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(Welcome.this, error, Toast.LENGTH_SHORT).show();
			} else {
				JSONArray wagersObj = response.getJSONArray(1);
				return FriendlyWager.createWagers(wagersObj);
			}
		} catch (Exception e) {
			Toast.makeText(Welcome.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
    	return null;
    }
	
	private class OnWagerClicked implements OnItemClickListener {
		
		protected List<FriendlyWager> wagers;
		protected String defaultItem;
		
		public OnWagerClicked(List<FriendlyWager> wagers, String defaultItem){
			this.wagers = wagers;
			this.defaultItem = defaultItem;
		}
		
		@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (defaultItem.equals(parent.getAdapter().getItem(position))) return;
			FriendlyWager selected = wagers.get(position);
			Intent goToViewWager;
			String vote;
			if (selected.isClosed()) {
				if (selected.isOwner()) goToViewWager = new Intent(Welcome.this, ViewClosedWagerAdmin.class);
				else goToViewWager = new Intent(Welcome.this, ViewClosedWager.class);
				vote = "did not vote";
				if (selected.getVote() != null) vote = selected.getVote().toString();
			} else {
				if (selected.isOwner()) goToViewWager = new Intent(Welcome.this, ViewWagerAdmin.class);
				else goToViewWager = new Intent(Welcome.this, ViewWager.class);
				vote = "no vote yet";
			}
			if (selected.getVote() != null) vote = selected.getVote().toString();
			goToViewWager.putExtra("vote",vote);
			goToViewWager.putExtra("wagerName",selected.getName());
			goToViewWager.putExtra("wagerLocation",selected.getLocation());
			goToViewWager.putExtra("wagerTime",selected.getTime());
			startActivity(goToViewWager);
    	}
	}
    
    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.welcome_menu, menu);
    	return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.help:
            onHelpClicked(null);
            return true;
        case R.id.logout:
            onLogoutClicked(null);
            return true;
        case R.id.refreshList:
        	createWagerLists();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
