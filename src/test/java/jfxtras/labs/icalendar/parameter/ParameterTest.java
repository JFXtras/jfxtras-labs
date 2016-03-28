package jfxtras.labs.icalendar.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.CalendarUser;
import jfxtras.labs.icalendar.parameters.CommonName;

public class ParameterTest
{
    @Test
    public void canParseCommonName()
    {
        CommonName parameter = new CommonName("David Bal");
        String expectedContent = ";CN=David Bal";
        assertEquals(expectedContent, parameter.toContentLine());
    }
    
    @Test
    public void canParseCalendarUser()
    {
        CalendarUser parameter = new CalendarUser("GROUP");
        String expectedContent = ";CUTYPE=GROUP";
        assertEquals(expectedContent, parameter.toContentLine());
    }
}
