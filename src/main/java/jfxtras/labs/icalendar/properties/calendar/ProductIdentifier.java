package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class ProductIdentifier extends PropertyBase<ProductIdentifier, String>
{
    public ProductIdentifier(String propertyString)
    {
        super(propertyString);
    }
    
    public ProductIdentifier(ProductIdentifier source)
    {
        super(source);
    }
    
    public ProductIdentifier()
    {
        super();
    }
}
