package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.ComponentProperty;

public class Location extends TextPropertyAbstract<Comment>
{    
    private final static String NAME = ComponentProperty.LOCATION.toString();

    public Location(String contentLine)
    {
        super(NAME, contentLine);
    }
    
    public Location(Location location)
    {
        super(location);
    }
    
    public Location() { super(NAME); }
}
