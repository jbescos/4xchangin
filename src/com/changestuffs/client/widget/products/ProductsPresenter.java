package com.changestuffs.client.widget.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.changestuffs.client.core.lookfor.LookforPresenter.Parameters;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.shared.actions.LookForAction;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.constants.Tags;
import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class ProductsPresenter extends
		PresenterWidget<ProductsPresenter.MyView> implements ProductsUiHandler {

	private final CurrentUser user;
	private final PlaceManager placeManager;
	private final SearcherTimer timer = new SearcherTimer();
	private Iterator<Map.Entry<String, IArticlesDto>> iterator;
	private final Map<String, IArticlesDto> filteredMap = new HashMap<String, IArticlesDto>();
	private final DispatchAsync dispatcher;
	private Tags tagEnum;
	
	public interface MyView extends View, HasUiHandlers<ProductsUiHandler> {
		void addResults(Map<String, IArticlesDto> articles);
		void clearSearcher();
		void clearCloud();
	}

	@Inject
	public ProductsPresenter(final DispatchAsync dispatcher, final PlaceManager placeManager, final EventBus eventBus, final MyView view, final CurrentUser user) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.placeManager = placeManager;
		this.user = user;
		this.dispatcher = dispatcher;
	}

	@Override
	public void handleKeypressTextBox(String value) {
		if (user.getSearchMap().get(tagEnum) != null) {
			GWT.log("Key pressed, looking for: " + value);
			timer.cancel();
			getView().clearCloud();
			filteredMap.clear();
			iterator = user.getSearchMap().get(tagEnum).getArticles().entrySet()
					.iterator();
			timer.text = value;
			timer.schedule(0);
			timer.scheduleRepeating(1);
		}
	}

	@Override
	public void handleClickName(String idHash) {
		GWT.log("Revealing id=" + idHash);
		PlaceRequest request = new PlaceRequest.Builder()
				.nameToken(placeManager.getCurrentPlaceRequest().getNameToken())
				.with(Parameters.id.name(), idHash).build();
		// Update browser first, and the reveal
		placeManager.updateHistory(request, true);
		placeManager.revealPlace(request, false);
	}
	
	public void retireveFromServer(final Tags tag) {
		dispatcher.execute(new LookForAction(tag, null),
				new AsyncCallback<LookForResult>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Some error", caught);
					}

					@Override
					public void onSuccess(LookForResult result) {
						Tags localtag = tag;
						tagEnum = localtag;
						user.getSearchMap().put(localtag, result);
						getView().clearCloud();
						getView().addResults(result.getArticles());
						watchRandomProduct(result);
					}
				});
	}
	
	private void watchRandomProduct(LookForResult result){
		List<String> ids = new ArrayList<String>(result.getArticles().keySet());
		int randomIndex = Random.nextInt(ids.size());
		PlaceRequest request = new PlaceRequest.Builder()
		.nameToken(NameTokens.getLookfor()).with(Parameters.id.name(), ids.get(randomIndex)).build();
		placeManager.revealPlace(request);
	}
	
	private class SearcherTimer extends Timer {

		private String text;

		@Override
		public void run() {
			if (iterator.hasNext()) {
				Entry<String, IArticlesDto> entry = iterator.next();
				if (entry.getValue().getName().toLowerCase().contains(text.toLowerCase())) {
					filteredMap.put(entry.getKey(), entry.getValue());
				}
			} else {
				getView().addResults(filteredMap);
				cancel();
				GWT.log("Search finished");
			}
		}

	}
	
}
