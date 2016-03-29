package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

/**
 * DELEGATED-FROM
 * Delegators
 * RFC 5545, 3.2.4, page 17
 * 
 * To specify the calendar users that have delegated their
 *    participation to the calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;DELEGATED-FROM="mailto:jsmith@example.com":mailto:
 *  jdoe@example.com
 * 
 * @author David Bal
 *
 */
public class Delegators extends ParameterBase<Delegators, List<URI>>
{
    public Delegators()
    {
        super();
    }
    
    public Delegators(String content)
    {
        super();
        List<URI> value = myParameterEnum().parse(content);
        setValue(value);
    }
    
    public Delegators(Delegators source)
    {
        super(source);
    }
}
