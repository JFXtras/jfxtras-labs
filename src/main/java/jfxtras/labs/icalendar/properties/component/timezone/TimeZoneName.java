package jfxtras.labs.icalendar.properties.component.timezone;

import jfxtras.labs.icalendar.components.StandardTime;
import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;

/**
 * TZNAME
 * Time Zone Name
 * RFC 5545, 3.8.3.2, page 103
 * 
 * This property specifies the customary designation for a time zone description.
 * 
 * EXAMPLES:
 * TZNAME:EST
 * TZNAME;LANGUAGE=fr-CA:HN
 * 
 * @author David Bal
 * @see DaylightSavingsTime
 * @see StandardTime
 */
public class TimeZoneName extends PropertyBaseLanguage<String, TimeZoneName>
{
    public TimeZoneName(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public TimeZoneName(TimeZoneName source)
    {
        super(source);
    }
}
