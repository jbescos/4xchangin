package com.changestuffs.shared.utils;

public class StringUtils {

	public static String subString(int top, String string){
		if(string.length()>top)
			return string.substring(0, top)+"...";
		else
			return string;
	}
	
}
