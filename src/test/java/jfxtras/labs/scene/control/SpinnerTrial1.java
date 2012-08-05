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

import java.math.BigInteger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.Spinner.ArrowDirection;
import jfxtras.labs.scene.control.Spinner.ArrowPosition;
import jfxtras.labs.util.StringConverterFactory;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class SpinnerTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		HBox lHBox = new HBox();
		
		{
			GridPane lGridPane = new GridPane();
			lGridPane.setVgap(5.0);
			lGridPane.setPadding(new Insets(5.0));
			int lRowIdx = 0;
			
			{
				lGridPane.add(new Label("Empty list"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>();
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Cyclic list"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c")
					.withCyclic(true)
					;
				lGridPane.add(lSpinner, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lValueTextField.textProperty().bind(lSpinner.valueProperty());
				lGridPane.add(lValueTextField, 2, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Cyclic list with null"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>( "a", "b", "c", null )
					.withCyclic(true)
					;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Editable cyclic list"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>( "a", "b", "c", "d", "e" )
					.withCyclic(true)
					.withEditable(true)
					.withStringConverter(StringConverterFactory.forString())
					;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Editable and adding cyclic list"), 0, lRowIdx);
				final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
				Spinner<String> lSpinner = new Spinner<String>( lObservableList )
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
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Observable list, 'z' prepended"), 0, lRowIdx);
				final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
				Spinner<String> lSpinner = new Spinner<String>( lObservableList );
				lObservableList.add(0, "z");
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Observable list, first removed"), 0, lRowIdx);
				final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
				Spinner<String> lSpinner = new Spinner<String>( lObservableList );
				lObservableList.remove("a");
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Observable list, emptied"), 0, lRowIdx);
				final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
				Spinner<String> lSpinner = new Spinner<String>( lObservableList );
				lObservableList.clear();
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("BigInteger range"), 0, lRowIdx);
				Spinner<BigInteger> lSpinner = new Spinner<BigInteger>(new SpinnerBigIntegerList(BigInteger.valueOf(Long.MIN_VALUE), BigInteger.valueOf(Long.MIN_VALUE + 1000)));
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(new SpinnerIntegerList());
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 10..110"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(10, 110);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 10..110 with setIndex 50"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(10, 110);
				lSpinner.setIndex(50);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 0..1000 step 10"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(0, 100, 10);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range -10..10"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(-10, 10);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 10..-10"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(10, -10);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("align right"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>( "a", "b", "c", "d", "e" )
					.withAlignment(Pos.CENTER_RIGHT)
					;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("align right"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>( "a", "b", "c", "d", "e" )
					.withAlignment(Pos.CENTER_RIGHT)
					.withEditable(true)
					.withStringConverter(StringConverterFactory.forString())
					;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 0..100 with %"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(0, 100)
						.withPostfix("%")
						.withAlignment(Pos.CENTER_RIGHT)
						;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer range 0..100 with %"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(0, 100)
						.withPostfix("%")
						.withAlignment(Pos.CENTER_RIGHT)
						.withEditable(true)
						.withStringConverter(StringConverterFactory.forInteger())
						;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer currency 0..100"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(0, 100)
						.withPrefix("$ ")
						.withAlignment(Pos.CENTER_RIGHT)
						;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("Integer currency 0..100"), 0, lRowIdx);
				Spinner<Integer> lSpinner = new Spinner<Integer>(0, 100)
						.withPrefix("$ ")
						.withAlignment(Pos.CENTER_RIGHT)
						.withEditable(true)
						.withStringConverter(StringConverterFactory.forInteger())
						;
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			
			lHBox.getChildren().add(lGridPane);
		}
		
		{
			GridPane lGridPane = new GridPane();
			lGridPane.setVgap(5.0);
			lGridPane.setPadding(new Insets(5.0));
			int lRowIdx = 0;

			// arrow position
			{
				lGridPane.add(new Label("HORIZONTAL LEADING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.LEADING);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("HORIZONTAL TRAILING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.TRAILING);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("HORIZONTAL SPLIT"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.SPLIT);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL LEADING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.LEADING);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL TRAILING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.TRAILING);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL SPLIT"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.SPLIT);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("HORIZONTAL LEADING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.LEADING).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("HORIZONTAL TRAILING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.TRAILING).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("HORIZONTAL SPLIT"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.SPLIT).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL LEADING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.LEADING).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL TRAILING"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.TRAILING).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			{
				lGridPane.add(new Label("VERTICAL SPLIT"), 0, lRowIdx);
				Spinner<String> lSpinner = new Spinner<String>("a", "b", "c").withArrowDirection(ArrowDirection.VERTICAL).withArrowPosition(ArrowPosition.SPLIT).withEditable(true);
				lGridPane.add(lSpinner, 1, lRowIdx++);
			}
			
			lHBox.getChildren().add(lGridPane);
		}
		
		
		// create scene
        Scene scene = new Scene(lHBox, 800, 600);
        
        // create stage
        stage.setTitle("SpinnerX");
        stage.setScene(scene);
        stage.show();
    }
}
