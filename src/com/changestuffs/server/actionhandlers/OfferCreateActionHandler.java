package com.changestuffs.server.actionhandlers;

import java.util.logging.Logger;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.GetOffersResult;
import com.changestuffs.shared.actions.OfferCreateAction;
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
public class OfferCreateActionHandler implements
		ActionHandler<OfferCreateAction, GetOffersResult> {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Logued
	@Override
	public GetOffersResult execute(OfferCreateAction action,
			ExecutionContext context) throws ActionException {
		GetOffersResult result = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		ArticlesOAM oam = provider.get();
		result = oam.createOffer(action.getProductId(), user.getEmail()); 
		log.info("Created offer: "+result);
		return result;
	}

	@Override
	public void undo(OfferCreateAction action, GetOffersResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<OfferCreateAction> getActionType() {
		return OfferCreateAction.class;
	}
}
