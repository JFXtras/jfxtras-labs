package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;
import jfxtras.labs.icalendar.properties.PropertyListString;

public class Resources extends AlternateTextRepresentationBase<Resources, List<String>> implements PropertyListString
{
    public Resources(String contentLine)
    {
        super(contentLine);
        List<String> values = Arrays.asList(getPropertyValueString().split(","))
                .stream()
                .collect(Collectors.toList());
        setValue(values);
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
    }
}
