package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendar.parameters.CalendarUser.CalendarUserType;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingEnum;

public enum ParameterEnum
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateTextRepresentation.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) makeURI(content);
        }
    },
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) parseString(content);
        }
    },
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) CalendarUserType.valueOf(parseString(content));
        }
    },
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) makeURIList(content);
        }
    },
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) makeURIList(content);
        }
    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) makeURI(content);
        }
    },
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) EncodingEnum.valueOf(parseString(content));
        }
    },
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public <U> U parse(String content)
        {
            return (U) parseString(content);
        }
    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyTime.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", Participation.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTrigger.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifier.class) {
        @Override
        public <U> U parse(String content)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    VALUE_DATA_TYPES ("VALUE", Value.class) {
        @Override
        public <U> U parse(String content)
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
    private static Map<Class<? extends Parameter>, ParameterEnum> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Parameter>, ParameterEnum> makeEnumFromClassMap()
    {
        Map<Class<? extends Parameter>, ParameterEnum> map = new HashMap<>();
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
    private Class<? extends Parameter> myClass;
    @Override  public String toString() { return name; }
//    private Class<? extends Property> propertyClasses[];
    ParameterEnum(String name, Class<? extends Parameter> myClass)
    {
        this.name = name;
        this.myClass = myClass;
//        this.propertyClasses = (Class<? extends Property>[]) propertyClasses;
    }
    
    private static String parseString(String content)
    {
        int equalsIndex = content.indexOf('=');
        String value = (equalsIndex > 0) ? content.substring(equalsIndex+1) : content;
        return Parameter.removeDoubleQuote(value);
    }
    
    private static List<URI> makeURIList(String content)
    {
        List<URI> uriList = new ArrayList<>();
        Iterator<String> i = Arrays.stream(parseString(content).split(",")).iterator();
        while (i.hasNext())
        {
            String element = Parameter.removeDoubleQuote(i.next());
            try
            {
                uriList.add(new URI(element));
            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        return uriList;
    }
    
    private static URI makeURI(String content)
    {
        URI uri = null;
        try
        {
            uri = new URI(Parameter.removeDoubleQuote(parseString(content)));
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return uri;
    }

    /*
     * ABSTRACT METHODS
     */
    /** Parse content string into parameter value type U */
    abstract public <U> U parse(String content);
}
