package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyAlternateTextRepresentation;

/**
 * RESOURCES
 * RFC 5545 iCalendar 3.8.1.10. page 91
 * 
 * This property defines the equipment or resources
 * anticipated for an activity specified by a calendar component.
 * 
 * Examples:
 * RESOURCES:EASEL,PROJECTOR,VCR
 * RESOURCES;LANGUAGE=fr:Nettoyeur haute pression
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 */
public class Resources extends PropertyAlternateTextRepresentation<Resources, List<String>>
{
    public Resources(String contentLine)
    {
        super(contentLine);
    }

    public Resources(List<String> values)
    {
        super(values);
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
    }
    
    // set one category
    public void setValue(String category)
    {
        setValue(Arrays.asList(category));
    }
    
    @Override
    protected List<String> valueFromString(String propertyValueString)
    {
        return Arrays.stream(propertyValueString.split(",")).collect(Collectors.toList());
    }
    
    @Override
    protected String valueToString(List<String> value)
    {
        return value.stream().collect(Collectors.joining(","));
    }
}
