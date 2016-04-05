package jfxtras.labs.icalendar.properties.component.time;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyLanguage;

/**
 * DURATION
 * RFC 5545, 3.8.1.2, page 81
 * 
 * This property specifies a positive duration of time.
 * 
 * When the "DURATION" property relates to a
 * "DTSTART" property that is specified as a DATE value, then the
 * "DURATION" property MUST be specified as a "dur-day" or "dur-week" value.
 * 
 * Based on ISO.8601.2004 (but Y and M for years and months is not supported by iCalendar)
 * 
 * Examples:
 * DURATION:PT1H0M0S
 * DURATION:PT15M
 * DURATION:P1D
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VAlarm
 */
public class DurationProp extends PropertyLanguage<DurationProp, TemporalAmount>
{
    public DurationProp(String contentLine)
    {
        super(contentLine);
    }

    public DurationProp(TemporalAmount value)
    {
        super(value);
    }

    public DurationProp(DurationProp source)
    {
        super(source);
    }
    
    @Override
    protected TemporalAmount valueFromString(String propertyValueString)
    {
        if (propertyValueString.contains("T"))
        {
            return Duration.parse(propertyValueString);
        } else
        {
            return Period.parse(propertyValueString);            
        }
    }
    
//    @Override
//    protected String valueToString(TemporalAmount value)
//    {
//        ZoneId z = value.getZone();
//        if (z.equals(ZoneId.of("Z")))
//        {
//            return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
//        } else
//        {
//            return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value); // Time zone is added through TimeZoneIdentifier parameter
//        }
//    }
}
