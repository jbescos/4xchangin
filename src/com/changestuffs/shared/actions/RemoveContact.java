package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.RemoveContactResult;

import java.lang.String;

import javax.validation.constraints.NotNull;

public class RemoveContact extends UnsecuredActionImpl<RemoveContactResult> {

	@NotNull
	private String email;
	private boolean pending;

	@SuppressWarnings("unused")
	private RemoveContact() {
		// For serialization only
	}

	public RemoveContact(String email, boolean pending) {
		this.email = email;
		this.pending = pending;
	}

	public String getEmail() {
		return email;
	}

	public boolean isPending() {
		return pending;
	}
	
}
