package jfxtras.labs.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

public class RequestTest
{
    @Test
    public void canShiftWeeklyAll() // shift day of weekly
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentOriginal = ICalendarStaticComponents.getWeekly3();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRule2()
                                .withFrequency(FrequencyType.WEEKLY)
                                .withByRules(new ByDay(DayOfWeek.TUESDAY)))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
    
    @Test
    public void canShiftMonthlyAll() // shift day of week with monthly ordinal
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        VEvent vComponentOriginal = ICalendarStaticComponents.getMonthly7();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20151110T090000" + System.lineSeparator() + // one day later
                "DTEND:20151110T103000" + System.lineSeparator() + // one day later, half hour longer
                "UID:20150110T080000-002@jfxtras.org" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "RRULE:FREQ=MONTHLY;BYDAY=3TU" + System.lineSeparator() + // Changed Monday to Tuesday
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        VEvent expectedVComponent = ICalendarStaticComponents.getWeekly3()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 10, 30))
                .withRecurrenceRule(new RecurrenceRule2()
                                .withFrequency(FrequencyType.MONTHLY)
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.TUESDAY, 3))))
                .withSummary("Edited summary")
                .withSequence(1);
        assertEquals(expectedVComponent, myComponent);
    }
}
