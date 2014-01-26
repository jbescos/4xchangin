package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class ArticlesGetAction implements Action<LookForResult> {

	private boolean fullInfo;
	private String email;

	public ArticlesGetAction(boolean fullInfo, String email) {
		super();
		this.fullInfo = fullInfo;
		this.email=email;
	}

	// Serial
	public ArticlesGetAction() {
	}
	
	public boolean isFullInfo() {
		return fullInfo;
	}
	
	public String getEmail() {
		return email;
	}

	@Override
	public String getServiceName() {
		return Action.DEFAULT_SERVICE_NAME + "ArticlesGet";
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "ArticlesGetAction[" + "]";
	}
}
