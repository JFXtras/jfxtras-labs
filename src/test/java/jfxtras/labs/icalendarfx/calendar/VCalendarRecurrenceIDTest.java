package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;

public class VCalendarRecurrenceIDTest extends ICalendarTestAbstract2
{
    @Test
    public void canStreamVCalendar()
    {
        VEvent parent = getYearly1();
        VEvent child = getRecurrenceForYearly1();
//        VCalendar c = new VCalendar()
//                .withVEvents(parent, child);
        VCalendar c = new VCalendar();
        System.out.println("add parent");
        c.getVEvents().add(parent);
        System.out.println("add child");
        c.getVEvents().add(child);
        System.out.println("added child");
        assertEquals(2, c.getVEvents().size());
        System.out.println("found3:" + parent.childComponents().size());
        parent.childComponents().stream().forEach(cc -> System.out.println(cc.getSummary().toContent()));
        assertEquals(1, parent.childComponents().size());
        List<Temporal> expectedRecurrences = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2017, 11, 9, 10, 0),
                LocalDateTime.of(2018, 11, 9, 10, 0),
                LocalDateTime.of(2019, 11, 9, 10, 0),
                LocalDateTime.of(2020, 11, 9, 10, 0)
                );
        List<Temporal> madeRecurrences = parent.streamRecurrences().limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
