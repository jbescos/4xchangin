package com.changestuffs.client.core.login;

import com.changestuffs.client.widget.MyLabelImage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class LoginView extends ViewImpl implements LoginPresenter.MyView {

	private final Widget widget;
	private LoginUiHandler uiHandlers;
	
	@UiField
	HTMLPanel federatedLogin;
	@UiField
	MyLabelImage google;
	@UiField
	MyLabelImage yahoo;
	@UiField
	MyLabelImage mySpace;
	@UiField
	MyLabelImage aol;
	@UiField
	MyLabelImage myOpenId;
	@UiField 
	MyLabelImage liveJournal;
	@UiField 
	MyLabelImage hyves;
	@UiField 
	MyLabelImage blogger;
	@UiField 
	MyLabelImage wordpress;
	@UiField 
	MyLabelImage orange;
	@UiField
	TextBox completeUrl;

	public interface Binder extends UiBinder<Widget, LoginView> {
	}

	@Inject
	public LoginView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		federatedLogin.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(LoginUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}
	
	@UiHandler("google")
	public void google(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(google.getValue());
	}
	
	@UiHandler("hyves")
	public void hyves(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(hyves.getValue());
	}
	
	@UiHandler("orange")
	public void orange(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(orange.getValue());
	}
	
	@UiHandler("yahoo")
	public void yahoo(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(yahoo.getValue());
	}
	
	@UiHandler("mySpace")
	public void mySpace(ClickEvent event) {
		federatedLogin.setVisible(true);
		completeUrl.setText(mySpace.getValue()+"/username");
	}
	
	@UiHandler("liveJournal")
	public void liveJournal(ClickEvent event) {
		federatedLogin.setVisible(true);
		completeUrl.setText("username."+liveJournal.getValue());
	}
	
	@UiHandler("blogger")
	public void blogger(ClickEvent event) {
		federatedLogin.setVisible(true);
		completeUrl.setText("blogname."+blogger.getValue());
	}
	
	@UiHandler("wordpress")
	public void wordpress(ClickEvent event) {
		federatedLogin.setVisible(true);
		completeUrl.setText("username."+wordpress.getValue());
	}
	
	@UiHandler("aol")
	public void aol(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(aol.getValue());
	}
	
	@UiHandler("myOpenId")
	public void myOpenId(ClickEvent event) {
		federatedLogin.setVisible(false);
		uiHandlers.createLogin(myOpenId.getValue());
	}
	
	@UiHandler("go")
	public void go(ClickEvent event) {
		uiHandlers.createLogin(completeUrl.getText());
	}
}
