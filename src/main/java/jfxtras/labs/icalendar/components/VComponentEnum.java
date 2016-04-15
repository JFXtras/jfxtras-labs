package jfxtras.labs.icalendar.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.properties.PropertyEnum;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD,
            PropertyEnum.ORGANIZER, PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTODO (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VJOURNAL (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTIMEZONE (Arrays.asList(PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED, PropertyEnum.NON_STANDARD,
            PropertyEnum.TIME_ZONE_IDENTIFIER, PropertyEnum.TIME_ZONE_URL)),
    STANDARD_OR_SAVINGS_TIME (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD)),
    VALARM (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.COMMENT, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD)),
    VFREEBUSY (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.COMMENT,
            PropertyEnum.DATE_TIME_STAMP, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR));

    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties)
    {
        this.allowedProperties = allowedProperties;
    }
}
