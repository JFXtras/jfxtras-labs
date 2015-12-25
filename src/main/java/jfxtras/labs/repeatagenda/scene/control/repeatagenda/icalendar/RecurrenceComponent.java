package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

public interface RecurrenceComponent
{
    /** modified start date/time stream after applying changes (additions or removals) */
    public Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startDateTime);
}
