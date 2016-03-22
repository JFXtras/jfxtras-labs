package jfxtras.labs.icalendar.properties.component.timezone;

import jfxtras.labs.icalendar.properties.PropertyTextBase2;
import jfxtras.labs.icalendar.properties.PropertyType;

public class TimeZoneName extends PropertyTextBase2<TimeZoneName>
{
    private final static String NAME = PropertyType.TIME_ZONE_NAME.toString();
    
    /*
     * CONSTRUCTORS
     */
    public TimeZoneName(String propertyString)
    {
        super(NAME, propertyString);
    }
}
