package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.VCalendar;
import jfxtras.labs.icalendar.properties.PropertyBase;

/**
 * PRODID
 * Product Identifier
 * RFC 5545, 3.7.3, page 78
 * 
 * This property specifies the identifier for the product that created the iCalendar object.
 * 
 * The vendor of the implementation SHOULD assure that
 * this is a globally unique identifier; using some technique such as
 * an FPI value, as defined in [ISO.9070.1991]
 * 
 * Example:
 * PRODID:-//ABC Corporation//NONSGML My Product//EN
 * 
 * @author David Bal
 * @see VCalendar
 */
public class ProductIdentifier extends PropertyBase<ProductIdentifier, String>
{
    public ProductIdentifier(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public ProductIdentifier(ProductIdentifier source)
    {
        super(source);
    }
}
