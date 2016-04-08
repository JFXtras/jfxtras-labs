package jfxtras.labs.icalendar.parameters;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public enum ValueType
{
    BINARY ("BINARY") {
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
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
                    System.out.println("b:" + object.toString().toUpperCase());
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
            // TODO Auto-generated method stub
            return null;
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
                    return DateTimeUtilities.LOCAL_DATE_FORMATTER.format((TemporalAccessor) object);
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
                    if (object instanceof ZonedDateTime)
                    {
                        ZonedDateTime value = (ZonedDateTime) object;
                        ZoneId z = value.getZone();
                        if (z.equals(ZoneId.of("Z")))
                        {
                            return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
                        } else
                        {
                            return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value); // Time zone is added through TimeZoneIdentifier parameter
                        }
                    } else if (object instanceof LocalDateTime)
                    {
                        LocalDateTime value = (LocalDateTime) object;
                        return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value);
                    } else
                    {
                        throw new DateTimeException("Unsuported Date-Time class:" + object.getClass().getSimpleName());
                    }
                }

                @Override
                public T fromString(String string)
                {
                    final String form1 = "^[0-9]{8}T([0-9]{6})";
                    final String form2 = "^[0-9]{8}T([0-9]{6})Z";
                    final String form3 = "^(.*/.*:)[0-9]{8}T([0-9]{6})";
                    if (string.matches(form1))
                    {
                        return (T) LocalDateTime.parse(string, DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER);                                                
                    } else if (string.matches(form2))
                    {
                        return (T) ZonedDateTime.parse(string, DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);                                                
                    } else if (string.matches(form3))
                    {
                        return (T) ZonedDateTime.parse(string, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);                                                
                    } else
                    {
                        throw new DateTimeException("Can't parse date-time string:" + string);                        
                    }
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
            // TODO Auto-generated method stub
            return null;
        }
    },
    /* Note: This string converter is only acceptable for values converted to Stings
     * without any additional processing.  For properties with TEXT value that is stored
     * as any time other than String, this converter MUST be replaced by setConverter for
     * Properties.
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
            // TODO Auto-generated method stub
            return null;
        }
    },
    UTC_OFFSET ("UTC-OFFSET") {
        
//        private final static DateTimeFormatter ZONE_OFFSET_FORMATTER = new DateTimeFormatterBuilder()
//                .appendOffset("+HHMM", "+0000")
//                .toFormatter();
        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
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
    
    public <T> StringConverter<T> stringConverter2()
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

    abstract public <T> StringConverter<T> getConverter();

}