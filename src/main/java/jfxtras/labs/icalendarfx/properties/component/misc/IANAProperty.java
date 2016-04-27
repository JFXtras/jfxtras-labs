package jfxtras.labs.icalendarfx.properties.component.misc;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;

/**
 * IANA Properties
 * RFC 5545, 3.8.8.1, page 140
 * 
 * An IANA-registered property name.
 * 
 * Allows a property in the REGISTERED_IANA_PROPERTY_NAMES list
 * This property can be of any value and have any parameter.  There is no
 * checking or restrictions on the values of the property or parameters.
 * 
 * Examples:
 * DRESSCODE:CASUAL
 * NON-SMOKING;VALUE=BOOLEAN:TRUE
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 * @see VTimeZone
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class IANAProperty extends UnknownProperty<Object, IANAProperty>
{
    public static final List<String> REGISTERED_IANA_PROPERTY_NAMES = 
            Arrays.asList("TESTPROP1", "TESTPROP2");
      
    public IANAProperty(Object value)
    {
        super(value);
    }
    
    public IANAProperty(IANAProperty source)
    {
        super(source);
    }
    
    public IANAProperty()
    {
        super();
    }
    
    @Override
    public boolean isValid()
    {
        return REGISTERED_IANA_PROPERTY_NAMES.contains(getPropertyName()) && super.isValid();
    }
    
    public static IANAProperty parse(String value)
    {
        IANAProperty property = new IANAProperty();
        property.parseContent(value);
        return property;
    }
}
