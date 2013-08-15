package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.util.StringConverterFactory;

public class ListSpinnerTrial2 extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		VBox lVBox = new VBox(20);
		
		final ListSpinnerIntegerList spinnerIntegerList = new ListSpinnerIntegerList(100, 1000, 100);
		final ListSpinner<Integer> segmentSpinner = new ListSpinner<Integer>(spinnerIntegerList);
		segmentSpinner.setValue(500);
		segmentSpinner.setEditable(true);
		segmentSpinner.setStringConverter(StringConverterFactory.forInteger());
		segmentSpinner.withStyle("-fxx-arrow-direction:VERTICAL;");
		segmentSpinner.setMaxWidth(60);
		segmentSpinner.addCallbackProperty().set(new Callback<Integer, Integer>()
		{
			@Override
			public Integer call(Integer integer)
			{
				if (integer < 100 || integer > 1000) return null;
				int l = integer; while (l > 100) l -= 100; l += 100;
				int u = integer; while (u < 1000) u += 100; u -= 100;
				ListSpinnerIntegerList spinnerIntegerList = new ListSpinnerIntegerList(l, u, 100);
				segmentSpinner.setItems(FXCollections.observableList(spinnerIntegerList));
				int i = spinnerIntegerList.indexOf(integer);
				return i;
			}
		});
		lVBox.getChildren().add(segmentSpinner);
		
		// just a focusable control
		lVBox.getChildren().add(new TextField());
		
		// create scene
        Scene scene = new Scene(lVBox, 800, 600);
        
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
	}
}
