package jfxtras.labs.icalendar.properties.component.relationship;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;
import jfxtras.labs.icalendar.properties.PropertyType;

/**
 * CONTACT: RFC 5545 iCalendar 3.8.4.2. page 109
 *  This property is used to represent contact information or
 * alternately a reference to contact information associated with the
 * calendar component.
 * Example:
 * CONTACT:Jim Dolittle\, ABC Industries\, +1-919-555-1234
 */
public class Contact extends PropertyTextBase3<Contact>
{
    private final static String NAME = PropertyType.CONTACT.toString();
    
    public Contact(String contentLine)
    {
        super(NAME, contentLine);
    }
    
    public Contact(Contact source)
    {
        super(source);
    }
    
    public Contact()
    {
        super(NAME);
    }
}
