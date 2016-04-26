package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.properties.PropertyEnum;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_CREATED,
            PropertyEnum.DATE_TIME_END, PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START,
            PropertyEnum.DESCRIPTION, PropertyEnum.DURATION, PropertyEnum.EXCEPTION_DATE_TIMES,
            PropertyEnum.GEOGRAPHIC_POSITION, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.LOCATION, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER, PropertyEnum.PRIORITY,
            PropertyEnum.RECURRENCE_DATE_TIMES, PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.REQUEST_STATUS,  PropertyEnum.RESOURCES, PropertyEnum.SEQUENCE,
            PropertyEnum.STATUS, PropertyEnum.SUMMARY, PropertyEnum.TIME_TRANSPARENCY, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTODO (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_COMPLETED,
            PropertyEnum.DATE_TIME_CREATED, PropertyEnum.DATE_TIME_DUE, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION, PropertyEnum.DURATION,
            PropertyEnum.EXCEPTION_DATE_TIMES, PropertyEnum.GEOGRAPHIC_POSITION, PropertyEnum.IANA_PROPERTY,
            PropertyEnum.LAST_MODIFIED, PropertyEnum.LOCATION,  PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.PERCENT_COMPLETE, PropertyEnum.PRIORITY, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO, PropertyEnum.RECURRENCE_RULE,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.RESOURCES, PropertyEnum.SEQUENCE, PropertyEnum.STATUS,
            PropertyEnum.SUMMARY, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VJOURNAL (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_CREATED,
            PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION,
            PropertyEnum.EXCEPTION_DATE_TIMES, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO, PropertyEnum.RECURRENCE_RULE, 
            PropertyEnum.REQUEST_STATUS, PropertyEnum.SEQUENCE, PropertyEnum.STATUS, PropertyEnum.SUMMARY,
            PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTIMEZONE (Arrays.asList(PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED, PropertyEnum.NON_STANDARD,
            PropertyEnum.TIME_ZONE_IDENTIFIER, PropertyEnum.TIME_ZONE_URL)),
    DAYLIGHT (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.TIME_ZONE_NAME, PropertyEnum.TIME_ZONE_OFFSET_FROM,
            PropertyEnum.TIME_ZONE_OFFSET_TO)),
    STANDARD (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.TIME_ZONE_NAME, PropertyEnum.TIME_ZONE_OFFSET_FROM,
            PropertyEnum.TIME_ZONE_OFFSET_TO)),
    VALARM (Arrays.asList(PropertyEnum.ACTION, PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.DESCRIPTION,
            PropertyEnum.DURATION, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.REPEAT_COUNT,
            PropertyEnum.SUMMARY, PropertyEnum.TRIGGER)),
    VFREEBUSY (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.CONTACT,
            PropertyEnum.DATE_TIME_END, PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START,
            PropertyEnum.FREE_BUSY_TIME, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR));

    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties)
    {
        this.allowedProperties = allowedProperties;
    }
}
