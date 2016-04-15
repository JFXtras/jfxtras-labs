package jfxtras.labs.icalendar.properties.component.misc;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.components.DaylightSavingTime;
import jfxtras.labs.icalendar.components.StandardTime;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTimeZone;
import jfxtras.labs.icalendar.components.VTodo;

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
    
    public IANAProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public IANAProperty(Object value)
    {
        super(value);
    }
    
    public IANAProperty(IANAProperty source)
    {
        super(source);
    }
    
    @Override
    public boolean isValid()
    {
        return REGISTERED_IANA_PROPERTY_NAMES.contains(getPropertyName()) && super.isValid();
    }
}
