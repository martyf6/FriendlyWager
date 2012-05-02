package com.friendlywagerapp;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FriendlyWagerServer {
	
	private static String TAG = "FriendlyWager";
	
	private enum Method {
		POST, GET
	}
	
	public static JSONObject login(String username, String password){
		// first encrypt the password
		String encryptedPassword = Util.encryptPassword(password);
		// create the POST arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("submit", "Login"));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", encryptedPassword));
		nameValuePairs.add(new BasicNameValuePair("remember","0"));
		String url = "http://friendlywagerapp.com/session.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONObject register(String username, String password, String email) {
		// first encrypt the password
		String encryptedPassword = Util.encryptPassword(password);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", encryptedPassword));
		nameValuePairs.add(new BasicNameValuePair("email",email));
		String url = "http://friendlywagerapp.com/register.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	// currently not implemented
	public static JSONObject forgotPassword(String username) {
		// create the GET arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		String url = "http://friendlywagerapp.com/forgot_password.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONObject createWager(String wagerName, String location, String time) {
		// create the POST arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("wagerName", wagerName));
		nameValuePairs.add(new BasicNameValuePair("location", location));
		nameValuePairs.add(new BasicNameValuePair("time", time));
		String url = "http://friendlywagerapp.com/new_wager.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONArray getWagers() {
		// create the GET arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		String url = "http://friendlywagerapp.com/get_wagers.php";
		return contactServer(nameValuePairs, url, Method.GET);
	}
	
	public static JSONObject inviteUser(String wagerName, String username) {
		// create the POST arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("wagerName", wagerName));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		String url = "http://friendlywagerapp.com/send_invites.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONObject voteOnWager(String wagerName, String vote) {
		// create the POST arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("wagerName", wagerName));
		nameValuePairs.add(new BasicNameValuePair("vote", vote));
		String url = "http://friendlywagerapp.com/vote_wager.php";
		JSONObject response = null;
		try {
			response = contactServer(nameValuePairs, url, Method.POST).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONArray getWagerResponses(String wagerName) {
		// create the GET arguments
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("wagerName", wagerName));
		String url = "http://friendlywagerapp.com/get_wager_responses.php";
		return contactServer(nameValuePairs, url, Method.GET);
	}
	
	private static JSONArray contactServer(ArrayList<NameValuePair> args, String url, Method method){
		try {
			FriendlyWagerContext fwContext = FriendlyWagerContext.getInstance();
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			DefaultHttpClient httpclient = new DefaultHttpClient(params);
			HttpUriRequest request;
			if (method.equals(Method.POST)) {
				HttpPost post = new HttpPost(url);
				post.setEntity(new UrlEncodedFormEntity(args));
				request = post;
			} else {
				// create GET args string
				String urlArgs = url;
				if (args.size() > 0){
					urlArgs += "?";
					for (NameValuePair queryTerm : args) {
						String query = queryTerm.getValue().trim().replace(" ", "%20");
						urlArgs += queryTerm.getName().trim() + "=" + query + "&";
					}
					// remove the last '&'
					urlArgs = urlArgs.substring(0, urlArgs.length()-1);
				}
				Log.i(TAG,"url: " + urlArgs);
				request = new HttpGet(urlArgs);
			}
			HttpResponse response = httpclient.execute(request, fwContext.getHttpContext());
			JSONArray responseJSON = Util.parseToJSON(response);
	        return responseJSON;
		} catch (Exception e) {
			Log.e(TAG, "Error in http connection.\n" + e.toString());
			return null;
		}
	}

}
