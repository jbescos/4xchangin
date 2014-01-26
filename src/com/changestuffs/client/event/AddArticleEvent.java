package com.changestuffs.client.event;

import com.changestuffs.shared.dto.IArticlesDto;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AddArticleEvent extends GwtEvent<AddArticleEvent.GlobalDataHandler> {
    private IArticlesDto article;
    
    protected AddArticleEvent() {
        // Possibly for serialization.
    }
    
    public AddArticleEvent(IArticlesDto article) {
        this.article = article;
    }

    public static void fire(HasHandlers source, IArticlesDto article) {
        AddArticleEvent eventInstance = new AddArticleEvent(article);
        source.fireEvent(eventInstance);
    }

    public static void fire(HasHandlers source, AddArticleEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    public interface HasGlobalDataHandlers extends HasHandlers {
        HandlerRegistration addGlobalHandler(GlobalDataHandler handler);
    }

    public interface GlobalDataHandler extends EventHandler {
        public void onGlobalEvent(AddArticleEvent event);
    }

    private static final Type<GlobalDataHandler> TYPE = new Type<GlobalDataHandler>();

    public static Type<GlobalDataHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<GlobalDataHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GlobalDataHandler handler) {
        handler.onGlobalEvent(this);
    }
    
    public IArticlesDto getArticle() {
        return this.article;
    }
}
