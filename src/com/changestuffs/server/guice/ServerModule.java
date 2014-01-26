package com.changestuffs.server.guice;


import java.util.logging.Logger;

import com.changestuffs.server.actionhandlers.AddFriendActionHandler;
import com.changestuffs.server.actionhandlers.ArticleRemoveActionHandler;
import com.changestuffs.server.actionhandlers.ArticlesAddActionHandler;
import com.changestuffs.server.actionhandlers.ArticlesGetActionHandler;
import com.changestuffs.server.actionhandlers.CreateTokenActionHandler;
import com.changestuffs.server.actionhandlers.GetOffersActionHandler;
import com.changestuffs.server.actionhandlers.GetUserInfoActionHandler;
import com.changestuffs.server.actionhandlers.IsLoguedActionHandler;
import com.changestuffs.server.actionhandlers.LoadMessagesActionHandler;
import com.changestuffs.server.actionhandlers.LoginsActionHandler;
import com.changestuffs.server.actionhandlers.LogoutActionHandler;
import com.changestuffs.server.actionhandlers.LookForSearchActionHandler;
import com.changestuffs.server.actionhandlers.OfferCreateActionHandler;
import com.changestuffs.server.actionhandlers.OfferRemoveActionHandler;
import com.changestuffs.server.actionhandlers.SendInvitationActionHandler;
import com.changestuffs.server.actionhandlers.SendMessageActionHandler;
import com.changestuffs.server.actionhandlers.UpdateOfferActionHandler;
import com.changestuffs.server.actionhandlers.UpdateUserInfoActionHandler;
import com.changestuffs.shared.actions.AddFriend;
import com.changestuffs.shared.actions.ArticleRemoveAction;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.ArticlesGetAction;
import com.changestuffs.shared.actions.CreateToken;
import com.changestuffs.shared.actions.GetOffersAction;
import com.changestuffs.shared.actions.GetUserInfo;
import com.changestuffs.shared.actions.IsLoguedAction;
import com.changestuffs.shared.actions.LoadMessages;
import com.changestuffs.shared.actions.LoginsAction;
import com.changestuffs.shared.actions.LogoutAction;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.OfferCreateAction;
import com.changestuffs.shared.actions.OfferRemove;
import com.changestuffs.shared.actions.SendInvitation;
import com.changestuffs.shared.actions.SendMessage;
import com.changestuffs.shared.actions.UpdateOfferAction;
import com.changestuffs.shared.actions.UpdateUserInfo;
import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.changestuffs.shared.actions.RemoveContact;
import com.changestuffs.server.actionhandlers.RemoveContactActionHandler;
import com.changestuffs.shared.actions.IsOnline;
import com.changestuffs.server.actionhandlers.IsOnlineActionHandler;

public class ServerModule extends HandlerModule {

	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Override
	protected void configureHandlers() {
		log.info("Configuring handlers");
		//ActionHandlers must be thread safe!!!
		bindHandler(LoginsAction.class, LoginsActionHandler.class);
		bindHandler(LogoutAction.class, LogoutActionHandler.class);
		bindHandler(ArticlesGetAction.class, ArticlesGetActionHandler.class);
		bindHandler(ArticleRemoveAction.class, ArticleRemoveActionHandler.class);
		bindHandler(LookForAction.class, LookForSearchActionHandler.class);
		bindHandler(ArticlesAddAction.class, ArticlesAddActionHandler.class);
		bindHandler(IsLoguedAction.class, IsLoguedActionHandler.class);
		bindHandler(GetOffersAction.class, GetOffersActionHandler.class);

		bindHandler(OfferCreateAction.class,
				OfferCreateActionHandler.class);

		bindHandler(UpdateOfferAction.class,
				UpdateOfferActionHandler.class);

		bindHandler(OfferRemove.class, OfferRemoveActionHandler.class);

		bindHandler(UpdateUserInfo.class, UpdateUserInfoActionHandler.class);

		bindHandler(GetUserInfo.class, GetUserInfoActionHandler.class);

		bindHandler(AddFriend.class, AddFriendActionHandler.class);

		bindHandler(CreateToken.class, CreateTokenActionHandler.class);

		bindHandler(SendMessage.class, SendMessageActionHandler.class);

		bindHandler(LoadMessages.class, LoadMessagesActionHandler.class);

		bindHandler(SendInvitation.class, SendInvitationActionHandler.class);

		bindHandler(RemoveContact.class, RemoveContactActionHandler.class);

		bindHandler(IsOnline.class, IsOnlineActionHandler.class);
	}
}
