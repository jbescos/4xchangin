package com.changestuffs.client.core.articles;

import java.util.Date;

import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.event.AddArticleEvent;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.widget.texteditor.TextEditorPresenter;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.ArticlesAddResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.ArticlesDtoOut;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.PlaceRequest.Builder;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AddArticlesPresenter extends
	PresenterWidget<AddArticlesPresenter.MyView>
		implements AddArticlesUiHandler {

	private final TextEditorPresenter editorPresenter;
	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetContent = new Type<RevealContentHandler<?>>();

	public interface MyView extends View, HasUiHandlers<AddArticlesUiHandler> {
	}

	@Inject
	public AddArticlesPresenter(EventBus eventBus, MyView view, TextEditorPresenter editorPresenter, DispatchAsync dispatcher,
			PlaceManager placeManager) {
		super(eventBus, view);
		this.editorPresenter = editorPresenter;
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		getView().setUiHandlers(this);
	}

	@Override
	public void handleSubmit(final Tags tag, final String name,
			final String interestedIn, final String language) {
		final Date date = new Date();
		dispatcher.execute(
				new ArticlesAddAction(tag, name, editorPresenter.getHtml(),
						date, interestedIn, language),
				new AsyncCallback<ArticlesAddResult>() {
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error", caught);
					}

					@Override
					public void onSuccess(ArticlesAddResult result) {
						ArticlesDtoOut article = new ArticlesDtoOut();
						article.setDate(date);
						article.setName(name);
						article.setTag(tag);
						article.setKeyHash(result.getIdHash());
						AddArticleEvent.fire(AddArticlesPresenter.this, article);
						Builder request = new PlaceRequest.Builder().nameToken(
								NameTokens.getLookfor()).with(
								LookforPresenter.Parameters.id.name(),
								result.getIdHash());

						placeManager.revealPlace(request.build());
					}
				});
	}

	@Override
	protected void onBind() {
		super.onBind();
		setInSlot(TYPE_SetContent, editorPresenter);
	}

	@Override
	public void handleCancel() {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens
				.getArticles());

		placeManager.revealPlace(request.build());
	}

}
