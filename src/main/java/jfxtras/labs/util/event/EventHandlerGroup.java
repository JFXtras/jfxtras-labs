/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.labs.util.event;

import java.util.ArrayList;
import java.util.Collection;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * A simple event handler group.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class EventHandlerGroup<T extends Event> implements EventHandler<T> {

    private Collection<EventHandler<T>> handlers =
            new ArrayList<>();

    public void addHandler(EventHandler<T> eventHandler) {
        handlers.add(eventHandler);
    }

    @Override
    public void handle(T t) {
        for (EventHandler<T> eventHandler : handlers) {
            eventHandler.handle(t);
        }
    }
}