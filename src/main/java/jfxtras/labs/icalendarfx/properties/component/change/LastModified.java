package jfxtras.labs.icalendarfx.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseUTC;

/**
 * LAST-MODIFIED
 * RFC 5545, 3.8.7.3, page 138
 * 
 * This property specifies the date and time that the
 * information associated with the calendar component was last
 * revised in the calendar store.
 *
 * Note: This is analogous to the modification date and time for a
 * file in the file system.
 * 
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * LAST-MODIFIED:19960817T133000Z
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see StandardTime
 * @see DaylightSavingTime
 */
public class LastModified extends PropertyBaseUTC<LastModified>
{    
    public LastModified(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public LastModified(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public LastModified(LastModified source)
    {
        super(source);
    }
}
