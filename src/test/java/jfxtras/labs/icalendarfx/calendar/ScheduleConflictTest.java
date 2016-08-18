package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public class ScheduleConflictTest extends ICalendarTestAbstract
{
    @Test
    public void canDetectScheduleConflict()
    {
        VCalendar c = new VCalendar()
                .withProductIdentifier(new ProductIdentifier())
                .withVersion(new Version())
                .withCalendarScale(new CalendarScale())
                .withVEvents(getYearly1());
        VEvent vEvent = getDaily1();
        boolean isConflict = DateTimeUtilities.checkScheduleConflict(vEvent, c.getVEvents());
        assertTrue(isConflict);
    }
    
    @Test
    public void canDetectScheduleConflict2()
    {
        VCalendar c = new VCalendar()
                .withProductIdentifier(new ProductIdentifier())
                .withVersion(new Version())
                .withCalendarScale(new CalendarScale())
                .withVEvents(getYearly1());
        VEvent vEvent = getDaily1();
        boolean isConflict = DateTimeUtilities.checkScheduleConflict(vEvent, c.getVEvents());
        assertTrue(isConflict);
    }
}
