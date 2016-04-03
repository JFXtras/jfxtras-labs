package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javafx.util.Pair;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
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
public class ValueParameter extends ParameterBase<ValueParameter, ValueType>
{
    public ValueParameter()
    {
        super();
    }
    
    public ValueParameter(ValueParameter source)
    {
        super(source);
    }
    
    public ValueParameter(ValueType value)
    {
        super(value);
    }
    
    public ValueParameter(String content)
    {
        super(ValueType.valueOf(extractValue(content)));
    }
    
    public enum ValueType
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
                return (U) LocalDate.parse(value, DateTimeUtilities.LOCAL_DATE_FORMATTER);
            }
        },
        DATE_LOCAL_DATE_TIME ("DATE-TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) LocalDateTime.parse(value, DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER);                        
            }
        },
        DATE_ZONED_DATE_TIME ("DATE-TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) ZonedDateTime.parse(value, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);
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
        ValueType(String name)
        {
            this.name = name;
        }
        abstract public <U> U parse(String value);

    }
}
