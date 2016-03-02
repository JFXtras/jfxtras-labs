package jfxtras.labs.icalendar;

import static org.junit.Assert.assertFalse;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.rrule.byxxx.Rule.ByRules;

@Deprecated // what do these do?  seem useless.
public class ICalendarEqualsTest extends ICalendarTestAbstract
{
    @Test
    @Ignore
    public void equalsTest1()
    {
        VEventMock vevent = getMonthly5();
        VEventMock vevent2 = getMonthly5();
        ByDay rule = (ByDay) vevent2.getRRule().getFrequency().getByRuleByType(ByRules.BYDAY);
        rule.addDayOfWeek(DayOfWeek.SATURDAY);
        assertFalse(vevent.equals(vevent2)); // check number of appointments
    }
    
    @Test
    @Ignore
    public void equalsTest2()
    {
        VEventMock vevent = getMonthly5();
        VEventMock vevent2 = getMonthly5();
        ExDate exDate = new ExDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        vevent2.setExDate(exDate);
        assertFalse(vevent.equals(vevent2)); // check number of appointments
    }
}
