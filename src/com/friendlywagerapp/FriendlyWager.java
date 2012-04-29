package com.friendlywagerapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendlyWager {
	
	private static String TAG = "FriendlyWager";
	
	private String name;
	private String location;
	private String time;
	private boolean hasVoted;
	private Object vote;
	private boolean isOwner;
	
	public FriendlyWager(String name, String location, String time, boolean isOwner){
		this.name = name;
		this.location = location;
		this.time = time;
		this.isOwner = isOwner;
		this.hasVoted = false;
	}
	
	public FriendlyWager(String name, String location, String time, boolean isOwner, Object vote){
		this.name = name;
		this.location = location;
		this.time = time;
		this.isOwner = isOwner;
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
	
	public boolean isOwner(){
		return isOwner;
	}
	
	public boolean hasVoted(){
		return hasVoted;
	}
	
	public Object getVote(){
		return vote;
	}
	
	public boolean isClosed(){
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone tz = TimeZone.getDefault();
		dfm.setTimeZone(tz);
		try {
			Calendar gmtCal = new GregorianCalendar(tz);
			Date current = gmtCal.getTime();
			Date eventDate = dfm.parse(time);
			return eventDate.before(current);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static FriendlyWager createWager(JSONObject wager){
		try {
			String eventName = wager.getString("event");
			String location = wager.getString("location");
			String time = wager.getString("time");
			boolean isOwner = wager.getBoolean("isOwner");
			boolean hasVoted = wager.getString("hasVoted").equals("1");
			if (hasVoted) {
				Object vote = wager.get("vote");
				return new FriendlyWager(eventName, location, time, isOwner, vote);
			} else {
				return new FriendlyWager(eventName, location, time, isOwner);
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
