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
package jfxtras.labs.internal.scene.control.skin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.internal.scene.control.behavior.CalendarPickerBehavior;
import jfxtras.labs.scene.control.CalendarPicker;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * This class contains common code to support skins that shows a month at once.
 * It assumes that there is a grid of clickables, one for every day of the month, and provides methods to help handle these.
 *  
 * @author Tom Eugelink
 *
 * @param <S> the actual skin class, so fluent methods return the correct class (see "return (S)this;")
 */
abstract public class CalendarPickerMonthlySkinAbstract<S> extends SkinBase<CalendarPicker, CalendarPickerBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPickerMonthlySkinAbstract(CalendarPicker control)
	{
		super(control, new CalendarPickerBehavior(control));
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
		// set displayed date
		setDisplayedCalendar(Calendar.getInstance());
		
		// react to changes in the locale 
		getSkinnable().localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				refreshLocale();
			} 
		});
		refreshLocale();
	}
	

	// ==================================================================================================================
	// PROPERTIES
	
	/** 
	 * DisplayedCalendar: this calendar always points to the first of the month being shown, 
	 * it also is used to determine first-day-of-week, weekday labels, etc
	 * The calendar should not be modified using any of its add or set methods (it should be considered immutable)  
	 */
	public Calendar getDisplayedCalendar() { return iDisplayedCalendarObjectProperty.getValue(); }
	public void setDisplayedCalendar(Calendar value)  
	{
		Calendar lValue = getDisplayedCalendar();
		
		// we need to explicitly break out if the YMD are equals (because we generate new Calendars which is because Calendars are mutable - bah), else we end up in an endless loop
		if ( value != null && lValue != null 
		  && value.get(Calendar.YEAR) == lValue.get(Calendar.YEAR)
		  && value.get(Calendar.MONTH) == lValue.get(Calendar.MONTH)
		  && value.get(Calendar.DATE) == lValue.get(Calendar.DATE))
		{
			return;
		}
		
		// set value
		iDisplayedCalendarObjectProperty.setValue(derriveDisplayedCalendar(value)); 
	}
	public S withDisplayedCalendar(Calendar value) { setDisplayedCalendar(value); return (S)this; } 
	public ObjectProperty<Calendar> displayedCalendar() { return iDisplayedCalendarObjectProperty; }
	volatile private ObjectProperty<Calendar> iDisplayedCalendarObjectProperty = new SimpleObjectProperty<Calendar>();
	final static public String DISPLAYEDCALENDAR_PROPERTY_ID = "displayedCalendar";
	private Calendar derriveDisplayedCalendar(Calendar lDisplayedCalendar)
	{
		// done
		if (lDisplayedCalendar == null) return null;
		
		// always the 1st of the month, without time
		Calendar lCalendar = Calendar.getInstance(getSkinnable().getLocale());
		lCalendar.set(Calendar.DATE, 1);
		lCalendar.set(Calendar.MONTH, lDisplayedCalendar.get(Calendar.MONTH));
		lCalendar.set(Calendar.YEAR, lDisplayedCalendar.get(Calendar.YEAR));
		lCalendar.set(Calendar.HOUR_OF_DAY, 0);
		lCalendar.set(Calendar.MINUTE, 0);
		lCalendar.set(Calendar.SECOND, 0);
		lCalendar.set(Calendar.MILLISECOND, 0);
		
		// done
		return lCalendar;
	}

	private void refreshLocale()
	{
		// create the formatter to use
		iSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, getSkinnable().getLocale());
		
		// update the displayed calendar
		setDisplayedCalendar(getDisplayedCalendar());
	}

	//
	private SimpleDateFormat iSimpleDateFormat = null;
	
	// ==================================================================================================================
	// SUPPORT
	
	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed calendar
	 */
	protected List<String> getWeekdayLabels()
	{
		// result
		List<String> lWeekdayLabels = new ArrayList<String>();

		// setup the dayLabels
		// Calendar.SUNDAY = 1 and Calendar.SATURDAY = 7
		iSimpleDateFormat.applyPattern("E");
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 6, 5); // july 5th 2009 is a Sunday
		for (int i = 0; i < 7; i++)
		{
			// next
			lCalendar.set(java.util.Calendar.DATE, 4 + getDisplayedCalendar().getFirstDayOfWeek() + i);

			// assign day
			lWeekdayLabels.add( iSimpleDateFormat.format(lCalendar.getTime()));
		}
		
		// done
		return lWeekdayLabels;
	}
	
	/**
	 * Get a list with the weeklabels
	 */
	protected List<Integer> getWeeknumbers()
	{
		// result
		List<Integer> lWeekLabels = new ArrayList<Integer>();

		// setup the weekLabels
		Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
		for (int i = 0; i <= 5; i++)
		{
			// set label
			lWeekLabels.add( lCalendar.get(java.util.Calendar.WEEK_OF_YEAR) );

			// next week
			lCalendar.add(java.util.Calendar.DATE, 7);
		}

		// done
		return lWeekLabels;
	}

	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed calendar
	 */
	protected List<String> getMonthLabels()
	{
		// result
		List<String> lMonthLabels = new ArrayList<String>();

		// setup the month
		iSimpleDateFormat.applyPattern("MMMM");
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 0, 1); 
		for (int i = 0; i < 12; i++)
		{
			// next
			lCalendar.set(java.util.Calendar.MONTH, i);

			// assign day
			lMonthLabels.add( iSimpleDateFormat.format(lCalendar.getTime()));
		}
		
		// done
		return lMonthLabels;
	}
	
	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	protected boolean isWeekday(int idx, int weekdaynr)
	{
		// setup the dayLabels
		// Calendar.SUNDAY = 1 and Calendar.SATURDAY = 7
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 6, 4 + getDisplayedCalendar().getFirstDayOfWeek()); // july 5th 2009 is a Sunday
		lCalendar.add(java.util.Calendar.DATE, idx);
		int lDayOfWeek = lCalendar.get(java.util.Calendar.DAY_OF_WEEK);

		// check
		return (lDayOfWeek == weekdaynr);
	}

	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	protected boolean isWeekdayWeekend(int idx) 
	{
		return (isWeekday(idx, java.util.Calendar.SATURDAY) || isWeekday(idx, java.util.Calendar.SUNDAY));
	}
	
	/**
	 * determine on which day of week idx the first of the months is
	 */
	protected int determineFirstOfMonthDayOfWeek()
	{
		// determine with which button to start
		int lFirstDayOfWeek = getDisplayedCalendar().getFirstDayOfWeek();
		int lFirstOfMonthIdx = getDisplayedCalendar().get(java.util.Calendar.DAY_OF_WEEK) - lFirstDayOfWeek;
		if (lFirstOfMonthIdx < 0) lFirstOfMonthIdx += 7;
		return lFirstOfMonthIdx;
	}
	
	/**
	 * determine the number of days in the month
	 */
	protected int determineDaysInMonth()
	{
		// determine the number of days in the month
		Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
		lCalendar.add(java.util.Calendar.MONTH, 1);
		lCalendar.set(java.util.Calendar.DATE, 1);
		lCalendar.add(java.util.Calendar.DATE, -1);
		return lCalendar.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * determine if a date is today
	 */
	protected boolean isToday(Calendar calendar)
	{
		int lYear = calendar.get(java.util.Calendar.YEAR);
		int lMonth = calendar.get(java.util.Calendar.MONTH);
		int lDay = calendar.get(java.util.Calendar.DATE);
		return (lYear == iTodayYear && lMonth == iTodayMonth && lDay == iTodayDay);
	}
	private Calendar iToday = java.util.Calendar.getInstance();
	private int iTodayYear = iToday.get(java.util.Calendar.YEAR);
	private int iTodayMonth = iToday.get(java.util.Calendar.MONTH);
	private int iTodayDay = iToday.get(java.util.Calendar.DATE);
}
