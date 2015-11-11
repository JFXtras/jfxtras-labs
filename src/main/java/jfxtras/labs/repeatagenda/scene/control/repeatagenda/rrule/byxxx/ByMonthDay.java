package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

/** BYMONTHDAY from RFC 5545, iCalendar */
public class ByMonthDay extends ByRuleAbstract
{
    private int[] daysOfMonth; // sorted array of days of month to apply the rule (i.e. 5, 10 = 5th and 10th days of the month, -3 = 3rd from last day of month)
    private int[] validDays; // array of valid days of month for current month
    
    /** Constructor with varargs to specify a group of daysOfMonth */
    public ByMonthDay(Frequency frequency, int... daysOfMonth) {
        super(frequency);
        this.daysOfMonth = daysOfMonth;
        int daysInMonth = getFrequency().getStartLocalDateTime().toLocalDate().lengthOfMonth();
        validDays = makeValidDays(daysInMonth, daysOfMonth);
    }

    /** Constructor that defaults to startLocalDateTime for dayOfMonth */
    public ByMonthDay(Frequency frequency) {
        super(frequency);
        daysOfMonth = new int[] { getFrequency().getStartLocalDateTime().toLocalDate().getDayOfMonth() }; // get day of month for startLocalDateTime
        validDays = daysOfMonth;
    }
    
//    /** Constructor that defaults to startLocalDateTime for dayOfMonth, no inStream.
//     * Only appropriate for the MONTHLY frequency */
//    public ByMonthDay(Frequency frequency) {
//        super(frequency);
//        daysOfMonth = new int[] { getFrequency().getStartLocalDateTime().toLocalDate().getDayOfMonth() }; // get day of month for startLocalDateTime
//        validDays = daysOfMonth;
//    }
    @Override
    public Stream<LocalDateTime> stream()
    {
        return stream(null);
    }
    
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream)
    { // infinite stream of valid dates made by rule
        switch (getFrequency().frequencyEnum())
        {
        case DAILY:
        {
//            int startMonth = getFrequency().getStartLocalDateTime().toLocalDate().getMonthValue();
            return inStream.filter(d ->
                    { // filter out all but qualifying days
                        int myDay = d.toLocalDate().getDayOfMonth();
                        int daysInMonth = d.toLocalDate().lengthOfMonth();
                        for (int day : daysOfMonth)
                        {
                            if (myDay == day) return true;
                            if ((day < 0) && (myDay == daysInMonth + day + 1)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
                        }
                        return false;
                    });
        }
        case MONTHLY:
        { // ignores inStream, can be null
//            Stream<LocalDateTime> s1 = 
            // Below stream works, but its not efficient.  Try to find a better approach.
//            return Stream.iterate(getFrequency().getStartLocalDateTime(), (d) -> { return d.plusDays(1); }) // stream of all days starting at startLocalDateTime
//                    .filter(d ->
//                    { // remove all but qualifying days
//                        int myMonth = d.toLocalDate().getMonthValue();
//                        if ((myMonth - startMonth) % getFrequency().getInterval() != 0) return false; // false if wrong month
//                        int myDay = d.toLocalDate().getDayOfMonth();
//                        int daysInMonth = d.toLocalDate().lengthOfMonth();
//                        for (int day : daysOfMonth)
//                        {
//                            if (myDay == day) return true;
//                            if ((day < 0) && (myDay == daysInMonth + day + 1)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
//                        }
//                        return false;
//                    });
            // Return a stream when each subsequent date is adjusted from the previous one
    
//            if (inStream == null)
//            {
                LocalDateTime start = getFrequency().getStartLocalDateTime();
                return Stream.iterate(start, (a) -> { return a.with(new NextAppointment()); });
//            }
            
//            return inStream.flatMap(d -> 
//                { // Add days of month not already present
//                    Set<LocalDateTime> dates = new TreeSet<LocalDateTime>();
//                    dates.add(d); // keep inStream date/time
//                    LocalDateTime firstDayOfMonth = d.with(TemporalAdjusters.firstDayOfMonth());
////                    int myDay = d.toLocalDate().getDayOfMonth();
////                    int daysInMonth = d.toLocalDate().lengthOfMonth();
//                    LocalDateTime newDay = firstDayOfMonth;
//                    for (int day : daysOfMonth)
//                    {
//                        if (day == firstDayOfMonth.getDayOfMonth()) dates.add(newDay);
//                        newDay = newDay.with(new NextAppointment());
//                        dates.add(newDay);
//                    }
//                    return dates.stream();
//                });
            
        }
        case WEEKLY:
            throw new InvalidParameterException("BYMONTHDAY is not available for WEEKLY frequency."); // Not available
        case YEARLY:
            return inStream.flatMap(d -> 
                    { // For each yearly date expand to be daysOfMonth days in that month
                        List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                        int myDay = d.toLocalDate().getDayOfMonth();
                        int daysInMonth = d.toLocalDate().lengthOfMonth();
                        for (int day : daysOfMonth)
                        {
                            if (myDay == day) dates.add(d);
                            if ((day < 0) && (myDay == daysInMonth + day + 1)) dates.add(d); // negative daysOfMonth (-3 = 3rd to last day of month)
                        }
                        return dates.stream();
                    });
        default:
            break;
        }  
        return null;
    }

    /**
     * Adjust date to become next date based on the Repeat rule.  Needs a input temporal on a valid date.
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            int dayShift = 0;
            int myDay = LocalDate.from(temporal).getDayOfMonth();
            if (myDay == validDays[validDays.length-1])
            { // if last day in validDays then skip months and make new array of validDays
                int interval = getFrequency().getInterval();
                temporal = temporal.plus(Period.ofMonths(interval)).with(TemporalAdjusters.firstDayOfMonth()); // adjust month
                int monthLength = LocalDate.from(temporal).lengthOfMonth();
                validDays = makeValidDays(monthLength, daysOfMonth);
                dayShift = validDays[0] - 1;
            } else
            { // get next day in validDays array
                for (int day : validDays)
                {
                    if (day > myDay)
                    {
                        dayShift = day - myDay;
                        break;
                    }
                }
            }
            return temporal.plus(Period.ofDays(dayShift)); // adjust day
        }
    }

    /** for current month, makes an array of valid days, works with both positive and negative daysOfMonth values */
    private int[] makeValidDays(int daysInMonth, int[] daysOfMonth)
    {
        int[] validDays = new int[daysOfMonth.length];
        int i=0;
        for (int day : daysOfMonth)
        {
            if (day == 0 || day < -31 || day > 31) throw new InvalidParameterException("Invalid BYMONTHDAY value (" + day + ").  Must be 1 to 31 or -31 to -1.");
            if (day > 0)
            {
                validDays[i] = day;
            } else
            {
                validDays[i] = (daysInMonth + day + 1); // negative daysOfMonth (-3 = 3rd to last day of month)                
            }
            i++;
        }
        return validDays;
    }


}
