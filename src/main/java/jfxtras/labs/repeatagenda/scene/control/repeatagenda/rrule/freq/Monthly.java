package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;

/** Repeat rule for frequency of Monthly */
public class Monthly extends FrequencyAbstract {   
        
//    private ByRule defaultRule = new ByMonthDay(this);
    
    // Constructor
    public Monthly(LocalDateTime startLocalDateTime)
    {
        super(startLocalDateTime);
    }
    
    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.MONTHLY; }
        
    @Override
    public Stream<LocalDateTime> stream()
    { // infinite stream of valid dates
        if (getByRules().isEmpty())
        { // if no rules, return stream repeating monthly on day of month matching startLocalDateTime
            System.out.println("empty rules, using default");
            return new ByMonthDay(this).stream(); // default is repeats once a month on start date/time
        } else
        {
            System.out.println("processing rules");
            Stream<LocalDateTime> s = new ByMonthDay(this).stream();
            for (ByRule r : getByRules())
            {
                s = r.stream(s);
            }
            return s;
        }
        // TODO - apply byRules one at a time
    }



//    /** adjusts temporal parameter to become date/time of next event */
//    private class NextAppointment implements TemporalAdjuster
//    {
//        @Override
//        public Temporal adjustInto(Temporal temporal)
//        {
//            for (int i=0; i<getInterval(); i++)
//            { // loops through interval
//                temporal = temporal.with(new TemporalAdjuster()
//                { // anonymous inner class that finds next valid date
//                    @Override
//                    public Temporal adjustInto(Temporal temporal)
//                    {
//                        return temporal.plus(Period.ofMonths(1));
//                    }
//                });
//            }; // end of looping anonymous inner class
//        return temporal;
//        }
//    }
    
}
