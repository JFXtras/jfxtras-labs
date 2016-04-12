package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;

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
public class AlarmTriggerRelationship extends ParameterBase<AlarmTriggerRelationship, AlarmTriggerRelationshipType>
{
    /** Set START as default value */
    public AlarmTriggerRelationship()
    {
        super(AlarmTriggerRelationshipType.START);
    }

    public AlarmTriggerRelationship(AlarmTriggerRelationshipType value)
    {
        super(value);
    }

    public AlarmTriggerRelationship(String content)
    {
        super(AlarmTriggerRelationshipType.valueOf(content.toUpperCase()));
    }

    public AlarmTriggerRelationship(AlarmTriggerRelationship source)
    {
        super(source);
    }
    
    public enum AlarmTriggerRelationshipType
    {
        START,
        END;
    }
}
