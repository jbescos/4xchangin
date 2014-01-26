package com.changestuffs.server.servlets.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changestuffs.client.place.NameTokens;
import com.changestuffs.server.utils.ArticlesOAM;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.constants.Constants;
import com.changestuffs.shared.constants.Locales;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class Sitemap implements IServletManager {

	private final static String CONTENT_TYPE = "application/xml";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.SITE_MAP_DATE_PATTERN);
	
	@Inject
	private Provider<ArticlesOAM> provider;
	
	@Override
	public void manage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType(CONTENT_TYPE);
		ArticlesOAM oam = provider.get();
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		addUrl(builder, Locales.es);
		addUrl(builder, Locales.en);
		for(Tags tag : Tags.values()){
			Map<String, IArticlesDto> articlesPerTag = getAllArticles(oam, tag);
			for(IArticlesDto dto : articlesPerTag.values()){
				addUrl(builder, dto);
			}
		}
		builder.append("</urlset>");
		resp.getWriter().println(builder.toString());
	}
	
	private void addUrl(StringBuilder builder, Locales locale){
		builder.append("<url>");
		builder.append("<loc>");
		builder.append(Constants.URL_BASE+"/?"+locale.getPair()+"#"+NameTokens.home);
		builder.append("</loc>");
		builder.append("<changefreq>never</changefreq>");
		builder.append("</url>");
	}
	
	private void addUrl(StringBuilder builder, IArticlesDto dto){
		builder.append("<url>");
		builder.append("<loc>");
		builder.append(Constants.URL_BASE+"/?"+dto.getLocale().getPair()+"#"+NameTokens.lookfor+";id=").append(dto.getKeyHash());
		builder.append("</loc>");
		builder.append("<lastmod>");
		builder.append(dateFormat.format(dto.getDate()));
		builder.append("</lastmod>");
		builder.append("<changefreq>monthly</changefreq>");
		builder.append("</url>");
	}
	
	private Map<String, IArticlesDto> getAllArticles(ArticlesOAM oam, Tags tag){
		LookForAction input = new LookForAction(tag, null);
		return oam.getArticles(input, null);
	}

}
