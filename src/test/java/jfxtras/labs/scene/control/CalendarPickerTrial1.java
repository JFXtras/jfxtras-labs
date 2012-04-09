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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Tom Eugelink
 */
public class CalendarPickerTrial1 extends Application {
	
    public static void main(String[] args) {
    	//java.util.Locale.setDefault(new java.util.Locale("de")); // weeks starts on monday
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

        // add a node
		CalendarPicker lXCalendarPicker = new CalendarPicker();
		lXCalendarPicker.setCalendar(new GregorianCalendar(2011, 06, 01)); // set a value
		//lXCalendarPicker.setMode(XCalendarPicker.Mode.RANGE);
		//lXCalendarPicker.setMode(XCalendarPicker.Mode.MULTIPLE);
        
        // create scene
        Scene scene = new Scene(lXCalendarPicker, 300, 300);

        // bind Picker to BusinessModelBean 
        BusinessModelBean lBusinessModelBean = new BusinessModelBean();
        //Binding.bind(lXCalendarPicker.calendar(), lBusinessModelBean.iCalendarObjectProperty); // works and handles the exception for odd dates, but only in the setter
        //lXCalendarPicker.calendar().bind( lBusinessModelBean.iCalendarObjectProperty ); // this won't work "A bound value cannot be set."
        //lBusinessModelBean.iCalendarObjectProperty.bind(lXCalendarPicker.calendar()); // this works, but does not handle the exception for odd dates since it bypasses the setter and exceptions are ignored
        //Bindings.bindWithInverse(lXCalendarPicker.calendar(), lBusinessModelBean.iCalendarObjectProperty); // this works, but the exception is not preventing the value to be set
        
        // create stage
        stage.setTitle("CalendarPickerX");
        stage.setScene(scene);
        stage.show();	
    }
	

	class BusinessModelBean
	{
		public BusinessModelBean()
		{
//			iCalendarObjectProperty.addListener(new InvalidationListener<Calendar>()
//			{
//				@Override
//				public void invalidated(ObservableValue<? extends Calendar> observableValue)
//				{
//					Calendar value = observableValue.getValue();
//					if (value != null && value.get(Calendar.DATE) % 2 == 1)
//					{
//						System.out.println("odd date");
//						throw new IllegalArgumentException("odd date");
//					}
//				}
//			});
		}
		
		final public ObjectProperty<Calendar> iCalendarObjectProperty = new SimpleObjectProperty<Calendar>()
		{
			// check it in the setter, but the setter is not called when JavaFX binding is used, so this is not a 100% converage
			@Override
			public void set(Calendar value)
			{
				if (value != null && value.get(Calendar.DATE) % 2 == 1)
				{
					System.out.println("odd date");
					throw new IllegalArgumentException("odd date");
				}
				super.set(value);
			}
			
			// check after setting the value, when the property has become invalid, this is 100% converage but after the fact
//			@Override
//			public void invalidated()
//			{
//				Calendar value = get();
//				if (value != null && value.get(Calendar.DATE) % 2 == 1)
//				{
//					System.out.println("odd date");
//					throw new IllegalArgumentException("odd date");
//				}
//				super.invalidated();
//			}
		};
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
