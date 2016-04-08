package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBaseAltText;

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
public class Resources extends PropertyBaseAltText<Resources, List<String>>
{
    private final static StringConverter<List<String>> CONVERTER = new StringConverter<List<String>>()
    {
        @Override
        public String toString(List<String> object)
        {
            return object.stream().collect(Collectors.joining(","));
        }

        @Override
        public List<String> fromString(String string)
        {
            return Arrays.stream(string.split(",")).collect(Collectors.toList());
        }
    };
    
    public Resources(CharSequence contentLine)
    {
        super(contentLine, CONVERTER);
    }

    public Resources(List<String> values)
    {
        super(values, CONVERTER);
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    // set one category
    public void setValue(String category)
    {
        setValue(Arrays.asList(category));
    }
}
