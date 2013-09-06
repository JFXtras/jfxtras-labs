/**
 * CalendarTextFieldTrial1.java
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
import java.util.GregorianCalendar;
import java.util.Locale;

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

/**
 * @author Tom Eugelink
 */
public class CalendarTextFieldTrial1 extends Application {
	
    public static void main(String[] args) {
    	//java.util.Locale.setDefault(new java.util.Locale("de")); // weeks starts on monday
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		HBox lHBox = new HBox();
		CalendarTextField lFocusRequestingCalendarTextField = null;
		
		{
			GridPane lGridPane = new GridPane();
			lGridPane.setVgap(5.0);
			lGridPane.setPadding(new Insets(5.0));
			int lRowIdx = 0;
			
	        // default textfield
			{
				lGridPane.add(new Label("default"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.setTooltip(new Tooltip("custom tooltip"));
				lGridPane.add(lCalendarTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lCalendarTextField.calendarProperty().addListener(new ChangeListener<Calendar>()
				{
					@Override
					public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
					{
						lValueTextField.setText(CalendarPicker.quickFormatCalendar(newValue));
					}
				});
				lValueTextField.setText(CalendarPicker.quickFormatCalendar(lCalendarTextField.getCalendar()));
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
			}
			
	        // preset value
			{
				lGridPane.add(new Label("preset value with parse error"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
				lCalendarTextField.parseErrorCallbackProperty().set(new Callback<Throwable, Void>()
				{
					@Override
					public Void call(Throwable t)
					{
						System.out.println("parse error: " + t.getMessage());
						return null;
					}
				});
				
				// test requesting focus
				lFocusRequestingCalendarTextField = lCalendarTextField;
			}
			
	        // programmatically set to null
			{
				lGridPane.add(new Label("programatically to null"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.setPromptText("type date here");
				lGridPane.add(lCalendarTextField, 1, lRowIdx);
				
				final TextField lValueTextField = new TextField();
				lCalendarTextField.calendarProperty().addListener(new ChangeListener<Calendar>()
				{
					@Override
					public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
					{
						lValueTextField.setText(CalendarPicker.quickFormatCalendar(newValue));
					}
				});
				lValueTextField.setText(CalendarPicker.quickFormatCalendar(lCalendarTextField.getCalendar()));
				lValueTextField.setDisable(true);
				lGridPane.add(lValueTextField, 2, lRowIdx++);
				
				// set
				lCalendarTextField.setCalendar(Calendar.getInstance());
				lCalendarTextField.setCalendar(null);
			}
			
	        // preset value with time
			{
				lGridPane.add(new Label("preset value with time"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.dateFormatProperty().set(SimpleDateFormat.getDateTimeInstance());
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(Calendar.getInstance()); // set a value
			}
			
	        // alternate parsers
			{
				lGridPane.add(new Label("alternate parser"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.dateFormatsProperty().add(new SimpleDateFormat("yyyy-MM-dd"));
				lCalendarTextField.dateFormatsProperty().add(new SimpleDateFormat("yyyyMMdd"));
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
			}
			
	        // locale DE
			{
				lGridPane.add(new Label("locale DE"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.setLocale(Locale.GERMAN);				
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
			}
			
	        // disabled
			{
				lGridPane.add(new Label("custom icon disabled"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.disableProperty().set(true);
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
			}
			
	        // custom icon
			{
				lGridPane.add(new Label("custom icon"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.setId("custom");
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
			}
			
	        // custom icon disabled
			{
				lGridPane.add(new Label("custom icon disabled"), 0, lRowIdx);
				CalendarTextField lCalendarTextField = new CalendarTextField();
				lCalendarTextField.setId("custom");
				lCalendarTextField.disableProperty().set(true);
				lGridPane.add(lCalendarTextField, 1, lRowIdx++);
				
				lCalendarTextField.calendarProperty().set(new GregorianCalendar(2011, 2, 01)); // set a value
			}
			
			lHBox.getChildren().add(lGridPane);
		}
			
        // create scene
        Scene scene = new Scene(lHBox, 800, 600);

		// load custom CSS
        scene.getStylesheets().addAll(this.getClass().getResource(this.getClass().getSimpleName() + ".css").toExternalForm());
		
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
		lFocusRequestingCalendarTextField.requestFocus();
    }
	
	/*
	 * 
	 */
	static protected String quickFormatCalendar(Calendar value)
	{
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}

}
