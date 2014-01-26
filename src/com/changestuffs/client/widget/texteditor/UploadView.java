package com.changestuffs.client.widget.texteditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.HasUiHandlers;

public class UploadView extends Composite implements
		HasUiHandlers<TextEditorUiHandler> {

	interface MyUiBinder extends UiBinder<Widget, UploadView> {
	}

	private final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	Image loadingImage;
	@UiField
	FormPanel form;
	@UiField
	FileUpload upload;
	private TextEditorUiHandler uiHandlers;
	
	public UploadView(){
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("form")
	public void onSubmitComplete(SubmitCompleteEvent event) {
		uiHandlers.handleFormResponse(event);
		loadingImage.setVisible(false);
		upload.setVisible(true);
		upload.setEnabled(false);
	}

	@UiHandler("form")
	public void onSubmit(SubmitEvent event) {
		loadingImage.setVisible(true);
		upload.setVisible(false);
	}

	@UiHandler("upload")
	public void submit(ChangeEvent event) {
		GWT.log("Event: "+event);
		form.submit();
	}

	@Override
	public void setUiHandlers(TextEditorUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}

}
