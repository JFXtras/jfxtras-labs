package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.components.VEventUtilities.VEventProperty;
import jfxtras.labs.icalendar.properties.TextPropertyAbstract;

public class Description extends TextPropertyAbstract<Description>
{
    public Description(String contentLine)
    {
        super(contentLine);
    }
    
    public Description(Description description)
    {
        super(description);
    }
    
    public Description() { }

    @Override
    public String toContentLine()
    {
        return VEventProperty.DESCRIPTION.toString() + super.toContentLine();
    }
}
