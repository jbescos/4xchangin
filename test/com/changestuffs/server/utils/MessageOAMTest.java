package com.changestuffs.server.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.changestuffs.server.guice.MyModule;
import com.changestuffs.shared.actions.SendMessage;
import com.changestuffs.shared.dto.IMessageResponse;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class MessageOAMTest {

	private final Logger log = Logger.getLogger(getClass().getName());
	private final static Injector injector = Guice.createInjector(new MyModule(), new JpaPersistModule("appEngine"));
	private LocalServiceTestHelper helper;
	private MessageOAM oam;
	private final String person1 = "jorge";
	private final String person2 = "pablo";
	
	@BeforeClass
	public static void beforeClass(){
		PersistService service = injector.getInstance(PersistService.class);
		service.start();
	}
	
	@Before
	public void before(){
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
		oam = injector.getInstance(MessageOAM.class);
	}
	
	@After
	public void after(){
		helper.tearDown();
	}
	
	@Test
	public void jorgeConversation(){
		oam.addMessage(createMessage("Hi Pablo, I'm Jorge", person2), person1);
		oam.addMessage(createMessage("Ah, sure!!", person1), person2);
		
		Set<String> contacts = new HashSet<String>();
		contacts.add(person2);
		Map<String, List<IMessageResponse>> conversations = oam.getConversations(person1, contacts);
		assertEquals(1, conversations.size());
		List<IMessageResponse> conversation = conversations.get(person2);
		assertNotNull(conversation);
		assertEquals(2, conversation.size());
		assertEquals(conversation.get(0).toString(), person1, conversation.get(0).getFrom());
		assertEquals(conversation.get(1).toString(), person2, conversation.get(1).getFrom());
	}
	
	private SendMessage createMessage(String body, String sendingTo){
		SendMessage message = new SendMessage(sendingTo, body);
		return message;
	}
	
}
