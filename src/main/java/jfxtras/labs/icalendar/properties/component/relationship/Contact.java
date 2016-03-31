package jfxtras.labs.icalendar.properties.component.relationship;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

/**
 * CONTACT: RFC 5545 iCalendar 3.8.4.2. page 109
 *  This property is used to represent contact information or
 * alternately a reference to contact information associated with the
 * calendar component.
 * Example:
 * CONTACT:Jim Dolittle\, ABC Industries\, +1-919-555-1234
 */
public class Contact extends AlternateTextRepresentationBase<Contact, String>
{   
    public Contact(String contentLine)
    {
        super(contentLine);
        setValue(getPropertyValueString());
    }
    
    public Contact(Contact source)
    {
        super(source);
    }
    
    public Contact()
    {
        super();
    }
}
