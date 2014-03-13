package jfxtras.labs.scene.layout.test;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.RadialPane;

public class RadialPaneTrail extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		RadialPane lRadialPane = new RadialPane();
//		for (int i = 0; i < 8; i++) {
//			lRadialPane.getChildren().add(new javafx.scene.shape.Circle(10));
//		}
		
//		for (int i = 0; i < 8; i++) {
//			lRadialPane.getChildren().add(new javafx.scene.shape.Circle(5 + i));
//		}
		
		lRadialPane.setStartAngle(2 * Math.PI / 12);
		for (int i = 0; i < 12; i++) {
			javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.GRAY);
			javafx.scene.text.Text t = new javafx.scene.text.Text("" + (i + 1));
			StackPane lStackPane = new StackPane();
			lStackPane.getChildren().add(c);
			lStackPane.getChildren().add(t);
			lRadialPane.getChildren().add(lStackPane);
		}
		
//		for (int i = 0; i < 8; i++) {
//			lRadialPane.getChildren().add(new javafx.scene.control.Button("" + i));
//		}
	    
        // setup scene
		Scene scene = new Scene(lRadialPane);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}

	