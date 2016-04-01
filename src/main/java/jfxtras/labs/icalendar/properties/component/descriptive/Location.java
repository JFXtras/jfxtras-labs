package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

public class Location extends AlternateTextRepresentationBase<Location, String>
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
