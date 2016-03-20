package jfxtras.labs.icalendar.properties.relationship;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.VComponentProperty;

/**
 * CONTACT: RFC 5545 iCalendar 3.8.4.2. page 109
 *  This property is used to represent contact information or
 * alternately a reference to contact information associated with the
 * calendar component.
 * Example:
 * CONTACT:Jim Dolittle\, ABC Industries\, +1-919-555-1234
 */
public class Contact extends TextPropertyAbstract<Contact>
{
    public Contact(String contentLine)
    {
        super(contentLine, VComponentProperty.CONTACT.toString());
    }
    
    public Contact(Contact contact)
    {
        super(contact);
    }
    
    public Contact() { }

    @Override
    public String toContentLine()
    {
        return VComponentProperty.CONTACT.toString() + super.toContentLine();
    }
}
