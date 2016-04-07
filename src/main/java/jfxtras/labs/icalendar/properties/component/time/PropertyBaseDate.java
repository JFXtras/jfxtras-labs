package jfxtras.labs.icalendar.properties.component.time;

import java.time.LocalDate;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDate;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

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
    
    @Override
    protected LocalDate valueFromString(String propertyValueString)
    {
        return LocalDate.parse(propertyValueString, DateTimeUtilities.LOCAL_DATE_FORMATTER);
    }
    
    @Override
    protected String valueToString(LocalDate value)
    {
        return DateTimeUtilities.LOCAL_DATE_FORMATTER.format(value);  
    }
}
