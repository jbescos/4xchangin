package com.changestuffs.client.gatekeeper;

import com.changestuffs.client.resources.CurrentUser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class NeedsLoginKeeper implements Gatekeeper {

	private final CurrentUser user;
	
	@Inject
	public NeedsLoginKeeper(CurrentUser user){
		this.user=user;
	}
	
	@Override
	public boolean canReveal() {
		return user.getEmail()!=null;
	}

}
