package jfxtras.labs.icalendarfx.parameters;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyAlarmTrigger;
import jfxtras.labs.icalendarfx.properties.PropertyAltText;
import jfxtras.labs.icalendarfx.properties.PropertyAttachment;
import jfxtras.labs.icalendarfx.properties.PropertyAttendee;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.PropertyBaseAltText;
import jfxtras.labs.icalendarfx.properties.PropertyBaseLanguage;
import jfxtras.labs.icalendarfx.properties.PropertyDateTime;
import jfxtras.labs.icalendarfx.properties.PropertyFreeBusy;
import jfxtras.labs.icalendarfx.properties.PropertyRecurrenceID;
import jfxtras.labs.icalendarfx.properties.PropertyRelationship;
import jfxtras.labs.icalendarfx.properties.component.relationship.PropertyBaseCalendarUser;

public enum ParameterType
{
    // in properties COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateText.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseAltText<?,?> castProperty = (PropertyBaseAltText<?, ?>) property;
            castProperty.setAlternateText(new AlternateText(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAltText<?> castProperty = (PropertyAltText<?>) parent;
            return castProperty.getAlternateText();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBaseAltText<?,?> castSource = (PropertyBaseAltText<?,?>) source;
            PropertyBaseAltText<?,?> castDestination = (PropertyBaseAltText<?,?>) destination;
            AlternateText parameter = new AlternateText(castSource.getAlternateText());
            castDestination.setAlternateText(parameter);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setCommonName(new CommonName(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getCommonName();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castSource = (PropertyBaseCalendarUser<?,?>) source;
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            CommonName parameter = new CommonName(castSource.getCommonName());
            castDestination.setCommonName(parameter);
        }
    },
    // in property ATTENDEE
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setCalendarUser(new CalendarUser(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getCalendarUser();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            CalendarUser parameter = new CalendarUser(castSource.getCalendarUser());
            castDestination.setCalendarUser(parameter);
        }
    },
    // in property ATTENDEE
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setDelegators(new Delegators(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getDelegators();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            Delegators parameter = new Delegators(castSource.getDelegators());
            castDestination.setDelegators(parameter);
        }
    },
    // in property ATTENDEE
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setDelegatees(new Delegatees(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getDelegatees();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            Delegatees parameter = new Delegatees(castSource.getDelegatees());
            castDestination.setDelegatees(parameter);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setDirectoryEntryReference(new DirectoryEntryReference(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getDirectoryEntryReference();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castSource = (PropertyBaseCalendarUser<?,?>) source;
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            DirectoryEntryReference parameter = new DirectoryEntryReference(castSource.getDirectoryEntryReference());
            castDestination.setDirectoryEntryReference(parameter);
        }
    },
    // in property ATTACHMENT
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) property;
            castProperty.setEncoding(new Encoding(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) parent;
            return castProperty.getEncoding();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttachment<?> castSource = (PropertyAttachment<?>) source;
            PropertyAttachment<?> castDestination = (PropertyAttachment<?>) destination;
            Encoding parameter = new Encoding(castSource.getEncoding());
            castDestination.setEncoding(parameter);
        }
    },
    // in property ATTACHMENT
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) property;
            castProperty.setFormatType(new FormatType(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) parent;
            return castProperty.getFormatType();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttachment<?> castSource = (PropertyAttachment<?>) source;
            PropertyAttachment<?> castDestination = (PropertyAttachment<?>) destination;
            FormatType parameter = new FormatType(castSource.getFormatType());
            castDestination.setFormatType(parameter);
        }
    },
    // in property FREEBUSY
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyFreeBusy<?> castProperty = (PropertyFreeBusy<?>) property;
            castProperty.setFreeBusyType(new FreeBusyType(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyFreeBusy<?> castProperty = (PropertyFreeBusy<?>) parent;
            return castProperty.getFreeBusyType();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyFreeBusy<?> castSource = (PropertyFreeBusy<?>) source;
            PropertyFreeBusy<?> castDestination = (PropertyFreeBusy<?>) destination;
            FreeBusyType parameter = new FreeBusyType(castSource.getFreeBusyType());
            castDestination.setFreeBusyType(parameter);
        }
    },
    // in properties CATEGORIES, COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES, TZNAME
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseLanguage<?,?> castProperty = (PropertyBaseLanguage<?, ?>) property;
            castProperty.setLanguage(new Language(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseLanguage<?,?> castProperty = (PropertyBaseLanguage<?,?>) parent;
            return castProperty.getLanguage();
        }
        
        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBaseLanguage<?,?> castSource = (PropertyBaseLanguage<?,?>) source;
            PropertyBaseLanguage<?,?> castDestination = (PropertyBaseLanguage<?,?>) destination;
            Language parameter = new Language(castSource.getLanguage());
            castDestination.setLanguage(parameter);
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setGroupMembership(new GroupMembership(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getGroupMembership();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            GroupMembership parameter = new GroupMembership(castSource.getGroupMembership());
            castDestination.setGroupMembership(parameter);
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", ParticipationStatus.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setParticipationStatus(new ParticipationStatus(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getParticipationStatus();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            ParticipationStatus parameter = new ParticipationStatus(castSource.getParticipationStatus());
            castDestination.setParticipationStatus(parameter);
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyRecurrenceID<?> castProperty = (PropertyRecurrenceID<?>) property;
            castProperty.setRange(new Range(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyRecurrenceID<?> castProperty = (PropertyRecurrenceID<?>) parent;
            return castProperty.getRange();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyRecurrenceID<?> castSource = (PropertyRecurrenceID<?>) source;
            PropertyRecurrenceID<?> castDestination = (PropertyRecurrenceID<?>) destination;
            Range parameter = new Range(castSource.getRange());
            castDestination.setRange(parameter);
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTriggerRelationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAlarmTrigger<?> castProperty = (PropertyAlarmTrigger<?>) property;
            castProperty.setAlarmTrigger(new AlarmTriggerRelationship(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAlarmTrigger<?> castProperty = (PropertyAlarmTrigger<?>) parent;
            return castProperty.getAlarmTrigger();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAlarmTrigger<?> castSource = (PropertyAlarmTrigger<?>) source;
            PropertyAlarmTrigger<?> castDestination = (PropertyAlarmTrigger<?>) destination;
            AlarmTriggerRelationship parameter = new AlarmTriggerRelationship(castSource.getAlarmTrigger());
            castDestination.setAlarmTrigger(parameter);
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyRelationship<?> castProperty = (PropertyRelationship<?>) property;
            castProperty.setRelationship(new Relationship(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyRelationship<?> castProperty = (PropertyRelationship<?>) parent;
            return castProperty.getRelationship();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyRelationship<?> castSource = (PropertyRelationship<?>) source;
            PropertyRelationship<?> castDestination = (PropertyRelationship<?>) destination;
            Relationship parameter = new Relationship(castSource.getRelationship());
            castDestination.setRelationship(parameter);
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setParticipationRole(new ParticipationRole(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getParticipationRole();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            ParticipationRole parameter = new ParticipationRole(castSource.getParticipationRole());
            castDestination.setParticipationRole(parameter);
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setRSVP(new RSVP(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getRSVP();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyAttendee<?> castSource = (PropertyAttendee<?>) source;
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            RSVP parameter = new RSVP(castSource.getRSVP());
            castDestination.setRSVP(parameter);
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setSentBy(new SentBy(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getSentBy();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castSource = (PropertyBaseCalendarUser<?,?>) source;
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            SentBy parameter = new SentBy(castSource.getSentBy());
            castDestination.setSentBy(parameter);
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifierParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyDateTime<?> castProperty = (PropertyDateTime<?>) property;
            castProperty.setTimeZoneIdentifier(new TimeZoneIdentifierParameter(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyDateTime<?> castProperty = (PropertyDateTime<?>) parent;
            TimeZoneIdentifierParameter parameter = castProperty.getTimeZoneIdentifier();
            return ((parameter == null) || (parameter.getValue().equals(ZoneId.of("Z")))) ? null : parameter;
//            return parameter;
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyDateTime<?> castSource = (PropertyDateTime<?>) source;
            PropertyDateTime<?> castDestination = (PropertyDateTime<?>) destination;
            TimeZoneIdentifierParameter parameter = new TimeZoneIdentifierParameter(castSource.getTimeZoneIdentifier());
            castDestination.setTimeZoneIdentifier(parameter);
        }
    },
    VALUE_DATA_TYPES ("VALUE", ValueParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) property;
            ValueType valueType = castProperty.propertyType().allowedValueTypes().get(0);
            if (valueType.toString().equals(content))
            {
                castProperty.setValueParameter(new ValueParameter(valueType));
            } else
            {
                castProperty.setValueParameter(new ValueParameter(content)); // unknown value type
//                throw new IllegalArgumentException("Value type: " + content + " is not valid");
            }
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) parent;
            return castProperty.getValueParameter();
        }

        @Override
        public void copyParameter(Property<?> source, Property<?> destination)
        {
            PropertyBase<?,?> castSource = (PropertyBase<?,?>) source;
            PropertyBase<?,?> castDestination = (PropertyBase<?,?>) destination;
            ValueParameter parameter = new ValueParameter(castSource.getValueParameter());
            castDestination.setValueParameter(parameter);
        }
    };
    
    // Map to match up name to enum
    private static Map<String, ParameterType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ParameterType> makeEnumFromNameMap()
    {
        Map<String, ParameterType> map = new HashMap<>();
        ParameterType[] values = ParameterType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static ParameterType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Parameter<?>>, ParameterType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Parameter<?>>, ParameterType> makeEnumFromClassMap()
    {
        Map<Class<? extends Parameter<?>>, ParameterType> map = new HashMap<>();
        ParameterType[] values = ParameterType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static ParameterType enumFromClass(Class<? extends Parameter> myClass)
    {
        ParameterType p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(ParameterType.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    private Class<? extends Parameter<?>> myClass;
    @Override  public String toString() { return name; }
//    private Class<? extends Property> propertyClasses[];
    ParameterType(String name, Class<? extends Parameter<?>> myClass)
    {
        this.name = name;
        this.myClass = myClass;
//        this.propertyClasses = (Class<? extends Property>[]) propertyClasses;
    }

    /*
     * STATIC METHODS
     */

    /**
     * Remove parameter name and equals sign, if present, otherwise returns input string
     * 
     * @param input - parameter content with or without name and equals sign
     * @param name - name of parameter
     * @return - nameless string
     * 
     * example input:
     * ALTREP="CID:part3.msg.970415T083000@example.com"
     * output:
     * "CID:part3.msg.970415T083000@example.com"
     */
    static String extractValue(String content)
    {
        int equalsIndex = content.indexOf('=');
        return (equalsIndex > 0) ? content.substring(equalsIndex+1) : content;
    }

    /*
     * ABSTRACT METHODS
     */
    /** Parses string and sets parameter.  Called by {@link PropertyBase#parseContent()} */
    abstract public void parse(Property<?> property, String content);
    
    /** Returns associated Property<?> or List<Property<?>> */
    abstract public Parameter<?> getParameter(Property<?> parent);
    
    /** copies the associated parameter from the source property to the destination property */
    public void copyParameter(Property<?>  source, Property<?> destination) {}
}
