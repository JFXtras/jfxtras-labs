package jfxtras.labs.icalendar.properties.component.time;

import java.time.LocalDate;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Abstract class for all local-date classes
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see DTStartLocalDate
 * @see DTEndLocalDate
 */
public abstract class DateAbstract<T> extends PropertyBase<T, LocalDate> implements DateTime<LocalDate>
{
    public DateAbstract(LocalDate temporal)
    {
        super(temporal);
    }

    public DateAbstract(String propertyString)
    {
        super(propertyString);
    }
    
    public DateAbstract(DateAbstract<T> source)
    {
        super(source);
    }
    
//    public PropertyDate()
//    {
//        super();
//    }
    
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
