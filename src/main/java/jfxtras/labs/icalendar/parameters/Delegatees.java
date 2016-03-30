package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

/**
 * DELEGATED-TO
 * Delegatees
 * RFC 5545, 3.2.5, page 17
 * 
 * To specify the calendar users to whom the calendar user
 *    specified by the property has delegated participation.
 * 
 * Example:
 * ATTENDEE;DELEGATED-TO="mailto:jdoe@example.com","mailto:jqpublic
 *  @example.com":mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class Delegatees extends ParameterBase<Delegatees, List<URI>>
{
    public Delegatees()
    {
        super();
    }
    
    public Delegatees(String content)
    {
        super(makeURIList(extractValue(content)));
    }
    
    public Delegatees(Delegatees source)
    {
        super(source);
    }
}
