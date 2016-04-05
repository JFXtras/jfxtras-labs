package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.change.Sequence;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Status;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentBase64;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentURI;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;
import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDate;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;

public enum PropertyEnum
{
    ACTION ("ACTION", null, null, null), // Alarm
    ATTACHMENT_URI ("ATTACH" // property name
            , ValueType.UNIFORM_RESOURCE_IDENTIFIER // default property value type
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , AttachmentURI.class), // property class
    ATTACHMENT_BASE64 ("ATTACH" // property name
            , ValueType.BINARY // default property value type
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.INLINE_ENCODING, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , AttachmentBase64.class), // property class
    ATTENDEE ("ATTENDEE"    // property name
            , ValueType.CALENDAR_USER_ADDRESS   // default property value type
            , Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.CALENDAR_USER_TYPE, ParameterEnum.DELEGATEES,
                    ParameterEnum.DELEGATORS, ParameterEnum.DIRECTORY_ENTRY_REFERENCE,
                    ParameterEnum.GROUP_OR_LIST_MEMBERSHIP, ParameterEnum.LANGUAGE, ParameterEnum.PARTICIPATION_ROLE,
                    ParameterEnum.PARTICIPATION_STATUS, ParameterEnum.RSVP_EXPECTATION, ParameterEnum.SENT_BY,
                    ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Attendee.class), // property class
    CALENDAR_SCALE ("CALSCALE", null, null, CalendarScale.class), // Calendar
    CATEGORIES ("CATEGORIES" // property name
            , ValueType.TEXT // default property value type
            , Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Categories.class), // property class
    CLASSIFICATION ("CLASS", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Classification.class),
    COMMENT ("COMMENT", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Comment.class),
    CONTACT ("CONTACT", null, null, null), // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCompleted.class), // property class
    DATE_TIME_CREATED ("CREATED", null, null, DateTimeCreated.class), // Change management
    DATE_TIME_DUE ("DUE", null, null, null), // Date and Time
    DATE_TIME_END_LOCAL_DATE ("DTEND", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndLocalDate.class), // property class
    DATE_TIME_END_LOCAL_DATE_TIME ("DTEND", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndLocalDateTime.class), // property class
    DATE_TIME_END_ZONED_DATE_TIME ("DTEND", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndZonedDateTime.class), // property class
    DATE_TIME_STAMP ("DTSTAMP", null, null, DateTimeStamp.class), // Change management
    DATE_TIME_START_LOCAL_DATE ("DTSTART", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartLocalDate.class), // property class
    DATE_TIME_START_LOCAL_DATE_TIME ("DTSTART", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartLocalDateTime.class), // property class
    DATE_TIME_START_ZONED_DATE_TIME ("DTSTART", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartZonedDateTime.class), // property class
    DESCRIPTION ("DESCRIPTION", ValueType.TEXT, Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE), Description.class), // Descriptive
    DURATION ("DURATION", null, null, null), // Date and Time
    EXCEPTION_DATE_TIMES ("EXDATE", null, null, null), // Recurrence
    FREE_BUSY_TIME ("FREEBUSY", null, null, null), // Date and Time
    GEOGRAPHIC_POSITION ("GEO", null, null, null), // Descriptive
    LOCATION ("LOCATION", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Location.class), // property class
    METHOD ("METHOD", null, null, null), // Calendar
    ORGANIZER ("ORGANIZER", // name
            ValueType.CALENDAR_USER_ADDRESS, // default property value type
            Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.DIRECTORY_ENTRY_REFERENCE, ParameterEnum.LANGUAGE,
                    ParameterEnum.SENT_BY), // allowed parameters
            Organizer.class), // property class
    PERCENT_COMPLETE ("PERCENT", null, null, null), // Descriptive
    PRIORITY ("PRIORITY", null, null, null), // Descriptive
    PRODUCT_IDENTIFIER ("PRODID", null, null, null), // Calendar
    RECURRENCE_DATE_TIMES ("RDATE", null, null, null), // Recurrence
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", null, null, null),  // Relationship
    RECURRENCE_RULE ("RRULE", null, null, null), // Recurrence
    RELATED_TO ("RELATED-TO", null, null, null), // Relationship
    REPEAT_COUNT ("REPEAT", null, null, null), // Alarm
    REQUEST_STATUS ("REQUEST-STATUS", null, null, RequestStatus.class), // Miscellaneous
    RESOURCES ("RESOURCES", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Resources.class), // property class
    SEQUENCE ("SEQUENCE", null, null, Sequence.class), // Change management
    STATUS ("STATUS", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Status.class), // property class
    SUMMARY ("SUMMARY", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Summary.class), // property class
    TIME_TRANSPARENCY ("TRANSP", null, null, null), // Date and Time
    TIME_ZONE_IDENTIFIER ("TZID", null, null, null), // Time Zone
    TIME_ZONE_NAME ("TZNAME", null, null, null), // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", null, null, null), // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", null, null, null), // Time Zone
    TIME_ZONE_URL ("TZURL", null, null, null), // Time Zone
    TRIGGER ("TRIGGER", null, null, null),  // Alarm
    UNIQUE_IDENTIFIER ("UID", ValueType.TEXT, null, null), // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", null, null, null), // Relationship
    VERSION ("VERSION", ValueType.TEXT, null, null); // Calendar
    
    // Map to match up name to enum List
    private static Map<String, List<PropertyEnum>> enumListFromNameMap = makeEnumListFromNameMap();
    private static Map<String, List<PropertyEnum>> makeEnumListFromNameMap()
    {
        Map<String, List<PropertyEnum>> map = new HashMap<>();
        Arrays.stream(PropertyEnum.values())
        .forEach(e ->
        {
            if (map.get(e.toString()) == null)
            {
                map.put(e.toString(), new ArrayList<>(Arrays.asList(e)));
            } else
            {
                map.get(e.toString()).add(e);
            }
        });
        return map;
    }
    public static List<PropertyEnum> enumListFromName(String propertyName)
    {
        return enumListFromNameMap.get(propertyName.toUpperCase());
    }
    
    
    private static Map<Pair<String, ValueType>, PropertyEnum> enumFromNameMap = makeEnumFromNameMap();
    private static Map<Pair<String, ValueType>, PropertyEnum> makeEnumFromNameMap()
    {
        Map<Pair<String, ValueType>, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(new Pair<String, ValueType>(values[i].toString(), values[i].defaultValueType()), values[i]);
        }
        return map;
    }
    @Deprecated
    public static PropertyEnum enumFromName(String propertyName, ValueType valueType)
    {
        return enumFromNameMap.get(new Pair<String, ValueType>(propertyName.toUpperCase(),valueType));
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
    
    private Class<? extends Property> myClass;

    @Override
    public String toString() { return name; }
    private String name;
    
    private ValueType valueType;
    public ValueType defaultValueType() { return valueType; }

    private List<ParameterEnum> allowedParameters;
    public List<ParameterEnum> allowedParameters() { return allowedParameters; }
    
    PropertyEnum(String name, ValueType valueType, List<ParameterEnum> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueType = valueType;
        this.myClass = myClass;
    }   
}
