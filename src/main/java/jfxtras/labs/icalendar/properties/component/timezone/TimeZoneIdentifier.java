package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.DateTimeException;
import java.time.ZoneId;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VTimeZone;
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
public class TimeZoneIdentifier extends PropertyBase<ZoneId,TimeZoneIdentifier>
{
    private final static StringConverter<ZoneId> CONVERTER = new StringConverter<ZoneId>()
    {
        @Override
        public String toString(ZoneId object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
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
                // null means value is unknown and should be stored as non-converted string by PropertyBase
                return null;
            }           
        }
    };

    public TimeZoneIdentifier(CharSequence contentLine)
    {
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
    }
    
    public TimeZoneIdentifier(TimeZoneIdentifier source)
    {
        super(source);
    }
    
    public TimeZoneIdentifier(ZoneId value)
    {
        super();
        setConverter(CONVERTER);
        setValue(value);
    }
    
    @Override
    public boolean isValid()
    {
        boolean isNonGlobalOK = (getValue() != null);
        boolean isGloballyUniqueOK = ((getUnknownValue() != null) && (getUnknownValue().charAt(0) == '/'));
        boolean isValueTypeOK = propertyType().allowedValueTypes().contains(getValueParameter().getValue());
//        System.out.println("TimeZoneIdentifier isValid:" + isNonGlobalOK + " " + isGloballyUniqueOK + " " + isValueTypeOK);
        return (isNonGlobalOK || isGloballyUniqueOK) && isValueTypeOK;
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
