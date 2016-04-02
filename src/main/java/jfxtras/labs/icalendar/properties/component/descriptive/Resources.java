package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;

public class Resources extends AlternateTextRepresentationProperty<Resources, List<String>>
{
    public Resources(String contentLine)
    {
        super(contentLine);
//        List<String> values = Arrays.asList(getPropertyValueString().split(","))
//                .stream()
//                .collect(Collectors.toList());
//        setValue(values);
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
    }
    
    @Override
    protected String getValueForContentLine()
    {
        return getValue().stream().collect(Collectors.joining(","));
    }
}
