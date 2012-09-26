package jfxtras.labs.scene.control;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import jfxtras.labs.scene.control.Spinner.ArrowDirection;
import jfxtras.labs.util.StringConverterFactory;

public class SpinnerTrail2 extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{

		final SpinnerIntegerList spinnerIntegerList = new SpinnerIntegerList(128, 4096, 128);
		final Spinner<Integer> segmentSpinner = new Spinner<Integer>(spinnerIntegerList);
		segmentSpinner.setValue(1024);
		segmentSpinner.setEditable(true);
		segmentSpinner.setStringConverter(StringConverterFactory.forInteger());
		segmentSpinner.setArrowDirection(ArrowDirection.VERTICAL);
		segmentSpinner.setMaxWidth(60);
		segmentSpinner.addCallbackProperty().set(new Callback<Integer, Integer>()
		{
			@Override
			public Integer call(Integer integer)
			{
				spinnerIntegerList.add(integer);
				return spinnerIntegerList.size() - 1;
//				int l = integer; while (l > 128) l -= 128; l += 128;
//				int u = integer; while (u < 4096) u += 128; u -= 128;
//				SpinnerIntegerList spinnerIntegerList = new SpinnerIntegerList(l, u, 128);
//				segmentSpinner.setItems(FXCollections.observableList(spinnerIntegerList));
//				int i = spinnerIntegerList.indexOf(integer);
//				return i;
			}
		});
		
		
		// create scene
        Scene scene = new Scene(segmentSpinner, 800, 600);
        
        // create stage
        stage.setTitle("SpinnerX");
        stage.setScene(scene);
        stage.show();
	}
}
