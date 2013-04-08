package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Factory for the zoom in button.
 * 
 * @author Mario Schröder
 */
class ZoomInButtonFactory extends ZoomButtonFactory{

    public ZoomInButtonFactory(MapControlable controlable) {
        super(controlable);
    }
    
    @Override
    String getImagePath() {
        return "plus.png";
    }

    @Override
    EventHandler<ActionEvent> getEventHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!controlable.isIgnoreRepaint()) {
                    controlable.zoomIn();
                }
            }
        };
    }
    
}
