package jfxtras.labs.icalendarfx.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;

/**
 * PARTSTAT
 * Participation Status
 * RFC 5545, 3.2.12, page 22
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class ParticipationStatus extends ParameterBase<ParticipationStatus, ParticipationStatusType>
{
    private String unknownValue; // contains exact string for unknown value

    /** Set NEEDS-ACTION as default value */
    public ParticipationStatus()
    {
        super(ParticipationStatusType.NEEDS_ACTION); // default value
    }
  
    public ParticipationStatus(ParticipationStatusType value)
    {
        super(value);
    }
    
    public ParticipationStatus(String content)
    {
        super(ParticipationStatusType.enumFromName(content));
        if (getValue() == ParticipationStatusType.UNKNOWN)
        {
            unknownValue = content;
        }
    }

    public ParticipationStatus(ParticipationStatus source)
    {
        super(source);
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == ParticipationStatusType.UNKNOWN) ? unknownValue : getValue().toString();
        String parameterName = myParameterEnum().toString();
        return ";" + parameterName + "=" + value;
    }

    public enum ParticipationStatusType
    {
        NEEDS_ACTION ("NEEDS-ACTION"),  // VEvent, VTodo, VJournal - DEFAULT VALUE
        ACCEPTED ("ACCEPTED"),          // VEvent, VTodo, VJournal
        COMPLETED ("COMPLETED"),        // VTodo
        DECLINED ("DECLINED"),          // VEvent, VTodo, VJournal
        IN_PROCESS ("IN-PROCESS"),      // VTodo
        TENTATIVE ("TENTATIVE"),        // VEvent, VTodo
        DELEGATED ("DELEGATED"),        // VEvent, VTodo
        UNKNOWN ("UNKNOWN");
        
        private static Map<String, ParticipationStatusType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, ParticipationStatusType> makeEnumFromNameMap()
        {
            Map<String, ParticipationStatusType> map = new HashMap<>();
            ParticipationStatusType[] values = ParticipationStatusType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static ParticipationStatusType enumFromName(String propertyName)
        {
            ParticipationStatusType type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private String name;
        @Override public String toString() { return name; }
        ParticipationStatusType(String name)
        {
            this.name = name;
        }
    }
}