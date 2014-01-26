package com.changestuffs.client.core.lookfor;

import java.util.Map.Entry;

import com.changestuffs.client.styles.Styles;
import com.changestuffs.shared.dto.IArticlesDto;
import com.changestuffs.shared.dto.IArticlesDto.Offers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class OffersView extends ViewImpl implements OffersPresenter.MyView {

	private OffersUiHandler uiHandlers;
	private final Widget widget;
	@UiField
	FlowPanel offers;
	@UiField
	SubmitButton addOffer;
	
	public interface Binder extends UiBinder<Widget, OffersView> {
	}

	@Inject
	public OffersView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setUiHandlers(OffersUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}

	@Override
	public void setOffers(IArticlesDto article) {
		offers.clear();
		for(Offers offer : article.getOffers()){
			FlowPanel groupOffer = new FlowPanel();
			groupOffer.setStylePrimaryName("row");
			for(Entry<String,String> entry : offer.getIdNameProducts().entrySet()){
				addOffer(entry.getKey(), entry.getValue(), groupOffer);
			}
			offers.add(groupOffer);
		}
	}
	
	@UiHandler("addOffer")
	public void addOfferClickHandler(ClickEvent click){
		uiHandlers.addOffer();
	}
	
	private void addOffer(final String id, String name, Panel panel){
		Label product = new Label(name);
		product.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleWatchProduct(id);
			}
		});
		product.setStylePrimaryName(Styles.CLICKABLE_OVAL_COLUMNS);
		panel.add(product);
	}

}
