package com.changestuffs.client.core.profile;

import com.changestuffs.shared.actions.GetUserInfoResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProfileView extends ViewImpl implements ProfilePresenter.MyView {

	private final Widget widget;
	private ProfileUiHandler uiHandlers;
	@UiField
	Label email;
	@UiField
	TextBox cell;
	@UiField
	TextBox country;
	@UiField
	TextBox city;
	@UiField
	CheckBox receiveEmails;
	@UiField
	SubmitButton submit;
	@UiField 
	Label updated;

	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		successUpdated(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiHandler("submit")
	public void submit(ClickEvent event){
		uiHandlers.handleSubmit(cell.getText(), city.getText(), country.getText(), receiveEmails.isAttached());
	}

	@Override
	public void setUiHandlers(ProfileUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}

	@Override
	public void setFields(GetUserInfoResult result) {
		cell.setText(result.getCell());
		city.setText(result.getCity());
		country.setText(result.getCountry());
		receiveEmails.setValue(result.isReceiveEmails());
		email.setText(result.getEmail());
	}

	@Override
	public void successUpdated(boolean done) {
		updated.setVisible(done);
		
	}
}
