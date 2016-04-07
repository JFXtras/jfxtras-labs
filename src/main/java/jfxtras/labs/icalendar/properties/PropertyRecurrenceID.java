package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.Range;

public interface PropertyRecurrenceID<T> extends Property<T>
{
    Range getRange();
    ObjectProperty<Range> rangeProperty();
    void setRange(Range range);
}
