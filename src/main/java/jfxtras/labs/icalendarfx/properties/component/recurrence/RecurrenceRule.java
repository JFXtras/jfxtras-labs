package jfxtras.labs.icalendarfx.properties.component.recurrence;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNewInt;
import jfxtras.labs.icalendarfx.components.VTodoInt;
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
 * @see VEventNewInt
 * @see VTodoInt
 * @see TJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceRule extends PropertyBase<RecurrenceRuleParameter, RecurrenceRule>
{
    public RecurrenceRule(CharSequence contentLine)
    {
        super(contentLine);
    }

    public RecurrenceRule(RecurrenceRuleParameter value)
    {
        super(value);
    }
}
