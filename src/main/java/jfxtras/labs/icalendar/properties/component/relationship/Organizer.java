package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;
import java.net.URISyntaxException;

import jfxtras.labs.icalendar.properties.CalendarUserAddressProperty;

/**
 * ORGANIZER
 * RFC 5545, 3.8.4.3
 * 
 * This property defines the organizer for a calendar component.
 * 
 * Example:
 * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class Organizer extends CalendarUserAddressProperty<Organizer,URI>
{
    public Organizer(String propertyString) throws URISyntaxException
    {
        super(propertyString);
//        setValue(new URI(getPropertyValueString()));
    }
    
    public Organizer(Organizer source)
    {
        super(source);
    }
    
    public Organizer()
    {
        super();
    }
}
