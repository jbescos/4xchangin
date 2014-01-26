package com.changestuffs.client.widget.products;

import java.util.Map;
import java.util.Map.Entry;

import com.changestuffs.client.styles.Styles;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProductsView extends ViewImpl implements ProductsPresenter.MyView{

	private final Widget widget;
	private ProductsUiHandler uiHandlers;
	@UiField
	FlowPanel cloud;
	@UiField
	TextBox searcher;
	
	public interface Binder extends UiBinder<Widget, ProductsView> {
	}

	@Inject
	public ProductsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setUiHandlers(ProductsUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}
	
	@UiHandler("searcher")
	public void keyPress(KeyUpEvent event){
		uiHandlers.handleKeypressTextBox(searcher.getValue());
	}
	
	@Override
	public void addResults(Map<String, IArticlesDto> articles) {
		for(Entry<String, IArticlesDto> article:articles.entrySet()){
			Label link = new Label(article.getValue().getName());
			link.setStylePrimaryName(Styles.CLICKABLE_OVAL_COLUMNS);
			handleClickName(link, article.getKey());
			cloud.add(link);
		}
	}
	
	private void handleClickName(HasClickHandlers handler, final String idHash){
		handler.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.handleClickName(idHash);
			}
		});
	}
	
	@Override
	public void clearSearcher() {
		searcher.setValue("");
	}
	
	@Override
	public void clearCloud() {
		cloud.clear();
	}
	
}
