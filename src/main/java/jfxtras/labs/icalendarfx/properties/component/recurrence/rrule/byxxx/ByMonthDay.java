package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * By Month Day
 * BYMONTHDAY
 * RFC 5545, iCalendar 3.3.10, page 42
 * 
 * The BYMONTHDAY rule part specifies a COMMA-separated list of days
      of the month.  Valid values are 1 to 31 or -31 to -1.  For
      example, -10 represents the tenth to the last day of the month.
      The BYMONTHDAY rule part MUST NOT be specified when the FREQ rule
      part is set to WEEKLY.
  *
  * @author David Bal
  * 
 * */
public class ByMonthDay extends ByRuleIntegerAbstract<ByMonthDay>
{
    /** sorted array of days of month
     * (i.e. 5, 10 = 5th and 10th days of the month, -3 = 3rd from last day of month)
     * Uses a varargs parameter to allow any number of days
     */
    
//    @Override
//    public void setValue(ObservableList<Integer> monthDays)
//    {
//        super.setValue(monthDays);
//        getValue().addListener(validValueListener);
//    }
//    @Override
//    public void setValue(int... monthDays)
//    {
//        setValue(FXCollections.observableArrayList(monthDays));
//    }
//    @Override
//    public void setValue(String months)
//    {
//        parseContent(months);
//    }
//    @Override
//    public ByMonthDay withValue(Integer... monthDays)
//    {
//        setValue(monthDays);
//        return this;
//    }
//    @Override
//    public ByMonthDay withValue(String monthDays)
//    {
//        setValue(monthDays);
//        return this;
//    }
    
    /*
     * CONSTRUCTORS
     */
    
    public ByMonthDay()
    {
        super();
    }

    public ByMonthDay(Integer... daysOfMonth)
    {
        this();
    }
    
    public ByMonthDay(ByMonthDay source)
    {
        super(source);
    }
    
    @Override
    Predicate<Integer> isValidValue()
    {
        return (value) -> (value < 1) || (value > 31);
    }


//    @Override
//    public void copyTo(ByRule destination)
//    {
//        ByMonthDay destination2 = (ByMonthDay) destination;
//        destination2.daysOfMonth = new int[daysOfMonth.length];
//        for (int i=0; i<daysOfMonth.length; i++)
//        {
//            destination2.daysOfMonth[i] = daysOfMonth[i];
//        }
//    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        ByMonthDay testObj = (ByMonthDay) obj;
//        boolean daysOfMonthEquals = getValue().equals(testObj.getValue());
//        return daysOfMonthEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 5;
//        hash = (31 * hash) + getValue().hashCode();
//        return hash;
//    }
    
//    @Override
//    public String toContent()
//    {
//        String days = getValue().stream()
//                .map(d -> d + ",")
//                .collect(Collectors.joining(","));
//        return RRuleElementType.BY_MONTH_DAY + "=" + days; //.substring(0, days.length()-1); // remove last comma
//    }
    
    /**
     * Return stream of valid dates made by rule (infinite if COUNT or UNTIL not present)
     */
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart)
    {
//        if (getValue() == null)
//        { // if no days specified when constructing, get day of month for startDateTime
//            setValue(MonthDay.from(dateTimeStart).getDayOfMonth());
//        }
//        ChronoUnit originalChronoUnit = chronoUnit.get();
//        chronoUnit.set(DAYS);
        switch (chronoUnit)
        {
        case HOURS:
        case MINUTES:
        case SECONDS:
        case DAYS:
            return inStream.filter(d ->
                    { // filter out all but qualifying days
                        int myDay = MonthDay.from(d).getDayOfMonth();
                        int myDaysInMonth = LocalDate.from(d).lengthOfMonth();
                        for (int day : getValue())
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
                for (int day : getValue())
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
            throw new IllegalArgumentException("BYMONTHDAY is not available for WEEKLY frequency."); // Not available
        default:
            throw new IllegalArgumentException("Not implemented: " + chronoUnit);
        }
    }
    
//    @Override
//    public void parseContent(String content)
//    {
//        Integer[] monthDayArray = Arrays.asList(content.split(","))
//                .stream()
//                .map(s -> Integer.parseInt(s))
//                .toArray(size -> new Integer[size]);
//        setValue(FXCollections.observableArrayList(monthDayArray));
//    }
    
    public static ByMonthDay parse(String content)
    {
        ByMonthDay element = new ByMonthDay();
        element.parseContent(content);
        return element;
    }
}
