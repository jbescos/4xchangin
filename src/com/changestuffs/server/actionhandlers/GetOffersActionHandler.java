package com.changestuffs.server.actionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.persistence.beans.Offer;
import com.changestuffs.server.persistence.beans.ProductOffered;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.GetOffersAction;
import com.changestuffs.shared.actions.GetOffersResult;
import com.changestuffs.shared.actions.GetOffersResult.OffersPerProduct;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Singleton
public class GetOffersActionHandler implements
		ActionHandler<GetOffersAction, GetOffersResult> {

	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Logued
	@Override
	public GetOffersResult execute(GetOffersAction action,
			ExecutionContext context) throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Map<String, OffersPerProduct> offersPerProduct = new HashMap<String, OffersPerProduct>();
		ArticlesOAM oam = provider.get();
		List<Offer> offers = oam.getOffers(user.getEmail());
		for (Offer offer : offers) {
			OffersPerProduct perProduct = new OffersPerProduct(
					KeyFactory.keyToString(offer.getKey()),
					KeyFactory.keyToString(offer.getProduct().getKey()),
					offer.getProduct().getName(), convertToMap(offer.getProductOffered()));
			offersPerProduct.put(perProduct.getOfferKey(), perProduct);

		}
		return new GetOffersResult(offersPerProduct);
	}
	
	private Map<String,String> convertToMap(Set<ProductOffered> offereds){
		Map<String,String> map = new HashMap<String,String>();
		for(ProductOffered offered : offereds){
			map.put(offered.getProductId(), offered.getProductName());
		}
		return map;
	}

	@Override
	public void undo(GetOffersAction action, GetOffersResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetOffersAction> getActionType() {
		return GetOffersAction.class;
	}
}
