package com.changestuffs.client.core.lookfor;

import com.changestuffs.client.place.NameTokens;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.PlaceRequest.Builder;

public class OffersPresenter extends PresenterWidget<OffersPresenter.MyView> implements OffersUiHandler{

	private IArticlesDto article;
	private final PlaceManager placeManager;
	
	public interface MyView extends View, HasUiHandlers<OffersUiHandler> {
		void setOffers(IArticlesDto article);
	}
	
	@Inject
	public OffersPresenter(final EventBus eventBus, final MyView view, final PlaceManager placeManager) {
		super(eventBus, view);
		this.placeManager=placeManager;
		getView().setUiHandlers(this);
	}
	
	public void setOffers(IArticlesDto article){
		this.article=article;
		getView().setOffers(article);
	}

	@Override
	public void handleWatchProduct(String id) {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens.getLookfor()).
				with(LookforPresenter.Parameters.id.name(), id);
		
		placeManager.revealPlace(request.build());
	}

	@Override
	public void addOffer() {
		Builder request = new PlaceRequest.Builder().nameToken(NameTokens.getInterests()).
				with(LookforPresenter.Parameters.id.name(), article.getKeyHash());
		
		placeManager.revealPlace(request.build());
	}
	
}
