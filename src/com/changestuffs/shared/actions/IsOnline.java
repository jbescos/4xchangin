package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.IsOnlineResult;
import java.lang.String;

public class IsOnline extends UnsecuredActionImpl<IsOnlineResult> {

	private String email;

	@SuppressWarnings("unused")
	private IsOnline() {
		// For serialization only
	}

	public IsOnline(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
