package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * UNTIL:
 * RFC 5545 iCalendar 3.3.10, page 41
 * 
 * The UNTIL rule part defines a DATE or DATE-TIME value that bounds
 * the recurrence rule in an inclusive manner.  If the value
 * specified by UNTIL is synchronized with the specified recurrence,
 * this DATE or DATE-TIME becomes the last instance of the
 * recurrence.  The value of the UNTIL rule part MUST have the same
 * value type as the "DTSTART" property.  Furthermore, if the
 * "DTSTART" property is specified as a date with local time, then
 * the UNTIL rule part MUST also be specified as a date with local
 * time.  If the "DTSTART" property is specified as a date with UTC
 * time or a date with local time and time zone reference, then the
 * UNTIL rule part MUST be specified as a date with UTC time.  In the
 * case of the "STANDARD" and "DAYLIGHT" sub-components the UNTIL
 * rule part MUST always be specified as a date with UTC time.  If
 * specified as a DATE-TIME value, then it MUST be specified in a UTC
 * time format.  If not present, and the COUNT rule part is also not
 * present, the "RRULE" is considered to repeat forever
 */
public class Until extends RRuleElementBase<Temporal, Until>
{
    public Until(Temporal until)
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toContent()
    {
        return ";" + elementType().toString() + "=" + DateTimeUtilities.temporalToString(getValue());
    }
}
