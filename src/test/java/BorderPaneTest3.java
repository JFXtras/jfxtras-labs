import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class BorderPaneTest3 extends Application
{
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		// setup contents
		contentPane.setPrefHeight(400);
		
		// create scrollpane
		scrollPane = ScrollPaneBuilder.create()
				.content(contentPane)
				.hbarPolicy(ScrollBarPolicy.NEVER)
				.pannable(false) // panning would conflict with creating a new appointment
				//.fitToWidth(true)
				.build();

		// create border pane
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setTop(new Label("TOP"));
		lBorderPane.setCenter(scrollPane);
		
		// make sure width matches
		scrollPane.viewportBoundsProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable arg0)
			{
				relayout();
			}
		});

		// create the scene
		Scene scene = new Scene(lBorderPane, 400, 300);

		// show stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}
	ContentPane contentPane = new ContentPane();
	ScrollPane scrollPane = null;
	
	private void relayout()
	{
		contentPane.setPrefWidth(scrollPane.getViewportBounds().getWidth());
	}
		
	class ContentPane extends Pane
	{
		public ContentPane()
		{
			// show my borders
			setStyle("-fx-border-width:1px; -fx-border-color:RED;");
			
			// just a child
			getChildren().add(new Label("CENTER"));
		}
	}
}
