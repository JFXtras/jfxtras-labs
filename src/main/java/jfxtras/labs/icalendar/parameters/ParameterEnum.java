package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;
import jfxtras.labs.icalendar.properties.LanguageBase;
import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;

public enum ParameterEnum
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateTextRepresentation.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            AlternateTextRepresentationBase<?,?> castProperty = (AlternateTextRepresentationBase<?, ?>) property;
            castProperty.setAlternateTextRepresentation(new AlternateTextRepresentation(content));
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

//        @Override
//        public String toContentLine(Property<?> parent)
//        {
//            AlternateTextRepresentationBase<?,?> castProperty = (AlternateTextRepresentationBase<?,?>) parent;
//            return castProperty.getAlternateTextRepresentation().toContentLine();
////            castProperty.getLanguage()
//        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            AlternateTextRepresentationBase<?,?> castProperty = (AlternateTextRepresentationBase<?,?>) parent;
            return castProperty.getAlternateTextRepresentation();
        }
    },
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attachment castProperty = (Attachment) property;
            castProperty.setEncoding(new Encoding(content));
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attachment castProperty = (Attachment) property;
            castProperty.setFormatType(new FormatType(content));
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            Attachment castProperty = (Attachment) parent;
            return castProperty.getFormatType();
        }
    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyTime.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            LanguageBase<?,?> castProperty = (LanguageBase<?, ?>) property;
            castProperty.setLanguage(new Language(extractValue(content)));
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object testParameter)
        {
            LanguageBase<?,?> castProperty = (LanguageBase<?,?>) parentProperty;
            return (testParameter == null) ? castProperty.getLanguage() == null : castProperty.getLanguage().equals(testParameter);
        }

        @Override
        public void copyTo(Parameter<?> sourceParameter, Property<?> destimation)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            LanguageBase<?,?> castProperty = (LanguageBase<?,?>) parent;
            return castProperty.getLanguage();
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
//            castProperty.setGroupMembership(new GroupMembership(content));
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", Participation.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTrigger.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifier.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    VALUE_DATA_TYPES ("VALUE", Value.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isEqualTo(Property<?> parentProperty, Object parameter2)
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            // TODO Auto-generated method stub
            return null;
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
     * Remove parameter name and equals sign, if present, otherwise return input string
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
//    public String toContentLine(Property<?> parent) { return null; }
    @Deprecated
    abstract public boolean isEqualTo(Property<?> parentProperty, Object parameter2);
    
    abstract public Parameter<?> getParameter(Property<?> parent);
//    public void setParameter(Property<?> property, Parameter<?> parameter)
//    {
//        // TODO Auto-generated method stub
//        
//    }
    public void copyTo(Parameter<?> sourceParameter, Property<?> destimation)
    {
        // TODO Auto-generated method stub
        
    }

}
