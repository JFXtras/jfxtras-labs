package jfxtras.labs.scene.control;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.responsivepane.Ref;
import jfxtras.labs.scene.layout.responsivepane.ResponsivePane;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class HarmonicaDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(HarmonicaDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Harmonica lHarmonica = new Harmonica();
		lHarmonica.tabs().add(new Harmonica.Tab("test1", new Label("test 1")));
		lHarmonica.tabs().add(new Harmonica.Tab("test2", new Label("test 2")));
		lHarmonica.tabs().add(new Harmonica.Tab("test3", new Label("test 3")));

		// show
		primaryStage.setScene(new Scene(lHarmonica, 1700, 1000));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
}
