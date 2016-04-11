package jfxtras.labs.icalendar.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.properties.PropertyEnum;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTODO (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VJOURNAL (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VTIMEZONE (Arrays.asList(PropertyEnum.UNIFORM_RESOURCE_LOCATOR)),
    VALARM (Arrays.asList(PropertyEnum.ATTACHMENT));

    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties)
    {
        this.allowedProperties = allowedProperties;
    }
}
