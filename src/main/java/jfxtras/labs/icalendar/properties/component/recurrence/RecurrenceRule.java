package jfxtras.labs.icalendar.properties.component.recurrence;

import jfxtras.labs.icalendar.components.DaylightSavingTime;
import jfxtras.labs.icalendar.components.StandardTime;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBase;

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
 * @see VEvent
 * @see VTodo
 * @see TJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceRule extends PropertyBase<RecurrenceRule, Recurrence>
{
//    // TODO - move to ValueType 
//    private final static StringConverter<Recurrence> CONVERTER = new StringConverter<Recurrence>()
//    {
//        @Override
//        public String toString(Recurrence object)
//        {
//            return object.toString();
//        }
//
//        @Override
//        public Recurrence fromString(String string)
//        {
//            return new RecurrenceImpl(string);
//        }
//    };
    
    public RecurrenceRule(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }

    public RecurrenceRule(Recurrence value)
    {
        super(value, null);
    }

}
