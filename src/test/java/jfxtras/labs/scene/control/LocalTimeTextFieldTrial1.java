/**
 * LocalTimeTextFieldTrial1.java
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

import java.text.SimpleDateFormat;

import javax.time.calendar.LocalTime;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author Tom Eugelink
 */
public class LocalTimeTextFieldTrial1 extends Application {
	
    public static void main(String[] args) {
    	//java.util.Locale.setDefault(new java.util.Locale("de")); // weeks starts on monday
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		HBox lHBox = new HBox();
		LocalTimeTextField lFocusRequestingLocalTimeTextField = null;
		
		{
			GridPane lGridPane = new GridPane();
			lGridPane.setVgap(5.0);
			lGridPane.setPadding(new Insets(5.0));
			int lRowIdx = 0;
			
	        // default textfield
			{
				lGridPane.add(new Label("default"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lLocalTimeTextField.setLocalTime(LocalTime.now());
				lLocalTimeTextField.setMinuteStep(5);
				lLocalTimeTextField.setShowLabels(false);
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalTimeTextField.localTimeProperty().addListener(new ChangeListener<LocalTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalTimeTextField.getLocalTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
			}
			
	        // preset value
			{
				lGridPane.add(new Label("preset value"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx++);
				
				lLocalTimeTextField.localTimeProperty().set(LocalTime.of(13, 45, 30)); // set a value
				lLocalTimeTextField.parseErrorCallbackProperty().set(new Callback<Throwable, Void>()
				{
					@Override
					public Void call(Throwable t)
					{
						System.out.println("parse error: " + t.getMessage());
						return null;
					}
				});
				
				// for setting focus
				lFocusRequestingLocalTimeTextField = lLocalTimeTextField;
			}
			
	        // programmatically set to null
			{
				lGridPane.add(new Label("programatically to null"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lLocalTimeTextField.setPromptText("type time here");
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalTimeTextField.localTimeProperty().addListener(new ChangeListener<LocalTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalTimeTextField.getLocalTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
				
				// set
				lLocalTimeTextField.setLocalTime(LocalTime.now());
				lLocalTimeTextField.setLocalTime(null);
			}
			
	        // DateFormat
			{
				lGridPane.add(new Label("date format"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lLocalTimeTextField.setDateFormat(new SimpleDateFormat("HH:mm:ss.SSS"));
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalTimeTextField.localTimeProperty().addListener(new ChangeListener<LocalTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalTimeTextField.getLocalTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
				
				// process an error
				lLocalTimeTextField.parseErrorCallbackProperty().set(new Callback<Throwable, Void>()
				{
					@Override
					public Void call(Throwable t)
					{
						System.out.println("parse error: " + t.getMessage());
						return null;
					}
				});

				// set
				lLocalTimeTextField.setLocalTime(LocalTime.now());
			}
						
	        // DateFormats
			{
				lGridPane.add(new Label("multiple date formats"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lLocalTimeTextField.setDateFormat(new SimpleDateFormat("HH:mm:ss"));
				lLocalTimeTextField.dateFormatsProperty().add(new SimpleDateFormat("HH:mm:ss.SSS"));
				lLocalTimeTextField.dateFormatsProperty().add(new SimpleDateFormat("HH:mm"));
				lLocalTimeTextField.dateFormatsProperty().add(new SimpleDateFormat("HH"));
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalTimeTextField.localTimeProperty().addListener(new ChangeListener<LocalTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalTimeTextField.getLocalTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
				
				// set
				lLocalTimeTextField.setLocalTime(LocalTime.now());
			}
			
	        // disabled
			{
				lGridPane.add(new Label("disabled"), 0, lRowIdx);
				LocalTimeTextField lLocalTimeTextField = new LocalTimeTextField();
				lLocalTimeTextField.disableProperty().set(true);
				lGridPane.add(lLocalTimeTextField, 1, lRowIdx++);
				
				lLocalTimeTextField.localTimeProperty().set(javax.time.calendar.LocalTime.of(13, 45, 30)); // set a value
			}
			
			lHBox.getChildren().add(lGridPane);
		}
			
		
        // create scene
        Scene scene = new Scene(lHBox, 800, 600);

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        // set focus
        lFocusRequestingLocalTimeTextField.requestFocus();
    }
}
