package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class IsLoguedResult implements Result {

	private String email;

	@SuppressWarnings("unused")
	private IsLoguedResult() {
		// For serialization only
	}

	public IsLoguedResult(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
