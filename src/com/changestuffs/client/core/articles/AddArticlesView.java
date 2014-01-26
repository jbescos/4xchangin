package com.changestuffs.client.core.articles;

import com.changestuffs.client.utils.WidgetUtils;
import com.changestuffs.client.widget.MyEnumListBox;
import com.changestuffs.shared.constants.Tags;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AddArticlesView extends ViewImpl implements
		AddArticlesPresenter.MyView {

	private final LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
	private final Widget widget;
	private AddArticlesUiHandler uiHandlers;
	@UiField
	FlowPanel textEditor;
	@UiField
	TextBox name;
	@UiField
	MyEnumListBox<Tags> tags;
	@UiField
	TextBox interestedIn;
	@UiField
	ListBox language;

	public interface Binder extends UiBinder<Widget, AddArticlesView> {
	}

	@Inject
	public AddArticlesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		tags.setEnum(Tags.values());
		WidgetUtils.selectItemListBox(language, localeInfo.getLocaleName());
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiHandler("save")
	public void submit(ClickEvent event) {
		uiHandlers.handleSubmit(
				Tags.valueOf(tags.getValue(tags.getSelectedIndex())),
				name.getText(), interestedIn.getText(),
				language.getValue(language.getSelectedIndex()));
	}
	
	@UiHandler("cancel")
	public void cancel(ClickEvent event) {
		uiHandlers.handleCancel();
	}

	@Override
	public void setUiHandlers(AddArticlesUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == AddArticlesPresenter.TYPE_SetContent) {
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

}
