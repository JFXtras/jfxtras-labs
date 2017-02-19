package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OverflowHBoxDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(OverflowHBoxDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		OverflowHBox lOverflowHBox = new OverflowHBox();
		for (int i = 1; i < 10; i++) {
			lOverflowHBox.add(new Button("label " + i));
		}
		
		// show
		primaryStage.setScene(new Scene(lOverflowHBox, 800, 300));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
