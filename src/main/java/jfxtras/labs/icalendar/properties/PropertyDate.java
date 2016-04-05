package jfxtras.labs.icalendar.properties;

import java.time.LocalDate;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public abstract class PropertyDate<T> extends PropertyBase<T, LocalDate>
{
    public PropertyDate(LocalDate temporal)
    {
        super(temporal);
    }

    public PropertyDate(String propertyString)
    {
        super(propertyString);
    }
    
    public PropertyDate(PropertyDate<T> source)
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
