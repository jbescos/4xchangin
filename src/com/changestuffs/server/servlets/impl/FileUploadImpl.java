package com.changestuffs.server.servlets.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.changestuffs.server.guice.aspect.Logued;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.constants.Constants;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class FileUploadImpl implements IServletManager{
	
	private final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Inject
	private Provider<ArticlesOAM> provider;

	@Logued
	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Uploading a file");
		Map<String, Blob> images = new LinkedHashMap<String, Blob>();
		// Get the image representation
		try{
		    ServletFileUpload upload = new ServletFileUpload();
		    FileItemIterator iter = upload.getItemIterator(req);
		    while(iter.hasNext()){
		    	FileItemStream imageItem = iter.next();
		    	InputStream imgStream = imageItem.openStream();
		    	byte[] data = IOUtils.toByteArray(imgStream);
		    	imgStream.close();
		    	if(!imageItem.isFormField() && data.length>0){
		    		log.info("Is a file: "+imageItem.getName());
				    Blob imageBlob = new Blob(resizeImage(data));
				    images.put(imageItem.getName(), imageBlob);
		    	}
		    }
	    	ArticlesOAM oam = provider.get();
	    	UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
	    	Map<String, String> imageKeys = oam.addImages(user.getEmail(), images);
	    	writeResponse(resp, imageKeys);
		    log.info("Servlet has finalized its work");
		}catch(FileUploadException e){
			log.log(Level.SEVERE, "Error parsing file", e);
		}
	}
	
	private void writeResponse(HttpServletResponse resp, Map<String, String> keys) throws IOException{
		BufferedWriter bf=new BufferedWriter(resp.getWriter());
		try{
			resp.setContentType("text/html");
			for(Entry<String, String> entry:keys.entrySet()){
				bf.write(entry.getKey());
				bf.write(Constants.NAME_VALUE_SEPARATOR);
				bf.write(entry.getValue());
				bf.write(Constants.SEPARATOR);
	    	}
		}finally{
			bf.flush();
			bf.close();
		}
	}

	private byte[] resizeImage(byte[] data){
		Image oldImage = ImagesServiceFactory.makeImage(data);
        Transform resize = ImagesServiceFactory.makeResize(620, 620);

        Image newImage = imagesService.applyTransform(resize, oldImage);

        byte[] newImageData = newImage.getImageData();
        
        return newImageData;
	}

}
