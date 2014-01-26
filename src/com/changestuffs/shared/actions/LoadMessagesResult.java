package com.changestuffs.shared.actions;

import java.util.List;
import java.util.Map;

import com.changestuffs.shared.dto.IMessageResponse;
import com.gwtplatform.dispatch.shared.Result;

@SuppressWarnings("serial")
public class LoadMessagesResult implements Result {

	private Map<String, List<IMessageResponse>> conversations;

	@SuppressWarnings("unused")
	private LoadMessagesResult() {
		// For serialization only
	}

	public LoadMessagesResult(Map<String, List<IMessageResponse>> conversations) {
		this.conversations=conversations;
	}

	public Map<String, List<IMessageResponse>> getConversations() {
		return conversations;
	}

}
