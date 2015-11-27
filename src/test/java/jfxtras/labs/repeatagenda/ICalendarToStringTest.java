package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;

public class ICalendarToStringTest extends ICalendarTestAbstract
{
    /** Tests FREQ=YEARLY */
    @Test
    public void yearly1ToString()
    {
        VEvent e = getYearly1();
        String madeString = e.toString();
        System.out.println(madeString);
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
        assertEquals(expectedString, madeString);
    }

    /** Tests FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 */
    @Test
    public void monthly5ToString()
    {

    VEvent e = getMonthly5();
    String madeString = e.toString();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTEND:19970902T110000" + System.lineSeparator()
                          + "DTSTAMP:19970901T083000" + System.lineSeparator()
                          + "DTSTART:19970902T100000" + System.lineSeparator()
                          + "RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13" + System.lineSeparator()
                          + "UID:19970901T083000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
    
    /** Tests FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 */
    @Test
    public void monthly6ToString()
    {

    VEvent e = getMonthly6();
    String madeString = e.toString();
    String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTEND:20150110T113000" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000" + System.lineSeparator()
                          + "DTSTART:20150110T100000" + System.lineSeparator()
                          + "RRULE:FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    assertEquals(expectedString, madeString);
    }
}
