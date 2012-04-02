package com.friendlywagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendlyWager {
	
	private String name;
	private String location;
	private String time;
	private Boolean hasVoted;
	private Object vote;
	
	public FriendlyWager(String name, String location, String time){
		this.name = name;
		this.location = location;
		this.time = time;
		this.hasVoted = false;
	}
	
	public FriendlyWager(String name, String location, String time, Object vote){
		this.name = name;
		this.location = location;
		this.time = time;
		this.hasVoted = vote == null;
		this.vote = vote;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getTime(){
		return time;
	}
	
	public boolean hasVoted(){
		return hasVoted;
	}
	
	public Object getVote(){
		return vote;
	}
	
	public static FriendlyWager createWager(JSONObject wager){
		try {
			String eventName = wager.getString("event");
			String location = wager.getString("location");
			String time = wager.getString("time");
			boolean hasVoted = wager.getString("hasVoted").equals("1");
			if (hasVoted) {
				Object vote = wager.get("vote");
				return new FriendlyWager(eventName, location, time, vote);
			} else {
				return new FriendlyWager(eventName, location, time);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<FriendlyWager> createWagers(JSONArray wagers){
		List<FriendlyWager> wagersList = new ArrayList<FriendlyWager>();
		for (int i = 0; i < wagers.length(); i++){
			try {
				JSONObject event = wagers.getJSONObject(i);
				FriendlyWager wagerObj = createWager(event);
				if (wagerObj != null) wagersList.add(wagerObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return wagersList;
	}
}
