package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;

public class ICalendarCopyTest extends ICalendarTestAbstract
{

    @Test
    public void canCopyVEvent1()
    {
        VEventImpl vevent = getMonthly5();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        VEventImpl veventCopy = new VEventImpl(vevent);
        assertEquals(vevent, veventCopy); // check number of appointments
        assertTrue(vevent != veventCopy); // insure not same reference
    }
    
    @Test
    public void canCopyVEvent2()
    {
        VEventImpl vevent = getWeekly3();
        VEventImpl veventCopy = new VEventImpl(vevent);
        assertEquals(vevent, veventCopy); // check number of appointments
        assertTrue(vevent != veventCopy); // insure not same reference
    }
}
