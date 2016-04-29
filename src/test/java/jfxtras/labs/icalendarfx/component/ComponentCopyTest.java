package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;

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
//    @Ignore // not implemented yet
    public void canCopyVEvent2()
    {
        VEventNew component1 = getWeekly3()
                .withExceptions(new Exceptions<>(
                LocalDateTime.of(2016, 2, 10, 12, 30)
              , LocalDateTime.of(2016, 2, 12, 12, 30)
              , LocalDateTime.of(2016, 2, 9, 12, 30) ));
        VEventNew component2 = new VEventNew(component1);
        System.out.println(component1.toContentLines());
        System.out.println(component2.toContentLines());
        assertEquals(component1, component2);
    }
}
