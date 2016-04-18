package jfxtras.labs.icalendarfx.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public enum ValueType
{
    BINARY ("BINARY", Arrays.asList(String.class)) {
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
    BOOLEAN ("BOOLEAN", Arrays.asList(Boolean.class)) {
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
    CALENDAR_USER_ADDRESS ("CAL-ADDRESS", Arrays.asList(URI.class)) {
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
    DATE ("DATE", Arrays.asList(LocalDate.class)) {
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
    DATE_TIME ("DATE-TIME", Arrays.asList(LocalDateTime.class, ZonedDateTime.class)) {
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
    DURATION ("DURATION", Arrays.asList(Duration.class, Period.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    String s = object.toString();
                    // move minus sign to front
                    if (s.contains("-"))
                    {
                        s = "-" + s.replaceFirst("-", "");
                    }
                    return s;
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
    FLOAT ("FLOAT", Arrays.asList(Double.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    INTEGER ("INTEGER", Arrays.asList(Integer.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
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
                    return (T) new Integer(Integer.parseInt(string));
                }
            };
        }
    },
    PERIOD ("PERIOD", Arrays.asList(List.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_RULE ("RECUR", Arrays.asList(RecurrenceRuleParameter.class)) {
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
                    return (T) new RecurrenceRuleParameter(string);
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
    TEXT ("TEXT", Arrays.asList(String.class, ZoneId.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    // Add escape characters
                    String line = object.toString();
                    StringBuilder builder = new StringBuilder(line.length()+20); 
                    for (int i=0; i<line.length(); i++)
                    {
                        char myChar = line.charAt(i);
                        for (int j=0;j<REPLACEMENT_CHARACTERS.length; j++)
                        {
                            if (myChar == REPLACEMENT_CHARACTERS[j])
                            {
                                builder.append('\\');
                                myChar = SPECIAL_CHARACTERS[j];
                                break;
                            }
                        }
                        builder.append(myChar);
                    }
                    return builder.toString();
                }

                @Override
                public T fromString(String string)
                {
                    // Remove escape characters \ , ; \n (newline)
                    StringBuilder builder = new StringBuilder(string.length()); 
                    for (int i=0; i<string.length(); i++)
                    {
                        char charToAdd = string.charAt(i);
                        if (string.charAt(i) == '\\')
                        {
                            char nextChar = string.charAt(i+1);
                            for (int j=0;j<SPECIAL_CHARACTERS.length; j++)
                            {
                                if (nextChar == SPECIAL_CHARACTERS[j])
                                {
                                    charToAdd = REPLACEMENT_CHARACTERS[j];
                                    i++;
                                    break;
                                }
                            }
                        }
                        builder.append(charToAdd);
                    }
                    return (T) builder.toString();
                }
            };
        }
    },
    TIME ("TIME", Arrays.asList(LocalTime.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    UNIFORM_RESOURCE_IDENTIFIER ("URI", Arrays.asList(URI.class)) {
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
    UTC_OFFSET ("UTC-OFFSET", Arrays.asList(ZoneOffset.class)) {
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
    UNKNOWN ("UNKNOWN", Arrays.asList(Object.class)) {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    };
    final private static char[] SPECIAL_CHARACTERS = new char[] {',' , ';' , '\\' , 'n', 'N' };
    final private static char[] REPLACEMENT_CHARACTERS = new char[] {',' , ';' , '\\' , '\n', '\n'};

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
    List<Class<?>> allowedClasses;
    public List<Class<?>> allowedClasses() { return allowedClasses; }
    ValueType(String name, List<Class<?>> allowedClasses)
    {
        this.name = name;
        this.allowedClasses = allowedClasses;
    }

    /** return default String converter associated with property value type */
    abstract public <T> StringConverter<T> getConverter();

}