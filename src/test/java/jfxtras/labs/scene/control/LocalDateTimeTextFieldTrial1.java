/**
 * LocalDateTimeTextFieldTrial1.java
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
import java.util.Calendar;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.time.calendar.LocalDateTime;

/**
 * @author Tom Eugelink
 */
public class LocalDateTimeTextFieldTrial1 extends Application {
	
    public static void main(String[] args) {
    	//java.util.Locale.setDefault(new java.util.Locale("de")); // weeks starts on monday
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		HBox lHBox = new HBox();
		LocalDateTimeTextField lFocusRequestingLocalDateTimeTextField = null;
		
		{
			GridPane lGridPane = new GridPane();
			lGridPane.setVgap(5.0);
			lGridPane.setPadding(new Insets(5.0));
			int lRowIdx = 0;
			
	        // default textfield
			{
				lGridPane.add(new Label("default"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.setTooltip(new Tooltip("custom tooltip"));
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalDateTimeTextField.localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalDateTimeTextField.getLocalDateTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
			}
			
	        // preset value
			{
				lGridPane.add(new Label("preset value with parse error"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.of(2011, 2, 01, 21, 22, 23)); // set a value
				lLocalDateTimeTextField.parseErrorCallbackProperty().set(new Callback<Throwable, Void>()
				{
					@Override
					public Void call(Throwable t)
					{
						System.out.println("parse error: " + t.getMessage());
						return null;
					}
				});
				
				// test requesting focus
				lFocusRequestingLocalDateTimeTextField = lLocalDateTimeTextField;
			}
			
	        // programmatically set to null
			{
				lGridPane.add(new Label("programatically to null"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.setPromptText("type date here");
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lLocalDateTimeTextField.localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>()
				{
					@Override
					public void changed(ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue)
					{
						lValueTextField.setText("" + newValue);
					}
				});
				lValueTextField.setText("" + lLocalDateTimeTextField.getLocalDateTime());
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
				
				// set
				lLocalDateTimeTextField.setCalendar(Calendar.getInstance());
				lLocalDateTimeTextField.setCalendar(null);
			}
			
	        // preset value with time
			{
				lGridPane.add(new Label("preset value with time"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.dateFormatProperty().set(SimpleDateFormat.getDateTimeInstance());
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.now()); // set a value
			}
			
	        // alternate parsers
			{
				lGridPane.add(new Label("alternate parser"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.dateFormatsProperty().add(new SimpleDateFormat("yyyy-MM-dd"));
				lLocalDateTimeTextField.dateFormatsProperty().add(new SimpleDateFormat("yyyyMMdd"));
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.of(2011, 2, 01, 21, 22, 23)); // set a value
			}
			
	        // disabled
			{
				lGridPane.add(new Label("custom icon disabled"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.disableProperty().set(true);
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.of(2011, 2, 01, 21, 22, 23)); // set a value
			}
			
	        // custom icon
			{
				lGridPane.add(new Label("custom icon"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.setId("custom");
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.of(2011, 2, 01, 21, 22, 23)); // set a value
			}
			
	        // custom icon disabled
			{
				lGridPane.add(new Label("custom icon disabled"), 0, lRowIdx);
				LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
				lLocalDateTimeTextField.setId("custom");
				lLocalDateTimeTextField.disableProperty().set(true);
				lGridPane.add(lLocalDateTimeTextField, 1, lRowIdx++);
				
				lLocalDateTimeTextField.localDateTimeProperty().set(LocalDateTime.of(2011, 2, 01, 21, 22, 23)); // set a value
			}
			
			lHBox.getChildren().add(lGridPane);
		}
			
        // create scene
        Scene scene = new Scene(lHBox, 800, 600);

		// load custom CSS
        //scene.getStylesheets().addAll(this.getClass().getResource(this.getClass().getSimpleName() + ".css").toExternalForm());
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
		lFocusRequestingLocalDateTimeTextField.requestFocus();
    }
}
