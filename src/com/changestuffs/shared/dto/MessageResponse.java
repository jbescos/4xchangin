package com.changestuffs.shared.dto;

import com.changestuffs.shared.constants.MessageType;


public class MessageResponse implements IMessageResponse {

	private String message;
	private String from;
	private MessageType messageType;
	
	public void setMessage(String message) {
		this.message = message;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageResponse other = (MessageResponse) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageType != other.messageType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageResponse [message=" + message + ", from=" + from
				+ ", messageType=" + messageType + "]";
	}

}
