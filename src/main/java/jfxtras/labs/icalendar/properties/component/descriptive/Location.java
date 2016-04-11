package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBaseAltText;

/**
 * LOCATION
 * RFC 5545 iCalendar 3.8.1.7. page 87
 * 
 * This property defines the intended venue for the activity defined by a calendar component.
 * 
 * Example:
 * LOCATION;ALTREP="http://xyzcorp.com/conf-rooms/f123.vcf":
 *  Conference Room - F123\, Bldg. 002
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 */
public class Location extends PropertyBaseAltText<String, Location>
{    
    public Location(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public Location(Location source)
    {
        super(source);
    }
}
