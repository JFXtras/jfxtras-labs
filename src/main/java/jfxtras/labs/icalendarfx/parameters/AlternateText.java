package jfxtras.labs.icalendarfx.parameters;

import java.net.URI;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;

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
public class AlternateText extends ParameterURI<AlternateText>
{
    public AlternateText(URI value)
    {
        super(value);
    }

    public AlternateText(AlternateText source)
    {
        super(source);
    }

    public AlternateText()
    {
        super();
    }
    
    public static AlternateText parse(String content)
    {
        AlternateText parameter = new AlternateText();
        parameter.parseContent(content);
        return parameter;
    }
}
