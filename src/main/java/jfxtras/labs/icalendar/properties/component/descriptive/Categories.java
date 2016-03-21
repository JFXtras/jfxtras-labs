package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.ComponentProperty;
import jfxtras.labs.icalendar.properties.Property2Base;

public class Categories extends Property2Base<Categories>
{
    private final static String NAME = ComponentProperty.CATEGORIES.toString();
    
    /*
     * CONSTRUCTORS
     */
    public Categories(String propertyString)
    {
        super(NAME, propertyString);
    }
}
