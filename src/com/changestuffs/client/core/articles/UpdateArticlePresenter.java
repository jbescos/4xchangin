package com.changestuffs.client.core.articles;

import java.util.Date;

import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.widget.texteditor.TextEditorPresenter;
import com.changestuffs.shared.actions.ArticlesAddAction;
import com.changestuffs.shared.actions.ArticlesAddResult;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.ArticlesDtoOut;
import com.changestuffs.shared.dto.IArticlesDto;
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

public class UpdateArticlePresenter extends
		PresenterWidget<UpdateArticlePresenter.MyView> implements UpdateArticleUiHandler, HasUiHandlers<UpdateSuccessArticleUiHandler> {

	private final TextEditorPresenter editorPresenter;
	private final DispatchAsync dispatcher;
	private UpdateSuccessArticleUiHandler uiHandlers;
	private String keyHash;
	private final PlaceManager placeManager;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetContent = new Type<RevealContentHandler<?>>();

	public interface MyView extends View, HasUiHandlers<UpdateArticleUiHandler> {
		void setFields(IArticlesDto dto);
	}

	@Inject
	public UpdateArticlePresenter(final EventBus eventBus, final MyView view,
			final TextEditorPresenter editorPresenter,
			final DispatchAsync dispatcher, PlaceManager placeManager) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.editorPresenter = editorPresenter;
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_SetContent, editorPresenter);
	}

	public void setFields(final IArticlesDto article) {
		dispatcher.execute(new LookForAction(null, article.getKeyHash()),
				new AsyncCallback<LookForResult>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Some error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						IArticlesDto dto = result.getArticles().get(article.getKeyHash());
						getView().setFields(dto);
						editorPresenter.setHtml(dto.getDescription());
						keyHash = article.getKeyHash();
					}
				});
	}

	@Override
	public void updateProduct(Tags tag, String name, String interestedIn, String language) {
		final ArticlesAddAction action = new ArticlesAddAction(tag, name, editorPresenter.getHtml(), new Date(), keyHash, interestedIn, language);
		dispatcher.execute(action, new AsyncCallback<ArticlesAddResult>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Some error", caught);
			}
			@Override
			public void onSuccess(ArticlesAddResult result) {
				ArticlesDtoOut articlesDto = new ArticlesDtoOut();
				articlesDto.setDate(action.getDate());
				articlesDto.setKeyHash(action.getKeyHash());
				articlesDto.setName(action.getName());
				articlesDto.setTag(result.getTagOut());
				uiHandlers.updateSuccessProduct(articlesDto);
				Builder request = new PlaceRequest.Builder().nameToken(
						NameTokens.getLookfor()).with(
						LookforPresenter.Parameters.id.name(),
						result.getIdHash());

				placeManager.revealPlace(request.build());
			}
		});
	}

	@Override
	public void setUiHandlers(UpdateSuccessArticleUiHandler uiHandlers) {
		this.uiHandlers=uiHandlers;
	}

	@Override
	public void handleCancel() {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens.getArticles());
		placeManager.revealPlace(request.build());
	}
}
