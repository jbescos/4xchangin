package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;


public class IsLoguedAction implements Action<IsLoguedResult> {

	public IsLoguedAction() {
	}

	@Override
	public String getServiceName() {
		return Action.DEFAULT_SERVICE_NAME + "IsLogued";
	}

	@Override
	public boolean isSecured() {
		return false;
	}
}
