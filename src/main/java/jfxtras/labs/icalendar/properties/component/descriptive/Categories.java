package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyTextBase2;

/**
 * 
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends PropertyTextBase2<Categories>
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
