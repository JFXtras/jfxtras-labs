package jfxtras.labs.icalendarfx.properties.component.descriptive;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBaseAltText;

/**
 * COMMENT
 * RFC 5545 iCalendar 3.8.1.4. page 83
 * 
 * This property specifies non-processing information intended
 * to provide a comment to the calendar user
 * 
 * Example:
 * COMMENT:The meeting really needs to include both ourselves
 *  and the customer. We can't hold this meeting without them.
 *  As a matter of fact\, the venue for the meeting ought to be at
 *  their site. - - John
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see StandardTime
 * @see DaylightSavingTime
 */
public class Comment extends PropertyBaseAltText<String, Comment>
{
    public Comment(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public Comment(Comment source)
    {
        super(source);
    }
}
