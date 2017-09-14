package net;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents a request to resize the window.
 * */
public class ResizeWindowEvent extends Event {
    public static final EventType<ResizeWindowEvent> RESIZE_WINDOW = new EventType("resize");
    /**
     * Creates a new ResizeWindowEvent.
     * */
    public ResizeWindowEvent() {
        super(ResizeWindowEvent.RESIZE_WINDOW);
    }
}
