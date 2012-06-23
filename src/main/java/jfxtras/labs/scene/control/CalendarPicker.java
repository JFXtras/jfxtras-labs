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
import java.util.List;
import java.util.Locale;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

/**
 * Calendar picker component
 * Unfortunately this control suffers from JavaFX issue RT-16732 (http://javafx-jira.kenai.com/browse/RT-16732)!
 * The workaround is to load the required CSS (CalendarPickerX.css) in the application's own CSS. 
 * 
 * @author Tom Eugelink
 */
public class CalendarPicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPicker()
	{
		construct();
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(this.getClass().getSimpleName().toLowerCase());
		
		// construct properties
		constructCalendar();
		constructCalendars();
		constructLocale();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/labs/internal/scene/control/" + this.getClass().getSimpleName() + ".css").toString();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** Calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return iCalendarObjectProperty; }
	final private ObjectProperty<Calendar> iCalendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar");
	// construct property
	private void constructCalendar()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				// if the new value is set to null, remove the old value
				if (oldValue != null) {
					calendars().remove(oldValue);
				}
				if (newValue != null && calendars().contains(newValue) == false) {
					calendars().add(newValue);
				}
			} 
		});
	}
	// java bean API
	public Calendar getCalendar() { return iCalendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { iCalendarObjectProperty.setValue(value); }
	public CalendarPicker withCalendar(Calendar value) { setCalendar(value); return this; } 

	/** Calendars: */
	public ObservableList<Calendar> calendars() { return iCalendars; }
	final private ObservableList<Calendar> iCalendars =  javafx.collections.FXCollections.observableArrayList();
	final static public String CALENDARS_PROPERTY_ID = "calendars";
	// construct property
	private void constructCalendars()
	{
		// make sure the singled out calendar is 
		calendars().addListener(new ListChangeListener<Calendar>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0)
			{
				// if the active calendar is not longer in calendars, select another
				if (!calendars().contains(getCalendar())) 
				{
					// if there are other left
					if (calendars().size() > 0) 
					{
						// select the first
						setCalendar( calendars().get(0) );
					}
					else 
					{
						// clear it
						setCalendar(null);
					}
				}
			} 
		});
	}

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return iLocaleObjectProperty; }
	volatile private ObjectProperty<Locale> iLocaleObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault());
	final static public String LOCALE_PROPERTY_ID = "locale";
	// java bean API
	public Locale getLocale() { return iLocaleObjectProperty.getValue(); }
	public void setLocale(Locale value) { iLocaleObjectProperty.setValue(value); }
	public CalendarPicker withLocale(Locale value) { setLocale(value); return (CalendarPicker)this; } 
	// construct property
	private void constructLocale()
	{
		// make sure the singled out calendar is 
		localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				// if the locale is changed, all selected calendars must be cleared
				// because "equals" takes the locale of the calendar in account and suddenly calendars that were equal in date no longer are
// TODO: skin are created after the control, so this clears any set values because it is initialized in the constructor
// so we need a smarter way to do this, however calendar does not have a "getLocale"
// should we recreated all values copying DMY?				
//calendars().clear();
			} 
		});
	}
	
	/** Mode: single, range or multiple */
	public ObjectProperty<Mode> modeProperty() { return iModeObjectProperty; }
	final private SimpleObjectProperty<Mode> iModeObjectProperty = new SimpleObjectProperty<Mode>(this, "mode", Mode.SINGLE);
	final static public String MODE_PROPERTY_ID = "mode";
	public enum Mode { SINGLE, MULTIPLE, RANGE };
	// java bean API
	public Mode getMode() { return iModeObjectProperty.getValue(); }
	public void setMode(Mode value)
	{
		if (value == null) throw new IllegalArgumentException("NULL not allowed");
		
		// set it
		iModeObjectProperty.setValue(value);
		
		// update the collection; remove excessive calendars
		while (getMode() == Mode.SINGLE && calendars().size() > 1) {
			calendars().remove(calendars().size() - 1);
		}
	}
	public CalendarPicker withMode(Mode value) { setMode(value); return this; } 


	// ==================================================================================================================
	// SUPPORT
	

	/*
	 * 
	 */
	static public String quickFormatCalendar(Calendar value)
	{
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	static public String quickFormatCalendar(List<Calendar> value)
	{
		if (value == null) return "null";
		String s = value.size() + "x [";
		for (Calendar lCalendar : value)
		{
			if (s.endsWith("[") == false) s += ",";
			s += quickFormatCalendar(lCalendar);
		}
		s += "]";
		return s;
	}
}
