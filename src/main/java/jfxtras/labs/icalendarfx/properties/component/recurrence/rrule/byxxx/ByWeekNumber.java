package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/** BYWEEKNO from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByWeekNumber extends ByRuleAbstract<ObservableList<Integer>, ByWeekNumber>
{
//    /** sorted array of weeks of the year
//     * (i.e. 5, 10 = 5th and 10th weeks of the year, -3 = 3rd from last week of the year)
//     * Uses a varargs parameter to allow any number of value.
//     */
//    public int[] getWeekNumbers() { return weekNumbers; }
//    private int[] weekNumbers;
//    public void setWeekNumbers(int... weekNumbers)
//    {
//        for (int w : weekNumbers)
//        {
//            if (w < -53 || w > 53 || w == 0) throw new IllegalArgumentException("Invalid BYWEEKNO value (" + w + "). Valid values are 1 to 53 or -53 to -1.");
//        }
//        this.weekNumbers = weekNumbers;
//    }
//    public ByWeekNumber withWeekNumbers(int... weekNumbers) { setWeekNumbers(weekNumbers); return this; }
    public void setValue(Integer... weekNumbers)
    {
        for (int w : weekNumbers)
        {
            if (w < -53 || w > 53 || w == 0) throw new IllegalArgumentException("Invalid BYWEEKNO value (" + w + "). Valid values are 1 to 53 or -53 to -1.");
        }
        setValue(FXCollections.observableArrayList(weekNumbers));
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
    public DayOfWeek getWeekStart() { return (weekStart.get() == null) ? DayOfWeek.MONDAY : weekStart.get(); }
    private final static int MIN_DAYS_IN_WEEK = 4;
//    private DayOfWeek weekStart = DayOfWeek.MONDAY; // default to start on Monday
//    public void setWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; }
//    public ByWeekNumber withWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; return this; }

    /*
     * CONSTRUCTORS
     */
    public ByWeekNumber()
    {
        super();
//        super(ByWeekNumber.class);
    }
    
//    /** takes String of comma-delimited integers, parses it to array of ints 
//     */
//    public ByWeekNumber(String weekNumbers)
//    {
//        this();
//        parseContent(weekNumbers);
//    }
    
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
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal startTemporal)
    {
//        ChronoUnit originalChronoUnit = chronoUnit.get();
//        chronoUnit.set(WEEKS);

        switch (chronoUnit)
        {
        case YEARS:
            WeekFields weekFields = WeekFields.of(getWeekStart(), MIN_DAYS_IN_WEEK);
            Stream<Temporal> outStream = inStream.flatMap(date -> 
            { // Expand to include matching days in all months
                DayOfWeek dayOfWeek = DayOfWeek.from(startTemporal);
                List<Temporal> dates = new ArrayList<>();
                for (int myWeekNumber: getValue())
                {
                    Temporal newTemporal = date.with(TemporalAdjusters.next(dayOfWeek));
                    int newDateWeekNumber = newTemporal.get(weekFields.weekOfWeekBasedYear());
                    int weekShift = myWeekNumber - newDateWeekNumber;
                    if (! DateTimeUtilities.isBefore(newTemporal, startTemporal))
                    {
                        dates.add(newTemporal.plus(weekShift, ChronoUnit.WEEKS));
                    }
                }
                return dates.stream();
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
    
//    public static int calcWeekNumber(LocalDate date, DayOfWeek startOfWeek)
//    {
//        System.out.println("calc week:" + date);
//        int weekNumber = 1;
//        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfYear());
////        LocalDate firstMatchingDay = firstDay.with(TemporalAdjusters.nextOrSame(startOfWeek));
//        long daysBetween = ChronoUnit.DAYS.between(firstDay, date) + 1;
//        
//        firstDay = (daysBetween >= 4) ? firstDay : firstDay.minusYears(1);
//        System.out.println("firstDay2:" + firstDay);
//        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, ChronoUnit.DAYS);
//        Iterator<LocalDate> dateIterator = Stream.iterate(firstDay, a -> a.with(adjuster)).iterator();
//        
//        // first week
//        DayOfWeek myDayOfWeek = DayOfWeek.from(firstDay);
//        int dayCounter = 0;
//        LocalDate myDate = firstDay;
//        while ((myDate.isBefore(date)) && (! myDayOfWeek.equals(startOfWeek)))
////        do
//        {
//            dayCounter++;
//            myDate = dateIterator.next();
//            myDayOfWeek = DayOfWeek.from(myDate);
////            System.out.println("first week:" + myDate + " " + date);
//        } //while (! myDayOfWeek.equals(startOfWeek));
//        if (dayCounter >= 4)
//        {
//            weekNumber++;
//        }
//
//        System.out.println("first week:" + weekNumber);
//        // count beyond first week
////        Year year = Year.from(firstDay);
////        Year myYear = year;
//        TemporalAdjuster adjuster2 = (temporal) -> temporal.plus(1, ChronoUnit.WEEKS);
//        Iterator<LocalDate> dateIterator2 = Stream.iterate(myDate, a -> a.with(adjuster2)).iterator();
//        LocalDate myDate2 = myDate;
//        while (myDate2.isBefore(date))
//        {
//            weekNumber++;
//            myDate2 = dateIterator2.next();
//            myDayOfWeek = DayOfWeek.from(myDate2);
//        };
//        
//        return weekNumber;
//    }
    
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
