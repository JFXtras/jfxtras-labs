package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

/** @see ExDate
 * @see RDate */
public interface Recurrence
{
    /** modified start date/time stream after applying changes (additions or removals) */
    Stream<Temporal> stream(Temporal startDateTime);
    
}
