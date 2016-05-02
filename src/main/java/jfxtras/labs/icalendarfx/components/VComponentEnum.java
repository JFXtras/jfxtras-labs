package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;

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
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            VEventNew e = new VEventNew();
            e.parseContent(contentLines);
            System.out.println("newv:" + contentLines);
            vCalendar.getVEvents().add(e);
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
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
        public void parse(VCalendar vCalendar, List<String> contentLines)
        {
            // TODO Auto-generated method stub
            
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
    abstract public void parse(VCalendar vCalendar, List<String> contentLines);
//    {
//        // TODO Auto-generated method stub
//        
//    }

    public VCalendarElement getElement()
    {
        // TODO Auto-generated method stub
        return null;
    }

//    public void parse(VCalendar vCalendar, String content)
//    {
//        // TODO Auto-generated method stub
//        
//    }
}
