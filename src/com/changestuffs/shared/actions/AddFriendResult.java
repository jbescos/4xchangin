package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

public class AddFriendResult implements Result {

	private boolean alreadyAdded;

	@SuppressWarnings("unused")
	private AddFriendResult() {
		// For serialization only
	}

	public AddFriendResult(boolean alreadyAdded) {
		this.alreadyAdded = alreadyAdded;
	}

	public boolean isAlreadyAdded() {
		return alreadyAdded;
	}
}
