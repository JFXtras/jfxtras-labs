package jfxtras.labs.scene.layout;

import javafx.application.Application;
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
		GridPane grid = new GridPane()
			.withHGap(10)
			.withVGap(10)
			.withPadding(new Insets(0, 10, 0, 10));

	    // Category in column 2, row 1
	    Text category = new Text("Sales:");
	    category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(category, new GridPane.C().col(1).row(0)); 

	    // Title in column 3, row 1
	    Text chartTitle = new Text("Current Year");
	    chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(chartTitle, new GridPane.C().col(2).row(0));

	    // Subtitle in columns 2-3, row 2
	    grid.add(new Text("Goods and Services"), new GridPane.C().col(1).row(1).colSpan(2).rowSpan(1));

	    // House icon in column 1, rows 1-2
	    Rectangle lRectangle1 = new Rectangle(50,50);
	    lRectangle1.setFill(Color.RED);
	    grid.add(lRectangle1, new GridPane.C().col(0).row(0).colSpan(1).rowSpan(2)); 

	    // Left label in column 1 (bottom), row 3
	    grid.add(new Text("Goods\n80%"), new GridPane.C().col(0).row(2).valignment(VPos.BOTTOM)); 

	    // Chart in columns 2-3, row 3
	    Rectangle lRectangle2 = new Rectangle(150,100);
	    lRectangle2.setFill(Color.BLUE);
	    grid.add(lRectangle2, new GridPane.C().col(1).row(2).colSpan(2).rowSpan(1)); 

	    // Right label in column 4 (top), row 3
	    grid.add(new Text("Services\n20%"), new GridPane.C().col(3).row(2).valignment(VPos.TOP));
	    
        // setup scene
		Scene scene = new Scene(grid, 800, 200);
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
	}

}
