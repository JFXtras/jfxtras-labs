package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

/**
 * 
 * Only VJournal allows multiple instances of DESCRIPTION
 * 
 * @author David Bal
 *
 */
public class Description extends AlternateTextRepresentationBase<Description, String>
{
    public Description(String contentLine)
    {
        super(contentLine);
//        setValue(getPropertyValueString());
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
