package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;

public class DateTimeStartTest
{
    @Test
    public void canParseDateTimeStartSimple()
    {
        DTStartLocalDateTime dateTimeStart = new DTStartLocalDateTime("20160322T174422");
        String expectedContentLine = "DTSTART:20160322T174422";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStartDate()
    {
        DTStartLocalDate dateTimeStart = new DTStartLocalDate("DTSTART;VALUE=DATE:20160322");
        String expectedContentLine = "DTSTART;VALUE=DATE:20160322";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStartZoned()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), dateTimeStart.getValue());
    }
    
    @Test
    public void canBuildDateTimeStartZoned()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime()
                .withValue(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
                .withTimeZoneIdentifier("America/Los_Angeles")
                .withValueParameter(ValueType.DATE_ZONED_DATE_TIME);
        DTStartZonedDateTime expectedDateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles;VALUE=DATE-TIME:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }
    
    @Test
    public void canBuildDateTimeStartZoned2()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        DTStartZonedDateTime expectedDateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
//        System.out.println(dateTimeStart.getValue() + " " + dateTimeStart.getTimeZoneIdentifier());
    }
}
