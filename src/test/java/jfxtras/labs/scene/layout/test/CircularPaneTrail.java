package jfxtras.labs.scene.layout.test;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import jfxtras.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.CircularPane;

public class CircularPaneTrail extends Application {

    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		HBox lHBox = new HBox(0);
		
		{
			CircularPane lRadialPane = new CircularPane();
			// lRadialPane.setStyle("-fx-border-color:red;");
			lRadialPane.setStartAngle(2 * Math.PI / 12);
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			final AtomicReference<Circle> lastFocus = new AtomicReference<>();
			final AtomicBoolean isPM = new AtomicBoolean(false);
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.GRAY);
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i + 1));
				t.getStyleClass().add("time");
				labels.add(t);
				c.setOnMouseEntered( (event) -> {
					int lastFocusIdx = circles.indexOf(lastFocus.get());
					int currentIdx = circles.indexOf( event.getSource() );
					
					if ( (lastFocusIdx == 11 && currentIdx == 0)
					  || (lastFocusIdx == 0 && currentIdx == 11)
					   ) {
						isPM.set( !isPM.get() );
					}
					
					for (int j = 0; j < 12; j++) {
						labels.get(j).setText( "" + ( (j + 1) + (isPM.get() ? 12 : 0) ) );
					}
				});
				c.setOnMouseExited( (event) -> {
					lastFocus.set( (Circle)event.getSource() );
				});
				StackPane lStackPane = new StackPane();
				lStackPane.setId("" + i);
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lRadialPane.getChildren().add(lStackPane);
			}
			lHBox.getChildren().add(lRadialPane);
		}
		
		{
			CircularPane lRadialPane = new CircularPane();
			// lRadialPane.setStyle("-fx-border-color:red;");
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			final AtomicReference<Circle> lastFocus = new AtomicReference<>();
			final AtomicBoolean isPM = new AtomicBoolean(false);
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.GRAY);
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i * 5));
				t.getStyleClass().add("time");
				labels.add(t);
				StackPane lStackPane = new StackPane();
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lRadialPane.getChildren().add(lStackPane);
			}
			StackPane lStackPane = new StackPane();
			lStackPane.getChildren().add(lRadialPane);
			Label l = new Label("M");
			l.getStyleClass().add("center");
			lStackPane.getChildren().add(l);
			lHBox.add(lStackPane);
		}
		
		{
			CircularPane lRadialPane = new CircularPane();
			// lRadialPane.setStyle("-fx-border-color:red;");
			final List<Label> labels = new ArrayList<>();
			final List<Circle> circles = new ArrayList<>();
			final AtomicReference<Circle> lastFocus = new AtomicReference<>();
			final AtomicBoolean isPM = new AtomicBoolean(false);
			for (int i = 0; i < 12; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(10, Color.GRAY);
				circles.add(c);
				javafx.scene.control.Label t = new javafx.scene.control.Label("" + (i * 5));
				t.getStyleClass().add("time");
				labels.add(t);
				StackPane lStackPane = new StackPane();
				lStackPane.getChildren().add(c);
				lStackPane.getChildren().add(t);
				lRadialPane.getChildren().add(lStackPane);
			}
			StackPane lStackPane = new StackPane();
			lStackPane.getChildren().add(lRadialPane);
			Label l = new Label("S");
			l.getStyleClass().add("center");
			lStackPane.getChildren().add(l);
			lHBox.add(lStackPane);
		}
		
//		{
//			RadialPane lRadialPane = new RadialPane();
//			// lRadialPane.setStyle("-fx-border-color:red;");
//			for (int i = 0; i < 12; i++) {
//				lRadialPane.getChildren().add(new javafx.scene.control.Button("" + i));
//			}
//			lHBox.getChildren().add(lRadialPane);
//		}
//		
//		{
//			RadialPane lRadialPane = new RadialPane();
//			lRadialPane.setMinSize(200,  200);
//			// lRadialPane.setStyle("-fx-border-color:red;");
//			for (int i = 0; i < 8; i++) {
//				lRadialPane.getChildren().add(new javafx.scene.shape.Circle(10));
//			}
//			lHBox.getChildren().add(lRadialPane);
//		}
//		
//		{
//			RadialPane lRadialPane = new RadialPane();
//			// lRadialPane.setStyle("-fx-border-color:red;");
//			for (int i = 0; i < 8; i++) {
//				lRadialPane.getChildren().add(new javafx.scene.shape.Circle(5 + i));
//			}
//			lHBox.getChildren().add(lRadialPane);
//		}

        // setup scene
		Scene scene = new Scene(lHBox);
		scene.getStylesheets().add(this.getClass().getName().replace(".", "/") + ".css");
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	
	

}

	