package jfxtras.labs.scene.control;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * Load a layout from FXML
 * 
 * @author Michael Paus and Tom Eugelink
 *
 */
public class CalendarPickerTrial2 extends Application {
	
    public static void main(String[] args) {
    	launch(args);       
    }

	@Override
	public void start(Stage stage)
	throws IOException
	{
    	// load FXML
		String lName = this.getClass().getSimpleName() + ".fxml";
		URL lURL = this.getClass().getResource(lName);
		System.out.println("loading FXML " + lName + " -> " + lURL);
		if (lURL == null) throw new IllegalStateException("FXML file not found");
		AnchorPane lRoot = (AnchorPane)FXMLLoader.load(lURL);

        // create scene
        Scene scene = new Scene(lRoot, 800, 300);
        
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
    }

}
