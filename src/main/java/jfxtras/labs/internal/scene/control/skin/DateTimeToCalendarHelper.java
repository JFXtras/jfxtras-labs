/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxtras.labs.internal.scene.control.skin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.util.DateTimeUtil;

/**
 *
 * @author user
 */
public class DateTimeToCalendarHelper {

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDate(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDate> localDateProperty, ObjectProperty<Locale> localeProperty)
	{
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateProperty.set(DateTimeUtil.createLocalDateFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateProperty.addListener( (ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) -> {
			calendarProperty.set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDate(newValue, localeProperty.get()));
		});
	}

	/**
	 * 
	 * @param calendarProperty
	 * @param localDateProperty
	 * @param localeProperty 
	 */
	static public void syncLocalDateTime(ObjectProperty<Calendar> calendarProperty, ObjectProperty<LocalDateTime> localDateTimeProperty, ObjectProperty<Locale> localeProperty)
	{
		// forward changes from calendar
		calendarProperty.addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			localDateTimeProperty.set(DateTimeUtil.createLocalDateTimeFromCalendar(newValue)); 
		});
		
		// forward changes to calendar
		localDateTimeProperty.addListener( (ObservableValue<? extends LocalDateTime> observableValue, LocalDateTime oldValue, LocalDateTime newValue) -> {
			calendarProperty.set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDateTime(newValue, localeProperty.get()));
		});
	}

	/**
	 * 
	 * @param calendars
	 * @param localDates
	 * @param localeProperty 
	 */
	static public void syncLocalDates(ObservableList<Calendar> calendars, ObservableList<LocalDate> localDates, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDate lLocalDate : localDates) {
			Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate)) {
						localDates.remove(lLocalDate);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (localDates.contains(lLocalDate) == false) {
						localDates.add(lLocalDate);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDates.addListener( (ListChangeListener.Change<? extends LocalDate> change) -> {
			while (change.next()) {
				for (LocalDate lLocalDate : change.getRemoved()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDate lLocalDate : change.getAddedSubList()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}
	
		/**
	 * 
	 * @param calendars
	 * @param localDateTimes
	 * @param localeProperty 
	 */
	static public void syncLocalDateTimes(ObservableList<Calendar> calendars, ObservableList<LocalDateTime> localDateTimes, ObjectProperty<Locale> localeProperty)
	{
		// initial values
		for (LocalDateTime lLocalDateTime : localDateTimes) {
			Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
			calendars.add(lCalendar);
		}
		
		// forward changes from calendar
		calendars.addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime)) {
						localDateTimes.remove(lLocalDateTime);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDateTime lLocalDateTime = DateTimeUtil.createLocalDateTimeFromCalendar(lCalendar);
					if (localDateTimes.contains(lLocalDateTime) == false) {
						localDateTimes.add(lLocalDateTime);
					}
				}
			}
		});
		
		// forward changes to calendar
		localDateTimes.addListener( (ListChangeListener.Change<? extends LocalDateTime> change) -> {
			while (change.next()) {
				for (LocalDateTime lLocalDateTime : change.getRemoved()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar)) {
						calendars.remove(lCalendar);
					}
				}
				for (LocalDateTime lLocalDateTime : change.getAddedSubList()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDateTime(lLocalDateTime, localeProperty.get());
					if (calendars.contains(lCalendar) == false) {
						calendars.add(lCalendar);
					}
				}
			}
		});
	}

}
