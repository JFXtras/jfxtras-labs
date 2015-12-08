package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface RecurrenceComponent
{
    /** modified start date/time stream after applying changes (additions or removals) */
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime);
}
