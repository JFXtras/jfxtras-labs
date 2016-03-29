package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.AlarmTrigger.AlarmTriggerRelationship;

/**
 * RELATED
 * Alarm Trigger Relationship
 * RFC 5545, 3.2.14, page 24
 * 
 * To specify the relationship of the alarm trigger with
 *  respect to the start or end of the calendar component.
 * 
 * Example:
 * TRIGGER;RELATED=END:PT5M
 * 
 * @author David Bal
 *
 */
public class AlarmTrigger extends ParameterBase<AlarmTrigger, AlarmTriggerRelationship>
{
    public AlarmTrigger()
    {
        super();
    }
  
    public AlarmTrigger(String content)
    {
        super(content);
    }

    public AlarmTrigger(AlarmTrigger source)
    {
        super(source);
    }
    
    public enum AlarmTriggerRelationship
    {
        START,
        END;
    }
}
