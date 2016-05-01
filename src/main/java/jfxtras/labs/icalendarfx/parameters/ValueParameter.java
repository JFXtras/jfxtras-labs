package jfxtras.labs.icalendarfx.parameters;

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
        String parameterName = parameterType().toString();
        return ";" + parameterName + "=" + value;
    }
}
