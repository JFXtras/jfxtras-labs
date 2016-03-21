package jfxtras.labs.icalendar.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;

public enum ICalendarParameter
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    COMMON_NAME ("CN", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    CALENDAR_USER_TYPE ("CUTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DELEGATORS ("DELEGATED-FROM", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DELEGATEES ("DELEGATED-TO", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    INLINE_ENCODING ("ENCODING", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    FORMAT_TYPE ("FMTTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    LANGUAGE ("LANGUAGE", new Class[] { Categories.class }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    PARTICIPATION_ROLE ("ROLE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RSVP_EXPECTATION ("RSVP", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    SENT_BY ("SENT-BY", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    VALUE_DATE_TYPES ("VALUE", new Class[] { }) {
        @Override
        public String toContentLine(Property property)
        {
            // TODO Auto-generated method stub
            return null;
        }
    };
    
    // Map to match up name to ICalendarParameter
    private static Map<String, ICalendarParameter> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ICalendarParameter> makeEnumFromNameMap()
    {
        Map<String, ICalendarParameter> map = new HashMap<>();
        ICalendarParameter[] values = ICalendarParameter.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static ICalendarParameter enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    @Override  public String toString() { return name; }
    private Class propertyClasses[];
    ICalendarParameter(String name, Class[] propertyClasses)
    {
        this.name = name;
        this.propertyClasses = propertyClasses;
    }
    
    private static Map<Class, List<ICalendarParameter>> enumListFrompropertyClass = makeEnumListFrompropertyClass();
    private static Map<Class, List<ICalendarParameter>> makeEnumListFrompropertyClass()
    {
        Map<Class, List<ICalendarParameter>> map = new HashMap<>();
        ICalendarParameter[] values = ICalendarParameter.values();
        for (int i=0; i<values.length; i++)
        {
            ICalendarParameter myParameter = values[i];
            for (int classIndex=0; classIndex<myParameter.propertyClasses.length; classIndex++)
            {
                Class c = myParameter.propertyClasses[classIndex];
                List<ICalendarParameter> parameterList = (map.get(c) == null) ? new ArrayList<>() : map.get(c);
                parameterList.add(myParameter);
            }
        }
        return map;
    }
    
    /**
     * The Parameter values for TextProperty1
     */
    public static ICalendarParameter[] textProperty1ParameterValues()
    {
        return new ICalendarParameter[] { LANGUAGE };
    }
    
//    public void setValue(VComponentProperty textProperty1, String value)
//    {
//        // TODO Auto-generated method stub
//        // instead of sending property send callback containing how to set value?
//        // how about a bunch of interfaces to define all the setters and getters?
//        
//    }
    public void setValue(Property property, String value)
    {
        // TODO Auto-generated method stub
        
    }
    public static List<ICalendarParameter> values(Class<? extends Property> propertyClass)
    {
        return enumListFrompropertyClass.get(propertyClass);
    }
    
    /*
     * ABSTRACT METHODS
     */
    /** makes content line (RFC 5545 3.1) from a vComponent property  */
    public abstract String toContentLine(Property property);
}
