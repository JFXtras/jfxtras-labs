package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.ResponsivePane.Layout;
import jfxtras.labs.scene.layout.ResponsivePane.Ref;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class ResponsivePaneDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(ResponsivePaneDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ResponsivePane lResponsivePane = new ResponsivePane();
		lResponsivePane.addRef("ref1", new Button("Ref1"));
		lResponsivePane.addRef("ref1", new Button("Ref2"));
		
		Layout lLayout0 = new Layout().withWidth(0.0);
		lLayout0.setRoot( new Label("?") );
		lResponsivePane.getLayouts().add(lLayout0);
		
		Layout lLayout1 = new Layout().withWidth(1024.0);
		lLayout1.setRoot( new VBox(new Button("Layout1"), new Ref("ref1")) );
		lResponsivePane.getLayouts().add(lLayout1);
		
		Layout lLayout2 = new Layout().withWidth(600.0);
		lLayout2.setRoot( new HBox(new Button("Layout2"), new Ref("ref1")) );
		lResponsivePane.getLayouts().add(lLayout2);
		
		lResponsivePane.setActiveLayout(lLayout1);
		
		primaryStage.setScene(new Scene(lResponsivePane, 600, 400));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
