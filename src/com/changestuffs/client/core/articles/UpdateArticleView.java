package com.changestuffs.client.core.articles;

import com.changestuffs.client.utils.WidgetUtils;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class UpdateArticleView extends ViewImpl implements
		UpdateArticlePresenter.MyView {

	private final Widget widget;
	private UpdateArticleUiHandler uiHandlers;
	private final LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();

	@UiField
	FlowPanel textEditor;
	@UiField
	TextBox name;
	@UiField
	TextBox interestedIn;
	@UiField
	SubmitButton cancel;
	@UiField
	SubmitButton save;
	@UiField
	ListBox language;

	public interface Binder extends UiBinder<Widget, UpdateArticleView> {
	}

	@Inject
	public UpdateArticleView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		WidgetUtils.selectItemListBox(language, localeInfo.getLocaleName());
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		GWT.log("Setting in slot " + content);
		if (slot == UpdateArticlePresenter.TYPE_SetContent) {
			setContent(textEditor, content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void setContent(FlowPanel base, IsWidget content) {
		base.clear();

		if (content != null) {
			base.add(content);
		}
	}

	@Override
	public void setFields(IArticlesDto dto) {
		name.setText(dto.getName());
		interestedIn.setText(dto.getInterestedIn());
	}

	@UiHandler("cancel")
	public void cancel(ClickEvent event) {
		uiHandlers.handleCancel();
	}

	@UiHandler("save")
	public void save(ClickEvent event) {
		uiHandlers.updateProduct(null, name.getText(), interestedIn.getText(), language.getValue(language.getSelectedIndex()));
	}

	@Override
	public void setUiHandlers(UpdateArticleUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}

}
