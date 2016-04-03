package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.LanguageProperty;

/**
 * 
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends LanguageProperty<Categories, List<String>>
{    
    public Categories(String propertyString)
    {
        super(propertyString);
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
    
//    @Override
//    protected String getValueForContentLine()
//    {
//        return getValue().stream().collect(Collectors.joining(","));
//    }
}
