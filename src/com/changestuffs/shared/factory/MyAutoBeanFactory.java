package com.changestuffs.shared.factory;

import com.changestuffs.shared.dto.IMessageResponse;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MyAutoBeanFactory extends AutoBeanFactory {

	public AutoBean<IMessageResponse> makeIMessageResponse();
	
}
