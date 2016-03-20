package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.VComponentProperty;

public class Description extends TextPropertyAbstract<Description>
{
    public Description(String contentLine)
    {
        super(contentLine, VComponentProperty.DESCRIPTION.toString());
    }
    
    public Description(Description description)
    {
        super(description);
    }
    
    public Description() { }

    @Override
    public String toContentLine()
    {
        return VComponentProperty.DESCRIPTION.toString() + super.toContentLine();
    }
}
