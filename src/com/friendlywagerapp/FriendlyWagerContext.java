package com.friendlywagerapp;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class FriendlyWagerContext {
	
	private static FriendlyWagerContext instance;
	
	private HttpContext httpContext;
	
	public FriendlyWagerContext () {
		// Create local HTTP context
	    httpContext = new BasicHttpContext();
	    // Create a local instance of cookie store
	    CookieStore cookieStore = new BasicCookieStore();
	    // Bind custom cookie store to the local context
	    httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
	}
	
	public HttpContext getHttpContext(){
		return httpContext;
	}
	
	public CookieStore getCookieStore(){
		return (CookieStore) getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
	}
	
	public static FriendlyWagerContext getInstance() {
		if (instance == null) instance = new FriendlyWagerContext ();
		return instance;
	}

}
