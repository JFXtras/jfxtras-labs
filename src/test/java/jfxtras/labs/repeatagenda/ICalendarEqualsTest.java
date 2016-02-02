package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertFalse;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule.ByRules;

public class ICalendarEqualsTest extends ICalendarTestAbstract
{
    @Test
    public void equalsTest1()
    {
        VEventImpl vevent = getMonthly5();
        VEventImpl vevent2 = getMonthly5();
        ByDay rule = (ByDay) vevent2.getRRule().getFrequency().getByRuleByType(ByRules.BYDAY);
        rule.addDayOfWeek(DayOfWeek.SATURDAY);
        assertFalse(vevent.equals(vevent2)); // check number of appointments
    }
    
    @Test
    public void equalsTest2()
    {
        VEventImpl vevent = getMonthly5();
        VEventImpl vevent2 = getMonthly5();
        ExDate exDate = new ExDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        vevent2.setExDate(exDate);
        assertFalse(vevent.equals(vevent2)); // check number of appointments
    }
}
