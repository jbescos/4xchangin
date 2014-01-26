package com.changestuffs.shared.actions;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import com.changestuffs.shared.actions.UpdateOfferResult;
import java.lang.String;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class UpdateOfferAction extends
		UnsecuredActionImpl<UpdateOfferResult> {

	@NotNull
	private String offerId;
	private Map<String,String> productIdNames;

	@SuppressWarnings("unused")
	private UpdateOfferAction() {
		// For serialization only
	}

	public UpdateOfferAction(String offerId, Map<String,String> productIdNames) {
		this.offerId = offerId;
		this.productIdNames = productIdNames;
	}

	public String getOfferId() {
		return offerId;
	}

	public Map<String,String> getProductIdNames() {
		return productIdNames;
	}
}
