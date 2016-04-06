package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class UniformResourceLocator extends PropertyBase<UniformResourceLocator, URI>
{
    public UniformResourceLocator(String contentLine)
    {
        super(contentLine);
    }
    
    public UniformResourceLocator(UniformResourceLocator source)
    {
        super(source);
    }
}
