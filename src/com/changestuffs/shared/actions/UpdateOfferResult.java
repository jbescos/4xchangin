package com.changestuffs.shared.actions;

import com.changestuffs.shared.actions.GetOffersResult.OffersPerProduct;
import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class UpdateOfferResult implements Result {

	private OffersPerProduct offersPerProduct;
	
	public UpdateOfferResult() {
	}

	public UpdateOfferResult(OffersPerProduct offersPerProduct) {
		this.offersPerProduct=offersPerProduct;
	}

	public OffersPerProduct getOffersPerProduct() {
		return offersPerProduct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((offersPerProduct == null) ? 0 : offersPerProduct.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateOfferResult other = (UpdateOfferResult) obj;
		if (offersPerProduct == null) {
			if (other.offersPerProduct != null)
				return false;
		} else if (!offersPerProduct.equals(other.offersPerProduct))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UpdateOfferResult [offersPerProduct=" + offersPerProduct + "]";
	}
	
	
}
