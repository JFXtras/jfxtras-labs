package jfxtras.labs.icalendarfx.calendar;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.VCalendar;

public class GeneralCalendarTest extends ICalendarTestAbstract2
{
    @Test
    public void canEscapeTest()
    {
        String contentLine = "DESCRIPTION:a dog\\nran\\, far\\;\\naway \\\\\\\\1";
        VCalendar c = new VCalendar()
                .withVEvents(getYearly1());
        c.toContentLines();
    }
}
