package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.properties.PropertyEnum;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.NON_STANDARD,
            PropertyEnum.ORGANIZER, PropertyEnum.RECURRENCE_DATE_TIMES, PropertyEnum.RECURRENCE_RULE,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.SUMMARY, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTODO (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.NON_STANDARD,
            PropertyEnum.ORGANIZER, PropertyEnum.RECURRENCE_DATE_TIMES, PropertyEnum.RECURRENCE_RULE,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.SUMMARY, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VJOURNAL (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.NON_STANDARD,
            PropertyEnum.ORGANIZER, PropertyEnum.RECURRENCE_DATE_TIMES, PropertyEnum.RECURRENCE_RULE,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.SUMMARY, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTIMEZONE (Arrays.asList(PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED, PropertyEnum.NON_STANDARD,
            PropertyEnum.TIME_ZONE_IDENTIFIER, PropertyEnum.TIME_ZONE_URL)),
    DAYLIGHT (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE)),
    STANDARD (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE)),
    VALARM (Arrays.asList(PropertyEnum.ACTION, PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.DESCRIPTION,
            PropertyEnum.DURATION, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.REPEAT_COUNT,
            PropertyEnum.SUMMARY, PropertyEnum.TRIGGER)),
    VFREEBUSY (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT,
            PropertyEnum.DATE_TIME_STAMP, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER));

    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties)
    {
        this.allowedProperties = allowedProperties;
    }
}
