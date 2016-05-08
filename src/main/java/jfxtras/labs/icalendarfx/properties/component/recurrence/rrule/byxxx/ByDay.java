package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/** BYDAY from RFC 5545, iCalendar 3.3.10, page 40 */
public class ByDay extends ByRuleAbstract<ByDayPair, ByDay>
{
//    private final TemporalField field;
//    private final int firstDayOfWeekAdjustment;
    /** Array of days of the week.  Ordinal number is optional.  Without will include all
     * days matching that day of the week, with the ordinal will be only include the
     * nth day of the week in the month, when n is the ordinal number.
     * 
     * Uses a varargs parameter to allow any number of days
     * The list of days with ordinals must be sorted.  For example 1MO,2TU,4SA not 2TU,1MO,4SA
     */
//    public ObservableList<ByDayPair> byDayPair() { return byDayPairs; }
//    ObservableList<ByDayPair> byDayPairs = FXCollections.observableArrayList();
//    public ByDayPair[] getValue() { return byDayPairs; }
//    private ByDayPair[] byDayPairs;
//    private void setByDayPair(ByDayPair... byDayPairs) { this.byDayPairs = byDayPairs; }

//    public void setValue(ByDayPair... byDayPairs)
//    {
//        setValue(FXCollections.observableArrayList(byDayPairs));
//    }
//    public void setValue(String byDayPairs)
//    {
//        parseContent(byDayPairs);
//    }
//    public ByDay withValue(ByDayPair... byDayPairs)
//    {
//        setValue(byDayPairs);
//        return this;
//    }
//    public ByDay withValue(String byDayPairs)
//    {
//        setValue(byDayPairs);
//        return this;
//    }
    
    /** Start of week - default start of week is Monday */
    public ObjectProperty<DayOfWeek> weekStartProperty() { return weekStart; }
    private ObjectProperty<DayOfWeek> weekStart =  new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString()); // bind to WeekStart element
    public DayOfWeek getWeekStart() { return (weekStart.get() == null) ? WeekStart.DEFAULT_WEEK_START : weekStart.get(); }
    private final static int MIN_DAYS_IN_WEEK = 4;
    
    //CONSTRUCTORS
    /** Parse iCalendar compliant list of days of the week.  For example 1MO,2TU,4SA
     */
    public ByDay()
    {
        super();

//        super(ByDay.class);
        // TODO - USE WKST PROPERTY FOR START OF WEEK
//        field = WeekFields.of(Locale.getDefault()).dayOfWeek();
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        firstDayOfWeekAdjustment = (weekFields.getFirstDayOfWeek() == DayOfWeek.SUNDAY) ? 1 : 0;
    }
    
