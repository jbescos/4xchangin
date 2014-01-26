package com.changestuffs.client.core.articles;

import java.util.HashMap;
import java.util.Map;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.client.i18n.Translator;
import com.changestuffs.client.styles.Styles;
import com.changestuffs.client.utils.DateFormatter;
import com.changestuffs.client.widget.MyTable;
import com.changestuffs.shared.constants.Constants;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ArticlesView extends ViewImpl implements ArticlesPresenter.MyView {

	private final Widget widget;
	private ArticlesUiHandler uiHandlers;
	private final DateFormatter dateFormatter = new DateFormatter(Constants.DATE_PATTERN);
	private Map<String, IArticlesDto> articlesDto = new HashMap<String, IArticlesDto>();
	private final Translator translator = GWT.create(Translator.class);
	private final ChangestuffsMessages messages = GWT.create(ChangestuffsMessages.class);

	@UiField
	MyTable articles;
	@UiField
	Label nameHead;
	@UiField
	Label dateHead;
	@UiField
	Label tagHead;
	@UiField
	Label removeHead;

	public interface Binder extends UiBinder<Widget, ArticlesView> {
	}

	@Inject
	public ArticlesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		this.articles.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(final ArticlesUiHandler uiHandlers) {
		GWT.log("Setting UiHandler in ArticlesView");
		this.uiHandlers = uiHandlers;
	}

	@Override
	public void addArticle(final IArticlesDto article, final String index) {
		GWT.log("Adding article " + article.getName() + " tag "
				+ article.getTag());

		Label name = new Label(article.getName());
		handleClickName(name, article.getKeyHash(),article.getTag());
		Label date = new Label(dateFormatter.dateToString(article.getDate()));
		Label tag = new Label(translator.getString(article.getTag().name()));
		Label edit = new Label();
		edit.getElement().setInnerHTML("<i class='icon-pencil'/>");
		edit.setStylePrimaryName(Styles.CLICKABLE);
		edit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.handleEditArticle(article);
			}
		});
		Label remove = new Label();
		remove.getElement().setInnerHTML("<i class='icon-trash'/>");
		remove.setStylePrimaryName(Styles.CLICKABLE);
		remove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm(messages.removeArticle(article.getName()))) {
					uiHandlers.handleRemoveArticle(article.getKeyHash());
				}
			}
		});
		this.articles.addCell(index, name);
		this.articles.addCell(index, date);
		this.articles.addCell(index, tag);
		this.articles.addCell(index, edit);
		this.articles.addCell(index, remove);
		if (!this.articles.isVisible())
			this.articles.setVisible(true);

		articlesDto.put(index, article);
	}

	private void handleClickName(Label label, final String idHash,
			final Tags tag) {
		label.setStylePrimaryName(Styles.CLICKABLE_OVAL);
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.handleClickName(tag, idHash);
			}
		});
	}

	@Override
	public void removeArticle(String indexOut) {
		GWT.log("Removing index " + indexOut);
		articlesDto.remove(indexOut);
		GWT.log("Size of list: " + articlesDto.size());
		articles.removeRow(indexOut);
	}
	
	@UiHandler("create")
	public void cancel(ClickEvent event) {
		uiHandlers.handleRevealAddArticle();
	}

}
