package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.Action;

public class GetOffersAction implements Action<GetOffersResult> {

	public GetOffersAction() {
	}

	@Override
	public String getServiceName() {
		return Action.DEFAULT_SERVICE_NAME + "GetOffers";
	}

	@Override
	public boolean isSecured() {
		return false;
	}
}
