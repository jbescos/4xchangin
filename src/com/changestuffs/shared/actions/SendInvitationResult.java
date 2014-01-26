package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Result;

public class SendInvitationResult implements Result {

	private boolean sent;
	
	public SendInvitationResult(boolean sent) {
		this.sent=sent;
	}
	
	public SendInvitationResult() {
		super();
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
}
