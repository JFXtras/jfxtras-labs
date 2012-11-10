package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HBoxTrial1 extends Application 
{
	
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		HBox lHBox = new HBox(5.0);
		lHBox.add(new Button("short"), new HBox.C().hgrow(Priority.ALWAYS));
		lHBox.add(new Button("medium length"), new HBox.C().hgrow(Priority.ALWAYS));
		lHBox.add(new Button("a longer description"), new HBox.C().hgrow(Priority.ALWAYS));
		lHBox.add(new Button("margin 5 grow"), new HBox.C().margin(new Insets(5.0)).hgrow(Priority.ALWAYS));
		lHBox.getChildren().add(new Button("old style"));
		lHBox.add(new Button("margin 20"), new HBox.C().margin(new Insets(20.0)));

        // setup scene
		Scene scene = new Scene(lHBox, 600, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
