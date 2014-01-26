package com.changestuffs.server.servlets.impl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.actions.GetUserInfoResult;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class ChannelStatus implements IServletManager{

	protected final Logger log = Logger.getLogger(getClass().getName());
	
	private final Provider<UserBeanOAM> provider;
	protected final DtoToJson toJson;
	
	@Inject
	public ChannelStatus(Provider<UserBeanOAM> provider, DtoToJson toJson){
		this.provider = provider;
		this.toJson = toJson;
	}
	
	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(req);
		String email = presence.clientId();
		UserBeanOAM oam = provider.get();
		oam.updateOnline(isOnline(), email);
		GetUserInfoResult result = oam.getGetUserInfoResult(email);
		for(String online : result.getOnline()){
			log.info(email+" is online = "+isOnline()+". Sending message to "+online);
			channelService.sendMessage(new ChannelMessage(online, getJson(email, online)));
		}
	}
	
	protected abstract boolean isOnline();
	
	protected abstract String getJson(String email, String friend);

}
