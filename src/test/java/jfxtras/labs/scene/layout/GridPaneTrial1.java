/**
 * GridPaneTrial1.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
