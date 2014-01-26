package com.changestuffs.server.utils;

public class UrlUtils {

	public static String getBaseURL(String url, String uri){
		return url.substring(0, url.indexOf(uri));
	}
	
}
