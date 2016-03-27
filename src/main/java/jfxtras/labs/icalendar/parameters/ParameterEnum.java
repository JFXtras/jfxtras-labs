package jfxtras.labs.icalendar.parameters;

import java.util.HashMap;
import java.util.Map;

public enum ParameterEnum
{
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateTextRepresentation.class) {

    },
    COMMON_NAME ("CN", CommonName.class) {

    },
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUserType.class) {

    },
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {

    },
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {

    },
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntryReference.class) {

    },
    INLINE_ENCODING ("ENCODING", Encoding.class) {

    },
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {

    },
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyTime.class) {

    },
    LANGUAGE ("LANGUAGE", Language.class) {

    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {

    },
    PARTICIPATION_STATUS ("PARTSTAT", Participation.class) {

    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", RecurrenceIdentifierRange.class) {

    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTrigger.class) {

    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {

    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {

    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {

    },
    SENT_BY ("SENT-BY", SentBy.class) {

    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifier.class) {

    },
    VALUE_DATA_TYPES ("VALUE", Value.class) {

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
    private Class<? extends Parameter> myClass;
    @Override  public String toString() { return name; }
//    private Class<? extends Property> propertyClasses[];
    ParameterEnum(String name, Class<? extends Parameter> myClass)
    {
        this.name = name;
        this.myClass = myClass;
//        this.propertyClasses = (Class<? extends Property>[]) propertyClasses;
    }
    
//    private static Map<Class<? extends Property>, List<ParameterEnum>> enumListFrompropertyClass = makeEnumListFrompropertyClass();
//    private static Map<Class<? extends Property>, List<ParameterEnum>> makeEnumListFrompropertyClass()
//    {
//        Map<Class<? extends Property>, List<ParameterEnum>> map = new HashMap<>();
//        ParameterEnum[] values = ParameterEnum.values();
//        for (int i=0; i<values.length; i++)
//        {
//            ParameterEnum myParameter = values[i];
//            for (int classIndex=0; classIndex<myParameter.propertyClasses.length; classIndex++)
//            {
//                Class<? extends Property> c = myParameter.propertyClasses[classIndex];
////                System.out.println("c:" + c);
//                List<ParameterEnum> parameterList = map.get(c);
//                if (parameterList == null)
//                {
//                    parameterList = new ArrayList<>();
//                    map.put(c, parameterList);
//                }
////                List<ICalendarParameter> parameterList = (map.get(c) == null) ? new ArrayList<>() : map.get(c);
//                parameterList.add(myParameter);
//            }
//        }
////        List<Class<? extends Property>> l = new ArrayList<Class<? extends Property>Arrays.asList(Categories.class);
////        System.out.println("map:" + map.size());
//        return map;
//    }
    
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
    
//    /**
//     * Returns list of iCalendar parameters that are associated with propertyClass.
//     * 
//     * @param propertyClass - implementation of Property
//     * @return - list of associated parameters
//     */
//    @Deprecated
//    public static List<ParameterEnum> values(Class<? extends Property> propertyClass)
//    {
//        return enumListFrompropertyClass.get(propertyClass);
//    }
    
    /*
     * ABSTRACT METHODS
     */
//    /** makes content line (RFC 5545 3.1) from a vComponent property  */
//    public abstract String toContentLine(Property property);
    
//    /** parses value and sets property associated with enum */
//    public abstract void parseAndSet(Property property, String value);

}
