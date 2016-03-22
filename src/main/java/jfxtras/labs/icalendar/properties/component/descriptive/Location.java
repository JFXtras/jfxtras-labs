package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;
import jfxtras.labs.icalendar.properties.PropertyType;

public class Location extends PropertyTextBase3<Location>
{    
    private final static String NAME = PropertyType.LOCATION.toString();

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
