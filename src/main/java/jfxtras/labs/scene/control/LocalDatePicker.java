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

import java.util.Calendar;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import java.time.LocalDate;


/**
 * LocalDate (JSR-310) picker component.
 * This is an extention of the CalendarPicker adding the new date API JSR-310.
 * Since Calendar will not be removed from the JDK, too many applications use it, this approach of extending CalendarPicker is the most flexible one. 
 * 
 * @author Tom Eugelink
 */
public class LocalDatePicker extends CalendarPicker
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localDate
	 */
	public LocalDatePicker(LocalDate localDate)
	{
		construct();
		setLocalDate(localDate);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// construct properties
		constructLocalDate();
		constructLocalDates();
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDate: */
	public ObjectProperty<LocalDate> localDateProperty() { return localDateObjectProperty; }
	private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	public LocalDate getLocalDate() { return localDateObjectProperty.getValue(); }
	public void setLocalDate(LocalDate value) { localDateObjectProperty.setValue(value); }
	public LocalDatePicker withLocalDate(LocalDate value) { setLocalDate(value); return this; } 
	private void constructLocalDate()
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
				calendarProperty().set( newValue == null ? null : createCalendarFromLocalDate(newValue));
			} 
		});
	}

	/** LocalDates: */
	public ObservableList<LocalDate> localDates() { return localDates; }
	private final ObservableList<LocalDate> localDates =  javafx.collections.FXCollections.observableArrayList();
	private void constructLocalDates()
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
		lCalendar.set(Calendar.MONTH, localDate.getMonthValue() - 1);
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
}
