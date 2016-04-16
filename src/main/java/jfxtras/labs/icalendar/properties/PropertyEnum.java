package jfxtras.labs.icalendar.properties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfxtras.labs.icalendar.components.VComponentNew;
import jfxtras.labs.icalendar.components.VComponentPersonal;
import jfxtras.labs.icalendar.components.VComponentPrimary;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.component.alarm.Action;
import jfxtras.labs.icalendar.properties.component.alarm.RepeatCount;
import jfxtras.labs.icalendar.properties.component.alarm.Trigger;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.change.LastModified;
import jfxtras.labs.icalendar.properties.component.change.Sequence;
import jfxtras.labs.icalendar.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Status;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendar.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.recurrence.Exceptions;
import jfxtras.labs.icalendar.properties.component.recurrence.RecurrenceRuleProp;
import jfxtras.labs.icalendar.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Contact;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;
import jfxtras.labs.icalendar.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendar.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendar.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendar.properties.component.time.DateTimeDue;
import jfxtras.labs.icalendar.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendar.properties.component.time.DurationProp;
import jfxtras.labs.icalendar.properties.component.time.FreeBusyTime;
import jfxtras.labs.icalendar.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneOffsetTo;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneURL;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public enum PropertyEnum
{
    // Alarm
    ACTION ("ACTION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Action.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.INLINE_ENCODING, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Attachment.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },

    ATTENDEE ("ATTENDEE"    // property name
            , Arrays.asList(ValueType.CALENDAR_USER_ADDRESS) // valid property value types, first is default
            , Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.CALENDAR_USER_TYPE, ParameterEnum.DELEGATEES,
                    ParameterEnum.DELEGATORS, ParameterEnum.DIRECTORY_ENTRY_REFERENCE,
                    ParameterEnum.GROUP_OR_LIST_MEMBERSHIP, ParameterEnum.LANGUAGE, ParameterEnum.PARTICIPATION_ROLE,
                    ParameterEnum.PARTICIPATION_STATUS, ParameterEnum.RSVP_EXPECTATION, ParameterEnum.SENT_BY,
                    ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Attendee.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getAttendees();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            castComponent.getAttendees().add(new Attendee(propertyContent));
        }
    },
    // Calendar
    CALENDAR_SCALE ("CALSCALE", null, null, CalendarScale.class) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    CATEGORIES ("CATEGORIES" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Categories.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // descriptive
    CLASSIFICATION ("CLASS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Classification.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Comment.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPrimary castProperty = (VComponentPrimary) vComponent;
            return castProperty.getComments();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPrimary castComponent = (VComponentPrimary) vComponent;
            castComponent.getComments().add(new Comment(propertyContent));
        }
    },
    CONTACT ("CONTACT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Contact.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCompleted.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCreated.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Change management
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeDue.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeEnd.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeStamp.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getDateTimeStamp();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            Temporal t = DateTimeUtilities.temporalFromString(propertyContent);
            castComponent.setDateTimeStamp(new DateTimeStamp((ZonedDateTime) t));                                
        }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeStart.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPrimary castComponent = (VComponentPrimary) vComponent;
            return castComponent.getDateTimeStart();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPrimary castComponent = (VComponentPrimary) vComponent;
//            int lastColonIndex = propertyContent.lastIndexOf(':');
            Temporal t = DateTimeUtilities.temporalFromString(propertyContent);
            if (t instanceof LocalDate)
            {
                castComponent.setDateTimeStart(new DateTimeStart<LocalDate>((LocalDate) t));                
            } else if (t instanceof LocalDateTime)
            {
                castComponent.setDateTimeStart(new DateTimeStart<LocalDateTime>((LocalDateTime) t));                
                
            } else if (t instanceof ZonedDateTime)
            {
                castComponent.setDateTimeStart(new DateTimeStart<ZonedDateTime>((ZonedDateTime) t));                                
            }
        }
    },

    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE),
            Description.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Descriptive
    DURATION ("DURATION", // property name
            Arrays.asList(ValueType.DURATION), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Exceptions.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            Arrays.asList(ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterEnum.FREE_BUSY_TIME_TYPE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            FreeBusyTime.class) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", null, null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Miscellaneous
    IANA_PROPERTY (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.get(0), /** property name (one in list of valid names at {@link #IANAProperty} */
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterEnum.values()), // all parameters allowed
            IANAProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            return vComponent.getIANAProperties();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            vComponent.getIANAProperties().add(new IANAProperty(propertyContent));
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            LastModified.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Calendar
    METHOD ("METHOD", null, null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Miscellaneous
    NON_STANDARD ("X-", // property name (begins with X- prefix)
            Arrays.asList(ValueType.values()), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterEnum.values()), // all parameters allowed
            NonStandardProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            return vComponent.getNonStandardProperties();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            vComponent.getNonStandardProperties().add(new NonStandardProperty(propertyContent));
        }
    },
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.DIRECTORY_ENTRY_REFERENCE, ParameterEnum.LANGUAGE,
                    ParameterEnum.SENT_BY), // allowed parameters
            Organizer.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getOrganizer();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            castComponent.setOrganizer(new Organizer(propertyContent));
        }
    }, // property class
    PERCENT_COMPLETE ("PERCENT", null, null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Descriptive
    PRIORITY ("PRIORITY", null, null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Descriptive
    // Calendar
    PRODUCT_IDENTIFIER ("PRODID", null, null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Recurrences.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.RECURRENCE_IDENTIFIER_RANGE, ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceId.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceRuleProp.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Recurrence
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.RELATIONSHIP_TYPE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RelatedTo.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Relationship
    // Alarm
    REPEAT_COUNT ("REPEAT", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RepeatCount.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RequestStatus.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getRequestStatus();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            castComponent.getRequestStatus().add(new RequestStatus(propertyContent));
        }
    },
    RESOURCES ("RESOURCES", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Resources.class)
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // property class
    SEQUENCE ("SEQUENCE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Sequence.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Change management
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Status.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE,
                    ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Summary.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }, // Time Zone
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALARM_TRIGGER_RELATIONSHIP, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Trigger.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            UniqueIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getUniqueIdentifier();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            castComponent.setUniqueIdentifier(new UniqueIdentifier(propertyContent));
        }
    },
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            UniformResourceLocator.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            return castComponent.getUniformResourceLocator();
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            VComponentPersonal castComponent = (VComponentPersonal) vComponent;
            castComponent.setUniformResourceLocator(new UniformResourceLocator(propertyContent));
        }
    }, // Relationship
    VERSION ("VERSION", Arrays.asList(ValueType.TEXT), null, null) {
        @Override
        public Object getProperty(VComponentNew vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    }; // Calendar
    
    // Map to match up name to enum List
    @Deprecated // go back to one name per proprety
    private static Map<String, List<PropertyEnum>> enumListFromNameMap = makeEnumListFromNameMap();
    private static Map<String, List<PropertyEnum>> makeEnumListFromNameMap()
    {
        Map<String, List<PropertyEnum>> map = new HashMap<>();
        Arrays.stream(PropertyEnum.values())
        .forEach(e ->
        {
            if (map.get(e.toString()) == null)
            {
                map.put(e.toString(), new ArrayList<>(Arrays.asList(e)));
            } else
            {
                map.get(e.toString()).add(e);
            }
        });
        return map;
    }
    @Deprecated // go back to one name per proprety
    public static List<PropertyEnum> enumListFromName(String propertyName)
    {
        return enumListFromNameMap.get(propertyName.toUpperCase());
    }
    
    private static Map<String, PropertyEnum> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, PropertyEnum> makeEnumFromNameMap()
    {
        Map<String, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static PropertyEnum enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName);
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Property>, PropertyEnum> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Property>, PropertyEnum> makeEnumFromClassMap()
    {
        Map<Class<? extends Property>, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static PropertyEnum enumFromClass(Class<? extends Property> myClass)
    {
        PropertyEnum p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(PropertyEnum.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private Class<? extends Property> myClass;

    @Override
    public String toString() { return name; }
    private String name;
    
    private List<ValueType> valueTypes;
    public List<ValueType> allowedValueTypes() { return valueTypes; }

    private List<ParameterEnum> allowedParameters;
    public List<ParameterEnum> allowedParameters() { return allowedParameters; }
    
    PropertyEnum(String name, List<ValueType> valueTypes, List<ParameterEnum> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueTypes = valueTypes;
        this.myClass = myClass;
    }
    /*
     * ABSTRACT METHODS
     */
    /** Returns either Property<?> or List<Property<?>> */
    abstract public Object getProperty(VComponentNew vComponent);
    /** Parses string and sets property */
    abstract public void parse(VComponentNew vComponent, String propertyContent);
}
