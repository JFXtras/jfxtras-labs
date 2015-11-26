package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import static java.time.temporal.ChronoUnit.DAYS;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

/** BYMONTHDAY from RFC 5545, iCalendar */
public class ByMonthDay extends ByRuleAbstract
{
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
     * If not setting daysOfMonth then defaults to startLocalDateTime for dayOfMonth */
    public ByMonthDay(Frequency frequency) {
        super(frequency);
        setSortOrder(30);
    }

    /** Constructor 
     * Contains varargs of daysOfMonth */
    public ByMonthDay(Frequency frequency, int... daysOfMonth)
    {
        super(frequency);
        setDaysOfMonth(daysOfMonth);
        setSortOrder(30);
    }
    public ByMonthDay() { }

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
//        System.out.println("ByMonthDay equals " + daysOfMonthEquals + " " + daysOfMonth[0] + " " + testObj.getDaysOfMonth()[0]);
        return daysOfMonthEquals;
    }
    
    @Override
    public String toString()
    {
        String days = Arrays.stream(getDaysOfMonth())
                .mapToObj(d -> d + ",")
                .collect(Collectors.joining());
        return "BYMONTHDAY=" + days.substring(0, days.length()-1); // remove last comma
    }
    
    /**
     * Return stream of valid dates made by rule (infinite if COUNT or UNTIL not present)
     */
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        if (daysOfMonth == null)
        { // if no days specified when constructing, get day of month for startDateTime
            daysOfMonth = new int[] { startDateTime.toLocalDate().getDayOfMonth() };
        }
//        int startDaysInMonth = startDateTime.toLocalDate().lengthOfMonth();
//        validDays = makeValidDays(startDaysInMonth, getDaysOfMonth());
//        switch (getFrequency().frequencyEnum())
        switch (getFrequency().getChronoUnit())
        {
        case DAYS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.filter(d ->
                    { // filter out all but qualifying days
                        int myDay = d.toLocalDate().getDayOfMonth();
                        int myDaysInMonth = d.toLocalDate().lengthOfMonth();
                        for (int day : getDaysOfMonth())
                        {
                            if (myDay == day) return true;
                            if ((day < 0) && (myDay == myDaysInMonth + day + 1)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
                        }
                        return false;
                    });
        case MONTHS:
        case YEARS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.flatMap(d -> 
            { // Expand to be daysOfMonth days in current month
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                LocalDateTime firstDateOfMonth = d.with(TemporalAdjusters.firstDayOfMonth());
                LocalDateTime lastDateOfMonth = d.with(TemporalAdjusters.lastDayOfMonth());
                for (int day : getDaysOfMonth())
                {
                    if (day > 0)
                    {
                        dates.add(firstDateOfMonth.plusDays(day-1));
                    } else
                    { // negative daysOfMonth (-3 = 3rd to last day of month)
                        dates.add(lastDateOfMonth.plusDays(day+1));                        
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
