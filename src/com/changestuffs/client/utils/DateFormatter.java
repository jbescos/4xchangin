package com.changestuffs.client.utils;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;

public class DateFormatter {

	private final DateTimeFormat timeFormat;
	
	public DateFormatter(final String PATTERN){
		timeFormat = DateTimeFormat.getFormat(PATTERN);
	}
	
	public String dateToString(Date date){
		return timeFormat.format(date);
	}
	
	public Date stringToDate(String text){
		return timeFormat.parse(text);
	}
	
}
