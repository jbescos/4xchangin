package com.changestuffs.server.servlets.email;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.changestuffs.shared.constants.Locales;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HtmlEngine {

	private final VelocityEngine ve = new VelocityEngine();
	private final String INVITE_TEMPLATE = "com/changestuffs/server/servlets/email/inviteContact";
	private final String UPDATE_OFFER_TEMPLATE = "com/changestuffs/server/servlets/email/updateOffer";
	
	@Inject
	public HtmlEngine(){
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.init();
	}

	public String htmlInviteContact(Locale locale, String emailTo, String emailFrom, String domain, String cancel, String token) {
		VelocityContext context = new VelocityContext();
		context.put("emailTo", emailTo);
		context.put("emailFrom", emailFrom);
		context.put("domain", domain);
		context.put("cancel", cancel);
		context.put("token", token);

		Template t = ve.getTemplate(getTemplate(locale, INVITE_TEMPLATE), "UTF-8");

		StringWriter writer = new StringWriter();

		t.merge(context, writer);
		return writer.toString();
	}
	
	public String htmlUpdateOffer(Locale locale, String emailTo, String productName, String productUrl, String cancelNotificationsUrl){
		VelocityContext context = new VelocityContext();
		context.put("emailTo", emailTo);
		context.put("productName", productName);
		context.put("productUrl", productUrl);
		context.put("cancelNotificationsUrl", cancelNotificationsUrl);

		Template t = ve.getTemplate(getTemplate(locale, UPDATE_OFFER_TEMPLATE), "UTF-8");

		StringWriter writer = new StringWriter();

		t.merge(context, writer);
		return writer.toString();
	}

	private String getTemplate(Locale locale, String template) {
		for (Locales locales : Locales.values()) {
			if(locales.name().equals(locale.toString()))
				return template+"_"+locales.name()+".vm";
		}
		return template+"_"+Locales.en.name()+".vm";
	}

}
