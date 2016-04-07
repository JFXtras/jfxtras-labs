package jfxtras.labs.icalendar.parameters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.PropValueStringConverter;
import jfxtras.labs.icalendar.properties.Property;

/**
 * VALUE
 * Value Date Types
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
        super(ValueType.enumFromName(content));
        if (getValue() == ValueType.UNKNOWN)
        {
            unknownValue = content;
        }
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == ValueType.UNKNOWN) ? unknownValue : getValue().toString();
        String parameterName = myParameterEnum().toString();
        return ";" + parameterName + "=" + value;
    }
    
    public enum ValueType
    {
        BINARY ("BINARY"),
        BOOLEAN ("BOOLEAN"), 
        CALENDAR_USER_ADDRESS ("CAL-ADDRESS"),
        DATE ("DATE"),
        DATE_TIME ("DATE-TIME"),
        DURATION ("DURATION"), // Based on ISO.8601.2004 (but Y and M for years and months is not supported by iCalendar)
        FLOAT ("FLOAT"),
        INTEGER ("INTEGER"),
        PERIOD ("PERIOD"),
        RECURRENCE_RULE ("RECUR"),
        TEXT ("TEXT"),
        TIME ("TIME"),
        UNIFORM_RESOURCE_IDENTIFIER ("URI"),
        UTC_OFFSET ("UTC-OFFSET"),
        UNKNOWN ("UNKNOWN");
        
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
        public <T> PropValueStringConverter<T> stringConverter(Property<T> property)
        {
            return new PropValueStringConverter<T>(property)
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    Type[] types = ((ParameterizedType)getProperty().getClass().getGenericSuperclass())
                            .getActualTypeArguments();
                    System.out.println("class2:" + getProperty().getClass());
                     Class<T> myClass = (Class<T>) types[types.length-1]; // get last parameterized type
                     if (myClass.equals(String.class))
                     {
                         return (T) string;            
                     }
                     throw new RuntimeException("can't convert property value to type: " + myClass.getSimpleName() +
                             ". You need to override string converter for class " + getProperty().getClass().getSimpleName());
                }
            };

        }
    }
}