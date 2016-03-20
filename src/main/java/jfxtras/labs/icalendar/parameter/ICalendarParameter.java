package jfxtras.labs.icalendar.parameter;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.VComponentProperty;

public enum ICalendarParameter
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP"),
    COMMON_NAME ("CN"),
    CALENDAR_USER_TYPE ("CUTYPE"),
    DELEGATORS ("DELEGATED-FROM"),
    DELEGATEES ("DELEGATED-TO"),
    DIRECTORY_ENTRY_REFERENCE ("DIR"),
    INLINE_ENCODING ("ENCODING"),
    FORMAT_TYPE ("FMTTYPE"),
    FREE_BUSY_TIME_TYPE ("FBTYPE"),
    LANGUAGE ("LANGUAGE"),
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER"),
    PARTICIPATION_STATUS ("PARTSTAT"),
    RECURRENCE_IDENTIFIER_RANGE ("RANGE"),
    ALARM_TRIGGER_RELATIONSHIP ("RELATED"),
    RELATIONSHIP_TYPE ("RELTYPE"),
    PARTICIPATION_ROLE ("ROLE"),
    RSVP_EXPECTATION ("RSVP"),
    SENT_BY ("SENT-BY"),
    TIME_ZONE_IDENTIFIER ("TZID"),
    VALUE_DATE_TYPES ("VALUE");
    
    // Map to match up name to ICalendarParameter
    private static Map<String, ICalendarParameter> propertyFromNameMap = makePropertiesFromNameMap();
    private static Map<String, ICalendarParameter> makePropertiesFromNameMap()
    {
        Map<String, ICalendarParameter> map = new HashMap<>();
        ICalendarParameter[] values = ICalendarParameter.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static ICalendarParameter propertyFromName(String propertyName)
    {
        return propertyFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    /**
     * The Parameter values for TextProperty1
     */
    public static ICalendarParameter[] textProperty1ParameterValues()
    {
        return new ICalendarParameter[] { LANGUAGE };
    }
    
    ICalendarParameter(String name)
    {
        this.name = name;
    }
    public void setValue(VComponentProperty textProperty1, String value)
    {
        // TODO Auto-generated method stub
        // instead of sending property send callback containing how to set value?
        // how about a bunch of interfaces to define all the setters and getters?
        
    }
//    // TODO - PROBLEM - CAN'T COPY PARAMETERS - THEY ARE IN MULTIPLE PROPERTIES
//    public Object copyProperty(ComponentProperty source, ComponentProperty destination)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
}
