package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEvent;

public class ICalendarMiscTest {

    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canConvertDurationString()
    {
        VEvent v = new VEvent();
        String duration = "P15DT5H0M20S";
        v.setDurationInSeconds(duration);
        assertTrue(v.getDurationInSeconds() == 1314020);
    }
    
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canConvertDurationString2()
    {
        VEvent v = new VEvent();
        String duration = "PT1H30M";
        v.setDurationInSeconds(duration);
        assertTrue(v.getDurationInSeconds() == 5400);
    }

    /** tests converting ISO.8601.2004 date time string to LocalDateTime */
    @Test
    public void canConvertDateTimeString1()
    {
        VEvent v = new VEvent();
        String duration = "TZID=America/New_York:19980119T020000";
        LocalDateTime l = v.iCalendarDateTimeToLocalDateTime(duration);
        System.out.println(l);
        assertEquals(l, LocalDateTime.of(1998, 1, 19, 2, 0));
    }

    /** tests converting ISO.8601.2004 date string to LocalDateTime */
    @Test
    public void canConvertDateTimeString2()
    {
        VEvent v = new VEvent();
        String duration = "VALUE=DATE:19980704";
        LocalDateTime l = v.iCalendarDateTimeToLocalDateTime(duration);
        System.out.println(l);
        assertEquals(l, LocalDateTime.of(1998, 7, 4, 0, 0));
    }

}