//    @Deprecated // use parse instead
//    public ByDay(String dayPairs)
//    {
//        this();
//        parseContent(dayPairs);
//    }
    
    /** Constructor with varargs ByDayPair */
    public ByDay(ByDayPair... byDayPairs)
    {
        this();
//        setValue(FXCollections.observableArrayList(byDayPairs));
    }
    
    public ByDay(ByDay source)
    {
        super(source);
//        field = WeekFields.of(Locale.getDefault()).dayOfWeek();
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        firstDayOfWeekAdjustment = (weekFields.getFirstDayOfWeek() == DayOfWeek.SUNDAY) ? 1 : 0;
    }

    /** Constructor that uses DayOfWeek values without a preceding integer.  All days of the 
     * provided types are included within the specified frequency */
    public ByDay(DayOfWeek... daysOfWeek)
    {
        this(Arrays.asList(daysOfWeek));
    }

    /** Constructor that uses DayOfWeek Collection.  No ordinals are allowed. */
    public ByDay(Collection<DayOfWeek> daysOfWeek)
    {
        this();
        ByDayPair[] dayArray = daysOfWeek.stream()
            .map(d -> new ByDayPair(d,0))
            .toArray(size -> new ByDayPair[size]);
        setValue(FXCollections.observableArrayList(dayArray));
    }

    
    
    /** Checks if byDayPairs has ordinal values.  If so returns true, otherwise false */
    public boolean hasOrdinals()
    {
        return getValue()
                .stream()
                .filter(p -> (p.ordinal != 0))
                .findAny()
                .isPresent();
    }
    
    /** add individual DayofWeek, without ordinal value, to BYDAY rule */
    public boolean addDayOfWeek(DayOfWeek dayOfWeek)
    {
        boolean isPresent = getValue()
            .stream()
            .map(a -> a.dayOfWeek)
            .filter(d -> d == dayOfWeek)
            .findAny()
            .isPresent();
        if (! isPresent)
        {
//            List<ByDayPair> list = new ArrayList<>(Arrays.asList(getValue()));
//            list.add(new ByDayPair(dayOfWeek, 0));
//            byDayPairs = list.toArray(byDayPairs);
            getValue().add(new ByDayPair(dayOfWeek, 0));
            return true;
        }
        return false;
    }

    /** remove individual DayofWeek from BYDAY rule */
    public boolean removeDayOfWeek(DayOfWeek dayOfWeek)
    {
        Optional<ByDayPair> optional = getValue().stream()
                .filter(v -> v.dayOfWeek == dayOfWeek)
                .findAny();
        boolean isFound = optional.isPresent();
        if (isFound)
        {
            getValue().remove(optional.get());
        }
        return isFound;
//        byDayPairs = Arrays.stream(getValue())
//                .filter(d -> d.dayOfWeek != dayOfWeek)
//                .toArray(size -> new ByDayPair[size]);
        
    }
    
    /** Return a list of days of the week that don't have an ordinal (as every FRIDAY) */
    public List<DayOfWeek> dayOfWeekWithoutOrdinalList()
    {
        return getValue().stream()
                     .filter(d -> d.ordinal == 0)
                     .map(d -> d.dayOfWeek)
                     .collect(Collectors.toList());
    }
    
//    @Override
//    public void copyTo(ByRule destination)
//    {
//        ByDay destination2 = (ByDay) destination;
//        destination2.byDayPairs = new ByDayPair[byDayPairs.length];
//        for (int i=0; i<byDayPairs.length; i++)
//        {
//            destination2.byDayPairs[i] = new ByDayPair(byDayPairs[i].dayOfWeek, byDayPairs[i].ordinal);
//        }
//    }

