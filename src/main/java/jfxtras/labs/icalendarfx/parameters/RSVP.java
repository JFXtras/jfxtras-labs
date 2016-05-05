package jfxtras.labs.icalendarfx.parameters;

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
        super(false); // default value
    }

    public RSVP(Boolean value)
    {
        super(value);
    }

    public RSVP(RSVP source)
    {
        super(source);
    }
    
    @Override
    public void parseContent(String content)
    {
        setValue(Boolean.parseBoolean(content));
    }

    public static RSVP parse(String content)
    {
        RSVP parameter = new RSVP();
        parameter.parseContent(content);
        return parameter;
    }
}
