package jfxtras.labs.icalendar.properties.component.relationship;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class UniqueIdentifier extends PropertyBase<UniqueIdentifier, String>
{
    public UniqueIdentifier(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }

    public UniqueIdentifier(UniqueIdentifier source)
    {
        super(source);
    }
}
