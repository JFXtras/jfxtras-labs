package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class Version extends PropertyBase<Version, String>
{
    public Version(String propertyString)
    {
        super(propertyString);
    }
    
    public Version(Version source)
    {
        super(source);
    }
    
    public Version()
    {
        super();
    }
}
