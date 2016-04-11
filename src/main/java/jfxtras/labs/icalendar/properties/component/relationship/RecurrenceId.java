package jfxtras.labs.icalendar.properties.component.relationship;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Range;
import jfxtras.labs.icalendar.parameters.Range.RangeType;
import jfxtras.labs.icalendar.properties.PropertyBaseDateTime;
import jfxtras.labs.icalendar.properties.PropertyRecurrenceID;

public class RecurrenceId<T extends Temporal> extends PropertyBaseDateTime<T, RecurrenceId<T>> implements PropertyRecurrenceID<T>
{
    /**
     * RANGE
     * Recurrence Identifier Range
     * RFC 5545, 3.2.13, page 23
     * 
     * To specify the effective range of recurrence instances from
     *  the instance specified by the recurrence identifier specified by
     *  the property.
     * 
     * Example:
     * RECURRENCE-ID;RANGE=THISANDFUTURE:19980401T133000Z
     * 
     * @author David Bal
     *
     */
    @Override
    public Range getRange() { return (range == null) ? null : range.get(); }
    @Override
    public ObjectProperty<Range> rangeProperty()
    {
        if (range == null)
        {
            range = new SimpleObjectProperty<>(this, ParameterEnum.RECURRENCE_IDENTIFIER_RANGE.toString());
        }
        return range;
    }
    private ObjectProperty<Range> range;
    @Override
    public void setRange(Range range)
    {
        if (range != null)
        {
            rangeProperty().set(range);
        }
    }
    public void setRange(String value) { setRange(new Range(value)); }
    public RecurrenceId<T> withRange(Range altrep) { setRange(altrep); return this; }
    public RecurrenceId<T> withRange(RangeType value) { setRange(new Range(value)); return this; }
    public RecurrenceId<T> withRange(String content) { setRange(content); return this; }

   public RecurrenceId(T temporal)
    {
        super(temporal);
    }

    public RecurrenceId(Class<T> clazz, CharSequence contentLine)
    {
        super(clazz, contentLine);
    }
    
    public RecurrenceId(RecurrenceId<T> source)
    {
        super(source);
    }
}
