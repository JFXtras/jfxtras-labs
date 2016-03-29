package jfxtras.labs.icalendar.parameters;

import java.net.URI;

/**
 * ALTREP
 * Alternate Text Representation
 * RFC 5545, 3.2.1, page 14
 * 
 * To specify an alternate text representation for the property value.
 * 
 * Example:
 * DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
 *  Project XYZ Review Meeting will include the following agenda
 *  items: (a) Market Overview\, (b) Finances\, (c) Project Man
 *  agement
 * 
 * @author David Bal
 *
 */
public class AlternateTextRepresentation extends ParameterBase<AlternateTextRepresentation, URI>
{
    public AlternateTextRepresentation()
    {
        super();
    }
  
    public AlternateTextRepresentation(String content)
    {
        super(content);
    }

    public AlternateTextRepresentation(AlternateTextRepresentation source)
    {
        super(source);
    }
}
