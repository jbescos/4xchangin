package com.changestuffs.shared.dto;

import com.changestuffs.shared.constants.MessageType;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface IMessageResponse extends IsSerializable{

	String getMessage();
	String getFrom();
	MessageType getMessageType();
	
}
