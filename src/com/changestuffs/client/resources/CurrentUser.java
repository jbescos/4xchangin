package com.changestuffs.client.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IMessageResponse;

public class CurrentUser {

	private String email;
	private final Map<String, List<IMessageResponse>> conversations = new HashMap<String, List<IMessageResponse>>();
	private final Set<String> online = new HashSet<String>();
	private final Set<String> pendingFriends = new HashSet<String>();
	private final Map<Tags, LookForResult> searchMap = new HashMap<Tags, LookForResult>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, List<IMessageResponse>> getConversations() {
		return conversations;
	}

	public Set<String> getOnline() {
		return online;
	}

	public Set<String> getPendingFriends() {
		return pendingFriends;
	}

	public Map<Tags, LookForResult> getSearchMap() {
		return searchMap;
	}
	
}
