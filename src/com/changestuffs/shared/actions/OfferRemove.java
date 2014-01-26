package com.changestuffs.shared.actions;

import javax.validation.constraints.NotNull;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class OfferRemove extends UnsecuredActionImpl<OfferRemoveResult> {

	@NotNull
	private String offerId;

	@SuppressWarnings("unused")
	private OfferRemove() {
		// For serialization only
	}

	public OfferRemove(String offerId) {
		this.offerId = offerId;
	}

	public String getOfferId() {
		return offerId;
	}
}
