package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

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
    private String unknownValue;
    
//    public ValueParameter()
//    {
//        super();
//    }
    
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
        super(ValueType.valueOf2(content));
        if (getValue() == ValueType.UNKNOWN)
        {
            unknownValue = content;
        }
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == ValueType.UNKNOWN) ? unknownValue : getValue().toString();
        return ";" + myParameterEnum().toString() + "=" + value;
    }

    
    public enum ValueType
    {
        BINARY ("BINARY") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        BOOLEAN ("BOOLEAN") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        }, 
        CALENDAR_USER_ADDRESS ("CAL-ADDRESS") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        DATE ("DATE") {
            @Override
            public <U> U parse(String value)
            {
                return (U) LocalDate.parse(value, DateTimeUtilities.LOCAL_DATE_FORMATTER);
            }

            @Override
            public <U> String makeContent(U value)
            {
                return DateTimeUtilities.LOCAL_DATE_FORMATTER.format((TemporalAccessor) value);
            }
        },
        DATE_LOCAL_DATE_TIME ("DATE-TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) LocalDateTime.parse(value, DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER);                        
            }
            
            @Override
            public <U> String makeContent(U value)
            {
                return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format((TemporalAccessor) value);
            }
        },
        DATE_ZONED_DATE_TIME ("DATE-TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) ZonedDateTime.parse(value, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);
            }

            @Override
            public <U> String makeContent(U value)
            {
                ZoneId z = ((ZonedDateTime) value).getZone();
                if (z.equals(ZoneId.of("Z")))
                {
                    return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format((TemporalAccessor) value);
                } else
                {
                    return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format((TemporalAccessor) value); // Time zone is added through TimeZoneIdentifier parameter
                }
            }
        },
//        DATE_UTC_DATE_TIME ("DATE-TIME") {
//            @Override
//            public <U> U parse(String value)
//            {
//                return (U) ZonedDateTime.parse(value, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);
//            }
//
//            @Override
//            public <U> String makeContent(U value)
//            {
//                return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format((TemporalAccessor) value);
//            }
//        },
        DURATION ("DURATION") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        FLOAT ("FLOAT") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        INTEGER ("INTEGER") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
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

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        RECURRENCE_RULE ("RECUR") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        TEXT ("TEXT") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                System.out.println("value2:" + value);
                return value.toString();
            }
        },
        TIME ("TIME") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
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

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        UTC_OFFSET ("UTC-OFFSET") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return value.toString();
            }
        },
        UNKNOWN ("UNKNOWN") {
            @Override
            public <U> U parse(String value)
            {
                return (U) value;
            }

            @Override
            public <U> String makeContent(U value)
            {
                return (String) value;
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
        
        abstract public <U> String makeContent(U value);

        static ValueType valueOf2(String value)
        {
            try
            {
                return valueOf(value);
            } catch (IllegalArgumentException e)
            {
                return UNKNOWN;
            }
        }

    }
}
