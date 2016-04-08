package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class UniformResourceLocator extends PropertyBase<UniformResourceLocator, URI>
{
    public UniformResourceLocator(String contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public UniformResourceLocator(URI value)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(value, null);
    }
    
    public UniformResourceLocator(UniformResourceLocator source)
    {
        super(source);
    }
}
