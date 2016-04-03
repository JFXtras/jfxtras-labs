package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;

/**
 * 
 * Only VJournal allows multiple instances of DESCRIPTION
 * 
 * @author David Bal
 *
 */
public class Description extends AlternateTextRepresentationProperty<Description, String>
{
    public Description(String contentLine)
    {
        super(contentLine, null);
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
