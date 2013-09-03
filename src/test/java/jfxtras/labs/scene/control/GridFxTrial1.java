/**
 * GridFxTrial1.java
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

package jfxtras.labs.scene.control;

import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.grid.GridCell;
import jfxtras.labs.scene.control.grid.GridView;
import jfxtras.labs.scene.control.grid.cell.ColorGridCell;

public class GridFxTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		stage.setTitle("GridFxTrial1");
		
		final ObservableList<Color> list = FXCollections.<Color>observableArrayList();
		GridView<Color> myGrid = new GridView<>(list);
		
		myGrid.setHorizontalAlignment(HPos.LEFT);
		
		myGrid.setCellFactory(new Callback<GridView<Color>, GridCell<Color>>() {
			
			@Override
			public GridCell<Color> call(GridView<Color> arg0) {
				return new ColorGridCell();
			}
		});
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0; i < 100; i++) {
			list.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0));
		}

		BorderPane root = new BorderPane();
		root.setCenter(myGrid);
		
		VBox box = new VBox();
		Slider columnWidthSlider = SliderBuilder.create().min(10).max(512).build();
		columnWidthSlider.valueProperty().bindBidirectional(myGrid.cellWidthProperty());
		box.getChildren().add(HBoxBuilder.create().children(new Label("ColumnWidth"), columnWidthSlider).build());
		
		Slider columnHeightSlider = SliderBuilder.create().min(10).max(512).build();
		columnHeightSlider.valueProperty().bindBidirectional(myGrid.cellHeightProperty());
		box.getChildren().add(HBoxBuilder.create().children(new Label("columnHeight"), columnHeightSlider).build());
		
		Slider horizontalCellSpacingSlider = SliderBuilder.create().min(0).max(64).build();
		horizontalCellSpacingSlider.valueProperty().bindBidirectional(myGrid.horizontalCellSpacingProperty());
		box.getChildren().add(HBoxBuilder.create().children(new Label("horizontalCellSpacing"), horizontalCellSpacingSlider).build());
		
		Slider verticalCellSpacingSlider = SliderBuilder.create().min(0).max(64).build();
		verticalCellSpacingSlider.valueProperty().bindBidirectional(myGrid.verticalCellSpacingProperty());
		box.getChildren().add(HBoxBuilder.create().children(new Label("verticalCellSpacing"), verticalCellSpacingSlider).build());
		
		root.setTop(box);
		
		Scene scene = new Scene(root, 540, 210);

		stage.setScene(scene);
		stage.show();
    }
}