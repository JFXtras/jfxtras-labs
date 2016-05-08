package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/** 
 * By Week Number
 * BYWEEKNO
 * RFC 5545, iCalendar 3.3.10, page 42
 * 
 * The BYWEEKNO rule part specifies a COMMA-separated list of
      ordinals specifying weeks of the year.  Valid values are 1 to 53
      or -53 to -1.  This corresponds to weeks according to week
      numbering as defined in [ISO.8601.2004].  A week is defined as a
      seven day period, starting on the day of the week defined to be
      the week start (see WKST).  Week number one of the calendar year
      is the first week that contains at least four (4) days in that
      calendar year.  This rule part MUST NOT be used when the FREQ rule
      part is set to anything other than YEARLY.  For example, 3
      represents the third week of the year.

         Note: Assuming a Monday week start, week 53 can only occur when
         Thursday is January 1 or if it is a leap year and Wednesday is
         January 1.
   *         
   * @author David Bal
   * 
 * */
public class ByWeekNumber extends ByRuleAbstract<ObservableList<Integer>, ByWeekNumber>
{
//    /** sorted array of weeks of the year
//     * (i.e. 5, 10 = 5th and 10th weeks of the year, -3 = 3rd from last week of the year)
//     * Uses a varargs parameter to allow any number of value.
//     */
    @Override
    public void setValue(ObservableList<Integer> weekNumbers)
    {
        super.setValue(weekNumbers);
        getValue().addListener(validValueListener);
    }
    public void setValue(Integer... weekNumbers)
    {
        getValue().addAll(weekNumbers);
    }
    public void setValue(String weekNumbers)
    {
        parseContent(weekNumbers);        
    }
    public ByWeekNumber withValue(Integer... weekNumbers)
    {
        setValue(weekNumbers);
        return this;
    }
    public ByWeekNumber withValue(String weekNumbers)
    {
        setValue(weekNumbers);
        return this;
    }


    
    /** Start of week - default start of week is Monday */
    public ObjectProperty<DayOfWeek> weekStartProperty() { return weekStart; }
    private ObjectProperty<DayOfWeek> weekStart =  new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString()); // bind to WeekStart element
    public DayOfWeek getWeekStart() { return (weekStart.get() == null) ? WeekStart.DEFAULT_WEEK_START : weekStart.get(); }
    private final static int MIN_DAYS_IN_WEEK = 4;

    /*
     * CONSTRUCTORS
     */
    public ByWeekNumber()
    {
        super();
        setValue(FXCollections.observableArrayList());
    }
    
    /** Constructor requires weeks of the year int value(s) */
    public ByWeekNumber(Integer...weekNumbers)
    {
        this();
        setValue(weekNumbers);
    }
    
    public ByWeekNumber(ByWeekNumber source)
    {
        super(source);
    }
    
    /**
     * Listener to validate additions to value list
     */
    private ListChangeListener<Integer> validValueListener = (ListChangeListener.Change<? extends Integer> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                Iterator<? extends Integer> i = change.getAddedSubList().iterator();
                while (i.hasNext())
                {
                    int value = i.next();
                    if ((value < -53) || (value > 53) || (value == 0))
                    {
                        throw new IllegalArgumentException("Invalid " + elementType().toString() + " value (" + value + "). Valid values are 1 to 53 or -53 to -1.");
                    }
                }
            }
        }
    };

//    @Override
//    public void copyTo(ByRule destination)
//    {
//        ByWeekNumber destination2 = (ByWeekNumber) destination;
//        destination2.weekNumbers = new int[weekNumbers.length];
//        for (int i=0; i<weekNumbers.length; i++)
//        {
//            destination2.weekNumbers[i] = weekNumbers[i];
//        }
//    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        ByWeekNumber testObj = (ByWeekNumber) obj;
//        
//        boolean weekNumbersEquals = Arrays.equals(getValue(), testObj.getValue());
//        return weekNumbersEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = (31 * hash) + getWeekNumbers().hashCode();
//        return hash;
//    }
    
    @Override
    public String toContent()
    {
        String days = getValue().stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(","));
        return RRuleElementType.BY_WEEK_NUMBER + "=" + days; //.substring(0, days.length()-1); // remove last comma
    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart )
    {
//        ChronoUnit originalChronoUnit = chronoUnit.get();
//        chronoUnit.set(WEEKS);

        switch (chronoUnit)
        {
        case YEARS:
            WeekFields weekFields = WeekFields.of(getWeekStart(), MIN_DAYS_IN_WEEK);
            Stream<Temporal> outStream = inStream.flatMap(date -> 
            { // Expand to include all days matching week numbers
//                DayOfWeek dayOfWeek = DayOfWeek.from(date);
                Set<Temporal> dates = new HashSet<>();
                for (int myWeekNumber : getValue())
                {
                    Temporal startDate = date
                            .with(weekFields.weekOfWeekBasedYear(), myWeekNumber)
                            .with(TemporalAdjusters.previousOrSame(getWeekStart()));
                    IntStream.range(0,7).forEach(days -> 
                    {
                        Temporal newTemporal = startDate.plus(days, ChronoUnit.DAYS);
                        if (! DateTimeUtilities.isBefore(newTemporal, dateTimeStart))
                        {
                            dates.add(newTemporal);
                        }
                    });
                }
                return dates.stream().sorted(DateTimeUtilities.TEMPORAL_COMPARATOR);
            });
            return outStream;
        case DAYS:
        case WEEKS:
        case MONTHS:
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new IllegalArgumentException("BYWEEKNO is not available for " + chronoUnit + " frequency."); // Not available
        default:
            break;
        }
        return null;    
    }

    @Override
    public void parseContent(String weekNumbers)
    {
        Integer[] weekArray = Arrays.asList(weekNumbers.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .toArray(size -> new Integer[size]);
        setValue(weekArray);
    }

    public static ByWeekNumber parse(String content)
    {
        ByWeekNumber element = new ByWeekNumber();
        element.parseContent(content);
        return element;
    }

}
