package jfxtras.labs.icalendar.properties;

import java.util.HashMap;
import java.util.Map;

public enum PropertyType
{
    ACTION ("ACTION"),  // Alarm
    ATTACHMENT ("ATTACH"), // Descriptive
    ATTENDEE ("ATTENDEE"), // Relationship
    CALENDAR_SCALE ("CALSCALE"), // Calendar
    CATEGORIES ("CATEGORIES"), // Descriptive
    CLASSIFICATION ("CLASS"), // Descriptive
    COMMENT ("COMMENT"), // Descriptive
    CONTACT ("CONTACT"), // Relationship
    DATE_TIME_COMPLETED ("COMPLETED"),
    DATE_TIME_DUE ("DUE"), // Date and Time
    DATE_TIME_END ("DTEND"), // Date and Time
    DATE_TIME_START ("DTSTART"), // Date and Time
    DESCRIPTION ("DESCRIPTION"), // Descriptive
    DURATION ("DURATION"), // Date and Time
    EXCEPTION_DATE_TIMES ("EXDATE"), // Recurrence
    FREE_BUSY_TIME ("FREEBUSY"), // Date and Time
    GEOGRAPHIC_POSITION ("GEO"), // Descriptive
    LOCATION ("LOCATION"), // Descriptive
    METHOD ("METHOD"), // Calendar
    ORGANIZER ("ORGANIZER"),  // Relationship
    PERCENT_COMPLETE ("PERCENT"), // Descriptive
    PRIORITY ("PRIORITY"), // Descriptive
    PRODUCT_IDENTIFIER ("PRODID"), // Calendar
    RECURRENCE_DATE_TIMES ("RDATE"), // Recurrence
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID"),  // Relationship
    RECURRENCE_RULE ("RRULE"), // Recurrence
    RELATED_TO ("RELATED-TO"), // Relationship
    REPEAT_COUNT ("REPEAT"), // Alarm
    RESOURCES ("RESOURCES"), // Descriptive
    STATUS ("STATUS"), // Descriptive
    SUMMARY ("SUMMARY"), // Descriptive
    TIME_TRANSPARENCY ("TRANSP"), // Date and Time
    TIME_ZONE_IDENTIFIER ("TZID"), // Time Zone
    TIME_ZONE_NAME ("TZNAME"), // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM"), // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO"), // Time Zone
    TIME_ZONE_URL ("TZURL"), // Time Zone
    TRIGGER ("TRIGGER"),  // Alarm
    UNIQUE_IDENTIFIER ("UID"), // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL"), // Relationship
    VERSION ("VERSION"); // Calendar
    
    // Map to match up name to enum
    private static Map<String, PropertyType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, PropertyType> makeEnumFromNameMap()
    {
        Map<String, PropertyType> map = new HashMap<>();
        PropertyType[] values = PropertyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static PropertyType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    PropertyType(String name)
    {
        this.name = name;
    }
}
