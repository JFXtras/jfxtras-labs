package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.ComponentElement;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public enum CalendarElement
{
    // MAIN COMPONENTS
    VEVENT ("VEVENT",
            Arrays.asList(ComponentElement.ATTACHMENT, ComponentElement.ATTENDEE, ComponentElement.CATEGORIES,
            ComponentElement.CLASSIFICATION, ComponentElement.COMMENT, ComponentElement.CONTACT, ComponentElement.DATE_TIME_CREATED,
            ComponentElement.DATE_TIME_END, ComponentElement.DATE_TIME_STAMP, ComponentElement.DATE_TIME_START,
            ComponentElement.DESCRIPTION, ComponentElement.DURATION, ComponentElement.EXCEPTION_DATE_TIMES,
            ComponentElement.GEOGRAPHIC_POSITION, ComponentElement.IANA_PROPERTY, ComponentElement.LAST_MODIFIED,
            ComponentElement.LOCATION, ComponentElement.NON_STANDARD, ComponentElement.ORGANIZER, ComponentElement.PRIORITY,
            ComponentElement.RECURRENCE_DATE_TIMES, ComponentElement.RECURRENCE_IDENTIFIER, ComponentElement.RELATED_TO,
            ComponentElement.RECURRENCE_RULE, ComponentElement.REQUEST_STATUS,  ComponentElement.RESOURCES, ComponentElement.SEQUENCE,
            ComponentElement.STATUS, ComponentElement.SUMMARY, ComponentElement.TIME_TRANSPARENCY, ComponentElement.UNIQUE_IDENTIFIER,
            ComponentElement.UNIFORM_RESOURCE_LOCATOR),
            true)
    {

        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVEvents();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            VEventNew e = new VEventNew();
            e.parseContent(contentLines);
            vCalendar.getVEvents().add(e);
            return e;
        }

    },
    VTODO ("VTODO",
            Arrays.asList(ComponentElement.ATTACHMENT, ComponentElement.ATTENDEE, ComponentElement.CATEGORIES,
            ComponentElement.CLASSIFICATION, ComponentElement.COMMENT, ComponentElement.CONTACT, ComponentElement.DATE_TIME_COMPLETED,
            ComponentElement.DATE_TIME_CREATED, ComponentElement.DATE_TIME_DUE, ComponentElement.DATE_TIME_STAMP,
            ComponentElement.DATE_TIME_START, ComponentElement.DESCRIPTION, ComponentElement.DURATION,
            ComponentElement.EXCEPTION_DATE_TIMES, ComponentElement.GEOGRAPHIC_POSITION, ComponentElement.IANA_PROPERTY,
            ComponentElement.LAST_MODIFIED, ComponentElement.LOCATION,  ComponentElement.NON_STANDARD, ComponentElement.ORGANIZER,
            ComponentElement.PERCENT_COMPLETE, ComponentElement.PRIORITY, ComponentElement.RECURRENCE_DATE_TIMES,
            ComponentElement.RECURRENCE_IDENTIFIER, ComponentElement.RELATED_TO, ComponentElement.RECURRENCE_RULE,
            ComponentElement.REQUEST_STATUS, ComponentElement.RESOURCES, ComponentElement.SEQUENCE, ComponentElement.STATUS,
            ComponentElement.SUMMARY, ComponentElement.UNIQUE_IDENTIFIER, ComponentElement.UNIFORM_RESOURCE_LOCATOR),
            true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVTodos();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    VJOURNAL ("VJOURNAL",
            Arrays.asList(ComponentElement.ATTACHMENT, ComponentElement.ATTENDEE, ComponentElement.CATEGORIES,
            ComponentElement.CLASSIFICATION, ComponentElement.COMMENT, ComponentElement.CONTACT, ComponentElement.DATE_TIME_CREATED,
            ComponentElement.DATE_TIME_STAMP, ComponentElement.DATE_TIME_START, ComponentElement.DESCRIPTION,
            ComponentElement.EXCEPTION_DATE_TIMES, ComponentElement.IANA_PROPERTY, ComponentElement.LAST_MODIFIED,
            ComponentElement.NON_STANDARD, ComponentElement.ORGANIZER, ComponentElement.RECURRENCE_DATE_TIMES,
            ComponentElement.RECURRENCE_IDENTIFIER, ComponentElement.RELATED_TO, ComponentElement.RECURRENCE_RULE, 
            ComponentElement.REQUEST_STATUS, ComponentElement.SEQUENCE, ComponentElement.STATUS, ComponentElement.SUMMARY,
            ComponentElement.UNIQUE_IDENTIFIER, ComponentElement.UNIFORM_RESOURCE_LOCATOR),
            true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVJournals();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    VTIMEZONE ("VTIMEZONE",
            Arrays.asList(ComponentElement.IANA_PROPERTY, ComponentElement.LAST_MODIFIED, ComponentElement.NON_STANDARD,
            ComponentElement.TIME_ZONE_IDENTIFIER, ComponentElement.TIME_ZONE_URL),
            true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVTimeZones();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    VFREEBUSY ("VFREEBUSY",
            Arrays.asList(ComponentElement.ATTENDEE, ComponentElement.COMMENT, ComponentElement.CONTACT,
            ComponentElement.DATE_TIME_END, ComponentElement.DATE_TIME_STAMP, ComponentElement.DATE_TIME_START,
            ComponentElement.FREE_BUSY_TIME, ComponentElement.IANA_PROPERTY, ComponentElement.NON_STANDARD, ComponentElement.ORGANIZER,
            ComponentElement.REQUEST_STATUS, ComponentElement.UNIQUE_IDENTIFIER, ComponentElement.UNIFORM_RESOURCE_LOCATOR),
            true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return vCalendar.getVFreeBusies();
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // NON-MAIN COMPONENTS - MUST BE NESTED IN A MAIN COMPONENT
    DAYLIGHT_SAVING_TIME ("DAYLIGHT",
            Arrays.asList(ComponentElement.COMMENT, ComponentElement.DATE_TIME_START,
            ComponentElement.IANA_PROPERTY, ComponentElement.NON_STANDARD, ComponentElement.RECURRENCE_DATE_TIMES,
            ComponentElement.RECURRENCE_RULE, ComponentElement.TIME_ZONE_NAME, ComponentElement.TIME_ZONE_OFFSET_FROM,
            ComponentElement.TIME_ZONE_OFFSET_TO),
            false)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VTimeZone
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    STANDARD_TIME ("STANDARD",
            Arrays.asList(ComponentElement.COMMENT, ComponentElement.DATE_TIME_START,
            ComponentElement.IANA_PROPERTY, ComponentElement.NON_STANDARD, ComponentElement.RECURRENCE_DATE_TIMES,
            ComponentElement.RECURRENCE_RULE, ComponentElement.TIME_ZONE_NAME, ComponentElement.TIME_ZONE_OFFSET_FROM,
            ComponentElement.TIME_ZONE_OFFSET_TO),
            false)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VTimeZone
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    VALARM ("VALARM",
            Arrays.asList(ComponentElement.ACTION, ComponentElement.ATTACHMENT, ComponentElement.ATTENDEE, ComponentElement.DESCRIPTION,
            ComponentElement.DURATION, ComponentElement.IANA_PROPERTY, ComponentElement.NON_STANDARD, ComponentElement.REPEAT_COUNT,
            ComponentElement.SUMMARY, ComponentElement.TRIGGER),
            false)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
        {
            return null; // not a main component - must be embedded inside a VEvent or VTodo
        }

        @Override
        public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            return null;
        }

    },
    // CALENDAR PROPERTIES
    CALENDAR_SCALE ("CALSCALE", null, true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
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
    METHOD ("METHOD", null, true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
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
    PRODUCT_IDENTIFIER ("PRODID", null, true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
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
    VERSION ("VERSION", null, true)
    {
        @Override
        public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar)
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
    private static Map<String, CalendarElement> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, CalendarElement> makeEnumFromNameMap()
    {
        Map<String, CalendarElement> map = new HashMap<>();
        CalendarElement[] values = CalendarElement.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static CalendarElement enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    private List<ComponentElement> allowedProperties;
    public List<ComponentElement> allowedProperties() { return allowedProperties; }
    private boolean isCalendarElement;
    public boolean isCalendarElement() { return isCalendarElement; }
    
    CalendarElement(String name, List<ComponentElement> allowedProperties, boolean isCalendarElement)
    {
        this.name = name;
        this.allowedProperties = allowedProperties;
        this.isCalendarElement = isCalendarElement;
    }

    abstract public List<? extends VComponentNew<?>> getComponents(VCalendar vCalendar);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public VCalendarElement parse(VCalendar vCalendar, List<String> contentLines);
//    {
//        // TODO Auto-generated method stub
//        
//    }

//    public Object getElement(VCalendar vCalendar) { return null; }
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }

//    public VCalendarElement parse(VCalendar vCalendar, String content)
//    {
//        // TODO Auto-generated method stub
//        
//    }
}
