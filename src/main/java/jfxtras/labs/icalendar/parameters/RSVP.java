package jfxtras.labs.icalendar.parameters;

/**
 * RSVP
 * RSVP Expectation
 * RFC 5545, 3.2.17, page 26
 * 
 * To specify whether there is an expectation of a favor of a
 *  reply from the calendar user specified by the property value.
 * 
 * Example:
 * ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class RSVP extends ParameterBase<RSVP, Boolean>
{
    public RSVP()
    {
        super();
    }

    public RSVP(Boolean value)
    {
        super(value);
    }
    
    public RSVP(String content)
    {
        super(Boolean.parseBoolean(extractValue(content)));
    }

    public RSVP(RSVP source)
    {
        super(source);
    }
}
