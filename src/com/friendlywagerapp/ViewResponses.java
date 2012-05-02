package com.friendlywagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewResponses extends Activity {
	
	private static String TAG = "ViewResponses";
	
	private String wagerName;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view_wager_responses);
	    
	    wagerName = getIntent().getExtras().get("wagerName").toString();
	    TextView wagerDetails = (TextView) findViewById(R.id.viewWagerResponsesText);
	    wagerDetails.setText("Responses for '" + wagerName + "'");
	    
	    
	    List<String> responses = getWagerResponses();
	    if (responses.isEmpty()) responses.add("No responses found for event");
		ListView listView = (ListView) findViewById(R.id.viewWagerResponsesList);

	    // First paramenter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the View to which the data is written
	    // Forth - the Array of data
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    	android.R.layout.simple_list_item_1, android.R.id.text1, responses);

	    // Assign adapter to ListView
	    listView.setAdapter(adapter);
	}
	
	public void onDoneClicked(View v) {
    	finish();
    }
	
	private List<String> getWagerResponses() {
		try {
			JSONArray response = FriendlyWagerServer.getWagerResponses(wagerName);
			JSONObject result = response.getJSONObject(0);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(ViewResponses.this, error, Toast.LENGTH_SHORT).show();
			} else {
				List<String> responses = new ArrayList<String>();
				JSONArray responsesObj = response.getJSONArray(1);
				for (int i = 0; i < responsesObj.length(); i++){
					try {
						JSONObject responseObj = responsesObj.getJSONObject(i);
						String username = responseObj.getString("username");
						String vote = responseObj.getString("vote");
						String responseStr = username + " : " + vote;
						responses.add(responseStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return responses;
			}
		} catch (Exception e) {
			Toast.makeText(ViewResponses.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
    	return null;
    }
}
