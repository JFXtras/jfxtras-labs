package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.ParticipationRole.ParticipationRoleType;

/**
 * ROLE
 * Participation Role
 * RFC 5545, 3.2.16, page 25
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class ParticipationRole extends ParameterBase<ParticipationRole, ParticipationRoleType>
{
    public ParticipationRole()
    {
        super(ParticipationRoleType.REQUIRED_PARTICIPANT); // default value
    }
  
    public ParticipationRole(ParticipationRoleType value)
    {
        super(value);
    }

    public ParticipationRole(String content)
    {
        super(ParticipationRoleType.enumFromName(content));
    }
    
    public ParticipationRole(ParticipationRole source)
    {
        super(source);
    }
    
    public enum ParticipationRoleType
    {
        CHAIR ("CHAIR"),
        REQUIRED_PARTICIPANT ("REQ-PARTICIPANT"),
        OPTIONAL_PARTICIPANT ("OPT-PARTICIPANT"),
        NON_PARTICIPANT ("NON-PARTICIPANT");
        
        private static Map<String, ParticipationRoleType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, ParticipationRoleType> makeEnumFromNameMap()
        {
            Map<String, ParticipationRoleType> map = new HashMap<>();
            ParticipationRoleType[] values = ParticipationRoleType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static ParticipationRoleType enumFromName(String propertyName)
        {
            return enumFromNameMap.get(propertyName.toUpperCase());
        }
        
        private String name;
        @Override public String toString() { return name; }
        ParticipationRoleType(String name)
        {
            this.name = name;
        }
    }
}
