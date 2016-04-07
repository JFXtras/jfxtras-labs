package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import jfxtras.labs.icalendar.components.VComponent;
import jfxtras.labs.icalendar.components.VComponentPrimary;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.change.Sequence;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.descriptive.Location;
import jfxtras.labs.icalendar.properties.component.descriptive.Resources;
import jfxtras.labs.icalendar.properties.component.descriptive.Status;
import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentBase64;
import jfxtras.labs.icalendar.properties.component.descriptive.attachment.AttachmentURI;
import jfxtras.labs.icalendar.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendar.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Contact;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDate;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendar.properties.component.time.DurationProp;
import jfxtras.labs.icalendar.properties.component.time.FreeBusyTime;
import jfxtras.labs.icalendar.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendar.properties.component.time.due.DueLocalDate;
import jfxtras.labs.icalendar.properties.component.time.due.DueLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.due.DueZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDate;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneOffsetTo;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneURL;

public enum PropertyEnum
{
    ACTION ("ACTION", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Alarm
    ATTACHMENT_URI ("ATTACH" // property name
            , ValueType.UNIFORM_RESOURCE_IDENTIFIER // default property value type
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , AttachmentURI.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    ATTACHMENT_BASE64 ("ATTACH" // property name
            , ValueType.BINARY // default property value type
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.INLINE_ENCODING, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , AttachmentBase64.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    ATTENDEE ("ATTENDEE"    // property name
            , ValueType.CALENDAR_USER_ADDRESS   // default property value type
            , Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.CALENDAR_USER_TYPE, ParameterEnum.DELEGATEES,
                    ParameterEnum.DELEGATORS, ParameterEnum.DIRECTORY_ENTRY_REFERENCE,
                    ParameterEnum.GROUP_OR_LIST_MEMBERSHIP, ParameterEnum.LANGUAGE, ParameterEnum.PARTICIPATION_ROLE,
                    ParameterEnum.PARTICIPATION_STATUS, ParameterEnum.RSVP_EXPECTATION, ParameterEnum.SENT_BY,
                    ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Attendee.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    CALENDAR_SCALE ("CALSCALE", null, null, CalendarScale.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Calendar
    CATEGORIES ("CATEGORIES" // property name
            , ValueType.TEXT // default property value type
            , Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Categories.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    CLASSIFICATION ("CLASS", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Classification.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            },
    COMMENT ("COMMENT", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Comment.class)
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            return null; // handled in getPropertyList
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            VComponentPrimary castProperty = (VComponentPrimary) parent;
            return castProperty.comments();
        }
    },
    CONTACT ("CONTACT", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Contact.class)
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Relationship
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCompleted.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_CREATED ("CREATED", null, null, DateTimeCreated.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Change management
    DATE_TIME_DUE_LOCAL_DATE ("DUE", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DueLocalDate.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_DUE_LOCAL_DATE_TIME ("DUE", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DueLocalDateTime.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_DUE_ZONED_DATE_TIME ("DUE", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DueZonedDateTime.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_END_LOCAL_DATE ("DTEND", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndLocalDate.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_END_LOCAL_DATE_TIME ("DTEND", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndLocalDateTime.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    DATE_TIME_END_ZONED_DATE_TIME ("DTEND", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTEndZonedDateTime.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DATE_TIME_STAMP ("DTSTAMP", null, null, DateTimeStamp.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Change management
    DATE_TIME_START_LOCAL_DATE ("DTSTART", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartLocalDate.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent parent)
        {
            VComponentPrimary castProperty = (VComponentPrimary) parent;
            return castProperty.getDateTimeStart();
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DATE_TIME_START_LOCAL_DATE_TIME ("DTSTART", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartLocalDateTime.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    DATE_TIME_START_ZONED_DATE_TIME ("DTSTART", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DTStartZonedDateTime.class)
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // property class
    DESCRIPTION ("DESCRIPTION",
            ValueType.TEXT,
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE),
            Description.class)
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Descriptive
    DURATION ("DURATION", // property name
            ValueType.DURATION, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            ValueType.PERIOD, // default property value type
            Arrays.asList(ParameterEnum.FREE_BUSY_TIME_TYPE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            FreeBusyTime.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Miscellaneous
    IANA_PROPERTY ("UNKNOWN", // property name
            ValueType.TEXT, // default property value type (any value allowed)
            Arrays.asList(ParameterEnum.values()), // all parameters allowed
            IANAProperty.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Calendar
    METHOD ("METHOD", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Miscellaneous
    NON_STANDARD ("X-", // property name (begins with X- prefix)
            ValueType.TEXT, // default property value type (any value allowed)
            Arrays.asList(ParameterEnum.values()), // all parameters allowed
            NonStandardProperty.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    ORGANIZER ("ORGANIZER", // name
            ValueType.CALENDAR_USER_ADDRESS, // default property value type
            Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.DIRECTORY_ENTRY_REFERENCE, ParameterEnum.LANGUAGE,
                    ParameterEnum.SENT_BY), // allowed parameters
            Organizer.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    PERCENT_COMPLETE ("PERCENT", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Descriptive
    PRIORITY ("PRIORITY", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Descriptive
    PRODUCT_IDENTIFIER ("PRODID", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Calendar
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER_LOCAL_DATE ("RECURRENCE-ID", // property name
            ValueType.DATE, // default property value type
            Arrays.asList(ParameterEnum.RECURRENCE_IDENTIFIER_RANGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceIDLocalDate.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER_LOCAL_DATE_TIME ("RECURRENCE-ID", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.RECURRENCE_IDENTIFIER_RANGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceIDLocalDateTime.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER_ZONED_DATE_TIME ("RECURRENCE-ID", // property name
            ValueType.DATE_TIME, // default property value type
            Arrays.asList(ParameterEnum.RECURRENCE_IDENTIFIER_RANGE, ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceIDZonedDateTime.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    RECURRENCE_RULE ("RRULE", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Recurrence
    RELATED_TO ("RELATED-TO", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Relationship
    REPEAT_COUNT ("REPEAT", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Alarm
    REQUEST_STATUS ("REQUEST-STATUS", null, null, RequestStatus.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Miscellaneous
    RESOURCES ("RESOURCES", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Resources.class) {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            }, // property class
    SEQUENCE ("SEQUENCE", null, null, Sequence.class) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Change management
    STATUS ("STATUS", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Status.class) // property class
    {
                @Override
                public Property<?> getProperty(VComponent vComponent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public List<? extends Property<?>> getPropertyList(VComponent parent)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            },
    SUMMARY ("SUMMARY", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE,
                    ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Summary.class) // property class
    {
            @Override
            public Property<?> getProperty(VComponent vComponent)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<? extends Property<?>> getPropertyList(VComponent parent)
            {
                // TODO Auto-generated method stub
                return null;
            }
        },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    TIME_ZONE_NAME ("TZNAME", // property name
            ValueType.TEXT, // default property value type
            Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            ValueType.UTC_OFFSET, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            ValueType.UTC_OFFSET, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            ValueType.UNIFORM_RESOURCE_IDENTIFIER, // default property value type
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Time Zone
    TRIGGER ("TRIGGER", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    },  // Alarm
    UNIQUE_IDENTIFIER ("UID", ValueType.TEXT, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", null, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }, // Relationship
    VERSION ("VERSION", ValueType.TEXT, null, null) {
        @Override
        public Property<?> getProperty(VComponent vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<? extends Property<?>> getPropertyList(VComponent parent)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }; // Calendar
    
    // Map to match up name to enum List
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
    public static List<PropertyEnum> enumListFromName(String propertyName)
    {
        return enumListFromNameMap.get(propertyName.toUpperCase());
    }
    
    private static Map<Pair<String, ValueType>, PropertyEnum> enumFromNameMap = makeEnumFromNameMap();
    private static Map<Pair<String, ValueType>, PropertyEnum> makeEnumFromNameMap()
    {
        Map<Pair<String, ValueType>, PropertyEnum> map = new HashMap<>();
        PropertyEnum[] values = PropertyEnum.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(new Pair<String, ValueType>(values[i].toString(), values[i].defaultValueType()), values[i]);
        }
        return map;
    }
    @Deprecated
    public static PropertyEnum enumFromName(String propertyName, ValueType valueType)
    {
        return enumFromNameMap.get(new Pair<String, ValueType>(propertyName.toUpperCase(),valueType));
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
    
    private ValueType valueType;
    public ValueType defaultValueType() { return valueType; }

    private List<ParameterEnum> allowedParameters;
    public List<ParameterEnum> allowedParameters() { return allowedParameters; }
    
    PropertyEnum(String name, ValueType valueType, List<ParameterEnum> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueType = valueType;
        this.myClass = myClass;
    }
    /*
     * ABSTRACT METHODS
     */
    abstract public Property<?> getProperty(VComponent vComponent);
    abstract public List<? extends Property<?>> getPropertyList(VComponent parent);
}