//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        ByDay testObj = (ByDay) obj;
//        boolean byDayPairsEquals = getValue().equals(testObj.getValue());
////        System.out.println("ByDay equals " + byDayPairsEquals);
//        return byDayPairsEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 11;
//        hash = (31 * hash) + getValue().hashCode();
//        return hash;
//    }
    
    @Override
    public String toContent()
    {
        String days = getValue().stream()
                .map(d ->
                {
                    String day = d.dayOfWeek.toString().substring(0, 2); // + ",";
                    return (d.ordinal == 0) ? day : d.ordinal + day;
                })
                .collect(Collectors.joining(","));
        return RRuleElementType.BY_DAY + "=" + days; //.substring(0, days.length()-1); // remove last comma
    }
    
    @Override // TODO - try to REMOVE startTemporal
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart)
    {
        // TODO - according to iCalendar standard a ByDay rule doesn't need any specified days - should use day from DTSTART, this is not implemented yet.  When implemented this line should be removed.
//        if (getValue().size() == 0) throw new RuntimeException("ByDay rule must have at least one day specified");
//        ChronoUnit originalChronoUnit = chronoUnit.get();
//        chronoUnit.set(DAYS);
        switch (chronoUnit)
        {
        case HOURS:
        case MINUTES:
        case SECONDS:
        case DAYS:
        {
            boolean isValid = getValue().stream().allMatch(v -> v.getOrdinal() == 0);
            if (! isValid)
            {
                throw new IllegalArgumentException("Numberic ordinal day values can't be set for FREQ as" + chronoUnit);
            }
            return inStream.filter(t ->
            { // filter out all but qualifying days
                DayOfWeek myDayOfWeek = DayOfWeek.from(t);
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.dayOfWeek == myDayOfWeek) return true;
                }
                return false;
            });
        }
        case WEEKS:
        {
            boolean isValid = getValue().stream().allMatch(v -> v.getOrdinal() == 0);
            if (! isValid)
            {
                throw new IllegalArgumentException("Numberic ordinal day values can't be set for FREQ as " + chronoUnit);
            }
            TemporalField dayOfWeekField = WeekFields.of(getWeekStart(), MIN_DAYS_IN_WEEK).dayOfWeek();
            return inStream.flatMap(t -> 
            { // Expand to be byDayPairs days in current week
                List<Temporal> dates = new ArrayList<>();
//                TemporalField field = WeekFields.of(Locale.getDefault()).dayOfWeek();
                for (ByDayPair byDayPair : getValue())
                {
                    int value = byDayPair.dayOfWeek.getValue(); //+ firstDayOfWeekAdjustment;
                    int valueAdj = (value > 7) ? value-7 : value;
                    Temporal newTemporal = t.with(dayOfWeekField, valueAdj);
//                    dates.add(newTemporal);
                    if (! DateTimeUtilities.isBefore(newTemporal, dateTimeStart)) dates.add(newTemporal);
                }
                Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            });
        }
        case MONTHS:
            return inStream.flatMap(date -> 
            {
                List<Temporal> dates = new ArrayList<>();
//                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in month
//                        sortNeeded = true;
                        Month myMonth = Month.from(date);
                        for (int weekNum=1; weekNum<=5; weekNum++)
                        {
                            Temporal newTemporal = date.with(TemporalAdjusters.dayOfWeekInMonth(weekNum, byDayPair.dayOfWeek));
//                            if (Month.from(newTemporal) == myMonth) dates.add(newTemporal);
                            if (Month.from(newTemporal) == myMonth && ! DateTimeUtilities.isBefore(newTemporal, dateTimeStart)) dates.add(newTemporal);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Month myMonth = Month.from(date);
                        Temporal newTemporal = date.with(TemporalAdjusters.dayOfWeekInMonth(byDayPair.ordinal, byDayPair.dayOfWeek));
//                        System.out.println("values:" + byDayPair.ordinal + " " + byDayPair.dayOfWeek + " " + newTemporal);
//                        if (Month.from(newTemporal) == myMonth) dates.add(newTemporal);
                        if (Month.from(newTemporal) == myMonth && ! DateTimeUtilities.isBefore(newTemporal, dateTimeStart)) dates.add(newTemporal);
                    }
                }
                if (getValue().size() > 1) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            });
        case YEARS:
            return inStream.flatMap(date -> 
            {
                List<Temporal> dates = new ArrayList<>();
//                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getValue())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in year
//                        sortNeeded = true;
                        Temporal newDate = date
                                .with(TemporalAdjusters.firstDayOfYear())
                                .with(TemporalAdjusters.nextOrSame(byDayPair.dayOfWeek));
                        while (Year.from(newDate).equals(Year.from(date)))
                        {
                            if (! DateTimeUtilities.isBefore(newDate, dateTimeStart)) dates.add(newDate);
//                            dates.add(newDate);
                            newDate = newDate.plus(1, ChronoUnit.WEEKS);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Temporal newDate = date.with(dayOfWeekInYear(byDayPair.ordinal, byDayPair.dayOfWeek));
//                        dates.add(newDate);
                        if (! DateTimeUtilities.isBefore(newDate, dateTimeStart)) dates.add(newDate);
                    }
                }
                if (getValue().size() > 1) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            }); 
        default:
            throw new RuntimeException("Not implemented ChronoUnit: " + chronoUnit);
        }
    }

    /** Finds nth occurrence of a week in a year.
     * Based on TemporalAdjusters.dayOfWeekInMonth */
    private TemporalAdjuster dayOfWeekInYear(int ordinal, DayOfWeek dayOfWeek)
    {
        int dowValue = dayOfWeek.getValue();
        return (temporal) -> {
            Temporal temp = (ordinal > 0) ? temporal.with(TemporalAdjusters.firstDayOfYear()) :
                temporal.plus(1, ChronoUnit.YEARS).with(TemporalAdjusters.firstDayOfYear());
            int curDow = temp.get(DAY_OF_WEEK);
            int dowDiff = (dowValue - curDow + 7) % 7;
            dowDiff = (ordinal > 0) ? dowDiff + (ordinal - 1) * 7 : dowDiff + (ordinal) * 7;
            return temp.plus(dowDiff, DAYS);
        };
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
        private DayOfWeek dayOfWeek;
        public DayOfWeek getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public ByDayPair withDayOfWeek(DayOfWeek dayOfWeek) { setDayOfWeek(dayOfWeek); return this; }
        
        private int ordinal = 0;
        public int getOrdinal() { return ordinal; }
        public void setOrdinal(int ordinal) { this.ordinal = ordinal; }
        public ByDayPair withOrdinal(int ordinal) { setOrdinal(ordinal); return this; }

        public ByDayPair(DayOfWeek dayOfWeek, int ordinal)
        {
            this.dayOfWeek = dayOfWeek;
            this.ordinal = ordinal;
        }
        
        public ByDayPair() { }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == this) return true;
            if((obj == null) || (obj.getClass() != getClass())) {
                return false;
            }
            ByDayPair testObj = (ByDayPair) obj;
            return (dayOfWeek == testObj.dayOfWeek)
                    && (ordinal == testObj.ordinal);
        }
        
        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = (31 * hash) + dayOfWeek.hashCode();
            hash = (31 * hash) + ordinal;
            return hash;
        }
        
        @Override
        public String toString()
        {
            return super.toString() + ", " + getDayOfWeek() + ", " + getOrdinal();
        }
        
    }
    
