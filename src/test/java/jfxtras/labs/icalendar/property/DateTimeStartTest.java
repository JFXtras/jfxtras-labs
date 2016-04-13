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
    public void canParseDateTimeStart1()
    {
        DateTimeStart<LocalDateTime> dateTimeStart = new DateTimeStart<>(LocalDateTime.class, "20160322T174422");
        String expectedContentLine = "DTSTART:20160322T174422";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStart2()
    {
        DateTimeStart<LocalDate> dateTimeStart = new DateTimeStart<>(LocalDate.class, "DTSTART;VALUE=DATE:20160322");
        String expectedContentLine = "DTSTART;VALUE=DATE:20160322";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), dateTimeStart.getValue());
        DateTimeStart<LocalDate> dateTimeStart2 = new DateTimeStart<>(LocalDate.of(2016, 3, 22));
        assertEquals(dateTimeStart, dateTimeStart2);
        assertEquals(dateTimeStart.toContentLine(), dateTimeStart2.toContentLine());
    }
    
    @Test
    public void canParseDateTimeStart3()
    {
        DateTimeStart<ZonedDateTime> dateTimeStart = new DateTimeStart<>(ZonedDateTime.class, "DTSTART;TZID=America/Los_Angeles:20160306T043000");
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), dateTimeStart.getValue());
    }
    
    @Test
    public void canParseDateTimeStart4()
    {
        DateTimeStart<ZonedDateTime> dateTimeStart = new DateTimeStart<>(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
                .withTimeZoneIdentifier("America/Los_Angeles")
                .withValueParameter(ValueType.DATE_TIME);;
        String expectedContentLine = "DTSTART;TZID=America/Los_Angeles;VALUE=DATE-TIME:20160306T043000";
        DateTimeStart<ZonedDateTime> expectedDateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.class, expectedContentLine);
        assertEquals(expectedDateTimeStart, dateTimeStart);
        String madeContentLine = dateTimeStart.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }

    @Test
    public void canBuildDateTimeStartZoned()
    {
        DateTimeStart<ZonedDateTime> dateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
//                .withValue(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")))
                .withTimeZoneIdentifier("America/Los_Angeles")
                .withValueParameter(ValueType.DATE_TIME);
        DateTimeStart<ZonedDateTime> expectedDateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.class, "DTSTART;TZID=America/Los_Angeles;VALUE=DATE-TIME:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }
    
    @Test
    public void canBuildDateTimeStartZoned2()
    {
        DateTimeStart<ZonedDateTime> dateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        DateTimeStart<ZonedDateTime> expectedDateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.class, "DTSTART;TZID=America/Los_Angeles:20160306T043000");
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }

    @Test
    public void canBuildDateTimeStartZonedUTC()
    {
        DateTimeStart<ZonedDateTime> dateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")));
        String expectedContentLine = "DTSTART:20160306T043000Z";
        DateTimeStart<ZonedDateTime> expectedDateTimeStart = new DateTimeStart<ZonedDateTime>(ZonedDateTime.class, expectedContentLine);
        assertEquals(expectedContentLine, dateTimeStart.toContentLine());
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }

    @Test
    public void canBuildDateTimeStartLocal()
    {
        DateTimeStart<LocalDateTime> dateTimeStart = new DateTimeStart<LocalDateTime> (LocalDateTime.of(2016, 3, 6, 4, 30));
        String expectedContentLine = "DTSTART:20160306T043000";
        DateTimeStart<LocalDateTime>  expectedDateTimeStart = new DateTimeStart<LocalDateTime> (LocalDateTime.class, expectedContentLine);
        assertEquals(expectedContentLine, dateTimeStart.toContentLine());
        assertEquals(expectedDateTimeStart, dateTimeStart);
    }
    
    @Test (expected=ClassCastException.class)
    public void canCatchWrongTemporalType()
    {
        new DateTimeStart<LocalDate>(LocalDate.class, "DTSTART;TZID=America/Los_Angeles:20160306T043000");
    }
}
