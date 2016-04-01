package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;

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
        UTC_OFFSET ("UTC-OFFSET");
        // x-name or IANA-token values must be added manually
        
        private String name;
        @Override public String toString() { return name; }
        ValueEnum(String name)
        {
            this.name = name;
        }
    }
}
