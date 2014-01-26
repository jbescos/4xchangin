package com.changestuffs.client.gin;

import com.changestuffs.client.core.articles.AddArticlesPresenter;
import com.changestuffs.client.core.articles.AddArticlesView;
import com.changestuffs.client.core.articles.ArticlesPresenter;
import com.changestuffs.client.core.articles.ArticlesView;
import com.changestuffs.client.core.articles.UpdateArticlePresenter;
import com.changestuffs.client.core.articles.UpdateArticleView;
import com.changestuffs.client.core.components.ChatPresenter;
import com.changestuffs.client.core.components.ChatView;
import com.changestuffs.client.core.components.LoggedMenuPresenter;
import com.changestuffs.client.core.components.LoggedMenuView;
import com.changestuffs.client.core.components.MainPresenter;
import com.changestuffs.client.core.components.MainView;
import com.changestuffs.client.core.components.NotLoggedMenuPresenter;
import com.changestuffs.client.core.components.NotLoggedMenuView;
import com.changestuffs.client.core.contact.ContactPresenter;
import com.changestuffs.client.core.contact.ContactView;
import com.changestuffs.client.core.dontNotify.DontNotifyPresenter;
import com.changestuffs.client.core.dontNotify.DontNotifyView;
import com.changestuffs.client.core.home.HomePresenter;
import com.changestuffs.client.core.home.HomeView;
import com.changestuffs.client.core.interests.InterestsPresenter;
import com.changestuffs.client.core.interests.InterestsView;
import com.changestuffs.client.core.login.LoginPresenter;
import com.changestuffs.client.core.login.LoginView;
import com.changestuffs.client.core.lookfor.LookforPresenter;
import com.changestuffs.client.core.lookfor.LookforView;
import com.changestuffs.client.core.lookfor.OffersPresenter;
import com.changestuffs.client.core.lookfor.OffersView;
import com.changestuffs.client.core.messages.MessagesPresenter;
import com.changestuffs.client.core.messages.MessagesView;
import com.changestuffs.client.core.profile.ProfilePresenter;
import com.changestuffs.client.core.profile.ProfileView;
import com.changestuffs.client.gatekeeper.NeedsLoginKeeper;
import com.changestuffs.client.place.NameTokens;
import com.changestuffs.client.resources.CurrentUser;
import com.changestuffs.client.widget.products.ProductsPresenter;
import com.changestuffs.client.widget.products.ProductsView;
import com.changestuffs.client.widget.social.SocialPresenter;
import com.changestuffs.client.widget.social.SocialView;
import com.changestuffs.client.widget.texteditor.TextEditorPresenter;
import com.changestuffs.client.widget.texteditor.TextEditorView;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsNavigationTracker;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(DefaultPlaceManager.class));
        install(new DispatchAsyncModule());
        
        bindConstant().annotatedWith(GaAccount.class).to("UA-42795708-1");
        bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();

		bindPresenter(HomePresenter.class, HomePresenter.MyView.class,
				HomeView.class, HomePresenter.MyProxy.class);
		
		bindPresenter(DontNotifyPresenter.class, DontNotifyPresenter.MyView.class,
				DontNotifyView.class, DontNotifyPresenter.MyProxy.class);

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		bindPresenter(ArticlesPresenter.class, ArticlesPresenter.MyView.class,
				ArticlesView.class, ArticlesPresenter.MyProxy.class);

		bindPresenter(ProfilePresenter.class, ProfilePresenter.MyView.class,
				ProfileView.class, ProfilePresenter.MyProxy.class);

		bindPresenter(MessagesPresenter.class, MessagesPresenter.MyView.class,
				MessagesView.class, MessagesPresenter.MyProxy.class);

		bindPresenter(LookforPresenter.class, LookforPresenter.MyView.class,
				LookforView.class, LookforPresenter.MyProxy.class);

		bindPresenter(InterestsPresenter.class,
				InterestsPresenter.MyView.class, InterestsView.class,
				InterestsPresenter.MyProxy.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.MyView.class,
				LoginView.class, LoginPresenter.MyProxy.class);


		bindPresenter(LoggedMenuPresenter.class,
				LoggedMenuPresenter.MyView.class, LoggedMenuView.class,
				LoggedMenuPresenter.MyProxy.class);

		bindPresenter(NotLoggedMenuPresenter.class,
				NotLoggedMenuPresenter.MyView.class, NotLoggedMenuView.class,
				NotLoggedMenuPresenter.MyProxy.class);
		
		bindPresenter(ContactPresenter.class, ContactPresenter.MyView.class, ContactView.class, ContactPresenter.MyProxy.class);
		
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.lookfor);
		//TODO
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.login);

        bindPresenterWidget(AddArticlesPresenter.class,
        		AddArticlesPresenter.MyView.class, AddArticlesView.class);
        
		bindPresenterWidget(TextEditorPresenter.class,
				TextEditorPresenter.MyView.class, TextEditorView.class);
		
		bindPresenterWidget(OffersPresenter.class,
				OffersPresenter.MyView.class, OffersView.class);

		bindPresenterWidget(UpdateArticlePresenter.class,
				UpdateArticlePresenter.MyView.class, UpdateArticleView.class);

		bindSingletonPresenterWidget(SocialPresenter.class,
				SocialPresenter.MyView.class, SocialView.class);
		
		bindSingletonPresenterWidget(ProductsPresenter.class,
				ProductsPresenter.MyView.class, ProductsView.class);
		
		bindSingletonPresenterWidget(ChatPresenter.class,
				ChatPresenter.MyView.class, ChatView.class);
		
		bind(NeedsLoginKeeper.class);
        bind(CurrentUser.class).asEagerSingleton();
        bind(ChannelFactory.class);
	}
}
