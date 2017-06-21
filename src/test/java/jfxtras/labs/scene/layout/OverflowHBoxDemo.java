package jfxtras.labs.scene.layout;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class OverflowHBoxDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(OverflowHBoxDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField lTextField = new TextField();
		String s = "sdfas dfasdf asdf sad fasrf awer ftar";
		
//		OverflowHBox lOverflowHBox = new OverflowHBox(10.0, 10.0);
		OverflowHBox lOverflowHBox = new OverflowHBox();
		
		for (int i = 1; i < 10; i++) {
			Button node = new Button("much longer label " + i + " " + s.substring(0, new Random().nextInt(s.length())));			
			lOverflowHBox.add(node);
			node.setOnAction(event -> {
				lTextField.requestFocus();
			});
		}
		
		// show
		primaryStage.setScene(new Scene(new VBox(lOverflowHBox
//				, lTextField, new CalendarPicker()
				), 800, 300));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
