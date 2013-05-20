package jfxtras.labs.scene.control;

import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.time.calendar.LocalDateTime;

import jfxtras.labs.util.DateTimeUtil;

/**
 * LocalDateTime (JSR-310) text field component.
 * This is an extension of the CalendarTextField adding the new date API JSR-310.
 * Since Calendar will not be removed from the JDK, too many applications use it, this approach of extending CalendarTextField is the most flexible one. 
 * 
 * @author Tom Eugelink
 */
public class LocalDateTimeTextField extends CalendarTextField
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDateTimeTextField()
	{
		construct();
	}

	/**
	 * 
	 * @param localDateTime
	 */
	public LocalDateTimeTextField(LocalDateTime localDateTime)
	{
		construct();
		setLocalDateTime(localDateTime);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// construct properties
		constructLocalDateTime();
	}

	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDateTime: */
	public ObjectProperty<LocalDateTime> localDateTimeProperty() { return localDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> localDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "localDateTime");
	public LocalDateTime getLocalDateTime() { return localDateTimeObjectProperty.getValue(); }
	public void setLocalDateTime(LocalDateTime value) { localDateTimeObjectProperty.setValue(value); }
	public LocalDateTimeTextField withLocalDateTime(LocalDateTime value) { setLocalDateTime(value); return this; } 
	private void constructLocalDateTime()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				localDateTimeProperty().set(DateTimeUtil.createLocalDateTimeFromCalendar(newValue));
			} 
		});
		
		// if the inherited value is changed, make sure calendar is updated
		localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue)
			{
				calendarProperty().set( newValue == null ? null : DateTimeUtil.createCalendarFromLocalDateTime(newValue, getLocale()));
			} 
		});
	}

	
	// ==================================================================================================================
	// SUPPORT
}
