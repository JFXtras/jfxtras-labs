package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.VComponentProperty;

public class Location extends TextPropertyAbstract<Comment>
{    
    public Location(String contentLine)
    {
        super(contentLine, VComponentProperty.LOCATION.toString());
    }
}
