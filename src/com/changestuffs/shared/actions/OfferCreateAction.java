package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class OfferCreateAction extends
		UnsecuredActionImpl<GetOffersResult> {

	private String productId;

	@SuppressWarnings("unused")
	private OfferCreateAction() {
		// For serialization only
	}

	public OfferCreateAction(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}
}
