/**
 * DateTimeUtil.java
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

package jfxtras.labs.util;

import java.util.Calendar;
import java.util.Locale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil
{
	/**
	 * 
	 * @param localDate
	 * @return
	 */
	public static Calendar createCalendarFromLocalDate(LocalDate localDate, Locale locale)
	{
		if (localDate == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.YEAR, localDate.getYear());
		lCalendar.set(Calendar.MONTH, localDate.getMonth().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDate.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, 0);
		lCalendar.set(Calendar.MINUTE, 0);
		lCalendar.set(Calendar.SECOND, 0);
		lCalendar.set(Calendar.MILLISECOND, 0);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param localDateTime
	 * @return
	 */
	public static Calendar createCalendarFromLocalDateTime(LocalDateTime localDateTime, Locale locale)
	{
		if (localDateTime == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.YEAR, localDateTime.getYear());
		lCalendar.set(Calendar.MONTH, localDateTime.getMonth().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDateTime.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
		lCalendar.set(Calendar.MINUTE, localDateTime.getMinute());
		lCalendar.set(Calendar.SECOND, localDateTime.getSecond());
		lCalendar.set(Calendar.MILLISECOND, localDateTime.getNano() / 1000000);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param localTime
	 * @return
	 */
	public static Calendar createCalendarFromLocalTime(LocalTime localTime)
	{
		if (localTime == null) return null;
		Calendar lCalendar = Calendar.getInstance();
		lCalendar.set(Calendar.HOUR_OF_DAY, localTime.getHour());
		lCalendar.set(Calendar.MINUTE, localTime.getMinute());
		lCalendar.set(Calendar.SECOND, localTime.getSecond());
		lCalendar.set(Calendar.MILLISECOND, localTime.getNano() / 1000000);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalDate createLocalDateFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalDate lLocalDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
		return lLocalDate;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalDateTime createLocalDateTimeFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalDateTime lLocalDateTime = LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND) * 1000000);
		return lLocalDateTime;
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	public static LocalTime createLocalTimeFromCalendar(Calendar calendar)
	{
		if (calendar == null) return null;
		LocalTime lLocalTime = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND) * 1000000);
		return lLocalTime;
	}
}
