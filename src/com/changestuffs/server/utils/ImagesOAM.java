package com.changestuffs.server.utils;

import javax.persistence.EntityManager;

import com.changestuffs.server.persistence.beans.Image;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.inject.Inject;

public class ImagesOAM {

	private final EntityManager model;

	@Inject 
	public ImagesOAM(EntityManager model) {
		this.model = model;
	}
	
	public Image getImage(String imageId){
		return model.find(Image.class, KeyFactory.stringToKey(imageId));
	}
	
}
