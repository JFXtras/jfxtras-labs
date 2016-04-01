package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.ZonedDateTime;

import javafx.util.Pair;
import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Value Date Types
 * VALUE
 * RFC 5545 iCalendar 3.2.10 page 29
 * 
 * To explicitly specify the value type format for a property value.
 * 
 *  Example:
 *  DTSTART;VALUE=DATE:20160307
 *   
 * @author David Bal
 *
 */
public class ValueType extends ParameterBase<ValueType, ValueEnum>
{
    public ValueType()
    {
        super();
    }
    
    public ValueType(ValueType source)
    {
        super(source);
    }
    
    public ValueType(ValueEnum value)
    {
        super(value);
    }
    
    public ValueType(String content)
    {
        super(ValueEnum.valueOf(extractValue(content)));
    }
    
    public enum ValueEnum
    {
        BINARY ("BINARY") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        BOOLEAN ("BOOLEAN") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        }, 
        CALENDAR_USER_ADDRESS ("CAL-ADDRESS") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        DATE ("DATE") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        DATE_TIME ("DATE-TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        DURATION ("DURATION") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        FLOAT ("FLOAT") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        INTEGER ("INTEGER") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        PERIOD ("PERIOD") {
            @Override
            public <U> U parse(String value)
            {
                String[] time = value.split("/");
                ZonedDateTime startInclusive = ZonedDateTime.parse(time[0], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
                final Duration duration;
                if (time[1].charAt(time[1].length()-1) == 'Z')
                {
                    ZonedDateTime endExclusive = ZonedDateTime.parse(time[1], DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                
                    duration = Duration.between(startInclusive, endExclusive);
                } else
                {
                    duration = Duration.parse(time[1]);
                }
                return (U) new Pair<ZonedDateTime, Duration>(startInclusive, duration);
            }
        },
        RECURRENCE_RULE ("RECUR") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        TEXT ("TEXT") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        TIME ("TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        },
        UNIFORM_RESOURCE_IDENTIFIER ("URI") {
            @Override
            public <U> U parse(String value)
            {
                try
                {
                    return (U) new URI(value);
                } catch (URISyntaxException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        },
        UTC_OFFSET ("UTC-OFFSET") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }
        };
        // x-name or IANA-token values must be added manually
        
        private String name;
        @Override public String toString() { return name; }
        ValueEnum(String name)
        {
            this.name = name;
        }
        abstract public <U> U parse(String value);

    }
}
