package jfxtras.labs.icalendar.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.change.Sequence;
import jfxtras.labs.icalendar.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;

public enum PropertyEnum
{
    ACTION ("ACTION", null, null), // Alarm
    ATTACHMENT ("ATTACH", null, Attachment.class), // Descriptive
    ATTENDEE ("ATTENDEE", null, null), // Relationship
    CALENDAR_SCALE ("CALSCALE", null, CalendarScale.class), // Calendar
    CATEGORIES ("CATEGORIES", null, Categories.class), // Descriptive
    CLASSIFICATION ("CLASS", null, null), // Descriptive
    COMMENT ("COMMENT", null, null), // Descriptive
    CONTACT ("CONTACT", null, null), // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", null, null), // Date and Time
    DATE_TIME_CREATED ("CREATED", null, DateTimeCreated.class), // Change management
    DATE_TIME_DUE ("DUE", null, null), // Date and Time
    DATE_TIME_END ("DTEND", null, null), // Date and Time
    DATE_TIME_STAMP ("DTSTAMP", null, DateTimeStamp.class), // Change management
    DATE_TIME_START ("DTSTART", null, DateTimeStart.class), // Date and Time
    DESCRIPTION ("DESCRIPTION", null, Description.class), // Descriptive
    DURATION ("DURATION", null, null), // Date and Time
    EXCEPTION_DATE_TIMES ("EXDATE", null, null), // Recurrence
    FREE_BUSY_TIME ("FREEBUSY", null, null), // Date and Time
    GEOGRAPHIC_POSITION ("GEO", null, null), // Descriptive
    LOCATION ("LOCATION", Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE), null), // Descriptive
    METHOD ("METHOD", null, null), // Calendar
    ORGANIZER ("ORGANIZER", null, null),  // Relationship
    PERCENT_COMPLETE ("PERCENT", null, null), // Descriptive
    PRIORITY ("PRIORITY", null, null), // Descriptive
    PRODUCT_IDENTIFIER ("PRODID", null, null), // Calendar
    RECURRENCE_DATE_TIMES ("RDATE", null, null), // Recurrence
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", null, null),  // Relationship
    RECURRENCE_RULE ("RRULE", null, null), // Recurrence
    RELATED_TO ("RELATED-TO", null, null), // Relationship
    REPEAT_COUNT ("REPEAT", null, null), // Alarm
    REQUEST_STATUS ("REQUEST-STATUS", null, RequestStatus.class), // Miscellaneous
    RESOURCES ("RESOURCES", null, null), // Descriptive
    SEQUENCE ("SEQUENCE", null, Sequence.class), // Change management
    STATUS ("STATUS", null, null), // Descriptive
    SUMMARY ("SUMMARY", null, Summary.class), // Descriptive
    TIME_TRANSPARENCY ("TRANSP", null, null), // Date and Time
    TIME_ZONE_IDENTIFIER ("TZID", null, null), // Time Zone
    TIME_ZONE_NAME ("TZNAME", null, null), // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", null, null), // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", null, null), // Time Zone
    TIME_ZONE_URL ("TZURL", null, null), // Time Zone
    TRIGGER ("TRIGGER", null, null),  // Alarm
    UNIQUE_IDENTIFIER ("UID", null, null), // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", null, null), // Relationship
    VERSION ("VERSION", null, null); // Calendar
    
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
    private List<ParameterEnum> allowedParameters;
    @Override
    public String toString() { return name; }
    
    PropertyEnum(String name, List<ParameterEnum> allowedParameters, Class<? extends Property<?>> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.myClass = myClass;
    }   
    public Collection<ParameterEnum> possibleParameters() { return allowedParameters; }

}
