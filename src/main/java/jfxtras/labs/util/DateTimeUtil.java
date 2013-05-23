package jfxtras.labs.util;

import java.util.Calendar;
import java.util.Locale;

import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.LocalTime;

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
	 * @param localDate
	 * @return
	 */
	public static Calendar createCalendarFromLocalDateTime(LocalDateTime localDateTime, Locale locale)
	{
		if (localDateTime == null) return null;
		Calendar lCalendar = Calendar.getInstance(locale);
		lCalendar.set(Calendar.YEAR, localDateTime.getYear());
		lCalendar.set(Calendar.MONTH, localDateTime.getMonthOfYear().getValue() - 1);
		lCalendar.set(Calendar.DATE, localDateTime.getDayOfMonth());
		lCalendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHourOfDay());
		lCalendar.set(Calendar.MINUTE, localDateTime.getMinuteOfHour());
		lCalendar.set(Calendar.SECOND, localDateTime.getSecondOfMinute());
		lCalendar.set(Calendar.MILLISECOND, localDateTime.getNanoOfSecond() / 1000000);
		return lCalendar;
	}
	
	/**
	 * 
	 * @param localDate
	 * @return
	 */
	public static Calendar createCalendarFromLocalTime(LocalTime localTime)
	{
		if (localTime == null) return null;
		Calendar lCalendar = Calendar.getInstance();
		lCalendar.set(Calendar.HOUR_OF_DAY, localTime.getHourOfDay());
		lCalendar.set(Calendar.MINUTE, localTime.getMinuteOfHour());
		lCalendar.set(Calendar.SECOND, localTime.getSecondOfMinute());
		lCalendar.set(Calendar.MILLISECOND, localTime.getNanoOfSecond() / 1000000);
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
