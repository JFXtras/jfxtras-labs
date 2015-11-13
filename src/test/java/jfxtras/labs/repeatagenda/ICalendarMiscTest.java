package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.RRule;

public class ICalendarMiscTest {

    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canConvertDurationString()
    {
        RRule r = new RRule(null);
        String duration = "P15DT5H0M20S";
        r.setDurationInSeconds(duration);
        assertTrue(r.getDurationInSeconds() == 1314020);
    }
    
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canConvertDurationString2()
    {
        RRule r = new RRule(null);
        String duration = "PT1H30M";
        r.setDurationInSeconds(duration);
        assertTrue(r.getDurationInSeconds() == 5400);
    }
}
