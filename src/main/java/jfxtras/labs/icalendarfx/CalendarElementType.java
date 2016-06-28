package jfxtras.labs.icalendarfx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentBase;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public enum CalendarElementType
{
    // MAIN COMPONENTS
    VEVENT ("VEVENT",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_CREATED,
            PropertyType.DATE_TIME_END, PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START,
            PropertyType.DESCRIPTION, PropertyType.DURATION, PropertyType.EXCEPTION_DATE_TIMES,
            PropertyType.GEOGRAPHIC_POSITION, PropertyType.IANA_PROPERTY, PropertyType.LAST_MODIFIED,
            PropertyType.LOCATION, PropertyType.NON_STANDARD, PropertyType.ORGANIZER, PropertyType.PRIORITY,
            PropertyType.RECURRENCE_DATE_TIMES, PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO,
            PropertyType.RECURRENCE_RULE, PropertyType.REQUEST_STATUS,  PropertyType.RESOURCES, PropertyType.SEQUENCE,
            PropertyType.STATUS, PropertyType.SUMMARY, PropertyType.TIME_TRANSPARENCY, PropertyType.UNIQUE_IDENTIFIER,
            PropertyType.UNIFORM_RESOURCE_LOCATOR),
            true,
            VEvent.class)
    {

        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVEvents();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            VEvent e = new VEvent();
            e.parseContent(contentLines);
            vCalendar.getVEvents().add(e);
            return e;
        }

    },
    VTODO ("VTODO",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_COMPLETED,
            PropertyType.DATE_TIME_CREATED, PropertyType.DATE_TIME_DUE, PropertyType.DATE_TIME_STAMP,
            PropertyType.DATE_TIME_START, PropertyType.DESCRIPTION, PropertyType.DURATION,
            PropertyType.EXCEPTION_DATE_TIMES, PropertyType.GEOGRAPHIC_POSITION, PropertyType.IANA_PROPERTY,
            PropertyType.LAST_MODIFIED, PropertyType.LOCATION,  PropertyType.NON_STANDARD, PropertyType.ORGANIZER,
            PropertyType.PERCENT_COMPLETE, PropertyType.PRIORITY, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO, PropertyType.RECURRENCE_RULE,
            PropertyType.REQUEST_STATUS, PropertyType.RESOURCES, PropertyType.SEQUENCE, PropertyType.STATUS,
            PropertyType.SUMMARY, PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
            true,
            VTodo.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVTodos();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            VTodo e = new VTodo();
            e.parseContent(contentLines);
            vCalendar.getVTodos().add(e);
            return e;
        }

    },
    VJOURNAL ("VJOURNAL",
            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_CREATED,
            PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START, PropertyType.DESCRIPTION,
            PropertyType.EXCEPTION_DATE_TIMES, PropertyType.IANA_PROPERTY, PropertyType.LAST_MODIFIED,
            PropertyType.NON_STANDARD, PropertyType.ORGANIZER, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO, PropertyType.RECURRENCE_RULE, 
            PropertyType.REQUEST_STATUS, PropertyType.SEQUENCE, PropertyType.STATUS, PropertyType.SUMMARY,
            PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
            true,
            VJournal.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVJournals();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            VJournal e = new VJournal();
            e.parseContent(contentLines);
            vCalendar.getVJournals().add(e);
            return e;
        }

    },
    VTIMEZONE ("VTIMEZONE",
            Arrays.asList(PropertyType.IANA_PROPERTY, PropertyType.LAST_MODIFIED, PropertyType.NON_STANDARD,
            PropertyType.TIME_ZONE_IDENTIFIER, PropertyType.TIME_ZONE_URL),
            true,
            VTimeZone.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVTimeZones();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            System.out.println("parse timezone");
            VTimeZone e = new VTimeZone();
            e.parseContent(contentLines);
            vCalendar.getVTimeZones().add(e);
            return e;
        }

    },
    VFREEBUSY ("VFREEBUSY",
            Arrays.asList(PropertyType.ATTENDEE, PropertyType.COMMENT, PropertyType.CONTACT,
            PropertyType.DATE_TIME_END, PropertyType.DATE_TIME_STAMP, PropertyType.DATE_TIME_START,
            PropertyType.FREE_BUSY_TIME, PropertyType.IANA_PROPERTY, PropertyType.NON_STANDARD, PropertyType.ORGANIZER,
            PropertyType.REQUEST_STATUS, PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
            true,
            VFreeBusy.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVFreeBusies();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            VFreeBusy e = new VFreeBusy();
            e.parseContent(contentLines);
            vCalendar.getVFreeBusies().add(e);
            return e;
        }
    },
    // NON-MAIN COMPONENTS - MUST BE NESTED IN A MAIN COMPONENT
    DAYLIGHT_SAVING_TIME ("DAYLIGHT",
            Arrays.asList(PropertyType.COMMENT, PropertyType.DATE_TIME_START,
            PropertyType.IANA_PROPERTY, PropertyType.NON_STANDARD, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_RULE, PropertyType.TIME_ZONE_NAME, PropertyType.TIME_ZONE_OFFSET_FROM,
            PropertyType.TIME_ZONE_OFFSET_TO),
            false,
            DaylightSavingTime.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VTimeZone
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            throw new RuntimeException("Not a main component - must be embedded inside a VTimeZone");
        }

    },
    STANDARD_TIME ("STANDARD",
            Arrays.asList(PropertyType.COMMENT, PropertyType.DATE_TIME_START,
            PropertyType.IANA_PROPERTY, PropertyType.NON_STANDARD, PropertyType.RECURRENCE_DATE_TIMES,
            PropertyType.RECURRENCE_RULE, PropertyType.TIME_ZONE_NAME, PropertyType.TIME_ZONE_OFFSET_FROM,
            PropertyType.TIME_ZONE_OFFSET_TO),
            false,
            StandardTime.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VTimeZone
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            throw new RuntimeException("Not a main component - must be embedded inside a VTimeZone");
        }

    },
    VALARM ("VALARM",
            Arrays.asList(PropertyType.ACTION, PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.DESCRIPTION,
            PropertyType.DURATION, PropertyType.IANA_PROPERTY, PropertyType.NON_STANDARD, PropertyType.REPEAT_COUNT,
            PropertyType.SUMMARY, PropertyType.TRIGGER),
            false,
            VAlarm.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VEvent or VTodo
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            throw new RuntimeException("Not a main component - must be embedded inside a VEvent or VTodo");
        }

    },
    // CALENDAR PROPERTIES
    CALENDAR_SCALE (PropertyType.CALENDAR_SCALE.toString(), null, true, CalendarScale.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            final String line;
            if (contentLines.size() == 1)
            {
                line = contentLines.get(0);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only have one line of content");
            }
            CalendarScale property = CalendarScale.parse(line);
            vCalendar.setCalendarScale(property);
            return property;
        }
    },
    METHOD (PropertyType.METHOD.toString(), null, true, Method.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            final String line;
            if (contentLines.size() == 1)
            {
                line = contentLines.get(0);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only have one line of content");
            }
            Method property = Method.parse(line);
            vCalendar.setMethod(property);
            return property;
        }
    },
    PRODUCT_IDENTIFIER (PropertyType.PRODUCT_IDENTIFIER.toString(), null, true, ProductIdentifier.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            final String line;
            if (contentLines.size() == 1)
            {
                line = contentLines.get(0);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only have one line of content");
            }
            ProductIdentifier property = ProductIdentifier.parse(line);
            vCalendar.setProductIdentifier(property);
            return property;
        }
    },
    VERSION (PropertyType.VERSION.toString(), null, true, Version.class)
    {
        @Override
        public List<? extends VComponent> getComponents(VCalendar vCalendar)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            final String line;
            if (contentLines.size() == 1)
            {
                line = contentLines.get(0);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only have one line of content");
            }
            Version property = Version.parse(line);
            vCalendar.setVersion(property);
            return property;
        }
    };

    // Map to match up name to enum
    private static Map<String, CalendarElementType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, CalendarElementType> makeEnumFromNameMap()
    {
        Map<String, CalendarElementType> map = new HashMap<>();
        CalendarElementType[] values = CalendarElementType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static CalendarElementType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends VCalendarElement>, CalendarElementType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends VCalendarElement>, CalendarElementType> makeEnumFromClassMap()
    {
        Map<Class<? extends VCalendarElement>, CalendarElementType> map = new HashMap<>();
        CalendarElementType[] values = CalendarElementType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static CalendarElementType enumFromClass(Class<? extends VCalendarElement> myClass)
    {
        return enumFromClassMap.get(myClass);
    }
    
    private Class<? extends VCalendarElement> myClass;
    public Class<? extends VCalendarElement> getElementClass() { return myClass; }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    private List<PropertyType> allowedProperties;
    public List<PropertyType> allowedProperties() { return allowedProperties; }

    private boolean isCalendarElement;
    public boolean isCalendarElement() { return isCalendarElement; }
    
    CalendarElementType(String name, List<PropertyType> allowedProperties, boolean isCalendarElement, Class<? extends VCalendarElement> myClass)
    {
        this.name = name;
        this.allowedProperties = allowedProperties;
        this.isCalendarElement = isCalendarElement;
        this.myClass = myClass;
    }

    abstract public List<? extends VComponent> getComponents(VCalendar vCalendar);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines);
    
    public void copyChild(VCalendarElement child, VCalendar vCalendar)
    {
        throw new RuntimeException("not implemented");
        // TODO Auto-generated method stub
        
    }
}
