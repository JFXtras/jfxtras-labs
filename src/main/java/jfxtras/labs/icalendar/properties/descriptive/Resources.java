package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.VComponentProperty;

public class Resources extends TextPropertyAbstract<Comment>
{
    public Resources(String contentLine)
    {
        super(contentLine, VComponentProperty.RESOURCES.toString());
    }
}
