package com.changestuffs.shared.actions;

import java.util.Map;

import com.gwtplatform.dispatch.shared.Result;

public class GetOffersResult implements Result {

	private static final long serialVersionUID = 2448540128055046576L;
	private Map<String, OffersPerProduct> offers;
	
	public GetOffersResult(Map<String, OffersPerProduct> offers) {
		super();
		this.offers = offers;
	}
	
	public GetOffersResult() {
		super();
	}

	public Map<String, OffersPerProduct> getOffers() {
		return offers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offers == null) ? 0 : offers.hashCode());
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
		GetOffersResult other = (GetOffersResult) obj;
		if (offers == null) {
			if (other.offers != null)
				return false;
		} else if (!offers.equals(other.offers))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GetOffersResult [offers=" + offers + "]";
	}

	public static class OffersPerProduct implements Result{

		private static final long serialVersionUID = -8960328429749980775L;
		private String offerKey;
		private String productId;
		private String productName; 
		private Map<String,String> productOffers;
		
		public OffersPerProduct() {
			super();
		}
		public OffersPerProduct(String offerKey, String productId,
				String productName, Map<String, String> productOffers) {
			super();
			this.offerKey = offerKey;
			this.productId = productId;
			this.productName = productName;
			this.productOffers = productOffers;
		}
		public String getOfferKey() {
			return offerKey;
		}
		public String getProductId() {
			return productId;
		}
		public String getProductName() {
			return productName;
		}
		public Map<String, String> getProductOffers() {
			return productOffers;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((offerKey == null) ? 0 : offerKey.hashCode());
			result = prime * result
					+ ((productId == null) ? 0 : productId.hashCode());
			result = prime * result
					+ ((productName == null) ? 0 : productName.hashCode());
			result = prime * result
					+ ((productOffers == null) ? 0 : productOffers.hashCode());
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
			OffersPerProduct other = (OffersPerProduct) obj;
			if (offerKey == null) {
				if (other.offerKey != null)
					return false;
			} else if (!offerKey.equals(other.offerKey))
				return false;
			if (productId == null) {
				if (other.productId != null)
					return false;
			} else if (!productId.equals(other.productId))
				return false;
			if (productName == null) {
				if (other.productName != null)
					return false;
			} else if (!productName.equals(other.productName))
				return false;
			if (productOffers == null) {
				if (other.productOffers != null)
					return false;
			} else if (!productOffers.equals(other.productOffers))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "OffersPerProduct [offerKey=" + offerKey + ", productId="
					+ productId + ", productName=" + productName
					+ ", productOffers=" + productOffers + "]";
		}
	}
	
}
