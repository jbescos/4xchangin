package com.changestuffs.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface Translator extends ConstantsWithLookup {
	
	@DefaultStringValue("Electronic")
	String electronic();
	
	@DefaultStringValue("Games")
	String games();
	
	@DefaultStringValue("Vehicles")
	String vehicles();
	
	@DefaultStringValue("Sports")
	String sports();
	
	@DefaultStringValue("Furniture")
	String furniture();
	
	@DefaultStringValue("Clothes")
	String clothes();
	
	@DefaultStringValue("Entertainment")
	String entertainment();
	
	@DefaultStringValue("Others")
	String others();

	@DefaultStringValue("Send")
	String send();
	
	@DefaultStringValue("Name")
	String name();
	
	@DefaultStringValue("I'm interested in...")
	String interestedIn();
	
	@DefaultStringValue("example@site.com")
	String mailExample();
	
}
