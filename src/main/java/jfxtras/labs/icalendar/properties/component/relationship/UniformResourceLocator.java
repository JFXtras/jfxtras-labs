package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;
import java.net.URISyntaxException;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class UniformResourceLocator extends PropertyBase<UniformResourceLocator, URI>
{
    public UniformResourceLocator(String contentLine) throws URISyntaxException
    {
        super(contentLine);
        setValue(new URI(getPropertyValueString()));
    }
    
    public UniformResourceLocator(UniformResourceLocator source)
    {
        super(source);
    }
    
    public UniformResourceLocator()
    {
        super();
    }
}
