package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBaseAltText;

/**
 * SUMMARY
 * RFC 5545 iCalendar 3.8.1.12. page 93
 * 
 * This property defines a short summary or subject for the calendar component.
 * 
 * Example:
 * SUMMARY:Department Party
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
public class Summary extends PropertyBaseAltText<String, Summary>
{
    public Summary(CharSequence contentLine)
    {
        super(contentLine);
    }

    public Summary(Summary source)
    {
        super(source);
    }
}
