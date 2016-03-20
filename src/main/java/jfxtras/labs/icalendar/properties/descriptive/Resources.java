package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.ComponentProperty;

public class Resources extends TextPropertyAbstract<Comment>
{
    private final static String NAME = ComponentProperty.RESOURCES.toString();

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
