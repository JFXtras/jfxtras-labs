package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.AlarmTrigger.AlarmTriggerRelationship;

public class AlarmTrigger extends ParameterBase<AlarmTrigger, AlarmTriggerRelationship>
{
//    @Override
//    public AlarmTriggerRelationship getValue() { return value.get(); }
//    @Override
//    public ObjectProperty<AlarmTriggerRelationship> valueProperty() { return value; }
//    private ObjectProperty<AlarmTriggerRelationship> value = new SimpleObjectProperty<>(this, ParameterEnum.ALARM_TRIGGER_RELATIONSHIP.toString());
//    public void setValue(AlarmTriggerRelationship value) { this.value.set(value); }
    
//    @Override
//    public void copyTo(Property source, Property destination)
//    {
//        AlarmTrigger castSource = (AlarmTrigger) source;
//        AlarmTrigger castDestinateion = (AlarmTrigger) destination;
//        castDestinateion.setValue(castSource.getValue());
//    }
    
    public enum AlarmTriggerRelationship
    {
        START,
        END;
        
    }
}
