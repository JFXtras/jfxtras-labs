package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule;

import java.security.InvalidParameterException;
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
    private int[] validDays;
    
    // Constructor
    public ByMonthDay(Frequency frequency, int... daysOfMonth) {
        super(frequency);
        this.daysOfMonth = daysOfMonth;
        int daysInMonth = getFrequency().getStartLocalDateTime().toLocalDate().lengthOfMonth();
        validDays = makeValidDays(daysInMonth, daysOfMonth);
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

    public static int[] makeValidDays(int daysInMonth, int[] daysOfMonth)
    {
        int[] validDays = new int[daysOfMonth.length];
        int i=0;
        for (int day : daysOfMonth)
        {
            if (day == 0 || day < -31 || day > 31) throw new InvalidParameterException("Invalid BYMONTHDAY value (" + day + ").  Must be 1 to 31 or -31 to -1.");
            if (day > 0)
            {
                validDays[i] = day;
            } else
            {
                validDays[i] = (daysInMonth + day + 1); // negative daysOfMonth (-3 = 3rd to last day of month)                
            }
            i++;
        }
        return validDays;
    }
    
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
//            return Stream.iterate(getFrequency().getStartLocalDateTime(), (d) -> { return d.plusDays(1); }) // stream of all days starting at startLocalDateTime
//                    .filter(d ->
//                    { // remove all but qualifying days
//                        int myMonth = d.toLocalDate().getMonthValue();
//                        if ((myMonth - startMonth) % getFrequency().getInterval() != 0) return false; // false if wrong month
//                        int myDay = d.toLocalDate().getDayOfMonth();
//                        int daysInMonth = d.toLocalDate().lengthOfMonth();
//                        for (int day : daysOfMonth)
//                        {
////                            System.out.println("match day " + myDay);
//                            if (myDay == day) return true;
//                            if ((day < 0) && (myDay == daysInMonth - day)) return true; // negative daysOfMonth (-3 = 3rd to last day of month)
//                        }
//                        return false;
//                    });
            
            return Stream.iterate(getFrequency().getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });

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
            int dayShift = 0;
            int myDay = LocalDate.from(temporal).getDayOfMonth();
            if (myDay == validDays[validDays.length-1])
            { // if last day in validDays then skip months and make new array of validDays
                int interval = getFrequency().getInterval();
                temporal = temporal.plus(Period.ofMonths(interval)); // adjust month
                validDays = makeValidDays(LocalDate.from(temporal).lengthOfMonth(), daysOfMonth);
                dayShift = validDays[0] - myDay;
            } else
            { // get next day in validDays array
                for (int day : validDays)
                {
                    if (day > myDay)
                    {
                        dayShift = day - myDay;
                        break;
                    }
                }
            }
            return temporal.plus(Period.ofDays(dayShift)); // adjust day
        }
    }

//    @Override
//    public FrequencyRuleEnum getFrequency() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
