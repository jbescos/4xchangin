package com.changestuffs.server.utils;

import com.changestuffs.shared.constants.MessageType;
import com.changestuffs.shared.dto.IMessageResponse;
import com.changestuffs.shared.dto.MessageResponse;
import com.changestuffs.shared.factory.MyAutoBeanFactory;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

@Singleton
public class DtoToJson {

	public String getJson(String body, String from, MessageType type){
		MyAutoBeanFactory beanFactory = AutoBeanFactorySource.create(MyAutoBeanFactory.class);
		MessageResponse response = new MessageResponse();
		response.setFrom(from);
		response.setMessage(body);
		response.setMessageType(type);
		AutoBean<IMessageResponse> bean = beanFactory.create(IMessageResponse.class, response);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
}
