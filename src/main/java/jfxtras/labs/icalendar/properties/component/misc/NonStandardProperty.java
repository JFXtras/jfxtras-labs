package jfxtras.labs.icalendar.properties.component.misc;

// TODO - NEED TO CHEKC FOR X- PREFEX
// TODO - NEED TO ALLOW ANY VALUE TYPE
public class NonStandardProperty extends UnknownProperty<Object, NonStandardProperty>
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
    
    @Override
    public boolean isValid()
    {
        if (! getPropertyName().substring(0, 2).equals("X-"))
        {
            return false;
        }
        return super.isValid();
    }
}
