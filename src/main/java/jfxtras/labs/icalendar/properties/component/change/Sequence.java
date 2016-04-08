package jfxtras.labs.icalendar.properties.component.change;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class Sequence extends PropertyBase<Sequence, Integer>
{
    public Sequence(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }

    public Sequence(Integer value)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(value, null);
    }
    
    public Sequence(Sequence source)
    {
        super(source);
    }
}
