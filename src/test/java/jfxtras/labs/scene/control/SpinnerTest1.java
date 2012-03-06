/**
 * Copyright (c) 2011, JFXtras
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

import com.sun.rowset.internal.InsertRow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.animation.Timer;
import jfxtras.labs.scene.control.Spinner.ArrowDirection;
import jfxtras.labs.util.StringConverterFactory;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class SpinnerTest1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		GridPane lGridPane = new GridPane();
		lGridPane.setVgap(5.0);
		lGridPane.setPadding(new Insets(5.0));
		ColumnConstraints column0 = new ColumnConstraints(10, 10, Double.MAX_VALUE);
		column0.setHgrow(Priority.ALWAYS);
		ColumnConstraints column1 = new ColumnConstraints(10, 10, Double.MAX_VALUE);
		column1.setHgrow(Priority.ALWAYS);
		lGridPane.getColumnConstraints().addAll(column0, column1);
		
		int lRowIdx = 0;
		
		{
			lGridPane.add(new Label("Empty list"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>();
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Cyclic list"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>("a", "b", "c")
				.withCyclic(true)
				;
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Cyclic list with null"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>( "a", "b", "c", null )
				.withCyclic(true)
				;
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Editable cyclic list"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>( "a", "b", "c", "d", "e" )
				.withCyclic(true)
				.withEditable(true)
				.withStringConverter(StringConverterFactory.forString())
				;
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Editable and adding cyclic list"), 0, lRowIdx);
			final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
			Spinner<String> lXSpinner = new Spinner<String>( lObservableList )
				.withCyclic(true)
				.withEditable(true)
				.withStringConverter(StringConverterFactory.forString())
				.withAddCallback(new Callback<String, Integer>()
				{					
					@Override
					public Integer call(String text)
					{
						lObservableList.add(text);
						return lObservableList.size() - 1; // notify spinner the value is appended at the end
					}
				})
				;
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Observable list, 'z' prepended"), 0, lRowIdx);
			final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
			Spinner<String> lXSpinner = new Spinner<String>( lObservableList );
			lObservableList.add(0, "z");
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Observable list, first removed"), 0, lRowIdx);
			final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
			Spinner<String> lXSpinner = new Spinner<String>( lObservableList );
			lObservableList.remove("a");
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Observable list, emptied"), 0, lRowIdx);
			final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
			Spinner<String> lXSpinner = new Spinner<String>( lObservableList );
			lObservableList.clear();
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList());
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range 10..110"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList(10, 110));
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range 10..110 with setIndex 50"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList(10, 110));
			lXSpinner.setIndex(50);
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range 0..1000 step 10"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList(0, 100, 10));
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range -10..10"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList(-10, 10));
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Integer range 10..-10"), 0, lRowIdx);
			Spinner<Integer> lXSpinner = new Spinner<Integer>(new SpinnerIntegerList(10, -10, -1));
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Arrows HORIZONTAL"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL);
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Arrows VERTICAL"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL);
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Arrows HORIZONTAL"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withEditable(true);
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}
		{
			lGridPane.add(new Label("Arrows VERTICAL"), 0, lRowIdx);
			Spinner<String> lXSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withEditable(true);
			lGridPane.add(lXSpinner, 1, lRowIdx++);
		}

		// create scene
        Scene scene = new Scene(lGridPane, 400, 600);
        
        // create stage
        stage.setTitle("SpinnerX");
        stage.setScene(scene);
        stage.show();
    }
}
