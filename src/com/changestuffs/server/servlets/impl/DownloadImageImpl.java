package com.changestuffs.server.servlets.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.server.persistence.beans.Image;
import com.changestuffs.server.utils.ImagesOAM;
import com.changestuffs.shared.constants.RequestParams;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class DownloadImageImpl implements IServletManager {

	private final static String MIME_TYPE = "image/*";
	@Inject
	private Provider<ImagesOAM> provider;

	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String imageId = req.getParameter(RequestParams.imageId.name());
		if (imageId != null) {
			resp.setContentType(MIME_TYPE);
			ImagesOAM oam = provider.get();
			Image image = oam.getImage(imageId);
			if (image != null) {
				resp.getOutputStream().write(image.getImage().getBytes());
			}
		}
	}

}
