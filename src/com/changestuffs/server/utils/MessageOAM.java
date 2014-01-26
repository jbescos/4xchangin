package com.changestuffs.server.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.changestuffs.server.persistence.beans.Message;
import com.changestuffs.shared.actions.SendMessage;
import com.changestuffs.shared.dto.IMessageResponse;
import com.changestuffs.shared.dto.MessageResponse;
import com.google.inject.persist.Transactional;

public class MessageOAM {
	
	private final EntityManager model;
	private final Logger log = Logger.getLogger(getClass().getName());
	private final static String SEPARATOR="|";
	private final static String ESCAPED_SEPARATOR="\\|";
	
	@Inject
	public MessageOAM(EntityManager model){
		this.model=model;
	}
	
	private String buildUserIdSenderUserIdReceiver(String sender, String receiver){
		return sender+SEPARATOR+receiver;
	}
	
	@Transactional
	public void addMessage(SendMessage message, String sender){
		Message bean = new Message();
		bean.setUserIdSenderUserIdReceiver(buildUserIdSenderUserIdReceiver(sender, message.getEmail()));
		bean.setDate(new Date());
		bean.setBody(message.getMessage());
		model.persist(bean);
		log.info("Saving "+bean);
	}
	
	public Map<String, List<IMessageResponse>> getConversations(String me, Set<String> contacts){
		Map<String, List<IMessageResponse>> conversations = new HashMap<String, List<IMessageResponse>>();
		log.info("Looking conversations for "+contacts);
		for(String contact : contacts){
			List<Message> conversation = getConversation(me, contact);
			conversation.addAll(getConversation(contact, me));
			orderByDate(conversation);
			conversations.put(contact, getIMessageResponseByMessages(conversation));
		}
		return conversations;
	}
	
	private void orderByDate(List<Message> conversation){
		Collections.sort(conversation, new Comparator<Message>() {
			@Override
			public int compare(Message o1, Message o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private List<Message> getConversation(String person1, String person2){
		Query query = model.createNamedQuery(Message.GET_CONVERSATION);
		final String keyword = buildUserIdSenderUserIdReceiver(person1, person2);
		query.setParameter("userIdSenderUserIdReceiver", keyword);
		List<Message> conversation = new LinkedList<Message>((List<Message>) query.getResultList());
		return conversation;
	}
	
	private List<IMessageResponse> getIMessageResponseByMessages(List<Message> conversation){
		List<IMessageResponse> responses = new LinkedList<IMessageResponse>();
		for(Message message : conversation){
			MessageResponse response = new MessageResponse();
			response.setFrom(getSender(message));
			response.setMessage(message.getBody());
			responses.add(response);
			log.info("Converting "+message+" to "+response);
		}
		return responses;
	}
	
	private String getSender(Message message){
		return message.getUserIdSenderUserIdReceiver().split(ESCAPED_SEPARATOR)[0];
	}

}
