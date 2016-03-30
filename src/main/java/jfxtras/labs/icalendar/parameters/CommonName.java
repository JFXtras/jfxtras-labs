package jfxtras.labs.icalendar.parameters;

/**
 * CN
 * Common Name
 * RFC 5545, 3.2.2, page 15
 * 
 * To specify the common name to be associated with the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class CommonName extends ParameterBase<CommonName, String>
{
    public CommonName()
    {
        super();
    }
  
    public CommonName(String content)
    {
        super(extractValue(content));
    }

    public CommonName(CommonName source)
    {
        super(source);
    }
}
