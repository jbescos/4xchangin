package com.changestuffs.client.core.lookfor;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class LookforView extends ViewImpl implements LookforPresenter.MyView {

	private final ChangestuffsMessages messages = GWT.create(ChangestuffsMessages.class);
	private final Widget widget;
	@UiField
	Label articleName;
	@UiField
	FlowPanel articleDescription;
	@UiField
	HTMLPanel articlePanel;
	@UiField
	FlowPanel socialPanel;
	@UiField
	FlowPanel productsPanel;
	@UiField
	FlowPanel offersPanel;
	@UiField
	SpanElement interestedIn;

	public interface Binder extends UiBinder<Widget, LookforView> {
	}

	@Inject
	public LookforView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		articlePanel.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void viewArticle(IArticlesDto article) {
		articleDescription.clear();
		articleName.setText(article.getName());
		interestedIn.setInnerText(article.getInterestedIn());
		articleDescription.add(new HTML(article.getDescription()));
		articlePanel.setVisible(true);
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == LookforPresenter.TYPE_SocialContent) {
			setContent(socialPanel, content);
		}else if(slot == LookforPresenter.TYPE_OffersContent){
			setContent(offersPanel, content);
		}else if(slot == LookforPresenter.TYPE_ProductsContent){
			setContent(productsPanel, content);
		}else {
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
	public void noArticle() {
		Window.alert(messages.articleDoesntExist());
	}
}
