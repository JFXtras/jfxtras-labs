package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.byxxx.ByMonth;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.byxxx.ByWeekNo;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Weekly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Yearly;

public abstract class ICalendarRepeatTestAbstract
{

    /** FREQ=YEARLY; */
    protected static VEventImpl getYearly1()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        return vevent;
    }

    /** FREQ=YEARLY;BYDAY=SU; */
    protected static VEventImpl getYearly2()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(yearly, DayOfWeek.FRIDAY);
        yearly.addByRule(byRule);
        return vevent;
    }
    
    /**Every Thursday, but only during June, July, and August, forever:
     * DTSTART;TZID=America/New_York:19970605T090000
     * RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8
     * example in RFC 5545 iCalendar, page 129 */
    protected static VEventImpl getYearly3()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(yearly, DayOfWeek.THURSDAY);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonth(yearly, Month.JUNE, Month.JULY, Month.AUGUST);
        yearly.addByRule(byRule2);
        return vevent;
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    protected static VEventImpl getYearly4()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(yearly, Month.JANUARY, Month.FEBRUARY);
        yearly.addByRule(byRule);
        return vevent;
    }

    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    protected static VEventImpl getYearly5()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(yearly, Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(yearly); // use default repeat date from startLocalDateTime (10th of month)
        yearly.addByRule(byRule2);
        return vevent;
    }

    /** RFC 5545 iCalendar, page 130 
     * Every 4 years, the first Tuesday after a Monday in November,
      forever (U.S. Presidential Election day):

       DTSTART;TZID=America/New_York:19961105T090000
       RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;
        BYMONTHDAY=2,3,4,5,6,7,8 */
    protected static VEventImpl getYearly6()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly()
                .withInterval(4);
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(yearly, Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByDay(yearly, DayOfWeek.TUESDAY);
        yearly.addByRule(byRule2);
        Rule byRule3 = new ByMonthDay(yearly, 2,3,4,5,6,7,8);
        yearly.addByRule(byRule3);
        return vevent;
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    protected static VEventImpl getYearly7()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(yearly, new ByDayPair(DayOfWeek.MONDAY, 20));
        yearly.addByRule(byRule);
        return vevent;
    }
    
    /** FREQ=YEARLY;WKST=MO;BYWEEKNO=20;BYDAY=MO */
    protected static VEventImpl getYearly8()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        ByWeekNo byRule = new ByWeekNo(yearly, 20);
        byRule.setWeekStart(DayOfWeek.MONDAY); // not needed, is default.
        yearly.addByRule(byRule);
        Rule byRule2 = new ByDay(yearly, DayOfWeek.MONDAY);
        yearly.addByRule(byRule2);

        return vevent;
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 - start before first valid date */
    protected static VEventImpl getYearly9()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 1, 1, 0, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(yearly, Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(yearly, 10); // use default repeat date from startLocalDateTime (10th of month)
        yearly.addByRule(byRule2);
        return vevent;
    }
    
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected static VEventImpl getMonthly1()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Monthly monthly = new Monthly();
        rule.setFrequency(monthly);
        return vevent;
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected static VEventImpl getMonthly2()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule by = new ByMonthDay(monthly)
                .withDaysOfMonth(-2);// repeats 2nd to last day
        monthly.addByRule(by);
        return vevent;
    }

    /** FREQ=MONTHLY;BYDAY=TU,WE,FR */
    protected static VEventImpl getMonthly3()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(monthly, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule);
        return vevent;
    }

    /** FREQ=MONTHLY;BYDAY=-1SA */
    protected static VEventImpl getMonthly4()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(monthly, new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1));
        monthly.addByRule(byRule);
        return vevent;
    }

    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    protected static VEventImpl getMonthly5()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(1997, 9, 2, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(monthly, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(monthly, 13);
        monthly.addByRule(byRule2);
        return vevent;
    }

    /** FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR - start before first valid date */
    protected static VEventImpl getMonthly6()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 1, 10, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule1 = new ByMonth(monthly, Month.NOVEMBER, Month.DECEMBER);
        monthly.addByRule(byRule1);
        Rule byRule2 = new ByDay(monthly, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule2);
        return vevent;
    }
    
    /** FREQ=WEEKLY, Basic weekly stream */
    protected static VEventImpl getWeekly1()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency weekly = new Weekly();
        rule.setFrequency(weekly);
        return vevent;
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    protected static VEventImpl getWeekly2()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency weekly = new Weekly()
                .withInterval(2);
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(weekly, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);
        return vevent;
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  - start before first valid date */
    protected static VEventImpl getWeekly3()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency weekly = new Weekly();
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(weekly, DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);
        return vevent;
    }
    
    /** FREQ=DAILY, Basic daily stream */
    protected static VEventImpl getDaily1()
    {
        VEventImpl vevent = new VEventImpl()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDurationInSeconds(3600)
                .withDescription("Daily1 Description");
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency daily = new Daily();
        rule.setFrequency(daily);
        return vevent;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static VEventImpl getDaily2()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule()
                .withCount(6);
        vevent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        return vevent;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 */
    protected static VEventImpl getDaily3()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule()
                .withCount(10);
        vevent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        Rule byRule = new ByMonthDay(daily)
                .withDaysOfMonth(9,10,11,12,13,14);
        daily.addByRule(byRule);
        return vevent;
    }

    /** FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    protected static VEventImpl getDaily4()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        Rule byRule = new ByMonthDay(daily); // use default repeat date from startLocalDateTime (9th of month)
        daily.addByRule(byRule);
        return vevent;
    }
    
    /** FREQ=DAILY;INVERVAL=2;BYDAY=FR */
    protected static VEventImpl getDaily5()
    {
        VEventImpl vevent = new VEventImpl();
        vevent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        RRule rule = new RRule();
        vevent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        Rule byRule = new ByDay(daily, DayOfWeek.FRIDAY);
        daily.addByRule(byRule);
        return vevent;
    }
}
