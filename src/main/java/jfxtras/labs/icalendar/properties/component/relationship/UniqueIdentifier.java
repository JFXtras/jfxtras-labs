package jfxtras.labs.icalendar.properties.component.relationship;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class UniqueIdentifier extends PropertyBase<UniqueIdentifier, String>
{
    public UniqueIdentifier(String contentLine)
    {
        super(contentLine);
    }

    public UniqueIdentifier(UniqueIdentifier source)
    {
        super(source);
    }
    
    public UniqueIdentifier()
    {
        super();
    }
}
