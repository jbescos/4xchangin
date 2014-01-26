package com.changestuffs.shared.actions;

import javax.validation.constraints.NotNull;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class AddFriend extends UnsecuredActionImpl<AddFriendResult> {

	@NotNull
	private String email;

	@SuppressWarnings("unused")
	private AddFriend() {
		// For serialization only
	}

	public AddFriend(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
