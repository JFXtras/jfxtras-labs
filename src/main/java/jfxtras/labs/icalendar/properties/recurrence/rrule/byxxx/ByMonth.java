package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

/** BYMONTH from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByMonth extends ByRuleAbstract
{
    private final static ByRuleType MY_RULE = ByRuleType.BYMONTH;

    /** sorted array of months to be included
     * January = 1 - December = 12
     * Uses a varargs parameter to allow any number of months
     */
    public Month[] getMonths() { return months; }
    private Month[] months;
    private void setMonths(Month... months) { this.months = months; }

    // CONSTRUCTORS
    /** This constructor is REQUIRED by 
     * {@link jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.Rule.ByRuleType#newInstance(String)}
     */
    public ByMonth(String months)
    {
        this();
        setMonths(
          Arrays.asList(months.split(","))
                .stream()
                .map(s -> Month.of(Integer.parseInt(s)))
                .toArray(size -> new Month[size]));
    }

    /**
     * This constructor is required by {@link jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency#copy}
     */
    public ByMonth()
    {
        super(MY_RULE);
    }
    
    public ByMonth(Month... months)
    {
        this();
        setMonths(months);
    }

    @Override
    public void copyTo(Rule destination)
    {
        ByMonth destination2 = (ByMonth) destination;
        destination2.months = months;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByMonth testObj = (ByMonth) obj;
        boolean monthEquals = Arrays.equals(getMonths(), testObj.getMonths());
        return monthEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = (31 * hash) + getMonths().hashCode();
        return hash;
    }

    @Override
    public String toString()
    {
        String days = Arrays.stream(getMonths())
                .map(d -> d.getValue() + ",")
                .collect(Collectors.joining());
        return ByRuleType.BYMONTH + "=" + days.substring(0, days.length()-1); // remove last comma
    }

    @Override
    public Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startDateTime)
    {
        ChronoUnit originalChronoUnit = chronoUnit.get();
        chronoUnit.set(MONTHS);
        switch (originalChronoUnit)
        {
        case DAYS:
        case WEEKS:
        case MONTHS:
            return inStream.filter(t ->
            { // filter out all but qualifying days
                Month myMonth = Month.from(t);
                for (Month month : getMonths())
                {
                    if (month == myMonth) return true;
                }
                return false;
            });
        case YEARS:
            return inStream.flatMap(t -> 
            { // Expand to include matching days in all months
                List<Temporal> dates = new ArrayList<>();
                int monthNum = Month.from(t).getValue();
                for (Month month : getMonths())
                {
                    int myMonthNum = month.getValue();
                    int monthShift = myMonthNum - monthNum;
                    dates.add(t.plus(monthShift, MONTHS));
                }
                return dates.stream();
            });
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new RuntimeException("Not implemented ChronoUnit: " + chronoUnit); // probably same as DAILY
        default:
            break;
        }
        return null;    
    }
}
