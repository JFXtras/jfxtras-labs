package jfxtras.labs.map;

import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory for zoom buttons.
 * @author Mario Schroeder
 */
public abstract class ZoomButtonFactory {
	
    protected Zoomable zoomable;

    public ZoomButtonFactory(Zoomable zoomable) {
        this.zoomable = zoomable;
    }
    
    /**
     * Creates a new instance of a button.
     * @return {@link Button}
     */
    public Button create(){
        
        Button btn = new Button();
        InputStream stream = getClass().getResourceAsStream(getImagePath());
		Image image = new Image(stream);
        btn.setGraphic(new ImageView(image));
        btn.setOnAction(getEventHandler());
        
        return btn;
    }
    
    protected abstract String getImagePath();
    
    protected abstract EventHandler<ActionEvent> getEventHandler();
}
