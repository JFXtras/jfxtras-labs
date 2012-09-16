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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.time.calendar.LocalDate;

/**
 * Calendar picker component
 * This class is a extention on LocalDatePicker to add Calendar support, it takes care of converting to and from LocalDate and makes easy binding possible. 
 * It adds a calendar property and a calenders observable list.
 * 
 * @author Tom Eugelink
 */
public class CalendarPicker2 extends LocalDatePicker
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPicker2()
	{
		construct();
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// construct properties
		constructCalendar();
		constructCalendars();
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** Calendar: */
	public ObjectProperty<Calendar> calendarProperty() { return calendarObjectProperty; }
	final private ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "calendar");
	// construct property
	private void constructCalendar()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				localDateProperty().set(createLocalDateFromCalendar(newValue));
			} 
		});
		
		// if the inherited value is changed, make sure calendar is updated
		localDateProperty().addListener(new ChangeListener<LocalDate>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue)
			{
				calendarObjectProperty.set( newValue == null ? null : createCalendarFromLocalDate(newValue));
			} 
		});
	}
	// java bean API
	public Calendar getCalendar() { return calendarObjectProperty.getValue(); }
	public void setCalendar(Calendar value) { calendarObjectProperty.setValue(value); }
	public CalendarPicker2 withCalendar(Calendar value) { setCalendar(value); return this; } 

	/** Calendars: */
	public ObservableList<Calendar> calendars() { return calendars; }
	final private ObservableList<Calendar> calendars =  javafx.collections.FXCollections.observableArrayList();
	// construct property
	private void constructCalendars()
	{
		// forward changes 
		calendars().addListener(new ListChangeListener<Calendar>() 
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends Calendar> change)
			{
				while (change.next())
				{
					 for (Calendar lCalendar : change.getRemoved()) 
					 {
						 LocalDate lLocalDate = createLocalDateFromCalendar(lCalendar);
                         if (localDates().contains(lLocalDate)) localDates().remove(lLocalDate);
                     }
                     for (Calendar lCalendar : change.getAddedSubList()) 
                     {
						 LocalDate lLocalDate = createLocalDateFromCalendar(lCalendar);
						 if (localDates().contains(lLocalDate) == false) localDates().add(lLocalDate);
                     }				
				}
			} 
		});
		// handle changes 
		localDates().addListener(new ListChangeListener<LocalDate>() 
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends LocalDate> change)
			{
				while (change.next())
				{
					 for (LocalDate lLocalDate : change.getRemoved()) 
					 {
						 Calendar lCalendar = createCalendarFromLocalDate(lLocalDate);
                         if (calendars().contains(lCalendar)) calendars().remove(lCalendar);
                     }
                     for (LocalDate lLocalDate : change.getAddedSubList()) 
                     {
						 Calendar lCalendar = createCalendarFromLocalDate(lLocalDate);
						 if (calendars().contains(lCalendar) == false) calendars().add(lCalendar);
                     }				
				}
			} 
		});
	}


	// ==================================================================================================================
	// SUPPORT

	/**
	 * 
	 * @param localDate
	 * @return
	 */
	private Calendar createCalendarFromLocalDate(LocalDate localDate)
	{
		if (localDate == null) return null;
		Calendar lCalendar = Calendar.getInstance(getLocale());
		lCalendar.set(Calendar.YEAR, localDate.getYear());
		lCalendar.set(Calendar.MONTH, localDate.getMonthOfYear().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDate.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, 0);
		lCalendar.set(Calendar.MINUTE, 0);
		lCalendar.set(Calendar.SECOND, 0);
		lCalendar.set(Calendar.MILLISECOND, 0);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	private LocalDate createLocalDateFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalDate lLocalDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
		return lLocalDate;
	}

	/**
	 * A quick utility method to make a calendar instance human readable
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendar(Calendar value)
	{
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * A quick utility method to make a list of calendar instances human readable
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendar(List<Calendar> value)
	{
		if (value == null) return "null";
		String s = value.size() + "x [";
		for (Calendar lCalendar : value)
		{
			if (s.endsWith("[") == false) s += ", ";
			s += quickFormatCalendar(lCalendar);
		}
		s += "]";
		return s;
	}
}
