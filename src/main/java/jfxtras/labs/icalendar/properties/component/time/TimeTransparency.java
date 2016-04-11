package jfxtras.labs.icalendar.properties.component.time;

import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VEventNew;
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
 * @see VEventNew
 */
    // TODO - REMOVE UNKNOWN AS VALUE - MAKE PROPER WITH REAL UNKNOWN WORK BEFORE DELETING CODE
public class TimeTransparency extends PropertyBase<TransparencyType,TimeTransparency>
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

    public TimeTransparency(CharSequence contentLine)
    {
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
        
    }
    
    public TimeTransparency(TransparencyType value)
    {
        super();
        setConverter(CONVERTER);
        setValue(value);
    }
    
    public TimeTransparency(TimeTransparency source)
    {
        super(source);
    }
    
    public TimeTransparency()
    {
        super();
        setConverter(CONVERTER);
        setValue(TransparencyType.OPAQUE); // default value
    }
    
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
