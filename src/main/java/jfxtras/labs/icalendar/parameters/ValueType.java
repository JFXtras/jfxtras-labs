package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public enum ValueType
{
    BINARY ("BINARY") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    System.out.println("nun binary converter");
                     return (T) string;            
                }
            };
        }
    },
    BOOLEAN ("BOOLEAN") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString().toUpperCase();
                }

                @Override
                public T fromString(String string)
                {
                         return (T) (Boolean) Boolean.parseBoolean(string);            
                }
            };
        }
    }, 
    CALENDAR_USER_ADDRESS ("CAL-ADDRESS") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    try
                    {
                        return (T) new URI(string);
                    } catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    },
    DATE ("DATE") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return DateTimeUtilities.temporalToString((LocalDate) object);
//                    return DateTimeUtilities.LOCAL_DATE_FORMATTER.format((TemporalAccessor) object);
                }

                @Override
                public T fromString(String string)
                {
                         return (T) LocalDate.parse(string, DateTimeUtilities.LOCAL_DATE_FORMATTER);
                }
            };
        }
    },
    DATE_TIME ("DATE-TIME") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return DateTimeUtilities.temporalToString((Temporal) object);
                }

                @Override
                public T fromString(String string)
                {
                    return (T) DateTimeUtilities.temporalFromString(string);
                }
            };
        }
    },
    DURATION ("DURATION") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    if (string.contains("T"))
                    {
                        return (T) Duration.parse(string);
                    } else
                    {
                        return (T) Period.parse(string);            
                    }
                }
            };
        }
    }, // Based on ISO.8601.2004 (but Y and M for years and months is not supported by iCalendar)
    FLOAT ("FLOAT") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    INTEGER ("INTEGER") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PERIOD ("PERIOD") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_RULE ("RECUR") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) new RecurrenceRule(string);
                }
            };
        }
    },
    /* Note: This string converter is only acceptable for values converted to Stings
     * without any additional processing.  For properties with TEXT value that is stored
     * as any type other than String, this converter MUST be replaced. (Use setConverter in
     * Property).
     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
     * ZoneId.  A different converter is required to make the conversion to ZoneId.
    */
    TEXT ("TEXT") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                         return (T) string;            
                }
            };
        }
    },
    TIME ("TIME") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    UNIFORM_RESOURCE_IDENTIFIER ("URI") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    System.out.println("nun URI converter");
                    try
                    {
                        return (T) new URI(string);
                    } catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    },
    UTC_OFFSET ("UTC-OFFSET") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ZONE_OFFSET_FORMATTER.format((TemporalAccessor) object);
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ZoneOffset.of(string);
                }
            };
        }
    },
    UNKNOWN ("UNKNOWN") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    };
    private final static DateTimeFormatter ZONE_OFFSET_FORMATTER = new DateTimeFormatterBuilder()
            .appendOffset("+HHMM", "+0000")
            .toFormatter();
    
    private static Map<String, ValueType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ValueType> makeEnumFromNameMap()
    {
        Map<String, ValueType> map = new HashMap<>();
        ValueType[] values = ValueType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get enum from name */
    public static ValueType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    @Override public String toString() { return name; }
    ValueType(String name)
    {
        this.name = name;
    }

    /** return default String converter associated with property value type */
    abstract public <T> StringConverter<T> getConverter();

}