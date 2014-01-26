package com.changestuffs.client.widget.chat;

import com.changestuffs.client.core.components.ChatUiHandler;
import com.changestuffs.client.styles.Styles;
import com.changestuffs.shared.utils.StringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.HasUiHandlers;

public class ChatPanel extends Composite implements HasUiHandlers<ChatUiHandler>  {

	private final int ENTER = 13;
	private final static Binder uiBinder = GWT.create(Binder.class);
	@UiField
	Label title;
	@UiField
	ScrollPanel scroll;
	@UiField
	FlowPanel textPanel;
	@UiField
	TextBox text;
	@UiField
	HTMLPanel content;
	private String lastWriter;
	private final String talkingWith;
	private boolean open = false;
	private ChatUiHandler uiHandlers;

	public interface Binder extends UiBinder<Widget, ChatPanel> {
	}

	@UiConstructor
	public ChatPanel(String talkingWith) {
		initWidget(uiBinder.createAndBindUi(this));
		this.talkingWith = talkingWith;
		title.setText(StringUtils.subString(25, talkingWith));
		title.setTitle(talkingWith);
		open();
	}
	
	public void setOnline(boolean online){
		String className = online ? Styles.CLICKABLE_ONLINE:Styles.CLICKABLE_OFFLINE;
		title.setStylePrimaryName(className);
	}
	
	@UiHandler("title")
	public void titleEvent(ClickEvent event){
		open();
	}
	
	@UiHandler("text")
	public void text(KeyUpEvent event){
		if(ENTER == event.getNativeKeyCode())
			submitMyMessage();
	}
	
	private void submitMyMessage(){
		uiHandlers.submitMessage(talkingWith, text.getText());
		text.setText("");
	}
	
	private void open(){
		content.setVisible(open);
		if(open){
			open = false;
			scroll.scrollToBottom();
		}else{
			open = true;
		}
	}
	
	public void addText(String writer, String message, boolean openMessages){
		if(openMessages){
			open = true;
			content.setVisible(open);	
		}
		if(!writer.equals(lastWriter)){
			Label labelWriter = new Label(writer+":");
			labelWriter.setStylePrimaryName(Styles.DEFAULT_LABEL);
			textPanel.add(labelWriter);
			lastWriter = writer;
		}
		Label labelWrite = new Label(message);
		labelWrite.getElement().setAttribute("style", "font-size: 12px; margin: 0; padding: 0;");
		textPanel.add(labelWrite);
		scroll.scrollToBottom();
	}

	@Override
	public void setUiHandlers(ChatUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
}
