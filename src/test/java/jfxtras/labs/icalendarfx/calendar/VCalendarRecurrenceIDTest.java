package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEventNew;

public class VCalendarRecurrenceIDTest extends ICalendarTestAbstract2
{
    @Test
    public void canStreamVCalendar()
    {
        VEventNew parent = getYearly1();
        VEventNew child = getRecurrenceForYearly1();
        VCalendar c = new VCalendar()
                .withVEvents(parent, child);
        assertEquals(2, c.getVEvents().size());
        assertEquals(1, parent.childComponentsWithRecurrenceIDs().size());
        parent.streamRecurrences().limit(5).forEach(System.out::println);
    }
}
