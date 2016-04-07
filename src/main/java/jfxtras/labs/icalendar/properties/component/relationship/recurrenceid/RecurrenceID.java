package jfxtras.labs.icalendar.properties.component.relationship.recurrenceid;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.Range;

public interface RecurrenceID
{
    Range getRange();
    ObjectProperty<Range> rangeProperty();
    void setRange(Range range);
}
