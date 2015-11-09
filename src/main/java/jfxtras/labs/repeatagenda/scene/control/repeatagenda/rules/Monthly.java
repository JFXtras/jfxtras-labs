package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.byrules.ByRule;

/** Repeat rule for frequency of Monthly */
public class Monthly implements FrequencyRule {

    /** Start date/time of repeat rule */
    private LocalDateTime startLocalDateTime;
    private int interval;
    
    public Monthly(LocalDateTime startLocalDateTime, int interval)
    {
        this.startLocalDateTime = startLocalDateTime;
        this.interval = interval;
    }
    
    @Override
    public Stream<LocalDateTime> stream()
    { // infinite stream of valid dates
        return Stream.iterate(startLocalDateTime, (a) -> { return a.with(new NextAppointment()); });
    }

    @Override
    public Collection<ByRule> getByRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setByRules(Collection<ByRule> c) {
        // TODO Auto-generated method stub
        
    }
    
    final private BooleanProperty repeatDayOfMonth = new SimpleBooleanProperty(this, "dayOfMonth", true); // default option
    protected Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
    private void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }

    final private BooleanProperty repeatDayOfWeek = new SimpleBooleanProperty(this, "dayOfWeek", false);
    protected Boolean isRepeatDayOfWeek() { return repeatDayOfWeek.getValue(); }
    public BooleanProperty repeatDayOfWeekProperty() { return repeatDayOfWeek; }
    private void setRepeatDayOfWeek(Boolean repeatDayOfWeek) { this.repeatDayOfWeek.set(repeatDayOfWeek); }
    private int ordinal; // used when repeatDayOfWeek is true, this is the number of weeks into the month the date is set (i.e 3rd Wednesday -> ordinal=3).
    
    public MonthlyRepeat getMonthlyRepeat()
    { // returns MonthlyRepeat enum from boolean properties
        if (isRepeatDayOfMonth()) return MonthlyRepeat.DAY_OF_MONTH;
        if (isRepeatDayOfWeek()) return MonthlyRepeat.DAY_OF_WEEK;
        return null; // should not get here
    }
    public void setMonthlyRepeat(MonthlyRepeat monthlyRepeat)
    { // sets boolean properties from MonthlyRepeat
        switch (monthlyRepeat)
        {
        case DAY_OF_MONTH:
            setRepeatDayOfMonth(true);
            setRepeatDayOfWeek(false);
            break;
        case DAY_OF_WEEK:
            setRepeatDayOfMonth(false);
            setRepeatDayOfWeek(true);
            DayOfWeek dayOfWeek = startLocalDateTime.getDayOfWeek();
            LocalDateTime myDay = startLocalDateTime
                    .with(TemporalAdjusters.firstDayOfMonth())
                    .with(TemporalAdjusters.next(dayOfWeek));
            ordinal = 0;
            if (dayOfWeek == myDay.getDayOfWeek()) ordinal++; // add one if first day of month is correct day of week
            while (! myDay.isAfter(startLocalDateTime))
            { // set ordinal number for day-of-week repeat
                ordinal++;
                myDay = myDay.with(TemporalAdjusters.next(dayOfWeek));
            }
        }
    }
    public FrequencyRule withMonthlyRepeat(MonthlyRepeat monthlyRepeat) { setMonthlyRepeat(monthlyRepeat); return this; }

    /**
     * Adjust date to become next date based on the Repeat rule.  Needs a input temporal on a valid date.
     * 
     * @return
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
//            final TemporalField weekOfYear;
//            final int initialWeek;
//            int currentWeek = 0;
//            if (getFrequency() == Frequency.WEEKLY)
//            {
//                weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
//                initialWeek = LocalDate.from(temporal).get(weekOfYear);
//                currentWeek = initialWeek;
//            } else { // variables not used in not a WEEKLY repeat, but still must be initialized
//                weekOfYear = null;
//                initialWeek = -1;
//            }
            int i=0;
            do
            { // loop that counts number of valid dates for total time interval (repeatFrequency)
                temporal = temporal.with(new TemporalAdjuster()
                { // anonymous inner class that finds next valid date
                    @Override
                    public Temporal adjustInto(Temporal temporal)
                    {
//                        LocalDate inputDate = LocalDate.from(temporal);
//                        switch (getFrequency())
//                        {
//                        case DAILY:
//                            return temporal.plus(Period.ofDays(1));
//                        case WEEKLY:
//                            final DayOfWeek d1 = inputDate.plusDays(1).getDayOfWeek();
////                            System.out.println("d1 " + d1);
//                            final Iterator<DayOfWeek> daysIterator = Stream
//                                .iterate(d1, (a) ->  a.plus(1))              // infinite stream of valid days of the week
//                                .limit(7)                                    // next valid day should be found within 7 days
//                                .iterator();
//                            while (daysIterator.hasNext()) {
//                                DayOfWeek d = daysIterator.next();
//                                if (getDayOfWeek(d)) return temporal.with(TemporalAdjusters.next(d));
//                            }
//                            return temporal; // only happens if no day of the week are selected (true)
//                            case MONTHLY:
                                switch (getMonthlyRepeat())
                                {
                                case DAY_OF_MONTH:
                                    return temporal.plus(Period.ofMonths(1));
                                case DAY_OF_WEEK:
                                    DayOfWeek dayOfWeek = startLocalDateTime.getDayOfWeek();
                                    return temporal.plus(Period.ofMonths(1))
                                            .with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek));
                                default:
                                    return temporal;
                                }
//                        case YEARLY:
//                            return temporal.plus(Period.ofYears(1));
//                        default:
//                            return temporal;
//                        }
                    }
                }); // end of anonymous inner TemporalAdjuster class
                // Increment repeat frequency counter
//                if (getFrequency() == Frequency.WEEKLY)
//                { // increment counter for weekly repeat when week number changes
//                    int newWeekNumber = LocalDate.from(temporal).get(weekOfYear);
//                    if (newWeekNumber == initialWeek) return temporal; // return new temporal if still in current week (assumes temporal starts on valid date)
//                    if (newWeekNumber != currentWeek)
//                    {
//                        currentWeek = newWeekNumber;
//                        i++;
//                    }
//                } else
//                { // all other IntervalUnit types (not WEEKLY) increment counter i for every cycle of anonymous inner class TemporalAdjuster
                    i++;
//                }
            } while (i < interval); // end of while looping anonymous inner class
        return temporal;
        }
    }
    
    public enum MonthlyRepeat {
        DAY_OF_MONTH, DAY_OF_WEEK;
    }
    
}
