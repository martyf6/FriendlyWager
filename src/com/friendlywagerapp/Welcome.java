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
	
	protected List<FriendlyWager> wagers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	    createWagerList();
	}
	
	private void createWagerList() {
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
		
		@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FriendlyWager selected = wagers.get(position);
			Intent goToViewWager;
			if (selected.isOwner()) goToViewWager = new Intent(Welcome.this, ViewWagerAdmin.class);
			else goToViewWager = new Intent(Welcome.this, ViewWager.class);
			String vote = "no vote yet";
			if (selected.getVote() != null) vote = selected.getVote().toString();
			goToViewWager.putExtra("vote",vote);
			goToViewWager.putExtra("wagerName",selected.getName().toString());
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
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
