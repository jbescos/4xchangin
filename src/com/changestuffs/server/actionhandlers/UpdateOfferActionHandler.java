package com.changestuffs.server.actionhandlers;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.bval.guice.Validate;
import org.scb.gwt.web.server.i18n.GWTI18N;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.servlets.email.HtmlEngine;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.server.utils.ArticlesOAM.OfferInfo;
import com.changestuffs.shared.actions.UpdateOfferAction;
import com.changestuffs.shared.actions.UpdateOfferResult;
import com.changestuffs.shared.constants.Constants;
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
public class UpdateOfferActionHandler implements
		ActionHandler<UpdateOfferAction, UpdateOfferResult> {

	private final Provider<ArticlesOAM> provider;
	private final HtmlEngine htmlEngine;
	private final Provider<HttpServletRequest> requestProvider;
	private final Properties props = new Properties();
	private final String PRODUCT_URL = Constants.URL_BASE + "/#"
			+ NameTokens.lookfor + ";id=";
	private final Logger log = Logger.getLogger(getClass().getName());

	@Inject
	public UpdateOfferActionHandler(Provider<ArticlesOAM> provider,
			HtmlEngine htmlEngine, Provider<HttpServletRequest> requestProvider) {
		this.provider = provider;
		this.htmlEngine = htmlEngine;
		this.requestProvider = requestProvider;
	}

	@Logued
	@Validate
	@Override
	public UpdateOfferResult execute(@Valid UpdateOfferAction action,
			ExecutionContext context) throws ActionException {
		UpdateOfferResult result = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		ArticlesOAM oam = provider.get();
		result = oam.updateOffer(user.getEmail(), action.getOfferId(),
				action.getProductIdNames());
		sendEmail(oam.getOwner(action.getOfferId()));
		return result;
	}

	private void sendEmail(OfferInfo offerInfo) {
		if (offerInfo.getUser().isReceiveEmails()) {
			Locale locale = requestProvider.get().getLocale();
			ChangestuffsMessages translator;
			try {
				translator = GWTI18N.create(ChangestuffsMessages.class,
						locale.toString());

				Session session = Session.getDefaultInstance(props, null);
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(Constants.CHANGESTUFFS_EMAIL));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						offerInfo.getUser().getEmail()));
				msg.setSubject(translator.offerReceibed(offerInfo
						.getProductName()));
				String body = htmlEngine.htmlUpdateOffer(locale, offerInfo
						.getUser().getEmail(), offerInfo.getProductName(),
						PRODUCT_URL + offerInfo.getProductId(),
						SendInvitationActionHandler.CANCEL
								+ offerInfo.getUser().getUserId());
				log.info(body);
				msg.setContent(body, "text/html; charset=utf-8");
				Transport.send(msg);
			} catch (Exception e) {
				log.log(Level.WARNING, "Error sending email", e);
			}
		}
	}

	@Override
	public void undo(UpdateOfferAction action, UpdateOfferResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<UpdateOfferAction> getActionType() {
		return UpdateOfferAction.class;
	}
}
