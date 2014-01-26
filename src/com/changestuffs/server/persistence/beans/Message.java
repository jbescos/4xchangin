package com.changestuffs.server.persistence.beans;

import java.util.Date;

import javax.jdo.annotations.Index;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;

@Entity
@NamedQueries({
	@NamedQuery(name = Message.GET_CONVERSATION, 
			query = "select x from Message x where x.userIdSenderUserIdReceiver = :userIdSenderUserIdReceiver"),
	@NamedQuery(name = Message.GET_ALL, 
			query = "select x from Message x")
	})
public class Message {

	@Transient
	public static final String GET_CONVERSATION = "getConversation";
	@Transient
	public static final String GET_ALL = "getAll";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	@Column(length=500)
	private String body;
	
	@Column
	private Date date;
	
	@Column
	@Index
    private String userIdSenderUserIdReceiver;

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUserIdSenderUserIdReceiver() {
		return userIdSenderUserIdReceiver;
	}
	public void setUserIdSenderUserIdReceiver(String userIdSenderUserIdReceiver) {
		this.userIdSenderUserIdReceiver = userIdSenderUserIdReceiver;
	}
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime
				* result
				+ ((userIdSenderUserIdReceiver == null) ? 0
						: userIdSenderUserIdReceiver.hashCode());
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
		Message other = (Message) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (userIdSenderUserIdReceiver == null) {
			if (other.userIdSenderUserIdReceiver != null)
				return false;
		} else if (!userIdSenderUserIdReceiver
				.equals(other.userIdSenderUserIdReceiver))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Message [key=" + key + ", body=" + body + ", date=" + date
				+ ", userIdSenderUserIdReceiver=" + userIdSenderUserIdReceiver
				+ "]";
	}
	
}
