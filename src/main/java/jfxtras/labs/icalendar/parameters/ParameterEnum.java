package jfxtras.labs.icalendar.parameters;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.AlternateTextRepresentationProperty;
import jfxtras.labs.icalendar.properties.CalendarUserAddressProperty;
import jfxtras.labs.icalendar.properties.LanguageProperty;
import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyTimeZone;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.Attachment;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentBase64;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.time.FreeBusyTime;

public enum ParameterEnum
{
    // in properties COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateTextRepresentation.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            AlternateTextRepresentationProperty<?,?> castProperty = (AlternateTextRepresentationProperty<?, ?>) property;
            castProperty.setAlternateTextRepresentation(new AlternateTextRepresentation(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            AlternateTextRepresentationProperty<?,?> castProperty = (AlternateTextRepresentationProperty<?,?>) parent;
            return castProperty.getAlternateTextRepresentation();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in properties ATTENDEE, ORGANIZER
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) property;
            castProperty.setCommonName(new CommonName(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) parent;
            return castProperty.getCommonName();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in property ATTENDEE
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setCalendarUser(new CalendarUser(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getCalendarUser();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in property ATTENDEE
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setDelegators(new Delegators(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getDelegators();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in property ATTENDEE
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setDelegatees(new Delegatees(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getDelegatees();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in properties ATTENDEE, ORGANIZER
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) property;
            castProperty.setDirectoryEntryReference(new DirectoryEntryReference(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) parent;
            return castProperty.getDirectoryEntryReference();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in property ATTACHMENT
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            AttachmentBase64 castProperty = (AttachmentBase64) property;
            castProperty.setEncoding(new Encoding(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            AttachmentBase64 castProperty = (AttachmentBase64) parent;
            return castProperty.getEncoding();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            AttachmentBase64 castProperty = (AttachmentBase64) destination;
            castProperty.setEncoding(new Encoding((Encoding) sourceParameter)); 
        }
    },
    // in property ATTACHMENT
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attachment<?,?> castProperty = (Attachment<?,?>) property;
            castProperty.setFormatType(new FormatType(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attachment<?,?> castProperty = (Attachment<?,?>) parent;
            return castProperty.getFormatType();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            Attachment<?,?> castProperty = (Attachment<?,?>) destination;
            castProperty.setFormatType(new FormatType((FormatType) sourceParameter)); 
        }
    },
    // in property FREEBUSY
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            FreeBusyTime castProperty = (FreeBusyTime) property;
            castProperty.setFreeBusyType(new FreeBusyType(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            FreeBusyTime castProperty = (FreeBusyTime) parent;
            return castProperty.getFreeBusyType();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // in properties CATEGORIES, COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES, TZNAME
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            LanguageProperty<?,?> castProperty = (LanguageProperty<?, ?>) property;
            castProperty.setLanguage(new Language(extractValue(content)));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            LanguageProperty<?,?> castProperty = (LanguageProperty<?,?>) parent;
            return castProperty.getLanguage();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setGroupMembership(new GroupMembership(extractValue(content)));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getGroupMembership();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", Participation.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setParticipation(new Participation(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getParticipation();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTrigger.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }


        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }


        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setParticipationRole(new ParticipationRole(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getParticipationRole();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
            castProperty.setRSVP(content);
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attendee castProperty = (Attendee) parent;
            return castProperty.getRSVP();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) property;
            castProperty.setSentBy(new SentBy(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            CalendarUserAddressProperty<?> castProperty = (CalendarUserAddressProperty<?>) parent;
            return castProperty.getSentBy();
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifier.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyTimeZone<?> castProperty = (PropertyTimeZone<?>) property;
            castProperty.setTimeZoneIdentifier(new TimeZoneIdentifier(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyTimeZone<?> castProperty = (PropertyTimeZone<?>) parent;
            TimeZoneIdentifier parameter = castProperty.getTimeZoneIdentifier();
            return ((parameter == null) || (parameter.getValue().equals(ZoneId.of("Z")))) ? null : parameter;
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    VALUE_DATA_TYPES ("VALUE", ValueParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) property;
            ValueType valueType = castProperty.propertyType().defaultValueType();
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
        public void copyTo(Parameter<?> sourceParameter, Property<?> destination)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) destination;
            castProperty.setValueParameter(new ValueParameter((ValueParameter) sourceParameter)); 
        }
    };
    
    // Map to match up name to enum
    private static Map<String, ParameterEnum> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ParameterEnum> makeEnumFromNameMap()
    {
        Map<String, ParameterEnum> map = new HashMap<>();
        ParameterEnum[] values = ParameterEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static ParameterEnum enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Parameter<?>>, ParameterEnum> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Parameter<?>>, ParameterEnum> makeEnumFromClassMap()
    {
        Map<Class<? extends Parameter<?>>, ParameterEnum> map = new HashMap<>();
        ParameterEnum[] values = ParameterEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static ParameterEnum enumFromClass(Class<? extends Parameter> myClass)
    {
        ParameterEnum p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(ParameterEnum.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    private Class<? extends Parameter<?>> myClass;
    @Override  public String toString() { return name; }
//    private Class<? extends Property> propertyClasses[];
    ParameterEnum(String name, Class<? extends Parameter<?>> myClass)
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
    /** Parse content string into parameter value type U */
//    @Deprecated
//    abstract public <U> U parse(String content);
    
    abstract public void parse(Property<?> property, String content);
    public Object copyTo(Property<?> source, Property<?> destination)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    abstract public Parameter<?> getParameter(Property<?> parent);
//    public void setParameter(Property<?> property, Parameter<?> parameter)
//    {
//        // TODO Auto-generated method stub
//        
//    }
    abstract public void copyTo(Parameter<?> sourceParameter, Property<?> destination);
//    {
//        // TODO Auto-generated method stub
//        
//    }

}
