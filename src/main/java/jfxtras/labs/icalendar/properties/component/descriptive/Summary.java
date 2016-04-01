package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

public class Summary extends AlternateTextRepresentationBase<Summary, String>
{
    private List<ValueEnum> allowedValueTypes = Arrays.asList(ValueEnum.TEXT);
    
    public Summary(String contentLine)
    {
        super(contentLine);
        setValue(getPropertyValueString());
    }

    public Summary(Summary source)
    {
        super(source);
    }
    
    public Summary()
    {
        super();
    }
        
    @Override
    public boolean isValid()
    {
        if (getValueType() == null)
        {
            return super.isValid();
        } else
        {
            return (super.isValid()) && (allowedValueTypes.contains(getValueType().getValue()));
        }
    }
}
