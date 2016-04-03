package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentBase64;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentURI;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;

public enum PropertyEnum
{
    ACTION ("ACTION", null, null, null), // Alarm
    ATTACHMENT_URI ("ATTACH", ValueType.UNIFORM_RESOURCE_IDENTIFIER, Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.VALUE_DATA_TYPES), AttachmentURI.class), // Descriptive
    ATTACHMENT_BASE64 ("ATTACH", ValueType.BINARY, Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.INLINE_ENCODING, ParameterEnum.VALUE_DATA_TYPES), AttachmentBase64.class), // Descriptive
    ATTENDEE ("ATTENDEE", null, null, null), // Relationship
    CALENDAR_SCALE ("CALSCALE", null, null, CalendarScale.class), // Calendar
    CATEGORIES ("CATEGORIES", ValueType.TEXT, Arrays.asList(ParameterEnum.LANGUAGE), Categories.class), // Descriptive
    CLASSIFICATION ("CLASS", null, null, null), // Descriptive
    COMMENT ("COMMENT", ValueType.TEXT, null, null), // Descriptive
    CONTACT ("CONTACT", null, null, null), // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", null, null, null), // Date and Time
    DATE_TIME_CREATED ("CREATED", null, null, DateTimeCreated.class), // Change management
    DATE_TIME_DUE ("DUE", null, null, null), // Date and Time
    DATE_TIME_END ("DTEND", null, null, null), // Date and Time
    DATE_TIME_STAMP ("DTSTAMP", null, null, DateTimeStamp.class), // Change management
    DATE_TIME_START_DATE ("DTSTART", ValueType.DATE, Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), DTStartLocalDate.class), // Date and Time
    DATE_TIME_START_LOCAL_DATE_TIME ("DTSTART", ValueType.DATE_LOCAL_DATE_TIME, Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), DTStartLocalDateTime.class), // Date and Time
    DATE_TIME_START_ZONED_DATE_TIME ("DTSTART", ValueType.DATE_ZONED_DATE_TIME, Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), DTStartZonedDateTime.class), // Date and Time
    DESCRIPTION ("DESCRIPTION", ValueType.TEXT, Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE), Description.class), // Descriptive
    DURATION ("DURATION", null, null, null), // Date and Time
    EXCEPTION_DATE_TIMES ("EXDATE", null, null, null), // Recurrence
    FREE_BUSY_TIME ("FREEBUSY", null, null, null), // Date and Time
    GEOGRAPHIC_POSITION ("GEO", null, null, null), // Descriptive
    LOCATION ("LOCATION", ValueType.TEXT, Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE), Location.class), // Descriptive
    METHOD ("METHOD", null, null, null), // Calendar
    ORGANIZER ("ORGANIZER", null, null, null),  // Relationship
    PERCENT_COMPLETE ("PERCENT", null, null, null), // Descriptive
    PRIORITY ("PRIORITY", null, null, null), // Descriptive
    PRODUCT_IDENTIFIER ("PRODID", null, null, null), // Calendar
    RECURRENCE_DATE_TIMES ("RDATE", null, null, null), // Recurrence
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", null, null, null),  // Relationship
    RECURRENCE_RULE ("RRULE", null, null, null), // Recurrence
    RELATED_TO ("RELATED-TO", null, null, null), // Relationship
    REPEAT_COUNT ("REPEAT", null, null, null), // Alarm
    REQUEST_STATUS ("REQUEST-STATUS", null, null, RequestStatus.class), // Miscellaneous
    RESOURCES ("RESOURCES", null, null, null), // Descriptive
    SEQUENCE ("SEQUENCE", null, null, Sequence.class), // Change management
    STATUS ("STATUS", null, null, null), // Descriptive
    SUMMARY ("SUMMARY", ValueType.TEXT, Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE), Summary.class), // Descriptive
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
    
    // Map to match up name to enum
    // TODO - RETURN LIST
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
            map.put(new Pair<String, ValueType>(values[i].toString(), values[i].getValueType()), values[i]);
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
    
    private String name;
    private ValueType valueType;
    private Class<? extends Property> myClass;
    private List<ParameterEnum> allowedParameters;
    @Override
    public String toString() { return name; }
    public ValueType getValueType() { return valueType; }
    
    PropertyEnum(String name, ValueType valueType, List<ParameterEnum> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueType = valueType;
        this.myClass = myClass;
    }   
    public Collection<ParameterEnum> possibleParameters() { return allowedParameters; }

}
