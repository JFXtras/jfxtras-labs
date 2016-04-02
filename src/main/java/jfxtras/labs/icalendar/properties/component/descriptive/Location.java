package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;

public class Location extends AlternateTextRepresentationProperty<Location, String>
{    
    public Location(String contentLine)
    {
        super(contentLine);
//        setValue(getPropertyValueString());
    }
    
    public Location(Location source)
    {
        super(source);
    }
    
    public Location()
    {
        super();
    }
}
