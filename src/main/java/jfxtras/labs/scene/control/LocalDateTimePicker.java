/**
 * LocalDateTimePicker.java
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

import java.util.Calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

import jfxtras.labs.util.DateTimeUtil;

/**
 * LocalDateTime (JSR-310) picker component.<br>
 * This is an extension of the CalendarPicker adding the new date API JSR-310.<br>
 * Since Calendar will not be removed from the JDK, too many applications use it, this approach of extending CalendarPicker is the most flexible one at this time. Do not assume that the calendar properties will be available in this control.<br>
 * 
 * @author Tom Eugelink
 */
public class LocalDateTimePicker extends CalendarPicker
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDateTimePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localDateTime
	 */
	public LocalDateTimePicker(LocalDateTime localDateTime)
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
		constructLocalDateTimes();
	}

	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDateTime: */
	public ObjectProperty<LocalDateTime> localDateTimeProperty() { return localDateTimeObjectProperty; }
	private final ObjectProperty<LocalDateTime> localDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "localDateTime");
	public LocalDateTime getLocalTimeDateTime() { return localDateTimeObjectProperty.getValue(); }
	public void setLocalDateTime(LocalDateTime value) { localDateTimeObjectProperty.setValue(value); }
	public LocalDateTimePicker withLocalDateTime(LocalDateTime value) { setLocalDateTime(value); return this; }
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
		localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>() {
			@Override
			public void changed(ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue) {
				calendarProperty().set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDateTime(newValue, getLocale()));
			}
		});
	}

	/** LocalDateTimes: */
	public ObservableList<LocalDateTime> localDateTimes() { return localDateTimes; }
	private final ObservableList<LocalDateTime> localDateTimes =  javafx.collections.FXCollections.observableArrayList();
	private void constructLocalDateTimes()
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
						 LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
                         if (localDateTimes().contains(lLocalDateTime)) localDateTimes().remove(lLocalDateTime);
                     }
                     for (Calendar lCalendar : change.getAddedSubList()) 
                     {
						 LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
						 if (localDateTimes().contains(lLocalDateTime) == false) localDateTimes().add(lLocalDateTime);
                     }				
				}
			} 
		});
		// handle changes 
		localDateTimes().addListener(new ListChangeListener<LocalDateTime>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends LocalDateTime> change) {
				while (change.next()) {
					for (LocalDateTime lLocalDateTime : change.getRemoved()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (calendars().contains(lCalendar)) calendars().remove(lCalendar);
					}
					for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (calendars().contains(lCalendar) == false) calendars().add(lCalendar);
					}
				}
			}
		});
	}

	/** highlightedLocalDateTimes: */
	public ObservableList<LocalDateTime> highlightedLocalDateTimes() { return localDateTimes; }
	private final ObservableList<LocalDateTime> highlightedLocalDateTimes =  javafx.collections.FXCollections.observableArrayList();
	private void constructHighlightedLocalDateTimes()
	{
		// forward changes
		highlightedCalendars().addListener(new ListChangeListener<Calendar>()
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends Calendar> change)
			{
				while (change.next())
				{
					for (Calendar lCalendar : change.getRemoved())
					{
						LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
						if (highlightedLocalDateTimes().contains(lLocalDateTime)) highlightedLocalDateTimes().remove(lLocalDateTime);
					}
					for (Calendar lCalendar : change.getAddedSubList())
					{
						LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
						if (highlightedLocalDateTimes().contains(lLocalDateTime) == false) highlightedLocalDateTimes().add(lLocalDateTime);
					}
				}
			}
		});
		// handle changes
		highlightedLocalDateTimes().addListener(new ListChangeListener<LocalDateTime>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends LocalDateTime> change) {
				while (change.next()) {
					for (LocalDateTime lLocalDateTime : change.getRemoved()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (highlightedLocalDateTimes().contains(lCalendar)) highlightedCalendars().remove(lCalendar);
					}
					for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (highlightedLocalDateTimes().contains(lCalendar) == false) highlightedCalendars().add(lCalendar);
					}
				}
			}
		});
	}

	/** disabledLocalDateTimes: */
	public ObservableList<LocalDateTime> disabledLocalDateTimes() { return localDateTimes; }
	private final ObservableList<LocalDateTime> disabledLocalDateTimes =  javafx.collections.FXCollections.observableArrayList();
	private void constructDisabledLocalDateTimes()
	{
		// forward changes
		disabledCalendars().addListener(new ListChangeListener<Calendar>()
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends Calendar> change)
			{
				while (change.next())
				{
					for (Calendar lCalendar : change.getRemoved())
					{
						LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
						if (disabledLocalDateTimes().contains(lLocalDateTime)) disabledLocalDateTimes().remove(lLocalDateTime);
					}
					for (Calendar lCalendar : change.getAddedSubList())
					{
						LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
						if (disabledLocalDateTimes().contains(lLocalDateTime) == false) disabledLocalDateTimes().add(lLocalDateTime);
					}
				}
			}
		});
		// handle changes
		disabledLocalDateTimes().addListener(new ListChangeListener<LocalDateTime>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends LocalDateTime> change) {
				while (change.next()) {
					for (LocalDateTime lLocalDateTime : change.getRemoved()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (disabledLocalDateTimes().contains(lCalendar)) disabledCalendars().remove(lCalendar);
					}
					for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
						Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, getLocale());
						if (disabledLocalDateTimes().contains(lCalendar) == false) disabledCalendars().add(lCalendar);
					}
				}
			}
		});
	}

	// ==================================================================================================================
	// SUPPORT

}
