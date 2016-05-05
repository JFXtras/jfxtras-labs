package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** BYMONTH from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByMonth extends ByRuleAbstract<ObservableList<Month>, ByMonth>
{
    /** sorted array of months to be included
     * January = 1 - December = 12
     * Uses a varargs parameter to allow any number of months
     */
//    public Month[] getValue() { return months; }
//    private Month[] months;
//    private void setMonths(Month... months) { this.months = months; }
    public void setValue(Month... months)
    {
        setValue(FXCollections.observableArrayList(months));
    }
    public ByMonth withValue(Month... months)
    {
        setValue(months);
        return this;
    }
    public ByMonth withValue(String months)
    {
        setValue(makeMonthList(months));
        return this;
    }
    
    /*
     * CONSTRUCTORS
     */
    public ByMonth()
    {
        super(ByMonth.class);
    }
    
//    @Deprecated // use parse
//    public ByMonth(String months)
//    {
//        this();        
//        setValue(
//          Arrays.asList(months.split(","))
//                .stream()
//                .map(s -> Month.of(Integer.parseInt(s)))
//                .toArray(size -> new Month[size]));
//    }
    
    public ByMonth(Month... months)
    {
        this();
        setValue(FXCollections.observableArrayList(months));
    }
    
    public ByMonth(ByMonth source)
    {
        super(source);
    }

//    @Override
//    public void copyTo(ByRule destination)
//    {
//        ByMonth destination2 = (ByMonth) destination;
//        destination2.setValue(getValue());
//    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByMonth testObj = (ByMonth) obj;
        boolean monthEquals = getValue().equals(testObj.getValue());
//        boolean monthEquals = Arrays.equals(getValue(), testObj.getValue());
        return monthEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = (31 * hash) + getValue().hashCode();
        return hash;
    }

    @Override
    public String toString()
    {
        String days = getValue().stream()
//        String days = Arrays.stream(getValue())
                .map(d -> d.getValue() + ",")
                .collect(Collectors.joining());
        return ByRuleType.BY_MONTH + "=" + days.substring(0, days.length()-1); // remove last comma
    }

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startDateTime)
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
                for (Month month : getValue())
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
                for (Month month : getValue())
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
    
    private static ObservableList<Month> makeMonthList(String months)
    {
        return FXCollections.observableArrayList(
            Arrays.asList(months.split(","))
                .stream()
                .map(s -> Month.of(Integer.parseInt(s)))
                .toArray(size -> new Month[size]));
    }

    public static ByMonth parse(String months)
    {
        ByMonth byMonth = new ByMonth();
        byMonth.setValue(makeMonthList(months) );
        return byMonth;
    }
}
