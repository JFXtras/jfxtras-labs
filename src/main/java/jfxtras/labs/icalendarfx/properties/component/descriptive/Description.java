package jfxtras.labs.icalendarfx.properties.component.descriptive;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseAltText;

/**
 * DESCRIPTION
 * RFC 5545 iCalendar 3.8.1.5. page 84
 * 
 * This property provides a more complete description of the
 * calendar component than that provided by the "SUMMARY" property.
 * 
 * Example:
 * DESCRIPTION:Meeting to provide technical review for "Phoenix"
 *  design.\nHappy Face Conference Room. Phoenix design team
 *  MUST attend this meeting.\nRSVP to team leader.
 *
 * Note: Only VJournal allows multiple instances of DESCRIPTION
 *   
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
public class Description extends PropertyBaseAltText<String,Description>
{
    public Description(Description source)
    {
        super(source);
    }

    public Description()
    {
        super();
    }
    
    public static Description parse(String propertyContent)
    {
        Description property = new Description();
        System.out.println("propertyContent:" + propertyContent);
        property.parseContent(propertyContent);
        return property;
    }
}
