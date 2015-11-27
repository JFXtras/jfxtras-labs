package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;

public class ICalendarParseTest extends ICalendarTestAbstract
{
    /** Tests FREQ=YEARLY */
    @Test
    public void yearly1ToString()
    {
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CREATED:20151109T082900" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "X-APPOINTMENT-GROUP:group13" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(expectedString);
        vEvent.getAppointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        VEvent expectedVEvent = getYearly1();
        assertEquals(expectedVEvent, vEvent);
    }

    /** Tests FREQ=YEARLY */
    @Test
    public void daily3ToString()
    {
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CREATED:20151109T082900" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "X-APPOINTMENT-GROUP:group13" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(expectedString);
        vEvent.getAppointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        VEvent expectedVEvent = getDaily3();
        System.out.println(expectedVEvent.toString());
        assertEquals(expectedVEvent, vEvent);
    }
    
}
