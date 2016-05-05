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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/** BYDAY from RFC 5545, iCalendar 3.3.10, page 40 */
public class ByDay extends ByRuleAbstract<ObservableList<ByDayPair>, ByMonth>
{
    private final TemporalField field;
    private final int firstDayOfWeekAdjustment;
    /** Array of days of the week.  Ordinal number is optional.  Without will include all
     * days matching that day of the week, with the ordinal will be only include the
     * nth day of the week in the month, when n is the ordinal number.
     * 
     * Uses a varargs parameter to allow any number of days
     * The list of days with ordinals must be sorted.  For example 1MO,2TU,4SA not 2TU,1MO,4SA
     */
//    public ObservableList<ByDayPair> byDayPair() { return byDayPairs; }
//    ObservableList<ByDayPair> byDayPairs = FXCollections.observableArrayList();
    public ByDayPair[] getByDayPairs() { return byDayPairs; }
    private ByDayPair[] byDayPairs;
    private void setByDayPair(ByDayPair... byDayPairs) { this.byDayPairs = byDayPairs; }
    /** Checks if byDayPairs has ordinal values.  If so returns true, otherwise false */
    public boolean hasOrdinals()
    {
        return Arrays.stream(getByDayPairs())
                .filter(p -> (p.ordinal != 0))
                .findAny()
                .isPresent();
    }
    
    /** add individual DayofWeek, without ordinal value, to BYDAY rule */
    public void addDayOfWeek(DayOfWeek dayOfWeek)
    {
        boolean isPresent = Arrays.stream(getByDayPairs())
            .map(a -> a.dayOfWeek)
            .filter(d -> d == dayOfWeek)
            .findAny()
            .isPresent();
        if (! isPresent)
        {
            List<ByDayPair> list = new ArrayList<>(Arrays.asList(getByDayPairs()));
            list.add(new ByDayPair(dayOfWeek, 0));
            byDayPairs = list.toArray(byDayPairs);
        }
    }

    /** remove individual DayofWeek from BYDAY rule */
    public void removeDayOfWeek(DayOfWeek dayOfWeek)
    {
        byDayPairs = Arrays.stream(getByDayPairs())
                .filter(d -> d.dayOfWeek != dayOfWeek)
                .toArray(size -> new ByDayPair[size]);
        
    }
    
    /** Return a list of days of the week that don't have an ordinal (as every FRIDAY) */
    public List<DayOfWeek> dayOfWeekWithoutOrdinalList()
    {
        return Arrays.stream(getByDayPairs())
                     .filter(d -> d.ordinal == 0)
                     .map(d -> d.dayOfWeek)
                     .collect(Collectors.toList());
    }
    
    //CONSTRUCTORS
    /** Parse iCalendar compliant list of days of the week.  For example 1MO,2TU,4SA
     */
    public ByDay()
    {
        super(ByDay.class);
        // TODO - USE WKST PROPERTY FOR START OF WEEK
        field = WeekFields.of(Locale.getDefault()).dayOfWeek();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        firstDayOfWeekAdjustment = (weekFields.getFirstDayOfWeek() == DayOfWeek.SUNDAY) ? 1 : 0;
    }
    
