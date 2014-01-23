package jfxtras.labs.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TestUtil {

	/**
	 * 
	 */
	static public String quickFormatCalendar(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendar(List<Calendar> value) {
		if (value == null) return "null";
		String s = "[";
		for (Calendar lCalendar : value)
		{
			if (s.length() > 1) s += ",";
			s += quickFormatCalendar(lCalendar);
		}
		s += "]";
		return s;
	}
}
