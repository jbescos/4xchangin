package com.changestuffs.shared.constants;

import com.gwtplatform.dispatch.shared.ActionImpl;

public enum ServletPaths {

	channelConnected("/_ah/channel/connected/"),
	channelDisconnected("/_ah/channel/disconnected/"),
	uploadImages("/upload/images"),
	downloadImages("/download/images"),
	redirectLogin("/redirect"),
	sitemap("/sitemap"),
	dontNotify("/dontNotify"),
	dispatch("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*");
	
	private String path;
	
	private ServletPaths(String path){
		this.path=path;
	}
	
	public String getPath(){
		return path;
	}
	
}
