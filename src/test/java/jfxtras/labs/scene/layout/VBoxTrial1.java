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
//		VBox lVBox = new VBox(5.0);		
//		Button b1 = new Button("short");
//		lVBox.getChildren().add(b1);
//		VBox.setVgrow(b1, Priority.ALWAYS); 
//		b1.setMaxWidth(Integer.MAX_VALUE);
//		Button b2 = new Button("medium length");
//		VBox.setVgrow(b2, Priority.ALWAYS); 
//		b2.setMaxWidth(Integer.MAX_VALUE);
//		lVBox.getChildren().add(b2);
//		Button b3 = new Button("a longer description in order to test things");
//		VBox.setVgrow(b3, Priority.ALWAYS); 
//		b3.setMaxWidth(Integer.MAX_VALUE);
//		lVBox.getChildren().add(b3);
		VBox lVBox = new VBox(5.0);
		lVBox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
		lVBox.add(new Button("medium length"), new VBox.C().vgrow(Priority.ALWAYS));
		lVBox.add(new Button("a longer description in order to test things"), new VBox.C().vgrow(Priority.ALWAYS));
				
        // setup scene
		Scene scene = new Scene(lVBox, 300, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
