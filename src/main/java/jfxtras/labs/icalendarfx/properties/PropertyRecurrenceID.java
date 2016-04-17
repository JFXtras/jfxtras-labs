package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.parameters.Range;

public interface PropertyRecurrenceID<T> extends Property<T>
{
    Range getRange();
    ObjectProperty<Range> rangeProperty();
    void setRange(Range range);
}
