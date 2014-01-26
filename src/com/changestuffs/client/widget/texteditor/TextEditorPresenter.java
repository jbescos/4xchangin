package com.changestuffs.client.widget.texteditor;

import com.changestuffs.shared.constants.Constants;
import com.changestuffs.shared.constants.RequestParams;
import com.changestuffs.shared.constants.ServletPaths;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class TextEditorPresenter extends
		PresenterWidget<TextEditorPresenter.MyView> implements TextEditorUiHandler{
	
	public interface MyView extends View {
		void putImageUrl(String url, String alt);
		String getHtml();
		void setHtml(String html);
		void addUploadPanel(UploadView uploadView);
	}

	@Inject
	public TextEditorPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addUploadPanel();
	}
	
	private void addUploadPanel(){
		UploadView view = new UploadView();
		view.setUiHandlers(this);
		getView().addUploadPanel(view);
	}

	@Override
	public void handleFormResponse(SubmitCompleteEvent event) {
		String result = event.getResults();
		GWT.log("Image keys: "+result);
		String[] pairs = result.split(Constants.SEPARATOR);
		for(String pair:pairs){
			GWT.log("pair: "+pair);
			final String[] nameValue = pair.split(Constants.NAME_VALUE_SEPARATOR);
			StringBuilder imageUrl = new StringBuilder(
					ServletPaths.downloadImages.getPath()).append("?")
					.append(RequestParams.imageId.name()).append("=")
					.append(nameValue[0]);
			getView().putImageUrl(imageUrl.toString(), nameValue[1]);
			addUploadPanel();
		}
	}
	
	public void setHtml(String html){
		getView().setHtml(html);
	}
	
	public String getHtml(){
		return getView().getHtml();
	}
}
