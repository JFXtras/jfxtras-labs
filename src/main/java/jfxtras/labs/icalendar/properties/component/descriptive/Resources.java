package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;
import jfxtras.labs.icalendar.properties.PropertyType;

public class Resources extends PropertyTextBase3<Resources>
{
    private final static String NAME = PropertyType.RESOURCES.toString();

    public Resources(String contentLine)
    {
        super(NAME, contentLine);
    }
    
    public Resources(Resources resources)
    {
        super(resources);
    }
    
    public Resources() { super(NAME); }
}
