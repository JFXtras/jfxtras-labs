package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;

public class DateTimeStartTest
{
    @Test
    public void canParseDateTimeStartSimple()
    {
        DateTimeStart dateTimeStart = new DateTimeStart("20160322T174422");
        String expectedContentLine = "DTSTART:20160322T174422";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStartDate()
    {
        DateTimeStart dateTimeStart = new DateTimeStart("DTSTART;VALUE=DATE:20160322");
        String expectedContentLine = "DTSTART;VALUE=DATE:20160322";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStartZoned()
    {
        DateTimeStart dateTimeStart = new DateTimeStart("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), dateTimeStart.getValue());
    }
    
    @Test
    public void canBuildDateTimeStartZoned()
    {
        DateTimeStart dateTimeStart = new DateTimeStart()
                .withValue(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
                .withValueType(ValueType.DATE_TIME);
        System.out.println(dateTimeStart);
    }
    
    @Test
    public void canBuildDateTimeStartZoned2()
    {
        DateTimeStart dateTimeStart = new DateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        System.out.println(dateTimeStart);
    }
}
