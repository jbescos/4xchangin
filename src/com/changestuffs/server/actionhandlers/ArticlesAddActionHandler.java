package com.changestuffs.server.actionhandlers;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.apache.bval.guice.Validate;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.persistence.beans.Product;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.ArticlesAddResult;
import com.changestuffs.shared.constants.Tags;
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
public class ArticlesAddActionHandler implements
		ActionHandler<ArticlesAddAction, ArticlesAddResult> {

	private final Logger log = Logger.getLogger(getClass().getName());
	@Inject
	private Provider<ArticlesOAM> provider;

	@Override
	@Validate
	@Logued
	public ArticlesAddResult execute(@Valid ArticlesAddAction arg0,
			ExecutionContext arg1) throws ActionException {
		// TODO add language in product
		log.info("Adding " + arg0);
		ArticlesAddResult result = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		ArticlesOAM oam = provider.get();
		Product product = null;
		if (arg0.getKeyHash() == null) {
			product = oam.insertArticle(arg0, user.getEmail());
		} else {
			product = oam.updateArticle(arg0, user.getEmail());
		}
		result = new ArticlesAddResult(KeyFactory.keyToString(product.getKey()), Tags.valueOf(product.getTag().getTagId()));
		return result;
	}

	@Override
	public Class<ArticlesAddAction> getActionType() {
		return ArticlesAddAction.class;
	}

	@Override
	public void undo(ArticlesAddAction arg0, ArticlesAddResult arg1,
			ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
