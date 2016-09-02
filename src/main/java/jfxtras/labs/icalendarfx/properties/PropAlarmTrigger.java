package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship;

public interface PropAlarmTrigger<T> extends Property<T>
{
    AlarmTriggerRelationship getAlarmTrigger();
    ObjectProperty<AlarmTriggerRelationship> AlarmTriggerProperty();
    void setAlarmTrigger(AlarmTriggerRelationship AlarmTrigger);
}
