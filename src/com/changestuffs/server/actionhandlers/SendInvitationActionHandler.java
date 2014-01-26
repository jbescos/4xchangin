package com.changestuffs.server.actionhandlers;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.bval.guice.Validate;
import org.scb.gwt.web.server.i18n.GWTI18N;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.servlets.email.HtmlEngine;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.InfoLoginResult;
import com.changestuffs.shared.actions.SendInvitation;
import com.changestuffs.shared.actions.SendInvitationResult;
import com.changestuffs.shared.constants.RequestParams;
import com.changestuffs.shared.constants.ServletPaths;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SendInvitationActionHandler implements
		ActionHandler<SendInvitation, SendInvitationResult> {

	private final String DOMAIN = "http://4xchangin.appspot.com";
	public final static String CANCEL = "http://4xchangin.appspot.com"+ServletPaths.dontNotify.getPath()+"?"+RequestParams.token.name()+"=";
	private final Logger log = Logger.getLogger(getClass().getName());
	private final Properties props = new Properties();
	private final Provider<UserBeanOAM> provider;
	private final Provider<HttpServletRequest> requestProvider;
	private final SecureRandom random;
	private final HtmlEngine htmlEngine;
	
	@Inject
	public SendInvitationActionHandler(HtmlEngine htmlEngine, SecureRandom random, Provider<UserBeanOAM> provider, Provider<HttpServletRequest> requestProvider){
		this.random=random;
		this.provider=provider;
		this.requestProvider=requestProvider;
		this.htmlEngine = htmlEngine;
	}

	@Override
	@Logued
	@Validate
	public SendInvitationResult execute(@Valid SendInvitation action, ExecutionContext context) throws ActionException {
		boolean emailSent = true;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserBeanOAM oam = provider.get();
		User newUser = new User(action.getEmail(), UserBeanOAM.AUTH_DOMAIN_NOT_REGISTERED, new BigInteger(130, random).toString(32));
		InfoLoginResult result = oam.persistUser(null, newUser, user.getEmail(), user.getEmail());
		if(result.isSendEmail()){
			try {
				String token = result.getToken();
				sendEmail(user.getEmail(), newUser.getEmail(), token);
			} catch (MessagingException|IOException e) {
				emailSent = false;
				log.log(Level.WARNING, "Error sending email", e);
			}
		}
		return new SendInvitationResult(emailSent);
	}
	
	private void sendEmail(String emailFrom, String emailTo, String token) throws AddressException, MessagingException, IOException{
		Locale locale = requestProvider.get().getLocale();
		ChangestuffsMessages translator = GWTI18N.create(ChangestuffsMessages.class, locale.toString());
		log.info("locale: "+locale);
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailFrom));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        msg.setSubject(translator.inviteContact(emailFrom));
        String body = htmlEngine.htmlInviteContact(locale, emailTo, emailFrom, DOMAIN, CANCEL, token);
        msg.setContent(body, "text/html; charset=utf-8");
        log.info("Trying to send email: "+body);
        Transport.send(msg);
	}

	@Override
	public void undo(SendInvitation action, SendInvitationResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<SendInvitation> getActionType() {
		return SendInvitation.class;
	}
	
}
