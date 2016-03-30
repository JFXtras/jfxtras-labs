package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.List;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.LanguageBase;

/**
 * 
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends LanguageBase<Categories, List<String>>
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
}
