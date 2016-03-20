package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.ComponentProperty;

public class Description extends TextPropertyAbstract<Description>
{
    private final static String NAME = ComponentProperty.DESCRIPTION.toString();
    
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
