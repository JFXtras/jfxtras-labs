package jfxtras.labs.icalendar.properties.component.change;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class Sequence extends PropertyBase<Sequence, Integer>
{
    public Sequence(CharSequence contentLine)
    {
        super(contentLine);
    }

    public Sequence(Integer value)
    {
        super(value);
    }
    
    public Sequence(Sequence source)
    {
        super(source);
    }
}
