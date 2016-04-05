package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.Range.RecurrenceIdentifier;

/**
 * RANGE
 * Recurrence Identifier Range
 * RFC 5545, 3.2.13, page 23
 * 
 * To specify the effective range of recurrence instances from
 *  the instance specified by the recurrence identifier specified by
 *  the property.
 * 
 * Example:
 * RECURRENCE-ID;RANGE=THISANDFUTURE:19980401T133000Z
 * 
 * @author David Bal
 *
 */
public class Range extends ParameterBase<Range, RecurrenceIdentifier>
{
    public Range()
    {
        super();
    }
  
    public Range(RecurrenceIdentifier value)
    {
        super(value);
    }

    public Range(String content)
    {
        super(RecurrenceIdentifier.enumFromName(content));
    }
    
    public Range(Range source)
    {
        super(source);
    }
    
    public enum RecurrenceIdentifier
    {
        THIS_AND_FUTURE ("THISANDFUTURE"),
        THIS_AND_PRIOR ("THISANDPRIOR"); // "THISANDPRIOR" is deprecated by this revision of iCalendar and MUST NOT be generated by applications.
        
        private static Map<String, RecurrenceIdentifier> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, RecurrenceIdentifier> makeEnumFromNameMap()
        {
            Map<String, RecurrenceIdentifier> map = new HashMap<>();
            RecurrenceIdentifier[] values = RecurrenceIdentifier.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static RecurrenceIdentifier enumFromName(String propertyName)
        {
            return enumFromNameMap.get(propertyName.toUpperCase());
        }
        
        private String name;
        @Override public String toString() { return name; }
        RecurrenceIdentifier(String name)
        {
            this.name = name;
        }
    }
}