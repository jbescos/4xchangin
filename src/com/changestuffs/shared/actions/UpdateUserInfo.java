package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.UpdateUserInfoResult;
import java.lang.String;

public class UpdateUserInfo extends UnsecuredActionImpl<UpdateUserInfoResult> {

	
	private String cell;
	private String city;
	private String country;
	private boolean receiveEmails;

	@SuppressWarnings("unused")
	private UpdateUserInfo() {
		// For serialization only
	}

	public UpdateUserInfo(String cell, String city, String country, boolean receiveEmails) {
		this.cell = cell;
		this.city = city;
		this.country = country;
		this.receiveEmails=receiveEmails;
	}

	public String getCell() {
		return cell;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public boolean isReceiveEmails() {
		return receiveEmails;
	}
	
}
