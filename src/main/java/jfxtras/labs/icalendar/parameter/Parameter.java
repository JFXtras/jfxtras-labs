package jfxtras.labs.icalendar.parameter;

public enum Parameter
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP"),
    COMMON_NAME ("CN"),
    CALENDAR_USER_TYPE ("CUTYPE"),
    DELEGATORS ("DELEGATED-FROM"),
    DELEGATEES ("DELEGATED-TO"),
    DIRECTORY_ENTRY_REFERENCE ("DIR"),
    INLINE_ENCODING ("ENCODING"),
    FORMAT_TYPE ("FMTTYPE"),
    FREE_BUSY_TIME_TYPE ("FBTYPE"),
    LANGUAGE ("LANGUAGE"),
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER"),
    PARTICIPATION_STATUS ("PARTSTAT"),
    RECURRENCE_IDENTIFIER_RANGE ("RANGE"),
    ALARM_TRIGGER_RELATIONSHIP ("RELATED"),
    RELATIONSHIP_TYPE ("RELTYPE"),
    PARTICIPATION_ROLE ("ROLE"),
    RSVP_EXPECTATION ("RSVP"),
    SENT_BY ("SENT-BY"),
    TIME_ZONE_IDENTIFIER ("TZID"),
    VALUE_DATE_TYPES ("VALUE");
    
    private String name;
    @Override
    public String toString() { return name; }
    
    Parameter(String name)
    {
        this.name = name;
    }
}
