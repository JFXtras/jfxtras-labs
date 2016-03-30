package jfxtras.labs.icalendar.properties;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.change.Sequence;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;

public enum PropertyEnum
{
    ACTION ("ACTION", null), // Alarm
    ATTACHMENT ("ATTACH", null), // Descriptive
    ATTENDEE ("ATTENDEE", null), // Relationship
    CALENDAR_SCALE ("CALSCALE", CalendarScale.class), // Calendar
    CATEGORIES ("CATEGORIES", Categories.class), // Descriptive
    CLASSIFICATION ("CLASS", null), // Descriptive
    COMMENT ("COMMENT", null), // Descriptive
    CONTACT ("CONTACT", null), // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", null), // Date and Time
    DATE_TIME_CREATED ("CREATED", DateTimeCreated.class), // Change management
    DATE_TIME_DUE ("DUE", null), // Date and Time
    DATE_TIME_END ("DTEND", null), // Date and Time
    DATE_TIME_STAMP ("DTSTAMP", DateTimeStamp.class), // Change management
    DATE_TIME_START ("DTSTART", DateTimeStart.class), // Date and Time
    DESCRIPTION ("DESCRIPTION", Description.class), // Descriptive
    DURATION ("DURATION", null), // Date and Time
    EXCEPTION_DATE_TIMES ("EXDATE", null), // Recurrence
    FREE_BUSY_TIME ("FREEBUSY", null), // Date and Time
    GEOGRAPHIC_POSITION ("GEO", null), // Descriptive
    LOCATION ("LOCATION", null), // Descriptive
    METHOD ("METHOD", null), // Calendar
    ORGANIZER ("ORGANIZER", null),  // Relationship
    PERCENT_COMPLETE ("PERCENT", null), // Descriptive
    PRIORITY ("PRIORITY", null), // Descriptive
    PRODUCT_IDENTIFIER ("PRODID", null), // Calendar
    RECURRENCE_DATE_TIMES ("RDATE", null), // Recurrence
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", null),  // Relationship
    RECURRENCE_RULE ("RRULE", null), // Recurrence
    RELATED_TO ("RELATED-TO", null), // Relationship
    REPEAT_COUNT ("REPEAT", null), // Alarm
    REQUEST_STATUS ("REQUEST-STATUS", RequestStatus.class), // Miscellaneous
    RESOURCES ("RESOURCES", null), // Descriptive
    SEQUENCE ("SEQUENCE", Sequence.class), // Change management
    STATUS ("STATUS", null), // Descriptive
    SUMMARY ("SUMMARY", Summary.class), // Descriptive
    TIME_TRANSPARENCY ("TRANSP", null), // Date and Time
    TIME_ZONE_IDENTIFIER ("TZID", null), // Time Zone
    TIME_ZONE_NAME ("TZNAME", null), // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", null), // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", null), // Time Zone
    TIME_ZONE_URL ("TZURL", null), // Time Zone
    TRIGGER ("TRIGGER", null),  // Alarm
    UNIQUE_IDENTIFIER ("UID", null), // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", null), // Relationship
    VERSION ("VERSION", null); // Calendar
    
    // Map to match up name to enum
    private static Map<String, PropertyEnum> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, PropertyEnum> makeEnumFromNameMap()
    {
        Map<String, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static PropertyEnum enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Property>, PropertyEnum> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Property>, PropertyEnum> makeEnumFromClassMap()
    {
        Map<Class<? extends Property>, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static PropertyEnum enumFromClass(Class<? extends Property> myClass)
    {
        PropertyEnum p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(PropertyEnum.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    private Class<? extends Property<?>> myClass;
    @Override
    public String toString() { return name; }
    
    PropertyEnum(String name, Class<? extends Property<?>> myClass)
    {
        this.name = name;
        this.myClass = myClass;
    }
}
