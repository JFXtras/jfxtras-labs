package jfxtras.labs.icalendar.properties.component.time;

import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.time.TimeTransparency.TransparencyType;

/**
 * TRANSP
 * Time Transparency
 * RFC 5545 iCalendar 3.8.2.7. page 101
 * 
 * This property defines whether or not an event is transparent to busy time searches.
 * Events that consume actual time SHOULD be recorded as OPAQUE.  Other
 * events, which do not take up time SHOULD be recorded as TRANSPARENT.
 *    
 * Example:
 * TRANSP:TRANSPARENT
 *
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 */
    // TODO - REMOVE UNKNOWN AS VALUE - MAKE PROPER WITH REAL UNKNOWN WORK BEFORE DELETING CODE
public class TimeTransparency extends PropertyBase<TimeTransparency, TransparencyType>
{
    private final static StringConverter<TransparencyType> CONVERTER = new StringConverter<TransparencyType>()
    {
        @Override
        public String toString(TransparencyType object)
        {
            if (object == TransparencyType.UNKNOWN)
            {
                // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
                return null;
            }
            return object.toString();
        }

        @Override
        public TransparencyType fromString(String string)
        {
            return TransparencyType.enumFromName(string);
        }
    };
//    private String unknownValue; // contains exact string for unknown property value

    public TimeTransparency(CharSequence contentLine)
    {
        super(contentLine, CONVERTER);
        
    }
    
    public TimeTransparency(TransparencyType value)
    {
        super(value, CONVERTER);
    }
    
    public TimeTransparency(TimeTransparency source)
    {
        super(source);
    }
    
    public TimeTransparency()
    {
        super(TransparencyType.OPAQUE, CONVERTER); // default value
    }
    
//    @Override
//    protected TransparencyType valueFromString(String propertyValueString)
//    {
//        TransparencyType type = TransparencyType.enumFromName(propertyValueString);
//        if (type == TransparencyType.UNKNOWN)
//        {
//            unknownValue = propertyValueString;
//        }
//        return type;
//    }
//    
//    @Override
//    protected String valueToString(TransparencyType value)
//    {
//        if (value == TransparencyType.UNKNOWN)
//        {
//            return unknownValue;
//        }
//        return getValue().toString();
//    }
    
    public enum TransparencyType
    {
        OPAQUE,
        TRANSPARENT,
        UNKNOWN;
        
        private static Map<String, TransparencyType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, TransparencyType> makeEnumFromNameMap()
        {
            Map<String, TransparencyType> map = new HashMap<>();
            TransparencyType[] values = TransparencyType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static TransparencyType enumFromName(String propertyName)
        {
            TransparencyType type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
    }
}
