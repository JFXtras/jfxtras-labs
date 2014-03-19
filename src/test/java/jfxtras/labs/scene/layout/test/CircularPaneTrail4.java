package jfxtras.labs.scene.layout.test;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.scene.layout.HBox;

public class CircularPaneTrail4 extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		VBox lVBox = new VBox();		
		HBox lHBox = new HBox(0);
		lVBox.getChildren().add(lHBox);

		for (int j = 1; j < 360; j += 15)
		{
			CircularPane lCircularPane = new CircularPane();
			lCircularPane.setStyle("-fx-border-color:black;");
			lCircularPane.setShowDebug(Color.GREEN);
			lCircularPane.setStartAngle( (double)j );
			lCircularPane.setArc(90.0);
			for (int i = 0; i < 10; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(10,10);
				//c.setStroke(Color.RED);
				lCircularPane.getChildren().add(c);
			}
			lHBox.getChildren().add(lCircularPane);
			if (lHBox.prefWidth(-1) > 1500) {
				lHBox = new HBox(0);
				lVBox.getChildren().add(lHBox);
			}
		}

        // setup scene
		Scene scene = new Scene(lVBox);
		scene.getStylesheets().add(this.getClass().getName().replace(".", "/") + ".css");
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	
	

}

	