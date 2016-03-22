package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyTextBase2;
import jfxtras.labs.icalendar.properties.PropertyType;

/**
 * 
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Categories extends PropertyTextBase2<Categories>
{
    private final static String NAME = PropertyType.CATEGORIES.toString();
    
    public Categories(String propertyString)
    {
        super(NAME, propertyString);
    }
}
