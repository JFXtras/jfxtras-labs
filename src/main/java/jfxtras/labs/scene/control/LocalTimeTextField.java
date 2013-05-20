package jfxtras.labs.scene.control;

import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.time.calendar.LocalTime;

import jfxtras.labs.util.DateTimeUtil;

/**
 * LocalTime (JSR-310) text field component.
 * This is an extension of the CalendarTimeTextField adding the new date API JSR-310.
 * Since Calendar will not be removed from the JDK, too many applications use it, this approach of extending CalendarTextField is the most flexible one. 
 * 
 * @author Tom Eugelink
 */
public class LocalTimeTextField extends CalendarTimeTextField
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalTimeTextField()
	{
		construct();
	}

	/**
	 * 
	 * @param localTime
	 */
	public LocalTimeTextField(LocalTime localTime)
	{
		construct();
		setLocalTime(localTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// construct properties
		constructLocalTime();
	}

	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalTime: */
	public ObjectProperty<LocalTime> localTimeProperty() { return localTimeObjectProperty; }
	private final ObjectProperty<LocalTime> localTimeObjectProperty = new SimpleObjectProperty<LocalTime>(this, "localTime");
	public LocalTime getLocalTime() { return localTimeObjectProperty.getValue(); }
	public void setLocalTime(LocalTime value) { localTimeObjectProperty.setValue(value); }
	public LocalTimeTextField withLocalTime(LocalTime value) { setLocalTime(value); return this; } 
	private void constructLocalTime()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				localTimeProperty().set(DateTimeUtil.createLocalTimeFromCalendar(newValue));
			} 
		});
		
		// if the inherited value is changed, make sure calendar is updated
		localTimeProperty().addListener(new ChangeListener<LocalTime>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalTime> observableValue, LocalTime oldValue, LocalTime newValue)
			{
				calendarProperty().set( newValue == null ? null : DateTimeUtil.createCalendarFromLocalTime(newValue));
			} 
		});
	}

	
	// ==================================================================================================================
	// SUPPORT
}
