package com.changestuffs.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PresenterSlotEvent extends GwtEvent<PresenterSlotEvent.GlobalDataHandler> {
    
	private PresenterWidget<? extends View> presenter;
    
    protected PresenterSlotEvent() {
        // Possibly for serialization.
    }
    
    public PresenterSlotEvent(PresenterWidget<? extends View> presenter) {
        this.presenter = presenter;
    }

    public static void fire(HasHandlers source, PresenterWidget<? extends View> presenter) {
        PresenterSlotEvent eventInstance = new PresenterSlotEvent(presenter);
        source.fireEvent(eventInstance);
    }

    public static void fire(HasHandlers source, PresenterSlotEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    public interface HasGlobalDataHandlers extends HasHandlers {
        HandlerRegistration addGlobalHandler(GlobalDataHandler handler);
    }

    public interface GlobalDataHandler extends EventHandler {
        public void onGlobalEvent(PresenterSlotEvent event);
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
    
    public PresenterWidget<? extends View> getPresenter() {
        return this.presenter;
    }
}
