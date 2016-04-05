package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;

public class DateTimeCompletedTest
{
    @Test
    public void canParseDateTimeEndSimple()
    {
        DateTimeCompleted property = new DateTimeCompleted("COMPLETED:20160322T174422Z");
        String expectedContentLine = "COMPLETED:20160322T174422Z";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2016, 3, 22, 17, 44, 22), ZoneId.of("Z")), property.getValue());
    }
}
