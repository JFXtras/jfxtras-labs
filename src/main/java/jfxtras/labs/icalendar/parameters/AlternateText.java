package jfxtras.labs.icalendar.parameters;

import java.net.URI;

import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.relationship.Contact;

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
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public class AlternateText extends ParameterBase<AlternateText, URI>
{
//    public AlternateTextRepresentation()
//    {
//        super();
//    }
    
    public AlternateText(URI value)
    {
        super(value);
    }
  
    public AlternateText(String content)
    {
        super(makeURI(content));
    }

    public AlternateText(AlternateText source)
    {
        super(source);
    }
}
