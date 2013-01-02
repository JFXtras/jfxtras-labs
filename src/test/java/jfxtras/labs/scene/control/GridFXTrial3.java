package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.grid.GridView;

public class GridFXTrial3  extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private int counter = 0;
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("GridFxTrial2");

		final ObservableList<Integer> list = FXCollections
				.<Integer> observableArrayList();
		GridView<Integer> myGrid = new GridView<>(list);

		Button addButton = new Button("ADD");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				list.add(counter++);
			}
		});
		Button removeButton = new Button("REMOVE");
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				list.remove(0, 1);
			}
		});
		Button changeButton = new Button("CHANGE");
		changeButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				list.set(0, counter++);
			}
		});
		HBox box = new HBox();
		box.getChildren().add(addButton);
		box.getChildren().add(removeButton);
		box.getChildren().add(changeButton);
		
		BorderPane root = new BorderPane();
		root.setCenter(myGrid);
		root.setTop(box);
		
		for(int i = 0; i < 10; i++) {
			list.add(counter++);
		}
		
		Scene scene = new Scene(root, 540, 210);

		stage.setScene(scene);
		stage.show();
	}
}