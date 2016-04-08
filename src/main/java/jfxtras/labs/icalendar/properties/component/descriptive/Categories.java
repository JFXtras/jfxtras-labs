package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;

/**
 * CATEGORIES
 * RFC 5545, 3.8.1.2, page 81
 * 
 * This property defines the categories for a calendar component.
 * 
 * Examples:
 * CATEGORIES:APPOINTMENT,EDUCATION
 * CATEGORIES:MEETING
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends PropertyBaseLanguage<Categories, List<String>>
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
    
    public Categories(CharSequence contentLine)
    {
        super(contentLine, CONVERTER);
    }
    
    public Categories(List<String> values)
    {
        super(values, CONVERTER);
    }
    
    public Categories(Categories source)
    {
        super(source);
    }
    
    // set one category
    public void setValue(String category)
    {
        setValue(Arrays.asList(category));
    }
}
