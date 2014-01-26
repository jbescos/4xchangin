package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.String;

public class CreateTokenResult implements Result {

	private String token;

	@SuppressWarnings("unused")
	private CreateTokenResult() {
		// For serialization only
	}

	public CreateTokenResult(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
