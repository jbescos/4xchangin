package com.changestuffs.client.core.interests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.changestuffs.client.i18n.ChangestuffsMessages;
import com.changestuffs.client.styles.Styles;
import com.changestuffs.client.widget.MyTable;
import com.changestuffs.shared.actions.GetOffersResult.OffersPerProduct;
import com.changestuffs.shared.actions.LookForResult;
import com.changestuffs.shared.dto.IArticlesDto;
import com.changestuffs.shared.dto.IArticlesDto.Offers;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class InterestsView extends ViewImpl implements
		InterestsPresenter.MyView {

	private final Widget widget;
	private InterestsUiHandler uiHandlers;
	private String offerId;
	private final Map<String, String> productIdNames = new HashMap<String, String>();
	private final ChangestuffsMessages messages = GWT.create(ChangestuffsMessages.class);
	@UiField
	MyTable offersSent;
	@UiField
	MyTable offersReceived;
	@UiField
	Button submitOffer;
	@UiField
	VerticalPanel checkBoxes;
	@UiField
	DialogBox productsPanel;

	public interface Binder extends UiBinder<Widget, InterestsView> {
	}

	@Inject
	public InterestsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setUiHandlers(InterestsUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}

	@Override
	public void setReceivedOffers(Map<String, IArticlesDto> articles) {
		offersReceived.removeBody();
		for (IArticlesDto dto : articles.values()) {
			for (Offers offer : dto.getOffers()) {
				addReceivedOffer(dto.getName(), dto.getKeyHash(), offer);
			}
		}
	}

	private void addReceivedOffer(final String myProductName,
			final String myProductId, final Offers offer) {
		Label productName = new Label(myProductName);
		productName.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleProduct(myProductId);
			}
		});
		Set<Widget> offersPerProduct = new HashSet<Widget>();
		for (Entry<String, String> entryIdName : offer.getIdNameProducts().entrySet()) {
			offersPerProduct.add(getOfferPerProductLabel(entryIdName.getValue(),
					entryIdName.getKey()));
		}

		final Label contact = new Label();
		final Label friend = new Label(offer.getUserId());
		
		if(offer.isFriend()){
			friend.setVisible(true);
			contact.setVisible(false);
		}else{
			friend.setVisible(false);
			contact.setVisible(true);
		}
		
		contact.getElement().setInnerHTML("<i class='icon-user-add'/>");
		contact.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if(Window.confirm(messages.addUser())){
					uiHandlers.handleContact(offer.getUserId());
					contact.setVisible(false);
					friend.setVisible(true);
				}
			}
		});
		friend.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleFriend(offer.getUserId());
			}
		});
		Label[] socialLabels = {contact, friend};
		productName.setStylePrimaryName(Styles.CLICKABLE_OVAL);
		contact.setStylePrimaryName(Styles.CLICKABLE);
		friend.setStylePrimaryName(Styles.CLICKABLE_OVAL);
		offersReceived.addCell(myProductId, productName);
		offersReceived.addCell(myProductId, offersPerProduct.toArray(new Widget[0]));
		offersReceived.addCell(myProductId, socialLabels);
	}

	private Label getOfferPerProductLabel(final String name,
			final String id) {
		Label productOffered = new Label(name);
		productOffered.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleProduct(id);
			}
		});
		productOffered.setStylePrimaryName(Styles.CLICKABLE_OVAL_COLUMNS);
		return productOffered;
	}

	@Override
	public void setSentOffers(Map<String, OffersPerProduct> offersPerProducts) {
		for (OffersPerProduct offerPerProduct : offersPerProducts.values()) {
			addSentOffer(offerPerProduct);
		}
	}

	private void addSentOffer(final OffersPerProduct offerPerProduct) {
		Label productName = new Label(offerPerProduct.getProductName());
		productName.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleProduct(offerPerProduct.getProductId());
			}
		});
		Set<Widget> offersPerProduct = new HashSet<Widget>();
		for (Entry<String, String> entryOffer : offerPerProduct
				.getProductOffers().entrySet()) {
			offersPerProduct.add(getOfferPerProductLabel(entryOffer.getValue(),
					entryOffer.getKey()));
		}
		Label addOffer = new Label();
		addOffer.getElement().setInnerHTML("<i class='icon-plus-circled'/>");
		addOffer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				uiHandlers.handleWatchArticles(offerPerProduct.getOfferKey());
				productsPanel.setVisible(true);
				productsPanel.center();
				productsPanel.show();
			}
		});
		Label remove = new Label();
		remove.getElement().setInnerHTML("<i class='icon-trash'/>");
		remove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (Window.confirm(messages.removeOffer())) {
					uiHandlers.handleRemoveOffer(offerPerProduct.getOfferKey());
				}
			}
		});
		productName.setStylePrimaryName(Styles.CLICKABLE_OVAL);
		addOffer.setStylePrimaryName(Styles.CLICKABLE);
		remove.setStylePrimaryName(Styles.CLICKABLE);
		offersSent.addCell(offerPerProduct.getOfferKey(), productName);
		offersSent.addCell(offerPerProduct.getOfferKey(), offersPerProduct.toArray(new Widget[0]));
		offersSent.addCell(offerPerProduct.getOfferKey(), addOffer);
		offersSent.addCell(offerPerProduct.getOfferKey(), remove);
	}

	@Override
	public void watchArticles(String offerId, LookForResult result) {
		this.offerId = offerId;
		checkBoxes.clear();
		productIdNames.clear();
		for (IArticlesDto article : result.getArticles().values()) {
			final CheckBox check = new CheckBox(article.getName());
			checkBoxes.add(check);
			addHandlerCheckBox(check, article);
		}
	}
	
	private void addHandlerCheckBox(CheckBox check, final IArticlesDto article){
		check.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				if(checked)
					productIdNames.put(article.getKeyHash(), article.getName());
				else
					productIdNames.remove(article.getKeyHash());
			}
		});
	}

	@UiHandler("submitOffer")
	public void submitOffer(ClickEvent event) {
		uiHandlers.handleAddOffer(offerId, productIdNames);
		productsPanel.hide();
	}

	@Override
	public void updateSentOffer(String offerId,
			OffersPerProduct offersPerProduct) {
		removeSentOffer(offerId);
		addSentOffer(offersPerProduct);
	}

	@Override
	public void removeSentOffer(String offerId) {
		offersSent.removeRow(offerId);
	}

	@Override
	public void userIsYourFriend(String email, boolean alreadyAdded) {
		// Dont do nothing
	}
}
