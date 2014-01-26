package com.changestuffs.shared.actions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class SendMessage extends UnsecuredActionImpl<SendMessageResult> {

	@NotNull
	private String email;
	@NotNull
	@Size(min=1)
	private String message;

	@SuppressWarnings("unused")
	private SendMessage() {
		// For serialization only
	}

	public SendMessage(String email, String message) {
		this.email = email;
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public String getMessage() {
		return message;
	}
	
}
