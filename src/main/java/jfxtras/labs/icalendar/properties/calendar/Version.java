package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.properties.PropertyTextBase;
import jfxtras.labs.icalendar.properties.PropertyType;

public class Version extends PropertyTextBase<Version>
{
    private final static String NAME = PropertyType.VERSION.toString();

    @Override
    public void parseAndSetValue(String value)
    {
        // TODO Auto-generated method stub
        
    }
    
    public Version(String propertyString)
    {
        super(NAME, propertyString);
    }
}
