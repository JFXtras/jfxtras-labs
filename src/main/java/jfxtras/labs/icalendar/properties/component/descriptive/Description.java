package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

public class Description extends AlternateTextRepresentationBase<Description, URI>
{
    public Description(String contentLine)
    {
        super(contentLine);
    }
    
    public Description(Description source)
    {
        super(source);
    }
    
    public Description()
    {
        super();
    }
}
