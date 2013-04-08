package jfxtras.labs.map;

import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory for zoom buttons.
 * @author Mario Schröder
 */
abstract class ZoomButtonFactory {
	
    protected MapControlable controlable;

    ZoomButtonFactory(MapControlable controlable) {
        this.controlable = controlable;
    }
    
    Button create(){
        
        Button btn = new Button();
        InputStream stream = getClass().getResourceAsStream(getImagePath());
		Image image = new Image(stream);
        btn.setGraphic(new ImageView(image));
        btn.setOnAction(getEventHandler());
        
        return btn;
    }
    
    abstract String getImagePath();
    
    abstract EventHandler<ActionEvent> getEventHandler();
}
