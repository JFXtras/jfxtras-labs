package jfxtras.labs.scene.layout;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GridPaneTrial1 extends Application 
{
	
    public static void main(String[] args) 
    {
        launch(args);       
    }

	@Override
	public void start(Stage stage) 
	{
		GridPane gridPane = new GridPane()
			.withHGap(5)
			.withVGap(5)
			.withPadding(new Insets(10, 10, 10, 10))
			.withGridLinesVisible(true);

	    gridPane.add(new Text("SingleCell"), new GridPane.C().col(1).row(0));
		gridPane.add(new Text("RIGHT"), new GridPane.C().col(2).row(0).halignment(HPos.RIGHT));

		gridPane.add(new Text("Span2Row\nSpan2Row\nSpan2Row"), new GridPane.C().col(0).row(0).colSpan(1).rowSpan(2));

	    gridPane.add(new Text("Span2Columns Span2Columns"), new GridPane.C().col(1).row(1).colSpan(2).rowSpan(1));

		gridPane.add(new Text("Single"), new GridPane.C().col(0).row(2));
		gridPane.add(new Text("Span2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter"), new GridPane.C().col(1).row(2).colSpan(2).rowSpan(2).halignment(HPos.CENTER));

		gridPane.add(new Text("BOTTOM"), new GridPane.C().col(0).row(3).valignment(VPos.BOTTOM));

	    gridPane.add(new Text("TOP"), new GridPane.C().col(3).row(3).valignment(VPos.TOP));
	    
        // setup scene
		Scene scene = new Scene(gridPane, 800, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
