package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;

public class Summary extends AlternateTextRepresentationProperty<Summary, String>
{
    public Summary(String contentLine)
    {
        super(contentLine);
    }

    public Summary(Summary source)
    {
        super(source);
    }
    
    public Summary()
    {
        super();
    }
}
