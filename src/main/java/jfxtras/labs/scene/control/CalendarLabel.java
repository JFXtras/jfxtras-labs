/**
 * CalendarLabel.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

import java.text.DateFormat;
import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

/**
 * Label implementation that displays a localized date (from Calendar). It
 * reacts on both: changes in format property and changes in the value property
 * (as opposed to the according method in {@link javafx.beans.binding.Bindings} which does not allow
 * to change the Format after the binding is done.
 * If either property is null the Label displays "n/a".
 *
 * @author Thomas Bolz
 */
public class CalendarLabel extends Label {

	// The value property
    final private ObjectProperty<Calendar> value = new SimpleObjectProperty<>();
    public final Calendar getValue() {return value.get();}
    public final void setValue(Calendar value) {this.value.set(value);}
    public ObjectProperty<Calendar> valueProperty() {return value;}

    // the DateFormat property
    final private ObjectProperty<DateFormat> format = new SimpleObjectProperty<>();
    public DateFormat getFormat() {return format.getValue();}
    public final void setFormat(DateFormat value) {format.set(value);}
    public ObjectProperty<DateFormat> formatProperty() {return format;}

    /**
     * Creates a {@link CalendarLabel} with a default DateFormat and value == null.
     */
    public CalendarLabel() {
        this(null);
    }

    /**
     * Creates a {@link CalendarLabel} with a default DateFormat and the give Calendar value.
     */
    public CalendarLabel(Calendar value) {
        this(value, DateFormat.getDateInstance());
    }

    /**
	 * Creates a {@link CalendarLabel} with the given DateFormat and Calendar.
     */
    public CalendarLabel(Calendar value, DateFormat df) {
        super();
        setFormat(df);
        initHandlers();
        setValue(value);
    }

    /**
	 *
     */
    private void initHandlers() {

        // Text is formatted and displayed if the underlying calendar or format changes.
        valueProperty().addListener(new ChangeListener<Calendar>() {

            @Override
            public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue) {
            	if (null != getValue()) {
            		setText(getFormat().format(getValue().getTime()));
            	}
            }
        });
		formatProperty().addListener(new ChangeListener<DateFormat>() {

			@Override
			public void changed(
					ObservableValue<? extends DateFormat> observable, DateFormat olValue, DateFormat newValue) {
				if (null != getValue()) {
					setText(getFormattedString());
				}
			}

		});
    }

    /**
	 * Sets the {@link #textProperty()} to the formatted value or "n/a" if either {@link #value} or {@link #format} is null.
     */
    private String getFormattedString() {
    	if(getValue() == null || getFormat() == null) {
    		return null;
    	}
    	try {
    		String formattedString = getFormat().format(getValue().getTime());
    		return formattedString;
		} catch (Exception e) {
			setText("n/a");
			e.printStackTrace();
			return null;
		}
    }

}
