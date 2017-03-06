package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class OverflowHBoxDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(OverflowHBoxDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField lTextField = new TextField();
		
//		OverflowHBox lOverflowHBox = new OverflowHBox(10.0, 10.0);
		OverflowHBox lOverflowHBox = new OverflowHBox();
		
		for (int i = 1; i < 10; i++) {
			Button node = new Button("much longer label " + i);			
//			lOverflowHBox.add(node, new HBox.C().margin(new Insets(10.0,10.0,10.0,10.0)), new VBox.C().margin(new Insets(10.0,10.0,10.0,10.0)));
			lOverflowHBox.add(node);
			node.setOnAction(event -> {
				lTextField.requestFocus();
			});
		}
		
		// show
		primaryStage.setScene(new Scene(new BorderPane(lOverflowHBox, null, null, lTextField, null), 800, 300));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
