package jfxtras.labs.icalendar.parameters;

import java.net.URI;

/**
 * SENT-BY
 * Sent By
 * RFC 5545, 3.2.18, page 27
 * 
 * To specify the calendar user that is acting on behalf of
 *  the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;SENT-BY="mailto:sray@example.com":mailto:
 *  jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class SentBy extends ParameterBase<SentBy, URI>
{
    public SentBy()
    {
        super();
    }
    
    public SentBy(String content)
    {
        super();
    }
    
    public SentBy(SentBy source)
    {
        super(source);
    }
}
