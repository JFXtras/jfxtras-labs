package jfxtras.labs.icalendar.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.properties.PropertyEnum;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START_LOCAL_DATE,
            PropertyEnum.DATE_TIME_START_LOCAL_DATE_TIME, PropertyEnum.DATE_TIME_START_ZONED_DATE_TIME,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTODO (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VJOURNAL (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTIMEZONE (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VALARM (Arrays.asList(PropertyEnum.ATTACHMENT_BASE64, PropertyEnum.ATTACHMENT_URI));

    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties)
    {
        this.allowedProperties = allowedProperties;
    }
}
