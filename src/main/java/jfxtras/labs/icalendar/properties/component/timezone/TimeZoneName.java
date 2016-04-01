package jfxtras.labs.icalendar.properties.component.timezone;

import jfxtras.labs.icalendar.properties.LanguageBase;

public class TimeZoneName extends LanguageBase<TimeZoneName, String>
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
