/**
 * CalendarPicker.java
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
 * The calendar is (and should) be treated as immutable. That means the setter of Calendar is not used to modify its value, but each time a new instance (clone) is put in the calendar property.
 * So you cannot rely that exactly the same Calendar object that was set or added will be stored and returned.
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
		this.getStyleClass().add(CalendarPicker.class.getSimpleName());
		
		// construct properties
		constructCalendar();
		constructCalendars();
		constructLocale();
//		constructMode();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/labs/internal/scene/control/" + CalendarPicker.class.getSimpleName() + ".css").toString();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar");
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarPicker withCalendar(Calendar value) { setCalendar(value); return this; } 
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
				if (newValue != null && calendars().contains(newValue) == false) {
					calendars().add(newValue);
				}
				if (oldValue != null) {
					calendars().remove(oldValue);
				}
			} 
		});
	}

	/** Calendars: */
	public ObservableList<Calendar> calendars() { return calendars; }
	final private ObservableList<Calendar> calendars =  javafx.collections.FXCollections.observableArrayList();
	// construct property
	private void constructCalendars()
	{
		// make sure the singled out calendar is 
		calendars().addListener(new ListChangeListener<Calendar>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> change)
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
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	volatile private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
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
	public ObjectProperty<Mode> modeProperty() { return modeObjectProperty; }
	final private SimpleObjectProperty<Mode> modeObjectProperty = new SimpleObjectProperty<Mode>(this, "mode", Mode.SINGLE)
	{
		public void set(Mode value)
		{
			if (value == null) throw new NullPointerException("Null not allowed");
			super.set(value);
		}
	};
	public enum Mode { SINGLE, MULTIPLE, RANGE };
	public Mode getMode() { return modeObjectProperty.getValue(); }
	public void setMode(Mode value) { modeObjectProperty.setValue(value); }
	public CalendarPicker withMode(Mode value) { setMode(value); return this; } 
	// construct property
//	private void constructMode()
//	{
//		// if this value is changed by binding, make sure related things are updated
//		modeProperty().addListener(new ChangeListener<Mode>()
//		{
//			@Override
//			public void changed(ObservableValue<? extends Mode> observableValue, Mode oldValue, Mode newValue)
//			{
//				// update the collection; remove excessive calendars
//				while (getMode() == Mode.SINGLE && calendars().size() > 1) {
//					calendars().remove(calendars().size() - 1);
//				}
//			} 
//		});
//	}

	/** ShowTime: only applicable in SINGLE mode */
	public ObjectProperty<Boolean> showTimeProperty() { return showTimeObjectProperty; }
	volatile private ObjectProperty<Boolean> showTimeObjectProperty = new SimpleObjectProperty<Boolean>(this, "showTime", false);
	public Boolean getShowTime() { return showTimeObjectProperty.getValue(); }
	public void setShowTime(Boolean value) { showTimeObjectProperty.setValue(value); }
	public CalendarPicker withShowTime(Boolean value) { setShowTime(value); return (CalendarPicker)this; } 
	

	// ==================================================================================================================
	// SUPPORT
	
	/**
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
