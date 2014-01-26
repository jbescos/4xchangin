package com.changestuffs.shared.actions;

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class LoadMessages extends UnsecuredActionImpl<LoadMessagesResult> implements IsSerializable {

	private Set<String> contacts;
	
	public LoadMessages(){
		
	}
	
	public LoadMessages(Set<String> contacts) {
		this.contacts=contacts;
	}

	public Set<String> getContacts() {
		return contacts;
	}

	public void setContacts(Set<String> contacts) {
		this.contacts = contacts;
	}
	
}
