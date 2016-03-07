package jfxtras.labs.icalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.rrule.byxxx.Rule.ByRuleType;

public class ICalendarEqualsTest extends ICalendarTestAbstract
{
    @Test
    public void equalsTest1()
    {
        VEventMock vevent = getMonthly5();
        VEventMock vevent2 = getMonthly5();
        assertTrue(VEventMock.isEqualTo(vevent, vevent2));
        ByDay rule = (ByDay) vevent2.getRRule().getFrequency().getByRuleByType(ByRuleType.BYDAY);
        rule.addDayOfWeek(DayOfWeek.SATURDAY);
        assertFalse(VEventMock.isEqualTo(vevent, vevent2, false));
    }
    
    @Test
    public void equalsTest2()
    {
        VEventMock vevent = getMonthly5();
        VEventMock vevent2 = getMonthly5();
        assertTrue(VEventMock.isEqualTo(vevent, vevent2));
        ExDate exDate = new ExDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        vevent2.setExDate(exDate);
        assertFalse(VEventMock.isEqualTo(vevent, vevent2, false));
    }
    
    @Test
    public void equalsTestWithUntil()
    {
        VEventMock vevent = getMonthly5();
        VEventMock vevent2 = getMonthly5();
        vevent2.getRRule().setUntil(ZonedDateTime.of(LocalDateTime.of(1998, 3, 13, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        assertFalse(VEventMock.isEqualTo(vevent, vevent2, false));
    }
}
