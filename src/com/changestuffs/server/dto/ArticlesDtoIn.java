package com.changestuffs.server.dto;

import java.util.Map;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ArticlesDtoIn implements IsSerializable {

	private String productId;
	private Map<String, Blob> images;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Map<String, Blob> getImages() {
		return images;
	}
	public void setImages(Map<String, Blob> images) {
		this.images = images;
	}
	
}
