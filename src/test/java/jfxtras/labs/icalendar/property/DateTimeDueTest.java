package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.time.due.DueLocalDate;
import jfxtras.labs.icalendar.properties.component.time.due.DueLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.due.DueZonedDateTime;

public class DateTimeDueTest
{
    @Test
    public void canParseDateTimeDue1()
    {
        DueLocalDateTime property = new DueLocalDateTime("20160322T174422");
        String expectedContentLine = "DUE:20160322T174422";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDateTime.of(2016, 3, 22, 17, 44, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeDue2()
    {
        DueLocalDate property = new DueLocalDate("20160322");
        String expectedContentLine = "DUE;VALUE=DATE:20160322";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(LocalDate.of(2016, 3, 22), property.getValue());
    }
    
    @Test
    public void canParseDateTimeDue3()
    {
        DueZonedDateTime property = new DueZonedDateTime(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")));
        String expectedContentLine = "DUE;TZID=America/Los_Angeles:20160306T043000";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles")), property.getValue());
    }
}
