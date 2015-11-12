package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Monthly;

public abstract class ICalendarRepeatTestAbstract
{
    
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected static RRule getMonthlyStream()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Monthly monthly = new Monthly();
        rule.setFrequency(monthly);
        return rule;
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected static RRule getMonthlyStream2()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 29, 10, 0));
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        ByRule by = new ByMonthDay(monthly)
                .withDaysOfMonth(-2);// repeats 2nd to last day
        monthly.addByRule(by);
        return rule;
    }

    /** FREQ=MONTHLY;BYDAY=TU,WE,FR */
    protected static RRule getMonthlyStream3()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        ByRule byRule = new ByDay(monthly, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule);
        return rule;
    }

    /** FREQ=MONTHLY;BYDAY=-1SA */
    protected static RRule getMonthlyStream4()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        ByRule byRule = new ByDay(monthly, new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1));
        monthly.addByRule(byRule);
        return rule;
    }
    
    /** FREQ=DAILY, Basic daily stream */
    protected static RRule getDailyStream()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Frequency daily = new Daily();
        rule.setFrequency( daily);
        return rule;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static RRule getDailyStream2()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withCount(6);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        return rule;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 */
    protected static RRule getDailyStream3()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withCount(10);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        ByRule byRule = new ByMonthDay(daily)
                .withDaysOfMonth(9,10,11,12,13,14);
        daily.addByRule(byRule);
        return rule;
    }

    /** FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    protected static RRule getDailyStream4()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        ByRule byRule = new ByMonthDay(daily); // use default repeat date from startLocalDateTime (9th of month)
        daily.addByRule(byRule);
        return rule;
    }
    
    /** FREQ=DAILY;INVERVAL=2;BYDAY=FR */
    protected static RRule getDailyStream5()
    {
        RRule rule = new RRule()
                .withStartLocalDate(LocalDateTime.of(2015, 11, 9, 10, 0));
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        ByRule byRule = new ByDay(daily, DayOfWeek.FRIDAY);
        daily.addByRule(byRule);
        return rule;
    }

}
