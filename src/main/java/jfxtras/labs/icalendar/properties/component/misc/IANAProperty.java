package jfxtras.labs.icalendar.properties.component.misc;

/**
 * 
 * contains all parameters
 * 
 * @author David Bal
 *
 */
// TODO - DO I WANT TO MAKE INTERFACES FOR PARAMETERS?
public class IANAProperty extends UnknownProperty<IANAProperty, Object>
{    
    public IANAProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public IANAProperty(Object value)
    {
        super(value);
    }
    
    public IANAProperty(IANAProperty source)
    {
        super(source);
    }
}
