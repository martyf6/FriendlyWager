package com.friendlywagerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.util.Log;

public class Util {
	
	private static String TAG = "Util";
	
	public static String encryptPassword (String pw){
		Log.i(TAG,"Original pw: " + pw);
		MessageDigest cript;
		try {
			cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(pw.getBytes("utf8"));
			String newPW = new String(Hex.encodeHex(cript.digest()));
			Log.i(TAG,"New pw: " + newPW);
			return newPW;
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Cannot find encryption algorithm SHA-1.\n" + e.toString());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG,"Cannot parse password in utf8.\n" + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONArray parseToJSON (HttpResponse response){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			System.out.println(builder.toString());
			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONArray finalResult = new JSONArray(tokener);
			return finalResult;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(TAG,"Error parsing data " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

}
