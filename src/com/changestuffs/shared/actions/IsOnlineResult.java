package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

public class IsOnlineResult implements Result {

	private boolean online;

	@SuppressWarnings("unused")
	private IsOnlineResult() {
		// For serialization only
	}

	public IsOnlineResult(boolean online) {
		this.online = online;
	}

	public boolean isOnline() {
		return online;
	}
}
