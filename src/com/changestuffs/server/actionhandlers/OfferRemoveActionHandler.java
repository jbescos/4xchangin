package com.changestuffs.server.actionhandlers;

import javax.validation.Valid;

import org.apache.bval.guice.Validate;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.OfferRemove;
import com.changestuffs.shared.actions.OfferRemoveResult;
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
public class OfferRemoveActionHandler implements
		ActionHandler<OfferRemove, OfferRemoveResult> {

	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Logued
	@Validate
	@Override
	public OfferRemoveResult execute(@Valid OfferRemove action, ExecutionContext context)
			throws ActionException {
		OfferRemoveResult result = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		ArticlesOAM oam = provider.get();
		result = oam.removeOffer(user.getEmail(), action.getOfferId());
		return result;
	}

	@Override
	public void undo(OfferRemove action, OfferRemoveResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<OfferRemove> getActionType() {
		return OfferRemove.class;
	}
}
