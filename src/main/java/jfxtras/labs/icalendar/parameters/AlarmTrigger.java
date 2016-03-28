package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.AlarmTrigger.AlarmTriggerRelationship;

public class AlarmTrigger extends ParameterBase<AlarmTrigger, AlarmTriggerRelationship>
{    
    public enum AlarmTriggerRelationship
    {
        START,
        END;
    }
}
