package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

/** BYDAY from RFC 5545, iCalendar 3.3.10, page 40 */
public class ByDay extends ByRuleAbstract
{
    /** sorted array of days of month
     * (i.e. 5, 10 = 5th and 10th days of the month, -3 = 3rd from last day of month)
     * Uses a varargs parameter to allow any number of days
     * The list of days with ordinals must be sorted.  For example 1MO,2TU,4SA not 2TU,1MO,4SA
     */
    public ByDayPair[] getByDayPair() { return byDayPairs; }
    private ByDayPair[] byDayPairs;
    private void setByDayPair(ByDayPair... byDayPairs) { this.byDayPairs = byDayPairs; }

    // TODO - IS THIS A GOOD FIT HERE?
    // SHOULD I HAVE PROPERTIES OR SHOULD I HAVE A LISTENER SET THE VALUES?
    // I THINK THAT IF THERE ARE NO ORDINAL VALUES I SHOULD USE PROPERTIES
    final private Map<DayOfWeek, BooleanProperty> dayOfWeekMap = Arrays // Initialized map of all days of the week, each BooleanProperty is false
            .stream(DayOfWeek.values())
            .collect(Collectors.toMap(k -> k, v -> new SimpleBooleanProperty(false)));
    public Map<DayOfWeek, BooleanProperty> getDayOfWeekMap() { return dayOfWeekMap; }
    public void setDayOfWeek(DayOfWeek d, boolean value) { getDayOfWeekMap().get(d).set(value); }
    public boolean getDayOfWeek(DayOfWeek d) { return getDayOfWeekMap().get(d).get(); }
    public BooleanProperty getDayOfWeekProperty(DayOfWeek d) { return getDayOfWeekMap().get(d); }
//    public Repeat withDayOfWeek(DayOfWeek d, boolean value) { setDayOfWeek(d, value); return this; }
    private boolean dayOfWeekMapEqual(Map<DayOfWeek, BooleanProperty> dayOfWeekMap2) {
        Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
            .stream(DayOfWeek.values())
            .limit(7)
            .iterator();
        while (dayOfWeekIterator.hasNext())
        {
            DayOfWeek key = dayOfWeekIterator.next();
            boolean b1 = getDayOfWeekMap().get(key).get();
            boolean b2 = dayOfWeekMap2.get(key).get();
            if (b1 != b2) return false;
        }
        return true;
    }
    
    public ByDay(Frequency frequency, ByDayPair... byDayPairs)
    {
        super(frequency);
        setByDayPair(byDayPairs);
        setSortOrder(40);
    }

    /** Constructor that uses DayOfWeek values without a preceding integer.  All days of the 
     * provided types are included within the specified frequency */
    public ByDay(Frequency frequency, DayOfWeek... daysOfWeek)
    {
        super(frequency);
        setSortOrder(40);
        byDayPairs = new ByDayPair[daysOfWeek.length];
        int i=0;
        for (DayOfWeek d : daysOfWeek)
        {
            byDayPairs[i++] = new ByDayPair(d, 0);
        }
    }
    
    public ByDay() { } 
    
    @Override
    public void copyTo(Rule destination)
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
        boolean byDayPairsEquals = Arrays.equals(getByDayPair(), testObj.getByDayPair());
        System.out.println("ByDay equals " + byDayPairsEquals);
        return byDayPairsEquals;
    }
    
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        switch (getFrequency().getChronoUnit())
        {
        case DAYS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.filter(date ->
            { // filter out all but qualifying days
                DayOfWeek myDayOfWeek = date.toLocalDate().getDayOfWeek();
                for (ByDayPair byDayPair : getByDayPair())
                {
                    if (byDayPair.dayOfWeek == myDayOfWeek) return true;
                }
                return false;
            });
        case WEEKS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.flatMap(date -> 
            { // Expand to be byDayPairs days in current week
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                int dayOfWeekValue = date.toLocalDate().getDayOfWeek().getValue();
                for (ByDayPair byDayPair : getByDayPair())
                {
                    int dayShift = byDayPair.dayOfWeek.getValue() - dayOfWeekValue;
                    LocalDateTime newDate = date.plusDays(dayShift);
                    if (! newDate.isBefore(startDateTime)) dates.add(newDate);
                }
                return dates.stream();
            });
        case MONTHS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.flatMap(date -> 
            {
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getByDayPair())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in month
                        sortNeeded = true;
                        Month myMonth = date.getMonth();
                        for (int weekNum=1; weekNum<=5; weekNum++)
                        {
                            LocalDateTime newDate = date.with(TemporalAdjusters.dayOfWeekInMonth(weekNum, byDayPair.dayOfWeek));
                            if (newDate.getMonth() == myMonth && ! newDate.isBefore(startDateTime)) dates.add(newDate);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        Month myMonth = date.getMonth();
                        LocalDateTime newDate = date.with(TemporalAdjusters.dayOfWeekInMonth(byDayPair.ordinal, byDayPair.dayOfWeek));
                        if (newDate.getMonth() == myMonth) dates.add(newDate);
                    }
                }
                if (sortNeeded) Collections.sort(dates);
                return dates.stream();
            });
        case YEARS:
            getFrequency().setChronoUnit(DAYS);
            return inStream.flatMap(date -> 
            {
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                boolean sortNeeded = false;
                for (ByDayPair byDayPair : getByDayPair())
                {
                    if (byDayPair.ordinal == 0)
                    { // add every matching day of week in year
                        sortNeeded = true;
                        final int startWeekNumber;
                        if (date.getYear() == startDateTime.getYear())
                        {
                            WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
                            startWeekNumber = date.get(weekFields.weekOfWeekBasedYear());
                        } else
                        {
                            startWeekNumber = 1;
                        }
                        for (int weekNum=startWeekNumber; weekNum<53; weekNum++)
                        {
                            LocalDateTime newDate = date.with(dayOfWeekInYear(weekNum, byDayPair.dayOfWeek));
                            dates.add(newDate);
                        }
                    } else
                    { // if never any ordinal numbers then sort is not required
                        LocalDateTime newDate = date.with(dayOfWeekInYear(byDayPair.ordinal, byDayPair.dayOfWeek));
                        if (! newDate.isBefore(startDateTime)) dates.add(newDate);
                    }
                }
                if (sortNeeded) Collections.sort(dates);
                return dates.stream();
            }); 
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new RuntimeException("Not implemented ChronoUnit: " + getFrequency().getChronoUnit()); // probably same as DAILY
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
        DayOfWeek dayOfWeek;
        int ordinal = 0;
        public ByDayPair(DayOfWeek d, int i)
        {
            dayOfWeek = d;
            ordinal = i;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (obj == this) return true;
            if((obj == null) || (obj.getClass() != getClass())) {
                return false;
            }
            ByDayPair testObj = (ByDayPair) obj;
            System.out.println("ByDayPairequals " + (dayOfWeek == testObj.dayOfWeek)
                    + " " + (ordinal == testObj.ordinal));
            return (dayOfWeek == testObj.dayOfWeek)
                    && (ordinal == testObj.ordinal);
        }        
    }
}
