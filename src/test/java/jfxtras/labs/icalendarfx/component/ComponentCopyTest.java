package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.components.VEventNew;

public class ComponentCopyTest extends ICalendarTestAbstract2
{
    // TODO - NEED TO MOVE COPY TO PROPERTY ENUMS FIRST
    @Test
    public void canCopyVEvent1()
    {
        VEventNew vevent = new VEventNew()
                .withDateTimeStart("20150831")
                .withComments("comment1", "comment2");
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
