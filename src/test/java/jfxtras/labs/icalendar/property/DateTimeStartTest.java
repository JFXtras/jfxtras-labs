package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DateTimeStart;

public class DateTimeStartTest
{
    @Test
    public void canParseDateTimeStart1()
    {
        DateTimeStart<LocalDateTime> dateTimeStart = new DateTimeStart<>("20160322T174422");
        String expectedContentLine = "DTSTART:20160322T174422";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStart2()
    {
        DateTimeStart<LocalDate> dateTimeStart = new DateTimeStart<>("DTSTART;VALUE=DATE:20160322");
        dateTimeStart.setTimeZoneIdentifier(ZoneId.of("Z"));
        String expectedContentLine = "DTSTART;VALUE=DATE:20160322";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStart3()
    {
        DateTimeStart<LocalDate> dateTimeStart = new DateTimeStart<>("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), dateTimeStart.getValue());
    }
    
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
    
    @Test // uses parse-content string constructor
    public void canParseDateTimeStartZoned()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), dateTimeStart.getValue());
    }
    
    @Test // uses no-arg constructor and chaining
    public void canBuildDateTimeStartZoned()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
//                .withValue(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
                .withTimeZoneIdentifier("America/Los_Angeles")
                .withValueParameter(ValueType.DATE_TIME);
        DTStartZonedDateTime expectedDateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles;VALUE=DATE-TIME:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }
    
    @Test // uses Temporal as parameter in constructor
    public void canBuildDateTimeStartZoned2()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        DTStartZonedDateTime expectedDateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }

    @Test
    public void canBuildDateTimeStartZonedUTC()
    {
        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")));
        String expectedContentLine = "DTSTART:20160306T043000Z";
        DTStartZonedDateTime expectedDateTimeStart = new DTStartZonedDateTime(expectedContentLine);
        assertEquals(expectedContentLine, dateTimeStart.toContentLine());
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }

    @Test
    public void canBuildDateTimeStartLocal()
    {
        DTStartLocalDateTime dateTimeStart = new DTStartLocalDateTime(LocalDateTime.of(2016, 3, 6, 4, 30));
        String expectedContentLine = "DTSTART:20160306T043000";
        DTStartLocalDateTime expectedDateTimeStart = new DTStartLocalDateTime(expectedContentLine);
        assertEquals(expectedContentLine, dateTimeStart.toContentLine());
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }
}
