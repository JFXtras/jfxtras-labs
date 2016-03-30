package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;
import jfxtras.labs.icalendar.properties.LanguageBase;
import jfxtras.labs.icalendar.properties.Property;
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
    },
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    FORMAT_TYPE ("FMTTYPE", Format.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyTime.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            LanguageBase<?,?> castProperty = (LanguageBase<?, ?>) property;
            castProperty.setLanguage(new Language(content));
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            Attendee castProperty = (Attendee) property;
//            castProperty.setGroupMembership(new GroupMembership(content));
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", Participation.class) {
        @Override
        public void parse(Property<?> property, String content)
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
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTrigger.class) {
        @Override
        public void parse(Property<?> property, String content)
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
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifier.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
        }
    },
    VALUE_DATA_TYPES ("VALUE", Value.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            // TODO Auto-generated method stub
            
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
    
//    private static String parseString(String content)
//    {
//        int equalsIndex = content.indexOf('=');
//        String value = (equalsIndex > 0) ? content.substring(equalsIndex+1) : content;
//        return Parameter.removeDoubleQuote(value);
//    }
    
//    @Deprecated
//    private static List<URI> makeURIList(String content)
//    {
//        List<URI> uriList = new ArrayList<>();
//        Iterator<String> i = Arrays.stream(parseString(content).split(",")).iterator();
//        while (i.hasNext())
//        {
//            String element = Parameter.removeDoubleQuote(i.next());
//            try
//            {
//                uriList.add(new URI(element));
//            } catch (URISyntaxException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return uriList;
//    }
    


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
}
