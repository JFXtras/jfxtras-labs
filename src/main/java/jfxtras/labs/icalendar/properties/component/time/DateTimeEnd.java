package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
import jfxtras.labs.icalendar.properties.PropertyBase;

public class DateTimeEnd extends PropertyBase<DateTimeEnd, Temporal>
{
    public DateTimeEnd(Temporal temporal)
    {
        super();
        setValue(temporal);
    }

    public DateTimeEnd(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeEnd(DateTimeEnd source)
    {
        super(source);
    }
    
    public DateTimeEnd()
    {
        super();
    }    
    
    @Override
    protected List<ValueEnum> allowedValueTypes()
    {
        return Arrays.asList(ValueEnum.DATE, ValueEnum.DATE_TIME);
    }

}
