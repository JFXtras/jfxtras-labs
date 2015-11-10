package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.Frequency;

/** BYMONTHDAY from RFC 5545, iCalendar */
public class ByMonthDay extends ByRuleAbstract
{

    private int[] daysOfMonth; // sorted array of days of month to apply the rule (i.e. 5, 10 = 5th and 10th days of the month)

    // Constructor
    public ByMonthDay(Frequency frequency, int... daysOfMonth) {
        super(frequency);
        this.daysOfMonth = daysOfMonth;
    }

//    // TODO - SHOULD I PASS FREQUENCY IN ITS ENTIRITY?
//    public ByMonthDay(LocalDateTime startLocalDateTime
//            , Stream<LocalDateTime> inStream
//            , FrequencyEnum frequencyRule
//            , int... daysOfMonth)
//    {
//        super(startLocalDateTime, inStream, frequencyRule);
//        this.daysOfMonth = daysOfMonth;
//    }

//    final private BooleanProperty repeatDayOfMonth = new SimpleBooleanProperty(this, "dayOfMonth", true); // default option
//    protected Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
//    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
//    private void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }

    @Override
    public Stream<LocalDateTime> stream()
    { // infinite stream of valid dates filtered by rule
        switch (getFrequency().frequencyEnum())
        {
        case DAILY:
            break;
        case MONTHLY:
            int startMonth = getFrequency().getStartLocalDateTime().toLocalDate().getMonthValue();
//            Stream<LocalDateTime> s1 = 
            // Below stream works, but its not efficient.  Try to find a better approach.
            return Stream.iterate(getFrequency().getStartLocalDateTime(), (d) -> { return d.plusDays(1); }) // stream of all days starting at startLocalDateTime
                    .filter(d ->
                    { // remove all but qualifying days
                        int myMonth = d.toLocalDate().getMonthValue();
//                        System.out.println((myMonth - startMonth) + " " + getFrequency().getInterval() + " " + (myMonth - startMonth) % getFrequency().getInterval());
                        if ((myMonth - startMonth) % getFrequency().getInterval() != 0) return false; // false if wrong month
                        int myDay = d.toLocalDate().getDayOfMonth();
                        int daysInMonth = d.toLocalDate().lengthOfMonth();
                        for (int day : daysOfMonth)
                        {
//                            System.out.println("match day " + myDay);
                            if (myDay == day) return true;
                            if ((day < 0) && (myDay == daysInMonth - day)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
                        }
                        return false;
                    });

//            Stream<LocalDateTime> stream = null;
//            LocalDateTime startDateTime = getFrequency().getStartLocalDateTime();
//            for (int days : daysOfMonth)
//            {                
//                LocalDateTime adjustedDateTime = (days > 0) ?
//                        startDateTime.with(TemporalAdjusters.firstDayOfMonth()).plusDays(days-1)
//                      : startDateTime.with(TemporalAdjusters.lastDayOfMonth()).minusDays(days-1);
//                if (adjustedDateTime.isBefore(startDateTime)) continue; // skip too early dates
//                Stream<LocalDateTime> s1 = Stream.iterate(adjustedDateTime, (a) -> { return a.with(new NextAppointment()); });
//                if (stream == null)
//                {
//                    stream = s1;
//                } else
//                {
//                    stream = ByRule.mergeStream(stream, s1);
//                }
//            }
//            return stream;
        case WEEKLY:
            break;
        case YEARLY:
            break;
        default:
            break;
        }
//        return getFrequency().stream().filter(d ->
//        {
//            int myDay = d.getDayOfMonth();
//            int daysInMonth = d.toLocalDate().lengthOfMonth();
//            for (int day : daysOfMonth)
//            {
//                if (myDay == day) return true;
//                if (myDay == daysInMonth - day) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
//            }
//            return false;
//        });    
        return null;
    }

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
//            LocalDate inputDate = LocalDate.from(temporal);
//            Month m = LocalDate.from(temporal).getMonth();
            int plusDays;
            int currentDay = LocalDate.from(temporal).getDayOfMonth();
            int lastDay = daysOfMonth[daysOfMonth.length-1];
            if (currentDay == lastDay)
            { // skip interval-1 months if on last day of months day array
                int monthsSkipped = getFrequency().getInterval()-1;
                temporal = temporal.plus(Period.ofMonths(monthsSkipped));
                plusDays = daysOfMonth[0] - currentDay;
            } else
            {
                for (int day : daysOfMonth)
                {    
                    if (day > currentDay) plusDays = day;
                }
            }
            
            
//            for (int i=0; i<getFrequency().getInterval(); i++)
//            { // loop that counts number of valid dates for total time interval (repeatFrequency)
//                temporal = temporal.with(new TemporalAdjuster()
//                { // anonymous inner class that finds next valid date
//                    @Override
//                    public Temporal adjustInto(Temporal temporal)
//                    {
//                        for (int days : daysOfMonth)
//                        {    
//                            
//                        }
//                        return temporal.plus(Period.ofMonths(1));
//                    }
//                });
//            }; // end of looping anonymous inner class
        return temporal;
        }
    }

//    @Override
//    public FrequencyRuleEnum getFrequency() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
