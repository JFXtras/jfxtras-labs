package jfxtras.labs.icalendarfx.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

/**
 * FBTYPE
 * Free/Busy Time Type
 * RFC 5545, 3.2.9, page 20
 * 
 * To specify the free or busy time type.
 * 
 * Example:
 * FREEBUSY;FBTYPE=BUSY:19980415T133000Z/19980415T170000Z
 * 
 * @author David Bal
 * @see FreeBusyTime
 */
public class FreeBusyType extends ParameterBase<FreeBusyType, FreeBusyTypeEnum>
{
    private String unknownValue; // contains exact string for unknown value

    /** set BUSY as default FreeBusy type value */
    public FreeBusyType()
    {
        super(FreeBusyTypeEnum.BUSY); // default value
    }
  
    public FreeBusyType(FreeBusyTypeEnum value)
    {
        super(value);
    }
    
    public FreeBusyType(FreeBusyType source)
    {
        super(source);
        unknownValue = source.unknownValue;
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == FreeBusyTypeEnum.UNKNOWN) ? unknownValue : getValue().toString();
        return valueToContent(value);
    }
    
    @Override
    public void parseContent(String content)
    {
        setValue(FreeBusyTypeEnum.enumFromName(content));
        if (getValue() == FreeBusyTypeEnum.UNKNOWN)
        {
            unknownValue = content;
        }
    }
    
    public enum FreeBusyTypeEnum
    {
        FREE (Arrays.asList("FREE")), // the time interval is free for scheduling
        BUSY (Arrays.asList("BUSY")), // the time interval is busy because one or more events have been scheduled for that interval - THE DEFAULT
        BUSY_UNAVAILABLE (Arrays.asList("BUSY-UNAVAILABLE", "BUSY_UNAVAILABLE")), // the time interval is busy and that the interval can not be scheduled
        BUSY_TENTATIVE (Arrays.asList("BUSY-TENTATIVE", "BUSY_TENTATIVE")), // the time interval is busy because one or more events have been tentatively scheduled for that interval
        UNKNOWN (Arrays.asList("UNKNOWN"));

        private static Map<String, FreeBusyTypeEnum> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, FreeBusyTypeEnum> makeEnumFromNameMap()
        { // map with multiple names for each type
            Map<String, FreeBusyTypeEnum> map = new HashMap<>();
            Arrays.stream(FreeBusyTypeEnum.values())
                    .forEach(r -> r.names.stream().forEach(n -> map.put(n, r)));
            return map;
        }
        
        /** get enum from name */
        public static FreeBusyTypeEnum enumFromName(String propertyName)
        {
            FreeBusyTypeEnum type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private List<String> names;
        @Override public String toString() { return names.get(0); } // name at index 0 is the correct name from RFC 5545
        FreeBusyTypeEnum(List<String> names)
        {
            this.names = names;
        }
    }

    public static FreeBusyType parse(String content)
    {
        FreeBusyType parameter = new FreeBusyType();
        parameter.parseContent(content);
        return parameter;
    }
}
