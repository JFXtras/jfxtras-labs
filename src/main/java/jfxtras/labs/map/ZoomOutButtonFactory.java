package jfxtras.labs.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Factory for the zoom out button.
 * 
 * @author Mario Schroeder
 */
public class ZoomOutButtonFactory extends ZoomButtonFactory{

    public ZoomOutButtonFactory(MapControlable controlable) {
        super(controlable);
    }
    
    @Override
    protected String getImagePath() {
        return "minus.png";
    }

    @Override
    protected EventHandler<ActionEvent> getEventHandler() {
        return  new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!zoomable.isIgnoreRepaint()) {
                    zoomable.zoomOut();
                }
            }
        };
    }
    
}
