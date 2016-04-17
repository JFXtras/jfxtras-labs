package jfxtras.labs.icalendarfx;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jfxtras.labs.icalendarfx.mocks.VEventMock;

public class ICalendarCopyTest extends ICalendarTestAbstract
{
    // TODO - NEED TO MOVE COPY TO PROPERTY ENUMS FIRST
    @Test
    public void canCopyVEvent1()
    {
        VEventMock vevent = getMonthly5();
        VEventMock veventCopy = new VEventMock(vevent);
        assertTrue(VEventMock.isEqualTo(vevent, veventCopy));
        assertTrue(vevent != veventCopy); // ensure not same reference
    }
    
    @Test
    public void canCopyVEvent2()
    {
        VEventMock vevent = getWeekly3();
        VEventMock veventCopy = new VEventMock(vevent);
        assertTrue(VEventMock.isEqualTo(vevent, veventCopy));
        assertTrue(vevent != veventCopy); // ensure not same reference
    }
}
