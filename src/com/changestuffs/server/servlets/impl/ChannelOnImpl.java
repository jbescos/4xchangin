package com.changestuffs.server.servlets.impl;

import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.constants.MessageType;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ChannelOnImpl extends ChannelStatus {

	@Inject
	public ChannelOnImpl(Provider<UserBeanOAM> provider, DtoToJson toJson) {
		super(provider, toJson);
	}

	@Override
	protected String getJson(String email, String friend) {
		log.info("Advising "+friend+" that "+email+" is online");
		return toJson.getJson(null, email, MessageType.login);
	}

	@Override
	protected boolean isOnline() {
		return true;
	}

}
