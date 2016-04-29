package jfxtras.labs.icalendarfx.properties.component.recurrence;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;

/**
 * RRULE
 * Recurrence Rule
 * RFC 5545 iCalendar 3.8.5.3, page 122.
 * 
 * This property defines a rule or repeating pattern for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * Produces a stream of start date/times after applying all modification rules.
 * 
 * @author David Bal
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceRule extends PropertyBase<RecurrenceRuleParameter, RecurrenceRule>
{
//    public RecurrenceRule(CharSequence contentLine)
//    {
//        super(contentLine);
//    }

    public RecurrenceRule(RecurrenceRuleParameter value)
    {
        super(value);
    }
    
    public RecurrenceRule()
    {
        super();
    }

    public RecurrenceRule(RecurrenceRule source)
    {
        super(source);
    }

    public static RecurrenceRule parse(String propertyContent)
    {
        RecurrenceRule property = new RecurrenceRule();
        property.parseContent(propertyContent);
        return property;
    }
}
