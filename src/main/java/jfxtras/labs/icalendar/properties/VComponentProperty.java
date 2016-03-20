package jfxtras.labs.icalendar.properties;

public enum VComponentProperty
{
    ACTION ("ACTION"),  // Alarm
    ATTACHMENT ("ATTACH"), // Descriptive
    ATTENDEE ("ATTENDEE"), // Relationship
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
    ORGANIZER ("ORGANIZER"),  // Relationship
    PERCENT_COMPLETE ("PERCENT"), // Descriptive
    PRIORITY ("PRIORITY"), // Descriptive
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
    UNIFORM_RESOURCE_LOCATOR ("URL"); // Relationship
    
    private String name;
    @Override
    public String toString() { return name; }
    
    VComponentProperty(String name)
    {
        this.name = name;
    }
}
