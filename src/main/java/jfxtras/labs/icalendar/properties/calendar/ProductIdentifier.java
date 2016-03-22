package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.properties.PropertyTextBase;
import jfxtras.labs.icalendar.properties.PropertyType;

public class ProductIdentifier extends PropertyTextBase<ProductIdentifier>
{
    private final static String NAME = PropertyType.PRODUCT_IDENTIFIER.toString();

    @Override
    public void parseAndSetValue(String value)
    {
        // TODO Auto-generated method stub
        
    }

    public ProductIdentifier()
    {
        super(NAME);
    }
}