    @Deprecated // use parse instead
    public ByDay(String dayPairs)
    {
        this();
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
                    DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(m2.group(2)).getDayOfWeek();
                    int ordinal = Integer.parseInt(m2.group(1));
                    dayPairsList.add(new ByDayPair(dayOfWeek, ordinal));
                }
            } else
            { // has no ordinal number
                DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(token).getDayOfWeek();
                dayPairsList.add(new ByDayPair(dayOfWeek, 0));
            }
        }
        byDayPairs = new ByDayPair[dayPairsList.size()];
        byDayPairs = dayPairsList.toArray(byDayPairs);
    }
    
    /** Constructor with varargs ByDayPair */
    public ByDay(ByDayPair... byDayPairs)
    {
        this();
        setByDayPair(byDayPairs);
    }
    
    public ByDay(ByRule source)
    {
        super(source);
        field = WeekFields.of(Locale.getDefault()).dayOfWeek();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        firstDayOfWeekAdjustment = (weekFields.getFirstDayOfWeek() == DayOfWeek.SUNDAY) ? 1 : 0;
    }

    /** Constructor that uses DayOfWeek values without a preceding integer.  All days of the 
     * provided types are included within the specified frequency */
    public ByDay(DayOfWeek... daysOfWeek)
    {
        this();
        if (daysOfWeek.length > 0) byDayPairs = Arrays.stream(daysOfWeek)
                .map(d -> new ByDayPair(d,0))
                .toArray(size -> new ByDayPair[size]);
    }

    /** Constructor that uses DayOfWeek Collection.  No ordinals are allowed. */
    public ByDay(Collection<DayOfWeek> daysOfWeeks)
    {
        this();
        if (daysOfWeeks.size() > 0) byDayPairs = daysOfWeeks.stream()
                .map(d -> new ByDayPair(d,0))
                .toArray(size -> new ByDayPair[size]);
    }

    
    @Override
    public void copyTo(ByRule destination)
    {
        ByDay destination2 = (ByDay) destination;
        destination2.byDayPairs = new ByDayPair[byDayPairs.length];
        for (int i=0; i<byDayPairs.length; i++)
        {
            destination2.byDayPairs[i] = new ByDayPair(byDayPairs[i].dayOfWeek, byDayPairs[i].ordinal);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByDay testObj = (ByDay) obj;
        boolean byDayPairsEquals = Arrays.equals(getByDayPairs(), testObj.getByDayPairs());
//        System.out.println("ByDay equals " + byDayPairsEquals);
        return byDayPairsEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 11;
        hash = (31 * hash) + getByDayPairs().hashCode();
        return hash;
    }
    
    @Override
    public String toString()
    {
        String days = Arrays.stream(getByDayPairs())
                .map(d ->
                {
                    String day = d.dayOfWeek.toString().substring(0, 2) + ",";
                    return (d.ordinal == 0) ? day : d.ordinal + day;
                })
                .collect(Collectors.joining());
        return ByRuleType.BY_DAY + "=" + days.substring(0, days.length()-1); // remove last comma
    }
    
    @Override // TODO - try to REMOVE startTemporal
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startTemporal)
    {
        // TODO - according to iCalendar standard a ByDay rule doesn't need any specified days - should use day from DTSTART, this is not implemented yet.  When implemented this line should be removed.
        if (getByDayPairs().length == 0) throw new RuntimeException("ByDay rule must have at least one day specified");
        ChronoUnit originalChronoUnit = chronoUnit.get();
        chronoUnit.set(DAYS);
        switch (originalChronoUnit)
        {
        case DAYS:
            return inStream.filter(t ->
            { // filter out all but qualifying days
                DayOfWeek myDayOfWeek = DayOfWeek.from(t);
                for (ByDayPair byDayPair : getByDayPairs())
                {
                    if (byDayPair.dayOfWeek == myDayOfWeek) return true;
                }
                return false;
            });
        case WEEKS:
            return inStream.flatMap(t -> 
            { // Expand to be byDayPairs days in current week
                List<Temporal> dates = new ArrayList<>();
                for (ByDayPair byDayPair : getByDayPairs())
                {
                    int value = byDayPair.dayOfWeek.getValue() + firstDayOfWeekAdjustment;
                    int valueAdj = (value > 7) ? value-7 : value;
                    Temporal newTemporal = t.with(field, valueAdj);
                    if (! DateTimeUtilities.isBefore(newTemporal, startTemporal)) dates.add(newTemporal);
                }
                Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            });
        case MONTHS:
            return inStream.flatMap(t -> 
            {
                List<Temporal> dates = new ArrayList<>();
                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getByDayPairs())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in month
                        sortNeeded = true;
                        Month myMonth = Month.from(t);
                        for (int weekNum=1; weekNum<=5; weekNum++)
                        {
                            Temporal newTemporal = t.with(TemporalAdjusters.dayOfWeekInMonth(weekNum, byDayPair.dayOfWeek));
                            if (Month.from(newTemporal) == myMonth && ! DateTimeUtilities.isBefore(newTemporal, startTemporal)) dates.add(newTemporal);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Month myMonth = Month.from(t);
                        Temporal newTemporal = t.with(TemporalAdjusters.dayOfWeekInMonth(byDayPair.ordinal, byDayPair.dayOfWeek));
                        if (Month.from(newTemporal) == myMonth) dates.add(newTemporal);
                    }
                }
                if (sortNeeded) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
                return dates.stream();
            });
        case YEARS:
            return inStream.flatMap(date -> 
            {
                List<Temporal> dates = new ArrayList<>();
                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getByDayPairs())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in year
                        sortNeeded = true;
                        Temporal newDate = (DayOfWeek.from(startTemporal).equals(byDayPair.dayOfWeek)) ? startTemporal
                                : startTemporal.with(TemporalAdjusters.next(byDayPair.dayOfWeek));
                        while (Year.from(newDate).equals(Year.from(startTemporal)))
                        {
                            dates.add(newDate);
                            newDate = newDate.plus(1, ChronoUnit.WEEKS);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Temporal newDate = date.with(dayOfWeekInYear(byDayPair.ordinal, byDayPair.dayOfWeek));
                        if (! DateTimeUtilities.isBefore(newDate, startTemporal)) dates.add(newDate);
                    }
                }
                if (sortNeeded) Collections.sort(dates, DateTimeUtilities.TEMPORAL_COMPARATOR);
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

    /** Finds nth occurrence of a week in a year.  Assumes ordinal is > 0 
     * Based on TemporalAdjusters.dayOfWeekInMonth */
    private TemporalAdjuster dayOfWeekInYear(int ordinal, DayOfWeek dayOfWeek)
    {
        int dowValue = dayOfWeek.getValue();
        return (temporal) -> {
            Temporal temp = temporal.with(TemporalAdjusters.firstDayOfYear());
            int curDow = temp.get(DAY_OF_WEEK);
            int dowDiff = (dowValue - curDow + 7) % 7;
            dowDiff += (ordinal - 1L) * 7L;  // safe from overflow
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
        private int ordinal = 0;
        public int getOrdinal() { return ordinal; }

        public ByDayPair(DayOfWeek dayOfWeek, int ordinal)
        {
            this.dayOfWeek = dayOfWeek;
            this.ordinal = ordinal;
        }
        
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
        
    }
    
    /** Match up iCalendar 2-character day of week to Java Time DayOfWeek */
    private enum ICalendarDayOfWeek
    {
        MO (DayOfWeek.MONDAY)
      , TU (DayOfWeek.TUESDAY)
      , WE (DayOfWeek.WEDNESDAY)
      , TH (DayOfWeek.THURSDAY)
      , FR (DayOfWeek.FRIDAY)
      , SA (DayOfWeek.SATURDAY)
      , SU (DayOfWeek.SUNDAY);
      
        private DayOfWeek dow;
      
        ICalendarDayOfWeek(DayOfWeek dow)
        {
          this.dow = dow;
        }
      
        public DayOfWeek getDayOfWeek() { return dow; }
    }

    public static ByRule parse(String dayPairs)
    {
        ByDay byRule = new ByDay();
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
                    DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(m2.group(2)).getDayOfWeek();
                    int ordinal = Integer.parseInt(m2.group(1));
                    dayPairsList.add(new ByDayPair(dayOfWeek, ordinal));
                }
            } else
            { // has no ordinal number
                DayOfWeek dayOfWeek = ICalendarDayOfWeek.valueOf(token).getDayOfWeek();
                dayPairsList.add(new ByDayPair(dayOfWeek, 0));
            }
        }
        byRule.byDayPairs = new ByDayPair[dayPairsList.size()];
        byRule.byDayPairs = dayPairsList.toArray(byRule.byDayPairs);
        return byRule;
    }
}
