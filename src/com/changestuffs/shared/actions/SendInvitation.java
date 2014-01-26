package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.SendInvitationResult;

import java.lang.String;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SendInvitation extends UnsecuredActionImpl<SendInvitationResult> {

	@NotNull
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;

	@SuppressWarnings("unused")
	private SendInvitation() {
		// For serialization only
	}

	public SendInvitation(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
