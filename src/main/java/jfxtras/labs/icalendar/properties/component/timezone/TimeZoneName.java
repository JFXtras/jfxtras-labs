package jfxtras.labs.icalendar.properties.component.timezone;

import jfxtras.labs.icalendar.properties.PropertyLanguage;

public class TimeZoneName extends PropertyLanguage<TimeZoneName, String>
{
    public TimeZoneName(String contentLine)
    {
        super(contentLine);
//        setValue(getPropertyValueString());
    }
    
    public TimeZoneName(TimeZoneName source)
    {
        super(source);
    }
    
    public TimeZoneName()
    {
        super();
    }
}
