package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * @author David Bal
 *
 */
public abstract class ByRuleAbstract implements Rule {

    /** Original stream of date/times before modification */
//    Stream<LocalDateTime> getInStream() { return inStream; }
//    private Stream<LocalDateTime> inStream;

    public Frequency getFrequency() { return frequency; }
    private Frequency frequency;
        
    // Constructor
    ByRuleAbstract(Frequency frequency)
    {
        this.frequency = frequency;
    }    


}
