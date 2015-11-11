package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

public abstract class ByRuleAbstract implements ByRule {

    /** Original stream of date/times before modification */
//    Stream<LocalDateTime> getInStream() { return inStream; }
//    private Stream<LocalDateTime> inStream;

    @Override public Frequency getFrequency() { return frequency; }
    private Frequency frequency;
        
    // Constructor
    ByRuleAbstract(Frequency frequency)
    {
        this.frequency = frequency;
    }    


}
