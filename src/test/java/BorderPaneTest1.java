import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class BorderPaneTest1 extends Application
{
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		// create border pane
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setTop(new Label("TOP"));
		lBorderPane.setCenter(new Label("CENTER"));
		//lBorderPane.getCenter().setStyle("-fx-border-width:1px; -fx-border-color:RED;");

		// create the scene
		Scene scene = new Scene(lBorderPane, 400, 300);

        // show stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
}
