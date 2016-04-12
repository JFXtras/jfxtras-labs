package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.AlarmTriggerRelationship;

public interface PropertyTrigger<T> extends Property<T>
{
    AlarmTriggerRelationship getRelationship();
    ObjectProperty<AlarmTriggerRelationship> RelationshipProperty();
    void setRelationship(AlarmTriggerRelationship AlarmTrigger);
}
