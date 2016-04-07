package jfxtras.labs.icalendar.properties.component.misc;

public class NonStandardProperty extends UnknownProperty<NonStandardProperty, Object>
{
    public NonStandardProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public NonStandardProperty(Object value)
    {
        super(value);
    }
    
    public NonStandardProperty(NonStandardProperty source)
    {
        super(source);
    }
}
