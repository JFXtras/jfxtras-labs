package jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * EXDATE, list of DATE-TIME exceptions for recurring events as defined in 
 * RFC 5545 iCalendar 3.8.5.1, page 117.
 * Used as a part of a VEVENT as defined by 3.6.1, page 52.
 * 
 * @author David Bal
 *
 */
public class EXDate implements Rule
{

    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream,
            LocalDateTime startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

}
