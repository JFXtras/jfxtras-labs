package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/** @see ExDate
 * @see RDate */
public interface Recurrence
{
    /** modified start date/time stream after applying changes (additions or removals) */
    public Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startDateTime);
    
    /** parses a comma delimited string of of iCalendar date-times*/
    @Deprecated // use parameter parse instead
    public static List<Temporal> parseTemporals(String string)
    {
        // find time zone id, if present
        final String tzid;
        if (string.matches("^TZID=.*"))
        {
            tzid = string.substring(0,string.indexOf(":")+1);
            string = string.substring(string.indexOf(":")+1).trim();
        } else
        {
            tzid = "";
        }
        return Arrays.asList(string.split(","))
                     .stream()
                     .map(s -> DateTimeUtilities.parse(tzid + s)) // if present, add tzid as prefix to every date-time string before parsing
                     .collect(Collectors.toList());
    }
}
