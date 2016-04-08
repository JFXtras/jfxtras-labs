package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.DateTimeException;
import java.time.ZoneId;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VTimeZone;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;

/**
 * TZID
 * Time Zone Identifier
 * RFC 5545, 3.8.3.1, page 102
 * 
 * To specify the identifier for the time zone definition for
 * a time component in the property value
 * 
 * LIMITATION: globally unique time zones are stored as strings and the ZoneID is null.
 * Only the toString and toContentLine methods will display the original string.  Another
 * method to convert the unique time zone string into a ZoneId is required.
 * 
 * EXAMPLE:
 * TZID:America/Los_Angeles
 * 
 * @author David Bal
 * @see VTimeZone
 */
public class TimeZoneIdentifier extends PropertyBase<TimeZoneIdentifier, ZoneId>
{
    private final static StringConverter<ZoneId> CONVERTER = new StringConverter<ZoneId>()
    {
        @Override
        public String toString(ZoneId object)
        {
            return (object == null) ? null: object.toString();
        }

        @Override
        public ZoneId fromString(String string)
        {
            try
            {
            return ZoneId.of(string);
            } catch (DateTimeException e)
            {
                // null means value is unknown value and should be stored as non-converted string
                return null;
            }           
        }
    };
    
//    private String unknownValue; // contains exact string for unknown property value

    public TimeZoneIdentifier(CharSequence contentLine)
    {
        super(contentLine, CONVERTER);
    }
    
    public TimeZoneIdentifier(TimeZoneIdentifier source)
    {
        super(source);
    }
    
    public TimeZoneIdentifier(ZoneId value)
    {
        super(value, CONVERTER);
    }
    
    @Override
    public boolean isValid()
    {
        boolean nonGlobalOK = (getValue() != null);
        boolean globallyUniqueOK = ((getUnknownValue() != null) && (getUnknownValue().charAt(0) == '/'));
        boolean valueTypeOK = ((getValueParameter() == null) || (getValueParameter().equals(ValueType.TEXT)));
        return (nonGlobalOK || globallyUniqueOK) && valueTypeOK;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean superEquals = super.equals(obj);
        if (superEquals == false)
        {
            return false;
        }
        TimeZoneIdentifier testObj = (TimeZoneIdentifier) obj;
        boolean unknownEquals = (getUnknownValue() == null) ? testObj.getUnknownValue() == null : getUnknownValue().equals(testObj.getUnknownValue());
        return unknownEquals;
    }
}
