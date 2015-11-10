package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.Frequency;

public abstract class ByRuleAbstract implements ByRule {

//    /** Start date/time of repeat rule */
//    private LocalDateTime startLocalDateTime;
//    public void setStartLocalDateTime(LocalDateTime s) { startLocalDateTime = s; }
//    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime; }

//    /** Original stream of date/times before modification */
//    public Stream<LocalDateTime> getInStream() { return inStream; }
//    public void setInStream(Stream<LocalDateTime> inStream) { this.inStream = inStream; }
//    private Stream<LocalDateTime> inStream;
//    
//    private FrequencyEnum frequency;
//    public void setFrequency(FrequencyEnum frequency) { this.frequency = frequency; }
    
    private Frequency frequency;
    @Override public Frequency getFrequency() { return frequency; }
    
    
    private Stream<LocalDateTime> outStream;
    public Stream<LocalDateTime> stream() { return outStream; }
    
//    private ByRuleAbstract() { }
    
//    // Constructor
//    public ByRuleAbstract(LocalDateTime startLocalDateTime, Stream<LocalDateTime> inStream, FrequencyEnum frequency)
//    {
//        setStartLocalDateTime(startLocalDateTime);
//        setInStream(inStream);
//        setFrequency(frequency);
//    }
    
    public ByRuleAbstract(Frequency frequency)
    {
        this.frequency = frequency;
//        setStartLocalDateTime(startLocalDateTime);
//        setInStream(inStream);
    }

}
