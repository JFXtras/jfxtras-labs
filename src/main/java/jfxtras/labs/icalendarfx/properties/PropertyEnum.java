package jfxtras.labs.icalendarfx.properties;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.StandardOrSavings;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponentAttendee;
import jfxtras.labs.icalendarfx.components.VComponentDateTimeEnd;
import jfxtras.labs.icalendarfx.components.VComponentDescribable;
import jfxtras.labs.icalendarfx.components.VComponentDescribable2;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentDuration;
import jfxtras.labs.icalendarfx.components.VComponentLastModified;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentNew;
import jfxtras.labs.icalendarfx.components.VComponentPersonal;
import jfxtras.labs.icalendarfx.components.VComponentPrimary;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.ParameterEnum;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action;
import jfxtras.labs.icalendarfx.properties.component.alarm.RepeatCount;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.PercentComplete;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeDue;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneURL;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public enum PropertyEnum
{
    // Alarm
    ACTION ("ACTION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Action.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getAction();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            if (castComponent.getAction() == null)
            {
                castComponent.setAction(Action.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterEnum.FORMAT_TYPE, ParameterEnum.INLINE_ENCODING, ParameterEnum.VALUE_DATA_TYPES) // allowed parameters
            , Attachment.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getAttachments();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            
            final ObservableList<Attachment<?>> list;
            if (castComponent.getAttachments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttachments(list);
            } else
            {
                list = castComponent.getAttachments();
            }

            list.add(Attachment.parse(propertyContent));
//            boolean isBinary = propertyContent.toUpperCase().contains("VALUE=BINARY");
//            if (isBinary)
//            {
//                list.add(new Attachment<String>(String.class, propertyContent));
//            } else
//            {
//                list.add(new Attachment<URI>(URI.class, propertyContent));
//            }
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
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentAttendee<?> castComponent = (VComponentAttendee<?>) vComponent;
            return castComponent.getAttendees();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentAttendee<?> castComponent = (VComponentAttendee<?>) vComponent;
            final ObservableList<Attendee> list;
            if (castComponent.getAttendees() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttendees(list);
            } else
            {
                list = castComponent.getAttendees();
            }
            list.add(Attendee.parse(propertyContent));
        }
    },
    // Calendar
    CALENDAR_SCALE ("CALSCALE", null, null, CalendarScale.class) {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getCategories();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            final ObservableList<Categories> list;
            if (castComponent.getCategories() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setCategories(list);
            } else
            {
                list = castComponent.getCategories();
            }
            list.add(Categories.parse(propertyContent));
        }
    },
    // descriptive
    CLASSIFICATION ("CLASS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Classification.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getClassification();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            if (castComponent.getClassification() == null)
            {
                castComponent.setClassification(Classification.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Comment.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPrimary<?> castProperty = (VComponentPrimary<?>) vComponent;
            return castProperty.getComments();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPrimary<?> castComponent = (VComponentPrimary<?>) vComponent;
            final ObservableList<Comment> list;
            if (castComponent.getComments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setComments(list);
            } else
            {
                list = castComponent.getComments();
            }
            list.add(Comment.parse(propertyContent));
        }
    },
    // Relationship
    CONTACT ("CONTACT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Contact.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            if (vComponent instanceof VFreeBusy)
            {// VJournal has one Contact
                VFreeBusy castComponent = (VFreeBusy) vComponent;
                return castComponent.getContact();                
            } else
            { // Other components have a list of Contacts
                VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
                return castComponent.getContacts();
            }
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            if (vComponent instanceof VFreeBusy)
            {// VJournal has one Contact
                VFreeBusy castComponent = (VFreeBusy) vComponent;
                castComponent.setContact(Contact.parse(propertyContent));                
            } else
            { // Other components have a list of Contacts
                VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
                final ObservableList<Contact> list;
                if (castComponent.getContacts() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setContacts(list);
                } else
                {
                    list = castComponent.getContacts();
                }
                list.add(Contact.parse(propertyContent));
            }
        }
    },
    // Date and Time
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCompleted.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeCompleted();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vComponent;
            if (castComponent.getDateTimeCompleted() == null)
            {
                castComponent.setDateTimeCompleted(DateTimeCompleted.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Change management
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeCreated.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getDateTimeCreated();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            if (castComponent.getDateTimeCreated() == null)
            {
                castComponent.setDateTimeCreated(DateTimeCreated.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Date and time
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeDue.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeDue();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vComponent;
            if (castComponent.getDateTimeDue() == null)
            {
                castComponent.setDateTimeDue(DateTimeDue.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Date and Time
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeEnd.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDateTimeEnd<?> castComponent = (VComponentDateTimeEnd<?>) vComponent;
            return castComponent.getDateTimeEnd();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDateTimeEnd<?> castComponent = (VComponentDateTimeEnd<?>) vComponent;
            if (castComponent.getDateTimeEnd() == null)
            {
                castComponent.setDateTimeEnd(DateTimeEnd.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeStamp.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getDateTimeStamp();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            if (castComponent.getDateTimeStamp() == null)
            {
                castComponent.setDateTimeStamp(DateTimeStamp.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DateTimeStart.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPrimary<?> castComponent = (VComponentPrimary<?>) vComponent;
            return castComponent.getDateTimeStart();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPrimary<?> castComponent = (VComponentPrimary<?>) vComponent;
            if (castComponent.getDateTimeStart() == null)
            {
                castComponent.setDateTimeStart(DateTimeStart.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE),
            Description.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            if (vComponent instanceof VJournal)
            {// VJournal has list of Description// VJournal has list of Description
                VJournal castComponent = (VJournal) vComponent;
                return castComponent.getDescriptions();                
            } else
            { // Other components has only one Description
                VComponentDescribable2<?> castComponent = (VComponentDescribable2<?>) vComponent;
                return castComponent.getDescription();
            }
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            if (vComponent instanceof VJournal)
            { // VJournal has list of Description
                VJournal castComponent = (VJournal) vComponent;
                final ObservableList<Description> list;
                if (castComponent.getDescriptions() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setDescriptions(list);
                } else
                {
                    list = castComponent.getDescriptions();
                }
                list.add(Description.parse(propertyContent));
            } else
            { // Other components has only one Description
                VComponentDescribable2<?> castComponent = (VComponentDescribable2<?>) vComponent;
                if (castComponent.getDescription() == null)
                {
                    castComponent.setDescription(Description.parse(propertyContent));                                
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
                }
            }
        }
    },
    // Date and Time
    DURATION ("DURATION", // property name
            Arrays.asList(ValueType.DURATION), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDuration<?> castComponent = (VComponentDuration<?>) vComponent;
            return castComponent.getDuration();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDuration<?> castComponent = (VComponentDuration<?>) vComponent;
            if (castComponent.getDuration() == null)
            {
                castComponent.setDuration(DurationProp.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Exceptions.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getExceptions();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            final ObservableList<Exceptions<? extends Temporal>> list;
            if (castComponent.getExceptions() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setExceptions(list);
            } else
            {
                list = castComponent.getExceptions();
            }
            list.add(Exceptions.parse(propertyContent));
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            Arrays.asList(ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterEnum.FREE_BUSY_TIME_TYPE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            FreeBusyTime.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VFreeBusy castComponent = (VFreeBusy) vComponent;
            return castComponent.getFreeBusyTime();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VFreeBusy castComponent = (VFreeBusy) vComponent;
            if (castComponent.getFreeBusyTime() == null)
            {
                castComponent.setFreeBusyTime(FreeBusyTime.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", // property name
            Arrays.asList(ValueType.TEXT), // In GeographicPosition there are two doubles for latitude and longitude
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            GeographicPosition.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getGeographicPosition();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            if (castComponent.getGeographicPosition() == null)
            {
                castComponent.setGeographicPosition(GeographicPosition.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Miscellaneous
    IANA_PROPERTY (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.get(0), /** property name (one in list of valid names at {@link #IANAProperty} */
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterEnum.values()), // all parameters allowed
            IANAProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            return vComponent.getIANAProperties();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            final ObservableList<IANAProperty> list;
            if (vComponent.getIANAProperties() == null)
            {
                list = FXCollections.observableArrayList();
                vComponent.setIANAProperties(list);
            } else
            {
                list = vComponent.getIANAProperties();
            }
            list.add(IANAProperty.parse(propertyContent));
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            LastModified.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentLastModified<?> castComponent = (VComponentLastModified<?>) vComponent;
            return castComponent.getDateTimeLastModified();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentLastModified<?> castComponent = (VComponentLastModified<?>) vComponent;
            if (castComponent.getDateTimeLastModified() == null)
            {
                castComponent.setDateTimeLastModified(LastModified.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getLocation();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            if (castComponent.getLocation() == null)
            {
                castComponent.setLocation(Location.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Calendar
    METHOD ("METHOD", null, null, null) {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public Object getProperty(VComponentNew<?> vComponent)
        {
            return vComponent.getNonStandardProperties();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            final ObservableList<NonStandardProperty> list;
            if (vComponent.getNonStandardProperties() == null)
            {
                list = FXCollections.observableArrayList();
                vComponent.setNonStandardProperties(list);
            } else
            {
                list = vComponent.getNonStandardProperties();
            }
            list.add(NonStandardProperty.parse(propertyContent));
        }
    },
    // Relationship
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterEnum.COMMON_NAME, ParameterEnum.DIRECTORY_ENTRY_REFERENCE, ParameterEnum.LANGUAGE,
                    ParameterEnum.SENT_BY), // allowed parameters
            Organizer.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getOrganizer();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            castComponent.setOrganizer(Organizer.parse(propertyContent));
        }
    },
    // Descriptive
    PERCENT_COMPLETE ("PERCENT-COMPLETE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            PercentComplete.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getPercentComplete();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vComponent;
            if (castComponent.getPercentComplete() == null)
            {
                castComponent.setPercentComplete(PercentComplete.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    PRIORITY ("PRIORITY", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Priority.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getPriority();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            if (castComponent.getPriority() == null)
            {
                castComponent.setPriority(Priority.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Calendar
    PRODUCT_IDENTIFIER ("PRODID", null, null, null) {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrences();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            final ObservableList<Recurrences<? extends Temporal>> list;
            if (castComponent.getRecurrences() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRecurrences(list);
            } else
            {
                list = castComponent.getRecurrences();
            }
            list.add(Recurrences.parse(propertyContent));
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.RECURRENCE_IDENTIFIER_RANGE, ParameterEnum.TIME_ZONE_IDENTIFIER, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceId.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getRecurrenceId();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            if (castComponent.getRecurrenceId() == null)
            {
                castComponent.setRecurrenceId(RecurrenceId.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Recurrence
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceRule.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceRule();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            if (castComponent.getRecurrenceRule() == null)
            {
                castComponent.setRecurrenceRule(RecurrenceRule.parse(propertyContent));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Relationship
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.RELATIONSHIP_TYPE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RelatedTo.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getRelatedTo();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            final ObservableList<RelatedTo> list;
            if (castComponent.getRelatedTo() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRelatedTo(list);
            } else
            {
                list = castComponent.getRelatedTo();
            }
            list.add(RelatedTo.parse(propertyContent));
        }
    },
    // Alarm
    REPEAT_COUNT ("REPEAT", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RepeatCount.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getRepeatCount();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            if (castComponent.getRepeatCount() == null)
            {
                castComponent.setRepeatCount(RepeatCount.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            RequestStatus.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getRequestStatus();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            final ObservableList<RequestStatus> list;
            if (castComponent.getRequestStatus() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRequestStatus(list);
            } else
            {
                list = castComponent.getRequestStatus();
            }
            list.add(RequestStatus.parse(propertyContent));
        }
    },
    // Descriptive
    RESOURCES ("RESOURCES", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Resources.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentLocatable<?> castProperty = (VComponentLocatable<?>) vComponent;
            return castProperty.getResources();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            final ObservableList<Resources> list;
            if (castComponent.getResources() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setResources(list);
            } else
            {
                list = castComponent.getResources();
            }
            list.add(Resources.parse(propertyContent));
        }
    },
    // Change management
    SEQUENCE ("SEQUENCE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Sequence.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getSequence();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            if (castComponent.getSequence() == null)
            {
                castComponent.setSequence(Sequence.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Status.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getStatus();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            if (castComponent.getStatus() == null)
            {
                castComponent.setStatus(Status.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Descriptive
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALTERNATE_TEXT_REPRESENTATION, ParameterEnum.LANGUAGE,
                    ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Summary.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getSummary();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            if (castComponent.getSummary() == null)
            {
                castComponent.setSummary(Summary.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VEventNew castComponent = (VEventNew) vComponent;
            return castComponent.getTimeTransparency();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VEventNew castComponent = (VEventNew) vComponent;
            if (castComponent.getTimeTransparency() == null)
            {
                castComponent.setTimeTransparency(new TimeTransparency(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneIdentifier();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            if (castComponent.getTimeZoneIdentifier() == null)
            {
                castComponent.setTimeZoneIdentifier(new TimeZoneIdentifier(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Time Zone
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.LANGUAGE, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrSavings<?> castProperty = (StandardOrSavings<?>) vComponent;
            return castProperty.getTimeZoneNames();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            StandardOrSavings<?> castComponent = (StandardOrSavings<?>) vComponent;
            final ObservableList<TimeZoneName> list;
            if (castComponent.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setTimeZoneNames(list);
            } else
            {
                list = castComponent.getTimeZoneNames();
            }
            list.add(new TimeZoneName(propertyContent));
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrSavings<?> castComponent = (StandardOrSavings<?>) vComponent;
            return castComponent.getTimeZoneOffsetFrom();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            StandardOrSavings<?> castComponent = (StandardOrSavings<?>) vComponent;
            if (castComponent.getTimeZoneOffsetFrom() == null)
            {
                castComponent.setTimeZoneOffsetFrom(new TimeZoneOffsetFrom(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrSavings<?> castComponent = (StandardOrSavings<?>) vComponent;
            return castComponent.getTimeZoneOffsetTo();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            StandardOrSavings<?> castComponent = (StandardOrSavings<?>) vComponent;
            if (castComponent.getTimeZoneOffsetTo() == null)
            {
                castComponent.setTimeZoneOffsetTo(new TimeZoneOffsetTo(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            // TODO Auto-generated method stub
            
        }
    },
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterEnum.ALARM_TRIGGER_RELATIONSHIP, ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            Trigger.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getTrigger();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            if (castComponent.getTrigger() == null)
            {
                Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyContent);
                String valueType = map.get(ParameterEnum.VALUE_DATA_TYPES.toString());
                String value = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
                if ((valueType != null) && (valueType.equals(ValueType.DATE_TIME.toString())))
                { // date-time
                    ZonedDateTime t = ZonedDateTime.parse(value, DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
                    castComponent.setTrigger(new Trigger<ZonedDateTime>(t));    
                } else if ((valueType == null) || (valueType.equals(ValueType.DURATION)))
                { // duration
                    castComponent.setTrigger(new Trigger<Duration>(Duration.parse(value)));
                } else
                {
                    throw new IllegalArgumentException("Unsupported value type:" + valueType);
                }
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            UniqueIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            return castComponent.getUniqueIdentifier();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            if (castComponent.getUniqueIdentifier() == null)
            {
                castComponent.setUniqueIdentifier(new UniqueIdentifier(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
    },
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterEnum.VALUE_DATA_TYPES), // allowed parameters
            UniformResourceLocator.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            return castComponent.getUniformResourceLocator();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            castComponent.setUniformResourceLocator(new UniformResourceLocator(propertyContent));
        }
    }, // Relationship
    VERSION ("VERSION", Arrays.asList(ValueType.TEXT), null, null) {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
    abstract public Object getProperty(VComponentNew<?> vComponent);
    /** Parses string and sets property */
    abstract public void parse(VComponentNew<?> vComponent, String propertyContent);
}
