package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.properties.component.time.PropertyBaseDateTime2;

public class DateTimeStart<T extends Temporal> extends PropertyBaseDateTime2<DateTimeStart<T>, T>
{
   public DateTimeStart(T temporal)
    {
        super(temporal);
    }

    public DateTimeStart(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public DateTimeStart(DateTimeStart<T> source)
    {
        super(source);
    }
}
