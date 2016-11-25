package jfxtras.labs.scene.layout.responsivepane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.fxml.JFXtrasBuilderFactory;

public class ResponsivePaneFXMLDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(ResponsivePaneFXMLDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			Locale.setDefault(Locale.ENGLISH);
			
	    	// load FXML
			String lName = this.getClass().getSimpleName() + ".fxml";
			URL lURL = this.getClass().getResource(lName);
			//System.out.println("loading FXML " + lName + " -> " + lURL);
			if (lURL == null) throw new IllegalStateException("FXML file not found: " + lName);
			Parent lRoot = (Parent)FXMLLoader.load(lURL, null, new JFXtrasBuilderFactory());

			// show
			primaryStage.setScene(new Scene(lRoot, 2000, 1000));
			primaryStage.sizeToScene();
			primaryStage.show();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
