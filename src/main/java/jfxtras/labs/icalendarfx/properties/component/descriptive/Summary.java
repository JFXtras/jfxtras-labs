package jfxtras.labs.icalendarfx.properties.component.descriptive;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseAltText;

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
    
    public Summary()
    {
        super();
    }
}