//    /** Match up iCalendar 2-character day of week to Java Time DayOfWeek */
//    @Deprecated
//    public enum ICalendarDayOfWeek
//    {
//        MO (DayOfWeek.MONDAY)
//      , TU (DayOfWeek.TUESDAY)
//      , WE (DayOfWeek.WEDNESDAY)
//      , TH (DayOfWeek.THURSDAY)
//      , FR (DayOfWeek.FRIDAY)
//      , SA (DayOfWeek.SATURDAY)
//      , SU (DayOfWeek.SUNDAY);
//      
//        private DayOfWeek dow;
//      
//        ICalendarDayOfWeek(DayOfWeek dow)
//        {
//          this.dow = dow;
//        }
//      
//        public DayOfWeek getDayOfWeek() { return dow; }
//    }
    
    @Override
    public void parseContent(String dayPairs)
    {
        List<ByDayPair> dayPairsList = new ArrayList<ByDayPair>();
        Pattern p = Pattern.compile("(-?[0-9]+)?([A-Z]{2})");
        Matcher m = p.matcher(dayPairs);
        while (m.find())
        {
            String token = m.group();
            if (token.matches("^(-?[0-9]+.*)")) // start with ordinal number
            {
                Matcher m2 = p.matcher(token);
                if (m2.find())
                {
//                    DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(m2.group(2)).getDayOfWeek();
                    DayOfWeek dayOfWeek = DateTimeUtilities.dayOfWeekFromAbbreviation(m2.group(2));
                    int ordinal = Integer.parseInt(m2.group(1));
                    dayPairsList.add(new ByDayPair(dayOfWeek, ordinal));
                }
            } else
            { // has no ordinal number
                DayOfWeek dayOfWeek = DateTimeUtilities.dayOfWeekFromAbbreviation(token);
//                DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(token).getDayOfWeek();
                dayPairsList.add(new ByDayPair(dayOfWeek, 0));
            }
        }
        setValue(FXCollections.observableArrayList(dayPairsList));
    }

    public static ByDay parse(String content)
    {
        ByDay element = new ByDay();
        element.parseContent(content);
        return element;
    }
    
//    @Override
//    public boolean isValid()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
}
