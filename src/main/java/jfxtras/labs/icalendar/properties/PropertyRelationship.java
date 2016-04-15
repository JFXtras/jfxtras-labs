package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.Relationship;

public interface PropertyRelationship<T> extends Property<T>
{
    Relationship getRelationship();
    ObjectProperty<Relationship> relationshipProperty();
    void setRelationship(Relationship relationship);
}
