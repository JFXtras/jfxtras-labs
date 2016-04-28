package jfxtras.labs.icalendarfx;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEventNew;

@Deprecated
public class ICalendarCopyTest extends ICalendarTestAbstract2
{
    // TODO - NEED TO MOVE COPY TO PROPERTY ENUMS FIRST
    @Test
    @Ignore // not implemented yet
    public void canCopyVEvent1()
    {
        VEventNew vevent = getMonthly5();
        VEventNew veventCopy = new VEventNew(vevent);
        assertEquals(vevent, veventCopy);
    }
    
    @Test
    @Ignore // not implemented yet
    public void canCopyVEvent2()
    {
        VEventNew vevent = getWeekly3();
        VEventNew veventCopy = new VEventNew(vevent);
        assertEquals(vevent, veventCopy);
    }
}
