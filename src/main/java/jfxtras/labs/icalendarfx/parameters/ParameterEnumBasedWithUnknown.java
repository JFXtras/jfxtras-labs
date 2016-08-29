package jfxtras.labs.icalendarfx.parameters;

import java.util.List;

public abstract class ParameterEnumBasedWithUnknown<U,T> extends ParameterBase<U,T>
{
    private String unknownValue; // contains exact string for unknown value

    /*
     * CONSTRUCTORS
     */
    public ParameterEnumBasedWithUnknown()
    {
        super();
    }
  
    public ParameterEnumBasedWithUnknown(T value)
    {
        super(value);
    }
    
    public ParameterEnumBasedWithUnknown(ParameterEnumBasedWithUnknown<U,T> source)
    {
        super(source);
        unknownValue = source.unknownValue;
    }
        
    @Override
    String valueAsString()
    {
        return (getValue().toString().equals("UNKNOWN")) ? unknownValue : super.valueAsString();
    }
    
    @Override
    public List<String> parseContent(String content)
    {
        super.parseContent(content);
        if (getValue().toString().equals("UNKNOWN"))
        {
            String valueString = Parameter.extractValue(content);
            unknownValue = valueString;
        }
        return errors();
    }
}
