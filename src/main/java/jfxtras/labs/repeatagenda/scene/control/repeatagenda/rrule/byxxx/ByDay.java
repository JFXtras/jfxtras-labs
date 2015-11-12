package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

/** BYDAY from RFC 5545, iCalendar 3.3.10, page 40 */
public class ByDay extends ByRuleAbstract
{
    /** sorted array of days of month
     * (i.e. 5, 10 = 5th and 10th days of the month, -3 = 3rd from last day of month)
     * Uses a varargs parameter to allow any number of days
     */
    public ByDayPair[] getByDayPair() { return byDayPairs; }
    private ByDayPair[] byDayPairs;
    private void setByDayPair(ByDayPair... byDayPairs) { this.byDayPairs = byDayPairs; }
//    public ByRule withByDayPair(ByDayPair... byDayPairs) { setByDayPair(byDayPairs); return this; }
    
    public ByDay(Frequency frequency, ByDayPair... byDayPairs)
    {
        super(frequency);
        setByDayPair(byDayPairs);
    }

    /** Constructor that uses DayOfWeek values without a preceding integer.  All days of the 
     * provided types are included within the specified frequency */
    public ByDay(Frequency frequency, DayOfWeek... daysOfWeek)
    {
        super(frequency);
        byDayPairs = new ByDayPair[daysOfWeek.length];
        int i=0;
        for (DayOfWeek d : daysOfWeek)
        {
            byDayPairs[i++] = new ByDayPair(d, 0);
        }
    }

    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        switch (getFrequency().frequencyEnum())
        {
        case DAILY:
            return inStream.filter(date ->
            { // filter out all but qualifying days
                DayOfWeek myDayOfWeek = date.toLocalDate().getDayOfWeek();
                for (ByDayPair byDayPair : getByDayPair())
                {
                    if (byDayPair.day == myDayOfWeek) return true;
                }
                return false;
            });
        case WEEKLY:
            return inStream.flatMap(date -> 
            { // Expand to be byDayPairs days in current week
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                int dayOfWeekValue = date.toLocalDate().getDayOfWeek().getValue();
                for (ByDayPair byDayPair : getByDayPair())
                {
                    int dayShift = byDayPair.day.getValue() - dayOfWeekValue;
                    dates.add(date.plusDays(dayShift));
                }
                return dates.stream();
            });
        case MONTHLY:
            return inStream.flatMap(date -> 
            { // Expand to be byDayPairs days in current week
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                for (ByDayPair byDayPair : getByDayPair())
                {
                    if (byDayPair.ordinal == 0)
                        // TODO - CONSIDER ADDING ALL DAYS IN MONTH AND REMOVING THE ONES THAT DON'T MATCH
                    { // add every matching day of week in month
                        Month myMonth = date.getMonth();
                        for (int i=1; i<5; i++)
                        {
                            LocalDateTime newDate = date.with(TemporalAdjusters.dayOfWeekInMonth(i, byDayPair.day));
                            if (newDate.getMonth() == myMonth && ! newDate.isBefore(startDateTime)) dates.add(newDate);
                        }
                    } else
                    {
                        Month myMonth = date.getMonth();
                        LocalDateTime newDate = date.with(TemporalAdjusters.dayOfWeekInMonth(byDayPair.ordinal, byDayPair.day));
                        if (newDate.getMonth() == myMonth) dates.add(newDate);
                    }
                }
                return dates.stream();
            });          
        case YEARLY:
        {
        }
        case HOURLY:
        case MINUTELY:
        case SECONDLY:
            throw new RuntimeException("Not implemented"); // probably same as DAILY
        default:
            break;
        }
        return null;
    }

    /**
     * Contains both the day of the week and an optional positive or negative integer (ordinal).
     * If the integer is present it represents the nth occurrence of a specific day within the 
     * MONTHLY or YEARLY frequency rules.  For example, with a MONTHLY rule 1MO indicates the
     * first Monday of the month.
     * If ordinal is 0 then all the matching days are included within the specified frequency rule.
     */
    public static class ByDayPair
    {
        DayOfWeek day;
        int ordinal = 0;
        public ByDayPair(DayOfWeek d, int i)
        {
            day = d;
            ordinal = i;
        }
    }
}
