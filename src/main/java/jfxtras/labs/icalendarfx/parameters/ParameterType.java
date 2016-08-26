package jfxtras.labs.icalendarfx.parameters;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.VElement;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.labs.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.labs.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.labs.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.labs.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.labs.icalendarfx.parameters.Range.RangeType;
import jfxtras.labs.icalendarfx.parameters.Relationship.RelationshipType;
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
import jfxtras.labs.icalendarfx.properties.ValueType;
import jfxtras.labs.icalendarfx.properties.component.relationship.PropertyBaseCalendarUser;
import jfxtras.labs.icalendarfx.utilities.StringConverters;

/**
 * For each VComponent property parameter (RFC 5545, 3.2, page 13) contains the following: <br>
 * <br>
 * Parameter name {@link #toString()} <br>
 * Parameter class {@link #getPropertyClass()}<br>
 * Method to parse parameter string into parent component {@link #parse(Property<?>, String)}<br>
 * Method to get parameter from property {@link #getParameter(Property<?>)}<br>
 * Method to copy parameter into new parent property {@link #copyParameter(Parameter, Property)}<br>
 * 
 * @author David Bal
 *
 */
public enum ParameterType
{
    // in properties COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateText.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseAltText<?,?> castProperty = (PropertyBaseAltText<?, ?>) property;
            castProperty.setAlternateText(AlternateText.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAltText<?> castProperty = (PropertyAltText<?>) parent;
            return castProperty.getAlternateText();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseAltText<?,?> castDestination = (PropertyBaseAltText<?,?>) destination;
            AlternateText parameterCopy = new AlternateText((AlternateText) child);
            castDestination.setAlternateText(parameterCopy);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setCommonName(CommonName.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getCommonName();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            CommonName parameterCopy = new CommonName((CommonName) child);
            castDestination.setCommonName(parameterCopy);
        }
    },
    // in property ATTENDEE
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setCalendarUser(CalendarUser.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getCalendarUser();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((CalendarUserType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) CalendarUserType.valueOfWithUnknown(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            CalendarUser parameterCopy = new CalendarUser((CalendarUser) child);
            castDestination.setCalendarUser(parameterCopy);
        }
    },
    // in property ATTENDEE
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setDelegators(Delegators.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getDelegators();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            Delegators parameterCopy = new Delegators((Delegators) child);
            castDestination.setDelegators(parameterCopy);
        }
    },
    // in property ATTENDEE
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setDelegatees(Delegatees.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getDelegatees();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            Delegatees parameterCopy = new Delegatees((Delegatees) child);
            castDestination.setDelegatees(parameterCopy);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setDirectoryEntryReference(DirectoryEntryReference.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getDirectoryEntryReference();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            DirectoryEntryReference parameterCopy = new DirectoryEntryReference((DirectoryEntryReference) child);
            castDestination.setDirectoryEntryReference(parameterCopy);
        }
    },
    // in property ATTACHMENT
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) property;
            castProperty.setEncoding(Encoding.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) parent;
            return castProperty.getEncoding();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((EncodingType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) EncodingType.enumFromName(string);
                }
            };
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttachment<?> castDestination = (PropertyAttachment<?>) destination;
            Encoding parameterCopy = new Encoding((Encoding) child);
            castDestination.setEncoding(parameterCopy);
        }
    },
    // in property ATTACHMENT
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) property;
            castProperty.setFormatType(FormatType.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttachment<?> castProperty = (PropertyAttachment<?>) parent;
            return castProperty.getFormatType();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttachment<?> castDestination = (PropertyAttachment<?>) destination;
            FormatType parameterCopy = new FormatType((FormatType) child);
            castDestination.setFormatType(parameterCopy);
        }
    },
    // in property FREEBUSY
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyFreeBusy<?> castProperty = (PropertyFreeBusy<?>) property;
            castProperty.setFreeBusyType(FreeBusyType.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyFreeBusy<?> castProperty = (PropertyFreeBusy<?>) parent;
            return castProperty.getFreeBusyType();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((FreeBusyTypeEnum) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) FreeBusyTypeEnum.enumFromName(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyFreeBusy<?> castDestination = (PropertyFreeBusy<?>) destination;
            FreeBusyType parameterCopy = new FreeBusyType((FreeBusyType) child);
            castDestination.setFreeBusyType(parameterCopy);
        }
    },
    // in properties CATEGORIES, COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES, TZNAME
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseLanguage<?,?> castProperty = (PropertyBaseLanguage<?, ?>) property;
            castProperty.setLanguage(Language.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseLanguage<?,?> castProperty = (PropertyBaseLanguage<?,?>) parent;
            return castProperty.getLanguage();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseLanguage<?,?> castDestination = (PropertyBaseLanguage<?,?>) destination;
            Language parameterCopy = new Language((Language) child);
            castDestination.setLanguage(parameterCopy);
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setGroupMembership(GroupMembership.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getGroupMembership();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyFreeBusy<?> castDestination = (PropertyFreeBusy<?>) destination;
            FreeBusyType parameterCopy = new FreeBusyType((FreeBusyType) child);
            castDestination.setFreeBusyType(parameterCopy);
        }
    },
    OTHER ("OTHER", OtherParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            final ObservableList<OtherParameter> list;
            if (property.getOtherParameters() == null)
            {
                list = FXCollections.observableArrayList();
                property.setOtherParameters(list);
            } else
            {
                list = property.getOtherParameters();
            }
            list.add(OtherParameter.parse(content));
        }

        @Override
        public Object getParameter(Property<?> parent)
        {
            return parent.getOtherParameters();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            final ObservableList<OtherParameter> list;
            if (destination.getOtherParameters() == null)
            {
                list = FXCollections.observableArrayList();
                destination.setOtherParameters(list);
            } else
            {
                list = destination.getOtherParameters();
            }
            list.add(new OtherParameter((OtherParameter) child));
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", ParticipationStatus.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setParticipationStatus(ParticipationStatus.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getParticipationStatus();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ParticipationStatusType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ParticipationStatusType.enumFromName(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            ParticipationStatus parameterCopy = new ParticipationStatus((ParticipationStatus) child);
            castDestination.setParticipationStatus(parameterCopy);
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyRecurrenceID<?> castProperty = (PropertyRecurrenceID<?>) property;
            castProperty.setRange(Range.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyRecurrenceID<?> castProperty = (PropertyRecurrenceID<?>) parent;
            return castProperty.getRange();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((RangeType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) RangeType.enumFromName(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyRecurrenceID<?> castDestination = (PropertyRecurrenceID<?>) destination;
            Range parameterCopy = new Range((Range) child);
            castDestination.setRange(parameterCopy);
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTriggerRelationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAlarmTrigger<?> castProperty = (PropertyAlarmTrigger<?>) property;
            castProperty.setAlarmTrigger(AlarmTriggerRelationship.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAlarmTrigger<?> castProperty = (PropertyAlarmTrigger<?>) parent;
            return castProperty.getAlarmTrigger();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((AlarmTriggerRelationshipType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) AlarmTriggerRelationshipType.valueOf(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAlarmTrigger<?> castDestination = (PropertyAlarmTrigger<?>) destination;
            AlarmTriggerRelationship parameterCopy = new AlarmTriggerRelationship((AlarmTriggerRelationship) child);
            castDestination.setAlarmTrigger(parameterCopy);
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyRelationship<?> castProperty = (PropertyRelationship<?>) property;
            castProperty.setRelationship(Relationship.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyRelationship<?> castProperty = (PropertyRelationship<?>) parent;
            return castProperty.getRelationship();
        }

        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((RelationshipType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) RelationshipType.valueOfWithUnknown(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyRelationship<?> castDestination = (PropertyRelationship<?>) destination;
            Relationship parameterCopy = new Relationship((Relationship) child);
            castDestination.setRelationship(parameterCopy);
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setParticipationRole(ParticipationRole.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getParticipationRole();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ParticipationRoleType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ParticipationRoleType.enumFromName(string);
                }
            };
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            ParticipationRole parameterCopy = new ParticipationRole((ParticipationRole) child);
            castDestination.setParticipationRole(parameterCopy);
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) property;
            castProperty.setRSVP(RSVP.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyAttendee<?> castProperty = (PropertyAttendee<?>) parent;
            return castProperty.getRSVP();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.booleanConverter();
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyAttendee<?> castDestination = (PropertyAttendee<?>) destination;
            RSVP parameterCopy = new RSVP((RSVP) child);
            castDestination.setRSVP(parameterCopy);
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setSentBy(SentBy.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getSentBy();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            SentBy parameterCopy = new SentBy((SentBy) child);
            castDestination.setSentBy(parameterCopy);
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifierParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyDateTime<?> castProperty = (PropertyDateTime<?>) property;
            castProperty.setTimeZoneIdentifier(TimeZoneIdentifierParameter.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyDateTime<?> castProperty = (PropertyDateTime<?>) parent;
            TimeZoneIdentifierParameter parameter = castProperty.getTimeZoneIdentifier();
            return ((parameter == null) || (parameter.getValue().equals(ZoneId.of("Z")))) ? null : parameter;
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ZoneId) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ZoneId.of(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyDateTime<?> castDestination = (PropertyDateTime<?>) destination;
            TimeZoneIdentifierParameter parameterCopy = new TimeZoneIdentifierParameter((TimeZoneIdentifierParameter) child);
            castDestination.setTimeZoneIdentifier(parameterCopy);
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
                castProperty.setValueType(new ValueParameter(valueType));
            } else
            {
                castProperty.setValueType(ValueParameter.parse(content)); // unknown value type
//                throw new IllegalArgumentException("Value type: " + content + " is not valid");
            }
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) parent;
            return castProperty.getValueType();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ValueType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ValueType.valueOf(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBase<?,?> castDestination = (PropertyBase<?,?>) destination;
            ValueParameter parameterCopy = new ValueParameter((ValueParameter) child);
            castDestination.setValueType(parameterCopy);
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
    public static ParameterType enumFromClass(Class<? extends VElement> myClass)
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
    // TODO - MAY BE OBSOLETE WITH USE OF ORDERER - ONLY BEING USED TO DOUBLE-CHECK EXISTANCE OF ALL PARAMETERS WHEN COPYING
    abstract public Object getParameter(Property<?> parent);
    
    /** return default String converter associated with property value type */
    public <T> StringConverter<T> getConverter()
    {
        return (StringConverter<T>) new StringConverter<String>()
        {
            @Override
            public String toString(String object)
            {
                return object.toString();
            }

            @Override
            public String fromString(String string)
            {
                return StringConverters.removeDoubleQuote(string);
            }
        };
    }
    
    abstract public void copyParameter(Parameter<?> child, Property<?> destination);
}
