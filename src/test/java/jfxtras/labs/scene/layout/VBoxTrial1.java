package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class VBoxTrial1 extends Application 
{
	
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		VBox lVBox = new VBox(5.0);
		lVBox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
		lVBox.add(new Button("medium length"), new VBox.C().vgrow(Priority.ALWAYS));
		lVBox.add(new Button("a longer description in order to test things"), new VBox.C().vgrow(Priority.ALWAYS));
		Button b = new Button("old style");
		lVBox.getChildren().add(b);
				
        // setup scene
		Scene scene = new Scene(lVBox, 300, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
