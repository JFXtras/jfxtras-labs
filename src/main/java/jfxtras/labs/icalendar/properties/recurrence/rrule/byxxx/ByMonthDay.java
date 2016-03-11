package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import static java.time.temporal.ChronoUnit.DAYS;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

/** BYMONTHDAY from RFC 5545, iCalendar */
public class ByMonthDay extends ByRuleAbstract
{
    private final static ByRuleType MY_RULE = ByRuleType.BYMONTHDAY;

    /** sorted array of days of month
     * (i.e. 5, 10 = 5th and 10th days of the month, -3 = 3rd from last day of month)
     * Uses a varargs parameter to allow any number of days
     */
    public int[] getDaysOfMonth() { return daysOfMonth; }
    private int[] daysOfMonth;
    public void setDaysOfMonth(int... daysOfMonth) { this.daysOfMonth = daysOfMonth; }
    public Rule withDaysOfMonth(int... daysOfMonth) { setDaysOfMonth(daysOfMonth); return this; }
    
//    private int[] validDays; // array of valid days of month for current month

    /** Constructor 
     * takes String of comma-delimited integers, parses it to array of ints
     * This constructor is REQUIRED by 
     * {@link jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.Rule.ByRuleType#newInstance(String)}
     */
    public ByMonthDay(String daysOfMonthString)
    {
        this();
        int[] days = Arrays
                .stream(daysOfMonthString.split(","))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray();
        setDaysOfMonth(days);
    }
    
    /**
     * This constructor is required by {@link jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency#copy}
     */
    public ByMonthDay()
    {
        super(MY_RULE);
    }

    /** Constructor 
     * Contains varargs of daysOfMonth */
    public ByMonthDay(int... daysOfMonth)
    {
        this();
        setDaysOfMonth(daysOfMonth);
    }

    @Override
    public void copyTo(Rule destination)
    {
        ByMonthDay destination2 = (ByMonthDay) destination;
        destination2.daysOfMonth = new int[daysOfMonth.length];
        for (int i=0; i<daysOfMonth.length; i++)
        {
            destination2.daysOfMonth[i] = daysOfMonth[i];
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByMonthDay testObj = (ByMonthDay) obj;
        boolean daysOfMonthEquals = Arrays.equals(getDaysOfMonth(), testObj.getDaysOfMonth());
        return daysOfMonthEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = (31 * hash) + getDaysOfMonth().hashCode();
        return hash;
    }
    
    @Override
    public String toString()
    {
        String days = Arrays.stream(getDaysOfMonth())
                .mapToObj(d -> d + ",")
                .collect(Collectors.joining());
        return ByRuleType.BYMONTHDAY + "=" + days.substring(0, days.length()-1); // remove last comma
    }
    
    /**
     * Return stream of valid dates made by rule (infinite if COUNT or UNTIL not present)
     */
    @Override
    public Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startTemporal)
    {
        if (daysOfMonth == null)
        { // if no days specified when constructing, get day of month for startDateTime
            daysOfMonth = new int[] { Month.from(startTemporal).getValue() };
        }
        ChronoUnit originalChronoUnit = chronoUnit.get();
        chronoUnit.set(DAYS);
        switch (originalChronoUnit)
        {
        case DAYS:
            return inStream.filter(d ->
                    { // filter out all but qualifying days
                        int myDay = MonthDay.from(d).getDayOfMonth();
                        int myDaysInMonth = LocalDate.from(d).lengthOfMonth();
                        for (int day : getDaysOfMonth())
                        {
                            if (myDay == day) return true;
                            if ((day < 0) && (myDay == myDaysInMonth + day + 1)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
                        }
                        return false;
                    });
        case MONTHS:
        case YEARS:
            return inStream.flatMap(d -> 
            { // Expand to be daysOfMonth days in current month
                List<Temporal> dates = new ArrayList<>();
                Temporal firstDateOfMonth = d.with(TemporalAdjusters.firstDayOfMonth());
                Temporal lastDateOfMonth = d.with(TemporalAdjusters.lastDayOfMonth());
                for (int day : getDaysOfMonth())
                {
                    if (day > 0)
                    {
                        dates.add(firstDateOfMonth.plus(day-1, ChronoUnit.DAYS));
                    } else
                    { // negative daysOfMonth (-3 = 3rd to last day of month)
                        dates.add(lastDateOfMonth.plus(day+1, ChronoUnit.DAYS));                        
                    }
                }
                return dates.stream();
            });       
        case WEEKS:
            throw new InvalidParameterException("BYMONTHDAY is not available for WEEKLY frequency."); // Not available
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new RuntimeException("Not implemented"); // probably same as DAILY
        default:
            break;
        }
        return null;
    }
}
