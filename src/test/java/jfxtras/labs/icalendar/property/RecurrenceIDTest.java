package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.Range.RangeType;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDate;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDZonedDateTime;

public class RecurrenceIDTest
{
    @Test
    public void canParseRecurrenceID1()
    {
        RecurrenceIDLocalDateTime property = new RecurrenceIDLocalDateTime("20160322T174422");
        String expectedContentLine = "RECURRENCE-ID:20160322T174422";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceID2()
    {
        RecurrenceIDLocalDate property = new RecurrenceIDLocalDate("20160322");
        String expectedContentLine = "RECURRENCE-ID;VALUE=DATE:20160322";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceID3()
    {
        RecurrenceIDZonedDateTime property = new RecurrenceIDZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        String expectedContentLine = "RECURRENCE-ID;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), property.getValue());
    }
    
    @Test
    public void canParseRecurrenceID4()
    {
        RecurrenceIDZonedDateTime property = new RecurrenceIDZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")))
                .withRange(RangeType.THIS_AND_FUTURE);
        String expectedContentLine = "RECURRENCE-ID;RANGE=THISANDFUTURE:20160306T043000Z";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z")), property.getValue());
    }
}
