package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Factory for the zoom in button.
 *
 * @author Mario Schroeder
 */
public class ZoomInButtonFactory extends ZoomButtonFactory {

    public ZoomInButtonFactory(Zoomable controlable) {
        super(controlable);
    }

    @Override
    protected String getImagePath() {
        return "plus.png";
    }

    @Override
    protected EventHandler<ActionEvent> getEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                zoomable.zoomIn();
            }
        };
    }
}
