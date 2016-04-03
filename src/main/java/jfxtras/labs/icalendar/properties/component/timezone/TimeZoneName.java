package jfxtras.labs.icalendar.properties.component.timezone;

import jfxtras.labs.icalendar.properties.LanguageProperty;

public class TimeZoneName extends LanguageProperty<TimeZoneName, String>
{
    public TimeZoneName(String contentLine)
    {
        super(contentLine, null);
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
