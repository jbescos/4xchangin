package com.changestuffs.client.widget.social;

import com.changestuffs.shared.constants.Constants;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SocialView extends ViewImpl implements SocialPresenter.MyView {

	private final Widget widget;
	@UiField
	FlowPanel fbLike;
	
	private final LocaleInfo locale = LocaleInfo.getCurrentLocale();

	public interface Binder extends UiBinder<Widget, SocialView> {
	}

	@Inject
	public SocialView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		fbLike.getElement().setAttribute("class", "fb-like");
		fbLike.getElement().setAttribute("data-send", "true");
		fbLike.getElement().setAttribute("data-width", "450");
		fbLike.getElement().setAttribute("data-show-faces", "true");
		GWT.log("Locale: "+locale.getLocaleName());
		addNeededScript();
	}

	private void addNeededScript(){
		final Element bodyElement = Document.get().getElementsByTagName("body").getItem(0);
        final Element fbRootElement = Document.get().createElement("div");
        fbRootElement.setId("fb-root");

        final String scriptStr = "(function(d, s, id) {"
                                 + "var js, fjs = d.getElementsByTagName(s)[0];"
                                 + "if (d.getElementById(id)) return;"
                                 + "js = d.createElement(s); js.id = id;"
                                 + "js.src = \"//connect.facebook.net/"+locale.getLocaleName()+"/all.js#xfbml=1&appId="+Constants.FACEBOOK_APP_ID+"\";"
                                 + "fjs.parentNode.insertBefore(js, fjs);"
                                 + "}(document, 'script', 'facebook-jssdk'));";

        final ScriptElement scriptElement = Document.get().createScriptElement(scriptStr);

        // facebook recommends to place the script just after the body tag
        bodyElement.insertFirst(fbRootElement);
        bodyElement.insertAfter(scriptElement, fbRootElement);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUrl(String url) {
		fbLike.getElement().setAttribute("data-href", url);
	}
	
}
