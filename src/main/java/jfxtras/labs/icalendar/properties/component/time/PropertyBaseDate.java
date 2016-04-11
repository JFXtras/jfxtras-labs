package jfxtras.labs.icalendar.properties.component.time;

import java.time.LocalDate;

import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDate;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;

/**
 * Abstract class for all local-date classes
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DTStartLocalDate
 * @see DTEndLocalDate
 * @see RecurrenceIDLocalDate
 */
public abstract class PropertyBaseDate<U> extends PropertyBase<U, LocalDate> implements PropertyDateTime<LocalDate>
{
    public PropertyBaseDate(LocalDate temporal)
    {
        super(temporal);
    }

    public PropertyBaseDate(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseDate(PropertyBaseDate<U> source)
    {
        super(source);
    }
    
    @Override
    public void setValue(LocalDate value)
    {
        super.setValue(value);
        setValueParameter(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
    }
}
