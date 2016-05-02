package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public enum VComponentEnum
{
    VEVENT (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_CREATED,
            PropertyEnum.DATE_TIME_END, PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START,
            PropertyEnum.DESCRIPTION, PropertyEnum.DURATION, PropertyEnum.EXCEPTION_DATE_TIMES,
            PropertyEnum.GEOGRAPHIC_POSITION, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.LOCATION, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER, PropertyEnum.PRIORITY,
            PropertyEnum.RECURRENCE_DATE_TIMES, PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.REQUEST_STATUS,  PropertyEnum.RESOURCES, PropertyEnum.SEQUENCE,
            PropertyEnum.STATUS, PropertyEnum.SUMMARY, PropertyEnum.TIME_TRANSPARENCY, PropertyEnum.UNIQUE_IDENTIFIER,
            PropertyEnum.UNIFORM_RESOURCE_LOCATOR),
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
    VTODO (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_COMPLETED,
            PropertyEnum.DATE_TIME_CREATED, PropertyEnum.DATE_TIME_DUE, PropertyEnum.DATE_TIME_STAMP,
            PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION, PropertyEnum.DURATION,
            PropertyEnum.EXCEPTION_DATE_TIMES, PropertyEnum.GEOGRAPHIC_POSITION, PropertyEnum.IANA_PROPERTY,
            PropertyEnum.LAST_MODIFIED, PropertyEnum.LOCATION,  PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.PERCENT_COMPLETE, PropertyEnum.PRIORITY, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO, PropertyEnum.RECURRENCE_RULE,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.RESOURCES, PropertyEnum.SEQUENCE, PropertyEnum.STATUS,
            PropertyEnum.SUMMARY, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR),
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
    VJOURNAL (Arrays.asList(PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.CATEGORIES,
            PropertyEnum.CLASSIFICATION, PropertyEnum.COMMENT, PropertyEnum.CONTACT, PropertyEnum.DATE_TIME_CREATED,
            PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START, PropertyEnum.DESCRIPTION,
            PropertyEnum.EXCEPTION_DATE_TIMES, PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED,
            PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_IDENTIFIER, PropertyEnum.RELATED_TO, PropertyEnum.RECURRENCE_RULE, 
            PropertyEnum.REQUEST_STATUS, PropertyEnum.SEQUENCE, PropertyEnum.STATUS, PropertyEnum.SUMMARY,
            PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR),
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
    VTIMEZONE (Arrays.asList(PropertyEnum.IANA_PROPERTY, PropertyEnum.LAST_MODIFIED, PropertyEnum.NON_STANDARD,
            PropertyEnum.TIME_ZONE_IDENTIFIER, PropertyEnum.TIME_ZONE_URL),
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
    VFREEBUSY (Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.COMMENT, PropertyEnum.CONTACT,
            PropertyEnum.DATE_TIME_END, PropertyEnum.DATE_TIME_STAMP, PropertyEnum.DATE_TIME_START,
            PropertyEnum.FREE_BUSY_TIME, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.ORGANIZER,
            PropertyEnum.REQUEST_STATUS, PropertyEnum.UNIQUE_IDENTIFIER, PropertyEnum.UNIFORM_RESOURCE_LOCATOR),
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
    DAYLIGHT (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.TIME_ZONE_NAME, PropertyEnum.TIME_ZONE_OFFSET_FROM,
            PropertyEnum.TIME_ZONE_OFFSET_TO),
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
    STANDARD (Arrays.asList(PropertyEnum.COMMENT, PropertyEnum.DATE_TIME_START,
            PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.RECURRENCE_DATE_TIMES,
            PropertyEnum.RECURRENCE_RULE, PropertyEnum.TIME_ZONE_NAME, PropertyEnum.TIME_ZONE_OFFSET_FROM,
            PropertyEnum.TIME_ZONE_OFFSET_TO),
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
    VALARM (Arrays.asList(PropertyEnum.ACTION, PropertyEnum.ATTACHMENT, PropertyEnum.ATTENDEE, PropertyEnum.DESCRIPTION,
            PropertyEnum.DURATION, PropertyEnum.IANA_PROPERTY, PropertyEnum.NON_STANDARD, PropertyEnum.REPEAT_COUNT,
            PropertyEnum.SUMMARY, PropertyEnum.TRIGGER),
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
    PRODID (null, true)
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
            ProductIdentifier prodid = ProductIdentifier.parse(line);
            vCalendar.setProductIdentifier(prodid);
            return prodid;
        }
    },
    VERSION (null, true)
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
            Version version = Version.parse(line);
            vCalendar.setVersion(version);
            return version;
        }
    };
//
//    private static List<VComponentEnum> calendarElements = Arrays.stream(VComponentEnum.values())
//            .filter(e -> e.isCalendarElement())
//            .collect(Collectors.toList());
    
    private List<PropertyEnum> allowedProperties;
    public List<PropertyEnum> allowedProperties() { return allowedProperties; }
    private boolean isCalendarElement;
    public boolean isCalendarElement() { return isCalendarElement; }
    
    VComponentEnum(List<PropertyEnum> allowedProperties, boolean isCalendarElement)
    {
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
