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