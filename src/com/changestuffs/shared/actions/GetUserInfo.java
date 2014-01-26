package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.GetUserInfoResult;

public class GetUserInfo extends UnsecuredActionImpl<GetUserInfoResult> {

	private String email;
	
	public GetUserInfo() {
	}
	
	public GetUserInfo(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
