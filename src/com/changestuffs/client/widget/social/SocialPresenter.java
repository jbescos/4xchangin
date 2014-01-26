package com.changestuffs.client.widget.social;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SocialPresenter extends PresenterWidget<SocialPresenter.MyView> {

	public interface MyView extends View {
		void setUrl(String url);
	}

	@Inject
	public SocialPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().setUrl(Window.Location.getHref());
	}
	
	public void updateUrl(String url){
		GWT.log("Updating Facebook url to: "+url);
		getView().setUrl(url);
	}
	
}
