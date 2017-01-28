package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class HarmonicaDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(HarmonicaDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Harmonica lHarmonica = new Harmonica();
		lHarmonica.tabs().add(new Harmonica.Tab("test1", new Label("test 1")));
		lHarmonica.tabs().add(new Harmonica.Tab("test2", createTree(5) ));
		lHarmonica.tabs().add(new Harmonica.Tab("test3", createTree(500) ));

		// show
		primaryStage.setScene(new Scene(lHarmonica, 1700, 1000));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	private TreeView<String> createTree(int size) {
		TreeItem<String> rootItem = new TreeItem<String>("Tree " + size);
		rootItem.setExpanded(true);
		for (int i = 0; i < size; i++) {
			rootItem.getChildren().add(new TreeItem<String>("Item " + i));
		}
		TreeView<String> treeView = new TreeView<String>(rootItem);
		return treeView;
	}
}
