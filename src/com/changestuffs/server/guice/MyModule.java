package com.changestuffs.server.guice;

import java.security.SecureRandom;

import com.changestuffs.server.servlets.email.HtmlEngine;
import com.changestuffs.server.servlets.impl.ChannelOffImpl;
import com.changestuffs.server.servlets.impl.ChannelOnImpl;
import com.changestuffs.server.servlets.impl.DontNotify;
import com.changestuffs.server.servlets.impl.DownloadImageImpl;
import com.changestuffs.server.servlets.impl.FileUploadImpl;
import com.changestuffs.server.servlets.impl.IServletManager;
import com.changestuffs.server.servlets.impl.RedirectImpl;
import com.changestuffs.server.servlets.impl.Sitemap;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.server.utils.DtoToJson;
import com.changestuffs.server.utils.ImagesOAM;
import com.changestuffs.server.utils.MessageOAM;
import com.changestuffs.server.utils.UserBeanOAM;
import com.changestuffs.shared.constants.ServletPaths;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class MyModule extends AbstractModule {
	
	@Override
	protected void configure() {
		
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.uploadImages.getPath())).to(FileUploadImpl.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.downloadImages.getPath())).to(DownloadImageImpl.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.redirectLogin.getPath())).to(RedirectImpl.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.sitemap.getPath())).to(Sitemap.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.channelConnected.getPath())).to(ChannelOnImpl.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.channelDisconnected.getPath())).to(ChannelOffImpl.class);
		bind(IServletManager.class).annotatedWith(Names.named(ServletPaths.dontNotify.getPath())).to(DontNotify.class);
		bind(ArticlesOAM.class);
	    bind(UserBeanOAM.class);
	    bind(ImagesOAM.class);
	    bind(MessageOAM.class);
	    bind(SecureRandom.class).in(Singleton.class);
	    bind(DtoToJson.class);
	    bind(HtmlEngine.class);
	}

}
