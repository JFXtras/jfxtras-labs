package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;

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
        DURATION ("DURATION"),
        FLOAT ("FLOAT"),
        INTEGER ("INTEGER"),
        PERIOD ("PERIOD"),
        RECURRENCE_RULE ("RECUR"),
        TEXT ("TEXT"),
        TIME ("TIME"),
        UNIFORM_RESOURCE_IDENTIFIER ("URI"),
        UTC_OFFSET ("UTC-OFFSET"),
        UNKNOWN ("UNKNOWN");
        // x-name or IANA-token values must be added manually
        
        private String name;
        @Override public String toString() { return name; }
        ValueType(String name)
        {
            this.name = name;
        }
//        @Deprecated
//        abstract public <U> U parse(String value);
//
//        @Deprecated        
//        abstract public <U> String makeContent(U value);

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
