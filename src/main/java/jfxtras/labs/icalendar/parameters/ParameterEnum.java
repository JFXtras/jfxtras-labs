package jfxtras.labs.icalendar.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.Comment;

import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.PropertyTextBase2;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;

public enum ParameterEnum
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", new Class[] { Comment.class }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    COMMON_NAME ("CN", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    CALENDAR_USER_TYPE ("CUTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DELEGATORS ("DELEGATED-FROM", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DELEGATEES ("DELEGATED-TO", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    INLINE_ENCODING ("ENCODING", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    FORMAT_TYPE ("FMTTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    LANGUAGE ("LANGUAGE", new Class[] { Categories.class, Comment.class }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            PropertyTextBase2<?> source2 = (PropertyTextBase2<?>) source;
            PropertyTextBase2<?> destination2 = (PropertyTextBase2<?>) destination;
//            destination2.setLanguage(new Language(source2.getLanguage()));
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    PARTICIPATION_ROLE ("ROLE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RSVP_EXPECTATION ("RSVP", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    SENT_BY ("SENT-BY", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    VALUE_DATE_TYPES ("VALUE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parseAndSet(Property property, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void copyTo(Property source, Property destination)
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
    
    private String name;
    @Override  public String toString() { return name; }
    private Class propertyClasses[];
    ParameterEnum(String name, Class[] propertyClasses)
    {
        this.name = name;
        this.propertyClasses = propertyClasses;
    }
    
    private static Map<Class<? extends Property>, List<ParameterEnum>> enumListFrompropertyClass = makeEnumListFrompropertyClass();
    private static Map<Class<? extends Property>, List<ParameterEnum>> makeEnumListFrompropertyClass()
    {
        Map<Class<? extends Property>, List<ParameterEnum>> map = new HashMap<>();
        ParameterEnum[] values = ParameterEnum.values();
        for (int i=0; i<values.length; i++)
        {
            ParameterEnum myParameter = values[i];
            for (int classIndex=0; classIndex<myParameter.propertyClasses.length; classIndex++)
            {
                Class<? extends Property> c = myParameter.propertyClasses[classIndex];
//                System.out.println("c:" + c);
                List<ParameterEnum> parameterList = map.get(c);
                if (parameterList == null)
                {
                    parameterList = new ArrayList<>();
                    map.put(c, parameterList);
                }
//                List<ICalendarParameter> parameterList = (map.get(c) == null) ? new ArrayList<>() : map.get(c);
                parameterList.add(myParameter);
            }
        }
//        List<Class<? extends Property>> l = new ArrayList<Class<? extends Property>Arrays.asList(Categories.class);
        System.out.println("map:" + map.size());
        return map;
    }
    
//    /**
//     * The Parameter values for TextProperty1
//     */
//    public static ICalendarParameter[] textProperty1ParameterValues()
//    {
//        return new ICalendarParameter[] { LANGUAGE };
//    }
    
//    public void setValue(VComponentProperty textProperty1, String value)
//    {
//        // TODO Auto-generated method stub
//        // instead of sending property send callback containing how to set value?
//        // how about a bunch of interfaces to define all the setters and getters?
//        
//    }
    
    /**
     * Returns list of iCalendar parameters that are associated with propertyClass.
     * 
     * @param propertyClass - implementation of Property
     * @return - list of associated parameters
     */
    public static List<ParameterEnum> values(Class<? extends Property> propertyClass)
    {
        return enumListFrompropertyClass.get(propertyClass);
    }
    
    /*
     * ABSTRACT METHODS
     */
    /** makes content line (RFC 5545 3.1) from a vComponent property  */
    public abstract String toContentLine(Property property);
    
    /** parses value and sets property associated with enum */
    public abstract void parseAndSet(Property property, String value);
    
    public abstract void copyTo(Property source, Property destination);

}
