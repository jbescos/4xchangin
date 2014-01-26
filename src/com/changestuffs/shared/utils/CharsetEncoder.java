package com.changestuffs.shared.utils;

import java.io.UnsupportedEncodingException;

public class CharsetEncoder {

	public static String parse(String data, String charsetFrom, String charsetTo) throws UnsupportedEncodingException{
		return new String(data.getBytes (charsetFrom), charsetTo);
	}
	
}
