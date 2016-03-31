package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.LanguageBase;
import jfxtras.labs.icalendar.properties.PropertyListString;

/**
 * 
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends LanguageBase<Categories, List<String>> implements PropertyListString
{    
    public Categories(String propertyString)
    {
        super(propertyString);
        List<String> values = Arrays.asList(getPropertyValueString().split(","))
                .stream()
                .collect(Collectors.toList());
        setValue(values);
    }
    
    public Categories(Categories source)
    {
        super(source);
    }
    
    public Categories()
    {
        super();
    }
    
    // set one category
    public void setValue(String category)
    {
        setValue(Arrays.asList(category));
    }
}
