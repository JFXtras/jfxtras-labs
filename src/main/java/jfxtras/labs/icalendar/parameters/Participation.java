package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.Participation.ParticipationStatus;

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
public class Participation extends ParameterBase<Participation, ParticipationStatus>
{
    private String unknownValue; // contains exact string for unknown value

    /** Set NEEDS-ACTION as default value */
    public Participation()
    {
        super(ParticipationStatus.NEEDS_ACTION); // default value
    }
  
    public Participation(ParticipationStatus value)
    {
        super(value);
    }
    
    public Participation(String content)
    {
        super(ParticipationStatus.enumFromName(content));
        if (getValue() == ParticipationStatus.UNKNOWN)
        {
            unknownValue = content;
        }
    }

    public Participation(Participation source)
    {
        super(source);
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == ParticipationStatus.UNKNOWN) ? unknownValue : getValue().toString();
        String parameterName = myParameterEnum().toString();
        return ";" + parameterName + "=" + value;
    }

    public enum ParticipationStatus
    {
        NEEDS_ACTION ("NEEDS-ACTION"),  // VEvent, VTodo, VJournal - DEFAULT VALUE
        ACCEPTED ("ACCEPTED"),          // VEvent, VTodo, VJournal
        COMPLETED ("COMPLETED"),        // VTodo
        DECLINED ("DECLINED"),          // VEvent, VTodo, VJournal
        IN_PROCESS ("IN-PROCESS"),      // VTodo
        TENTATIVE ("TENTATIVE"),        // VEvent, VTodo
        DELEGATED ("DELEGATED"),        // VEvent, VTodo
        UNKNOWN ("UNKNOWN");
        
        private static Map<String, ParticipationStatus> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, ParticipationStatus> makeEnumFromNameMap()
        {
            Map<String, ParticipationStatus> map = new HashMap<>();
            ParticipationStatus[] values = ParticipationStatus.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static ParticipationStatus enumFromName(String propertyName)
        {
            ParticipationStatus type = enumFromNameMap.get(propertyName.toUpperCase());
            return (type == null) ? UNKNOWN : type;
        }
        
        private String name;
        @Override public String toString() { return name; }
        ParticipationStatus(String name)
        {
            this.name = name;
        }
    }
}