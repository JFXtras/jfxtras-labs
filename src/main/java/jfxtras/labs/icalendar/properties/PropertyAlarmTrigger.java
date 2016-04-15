package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.AlarmTriggerRelationship;

public interface PropertyAlarmTrigger<T> extends Property<T>
{
    AlarmTriggerRelationship getAlarmTrigger();
    ObjectProperty<AlarmTriggerRelationship> AlarmTriggerProperty();
    void setAlarmTrigger(AlarmTriggerRelationship AlarmTrigger);
}
