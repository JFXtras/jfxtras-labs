package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;

public class ICalendarToStringTest extends ICalendarTestAbstract
{
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearly1ToString()
    {
        VEvent e = getYearly1();
        String madeString = e.toString();
        System.out.println(madeString);
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000" + System.lineSeparator()
                              + "CREATED:20151109T082900" + System.lineSeparator()
                              + "END:VEVENT";
        assertEquals(expectedString, madeString);
    }
}
