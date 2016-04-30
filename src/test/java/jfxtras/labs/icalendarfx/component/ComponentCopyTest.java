package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    public void canCopyVEvent2()
    {
        VEventNew component1 = getWeekly3()
                .withExceptions(new Exceptions<>(
                LocalDateTime.of(2016, 2, 10, 12, 30)
              , LocalDateTime.of(2016, 2, 12, 12, 30)
              , LocalDateTime.of(2016, 2, 9, 12, 30) ));
        VEventNew component2 = new VEventNew(component1);
        assertEquals(component1, component2);
        assertFalse(component1 == component2);
    }
    
    @Test
    public void canCopyVEvent3()
    {
       String content = "BEGIN:VEVENT" + System.lineSeparator() +
       "SUMMARY:test2" + System.lineSeparator() +
       "DESCRIPTION:test notes" + System.lineSeparator() +
       "CLASS:PUBLIC" + System.lineSeparator() +
       "DTSTART;TZID=Etc/GMT:20160306T060000Z" + System.lineSeparator() +
       "DTEND;TZID=Etc/GMT:20160306T103000Z" + System.lineSeparator() +
       "LOCATION:there" + System.lineSeparator() +
       "PRIORITY:0" + System.lineSeparator() +
       "SEQUENCE:1" + System.lineSeparator() +
       "STATUS:CONFIRMED" + System.lineSeparator() +
       "UID:3a87e308-1b48-48c6-8f69-742e8645efa6" + System.lineSeparator() +
       "CATEGORIES:fun" + System.lineSeparator() +
       "DTSTAMP:20160312T024041Z" + System.lineSeparator() +
       "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
       " o.com" + System.lineSeparator() +
       "RRULE:FREQ=WEEKLY;INTERVAL=1;BYDAY=SU,TU,FR" + System.lineSeparator() +
       "X-YAHOO-YID:daviddbal" + System.lineSeparator() +
       "TRANSP:OPAQUE" + System.lineSeparator() +
       "STATUS:CONFIRMED" + System.lineSeparator() +
       "X-YAHOO-USER-STATUS:BUSY" + System.lineSeparator() +
       "X-YAHOO-EVENT-STATUS:BUSY" + System.lineSeparator() +
       "END:VEVENT";
        VEventNew component1 = VEventNew.parse(content);
        VEventNew component2 = new VEventNew(component1);
        System.out.println(component1.toContentLines());
        System.out.println(component2.toContentLines());
        assertEquals(component1, component2);
    }

}
