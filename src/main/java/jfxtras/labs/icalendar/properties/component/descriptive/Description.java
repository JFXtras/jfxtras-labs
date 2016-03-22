package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;
import jfxtras.labs.icalendar.properties.PropertyType;

public class Description extends PropertyTextBase3<Description>
{
    private final static String NAME = PropertyType.DESCRIPTION.toString();
    
    public Description(String contentLine)
    {
        super(NAME, contentLine);
    }
    
    public Description(Description description)
    {
        super(description);
    }
    
    public Description() { super(NAME); }
}
