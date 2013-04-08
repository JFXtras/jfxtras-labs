package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Factory for the zoom out button.
 * 
 * @author Mario Schröder
 */
class ZoomOutButtonFactory extends ZoomButtonFactory{

    public ZoomOutButtonFactory(MapControlable controlable) {
        super(controlable);
    }
    
    @Override
    String getImagePath() {
        return "minus.png";
    }

    @Override
    EventHandler<ActionEvent> getEventHandler() {
        return  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!controlable.isIgnoreRepaint()) {
                    controlable.zoomOut();
                }
            }
        };
    }
    
}
