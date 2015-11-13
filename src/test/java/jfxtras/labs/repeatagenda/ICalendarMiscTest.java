package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertTrue;

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
}
