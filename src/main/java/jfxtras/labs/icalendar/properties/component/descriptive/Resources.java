package jfxtras.labs.icalendar.properties.component.descriptive;

import java.util.List;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;

public class Resources extends AlternateTextRepresentationProperty<Resources, List<String>>
{
    public Resources(String contentLine)
    {
        super(contentLine);
    }
    
    public Resources(Resources source)
    {
        super(source);
    }
    
    public Resources()
    {
        super();
    }
}
