package jfxtras.labs.icalendarfx.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.VElement;
import jfxtras.labs.icalendarfx.components.StandardOrDaylight;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentAttendee;
import jfxtras.labs.icalendarfx.components.VComponentBase;
import jfxtras.labs.icalendarfx.components.VComponentCommon;
import jfxtras.labs.icalendarfx.components.VComponentDateTimeEnd;
import jfxtras.labs.icalendarfx.components.VComponentDescribable;
import jfxtras.labs.icalendarfx.components.VComponentDescribable2;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentDuration;
import jfxtras.labs.icalendarfx.components.VComponentLastModified;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentPersonal;
import jfxtras.labs.icalendarfx.components.VComponentPrimary;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.ParameterType;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
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
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
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

/**
 * For each VComponent property (RFC 5545, 3.8, page 80) contains the following: <br>
 * <br>
 * Property name {@link #toString()} <br>
 * Allowed property value type (first is default value type) {@link PropertyType#allowedValueTypes()}<br>
 * Allowed parameters {@link #allowedParameters()}<br>
 * Property class {@link #getPropertyClass()}<br>
 * Method to get property from component {@link #getProperty(VComponent)}<br>
 * Method to parse property string into parent component {@link #parse(VComponent, String)}<br>
 * Method to copy property into new parent component {@link #copyProperty(Property, VComponent)}<br>
 * 
 * @author David Bal
 *
 */
public enum PropertyType
{
    // Alarm
    ACTION ("ACTION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Action.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getAction();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Action propertyCopy = new Action((Action) child);
            castDestination.setAction(propertyCopy);
        }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterType.FORMAT_TYPE, ParameterType.INLINE_ENCODING, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , Attachment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getAttachments();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDescribable<?> castDestination = (VComponentDescribable<?>) destination;
            final ObservableList<Attachment<?>> list;
            if (castDestination.getAttachments() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setAttachments(list);
            } else
            {
                list = castDestination.getAttachments();
            }
            list.add(new Attachment<>((Attachment<?>) child));
        }
    },

    ATTENDEE ("ATTENDEE"    // property name
            , Arrays.asList(ValueType.CALENDAR_USER_ADDRESS) // valid property value types, first is default
            , Arrays.asList(ParameterType.COMMON_NAME, ParameterType.CALENDAR_USER_TYPE, ParameterType.DELEGATEES,
                    ParameterType.DELEGATORS, ParameterType.DIRECTORY_ENTRY_REFERENCE,
                    ParameterType.GROUP_OR_LIST_MEMBERSHIP, ParameterType.LANGUAGE, ParameterType.PARTICIPATION_ROLE,
                    ParameterType.PARTICIPATION_STATUS, ParameterType.RSVP_EXPECTATION, ParameterType.SENT_BY,
                    ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , Attendee.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentAttendee<?> castComponent = (VComponentAttendee<?>) vComponent;
            return castComponent.getAttendees();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentAttendee<?> castDestination = (VComponentAttendee<?>) destination;
            final ObservableList<Attendee> list;
            if (castDestination.getAttendees() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setAttendees(list);
            } else
            {
                list = castDestination.getAttendees();
            }
            list.add(new Attendee((Attendee) child));
        }
    },
    // Calendar property
    CALENDAR_SCALE ("CALSCALE" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , CalendarScale.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Descriptive
    CATEGORIES ("CATEGORIES" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , Categories.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getCategories();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            final ObservableList<Categories> list;
            if (castDestination.getCategories() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setCategories(list);
            } else
            {
                list = castDestination.getCategories();
            }
            list.add(new Categories((Categories) child));
        }
    },
    // Descriptive
    CLASSIFICATION ("CLASS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Classification.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getClassification();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Classification propertyCopy = new Classification((Classification) child);
            castDestination.setClassification(propertyCopy);
        }
    },
    // Descriptive
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.OTHER), // allowed parameters
            Comment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPrimary<?> castProperty = (VComponentPrimary<?>) vComponent;
            return castProperty.getComments();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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
        
        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPrimary<?> castDestination = (VComponentPrimary<?>) destination;
            final ObservableList<Comment> list;
            if (castDestination.getComments() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setComments(list);
            } else
            {
                list = castDestination.getComments();
            }
            list.add(new Comment((Comment) child));
        }
    },
    // Relationship
    CONTACT ("CONTACT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.OTHER), // allowed parameters
            Contact.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
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
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            if (destination instanceof VFreeBusy)
            { // VFreeBusy has one Contact
                VFreeBusy castDestination = (VFreeBusy) destination;
                Contact propertyCopy = new Contact((Contact) child);
                castDestination.setContact(propertyCopy);
            } else
            { // Other components have a list of Contacts
                VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
                final ObservableList<Contact> list;
                if (castDestination.getContacts() == null)
                {
                    list = FXCollections.observableArrayList();
                    castDestination.setContacts(list);
                } else
                {
                    list = castDestination.getContacts();
                }
                list.add(new Contact((Contact) child));
            }
        }
    },
    // Date and Time
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeCompleted.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeCompleted();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeCompleted propertyCopy = new DateTimeCompleted((DateTimeCompleted) child);
            castDestination.setDateTimeCompleted(propertyCopy);
        }
    },
    // Change management
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeCreated.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getDateTimeCreated();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            DateTimeCreated propertyCopy = new DateTimeCreated((DateTimeCreated) child);
            castDestination.setDateTimeCreated(propertyCopy);
        }
    },
    // Date and time
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeDue.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeDue();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeDue propertyCopy = new DateTimeDue((DateTimeDue) child);
            castDestination.setDateTimeDue(propertyCopy);
        }
    },
    // Date and Time
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeEnd.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDateTimeEnd<?> castComponent = (VComponentDateTimeEnd<?>) vComponent;
            return castComponent.getDateTimeEnd();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDateTimeEnd<?> castDestination = (VComponentDateTimeEnd<?>) destination;
            DateTimeEnd propertyCopy = new DateTimeEnd((DateTimeEnd) child);
            castDestination.setDateTimeEnd(propertyCopy);
        }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeStamp.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getDateTimeStamp();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            DateTimeStamp propertyCopy = new DateTimeStamp((DateTimeStamp) child);
            castDestination.setDateTimeStamp(propertyCopy);
        }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DateTimeStart.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPrimary<?> castComponent = (VComponentPrimary<?>) vComponent;
            return castComponent.getDateTimeStart();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPrimary<?> castDestination = (VComponentPrimary<?>) destination;
            DateTimeStart propertyCopy = new DateTimeStart((DateTimeStart) child);
            castDestination.setDateTimeStart(propertyCopy);
        }
    },
    // Descriptive
    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.OTHER),
            Description.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            if (vComponent instanceof VJournal)
            {// VJournal has list of Descriptions
                VJournal castComponent = (VJournal) vComponent;
                return castComponent.getDescriptions();                
            } else
            { // Other components have only one Description
                VComponentDescribable2<?> castComponent = (VComponentDescribable2<?>) vComponent;
                return castComponent.getDescription();
            }
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            if (vComponent instanceof VJournal)
            { // VJournal has list of Descriptions
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
            { // Other components have only one Description
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            if (destination instanceof VJournal)
            { // VJournal has list of Descriptions
                VJournal castDestination = (VJournal) destination;
                final ObservableList<Description> list;
                if (castDestination.getDescriptions() == null)
                {
                    list = FXCollections.observableArrayList();
                    castDestination.setDescriptions(list);
                } else
                {
                    list = castDestination.getDescriptions();
                }
                list.add(new Description((Description) child));
            } else
            { // Other components have only one Description
                VComponentDescribable2<?> castDestination = (VComponentDescribable2<?>) destination;
                Description propertyCopy = new Description((Description) child);
                castDestination.setDescription(propertyCopy);
            }
        }
    },
    // Date and Time
    DURATION ("DURATION", // property name
            Arrays.asList(ValueType.DURATION), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDuration<?> castComponent = (VComponentDuration<?>) vComponent;
            return castComponent.getDuration();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDuration<?> castDestination = (VComponentDuration<?>) destination;
            DurationProp propertyCopy = new DurationProp((DurationProp) child);
            castDestination.setDuration(propertyCopy);
        }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            ExceptionDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getExceptionDates();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            final ObservableList<ExceptionDates> list;
            if (castComponent.getExceptionDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setExceptionDates(list);
            } else
            {
                list = castComponent.getExceptionDates();
            }
            list.add(ExceptionDates.parse(propertyContent));
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            final ObservableList<ExceptionDates> list;
            if (castDestination.getExceptionDates() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setExceptionDates(list);
            } else
            {
                list = castDestination.getExceptionDates();
            }
            list.add(new ExceptionDates((ExceptionDates) child));
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            Arrays.asList(ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.FREE_BUSY_TIME_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            FreeBusyTime.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VFreeBusy castComponent = (VFreeBusy) vComponent;
            return castComponent.getFreeBusyTime();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VFreeBusy castDestination = (VFreeBusy) destination;
            FreeBusyTime propertyCopy = new FreeBusyTime((FreeBusyTime) child);
            castDestination.setFreeBusyTime(propertyCopy);
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", // property name
            Arrays.asList(ValueType.TEXT), // In GeographicPosition there are two doubles for latitude and longitude
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            GeographicPosition.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getGeographicPosition();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            GeographicPosition propertyCopy = new GeographicPosition((GeographicPosition) child);
            castDestination.setGeographicPosition(propertyCopy);
        }
    },
    // Miscellaneous
    IANA_PROPERTY (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.get(0), /** property name (one in list of valid names at {@link #IANAProperty} */
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
            IANAProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentCommon<?> castComponent = (VComponentCommon<?>) vComponent;
            return castComponent.getIANAProperties();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentCommon<?> castComponent = (VComponentCommon<?>) vComponent;
            final ObservableList<IANAProperty> list;
            if (castComponent.getIANAProperties() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setIANAProperties(list);
            } else
            {
                list = castComponent.getIANAProperties();
            }
            list.add(IANAProperty.parse(propertyContent));
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentCommon<?> castDestination = (VComponentCommon<?>) destination;
            final ObservableList<IANAProperty> list;
            if (castDestination.getIANAProperties() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setIANAProperties(list);
            } else
            {
                list = castDestination.getIANAProperties();
            }
            list.add(new IANAProperty((IANAProperty) child));
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            LastModified.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLastModified<?> castComponent = (VComponentLastModified<?>) vComponent;
            return castComponent.getDateTimeLastModified();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentLastModified<?> castDestination = (VComponentLastModified<?>) destination;
            LastModified propertyCopy = new LastModified((LastModified) child);
            castDestination.setDateTimeLastModified(propertyCopy);
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.OTHER), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getLocation();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            Location propertyCopy = new Location((Location) child);
            castDestination.setLocation(propertyCopy);
        }
    },
    // Calendar property
    METHOD ("METHOD" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , Method.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Miscellaneous
    NON_STANDARD ("X-", // property name (begins with X- prefix)
            Arrays.asList(ValueType.values()), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
            NonStandardProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentCommon<?> castComponent = (VComponentCommon<?>) vComponent;
            return castComponent.getNonStandardProperties();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentCommon<?> castComponent = (VComponentCommon<?>) vComponent;
            final ObservableList<NonStandardProperty> list;
            if (castComponent.getNonStandardProperties() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setNonStandardProperties(list);
            } else
            {
                list = castComponent.getNonStandardProperties();
            }
            list.add(NonStandardProperty.parse(propertyContent));
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentCommon<?> castDestination = (VComponentCommon<?>) destination;
            final ObservableList<NonStandardProperty> list;
            if (castDestination.getNonStandardProperties() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setNonStandardProperties(list);
            } else
            {
                list = castDestination.getNonStandardProperties();
            }
            list.add(new NonStandardProperty((NonStandardProperty) child));
        }
    },
    // Relationship
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterType.COMMON_NAME, ParameterType.DIRECTORY_ENTRY_REFERENCE, ParameterType.LANGUAGE,
                    ParameterType.SENT_BY, ParameterType.OTHER), // allowed parameters
            Organizer.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getOrganizer();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            if (castComponent.getOrganizer() == null)
            {
                castComponent.setOrganizer(Organizer.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }
        
        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            Organizer propertyCopy = new Organizer((Organizer) child);
            castDestination.setOrganizer(propertyCopy);
        }
    },
    // Descriptive
    PERCENT_COMPLETE ("PERCENT-COMPLETE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            PercentComplete.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getPercentComplete();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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
        
        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VTodo castDestination = (VTodo) destination;
            PercentComplete propertyCopy = new PercentComplete((PercentComplete) child);
            castDestination.setPercentComplete(propertyCopy);
        }
    },
    // Descriptive
    PRIORITY ("PRIORITY", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Priority.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatable<?> castComponent = (VComponentLocatable<?>) vComponent;
            return castComponent.getPriority();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            Priority propertyCopy = new Priority((Priority) child);
            castDestination.setPriority(propertyCopy);
        }
    },
    // Calendar property
    PRODUCT_IDENTIFIER ("PRODID" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , ProductIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            RecurrenceDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceDates();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            final ObservableList<RecurrenceDates> list;
            if (castComponent.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRecurrenceDates(list);
            } else
            {
                list = castComponent.getRecurrenceDates();
            }
            list.add(RecurrenceDates.parse(propertyContent));
        }
        
        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentRepeatable<?> castDestination = (VComponentRepeatable<?>) destination;
            final ObservableList<RecurrenceDates> list;
            if (castDestination.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRecurrenceDates(list);
            } else
            {
                list = castDestination.getRecurrenceDates();
            }
            list.add(new RecurrenceDates((RecurrenceDates) child));
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.RECURRENCE_IDENTIFIER_RANGE, ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.OTHER), // allowed parameters
            RecurrenceId.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getRecurrenceId();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            RecurrenceId propertyCopy = new RecurrenceId((RecurrenceId) child);
            castDestination.setRecurrenceId(propertyCopy);
        }
    },
    // Recurrence
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            RecurrenceRule.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceRule();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentRepeatable<?> castDestination = (VComponentRepeatable<?>) destination;
            RecurrenceRule propertyCopy = new RecurrenceRule((RecurrenceRule) child);
            castDestination.setRecurrenceRule(propertyCopy);
        }
    },
    // Relationship
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.RELATIONSHIP_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            RelatedTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getRelatedTo();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            final ObservableList<RelatedTo> list;
            if (castDestination.getRelatedTo() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRelatedTo(list);
            } else
            {
                list = castDestination.getRelatedTo();
            }
            list.add(new RelatedTo((RelatedTo) child));
        }
    },
    // Alarm
    REPEAT_COUNT ("REPEAT", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            RepeatCount.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getRepeatCount();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            RepeatCount propertyCopy = new RepeatCount((RepeatCount) child);
            castDestination.setRepeatCount(propertyCopy);
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            RequestStatus.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?>) vComponent;
            return castComponent.getRequestStatus();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            final ObservableList<RequestStatus> list;
            if (castDestination.getRequestStatus() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setRequestStatus(list);
            } else
            {
                list = castDestination.getRequestStatus();
            }
            list.add(new RequestStatus((RequestStatus) child));
        }
    },
    // Descriptive
    RESOURCES ("RESOURCES", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.OTHER), // allowed parameters
            Resources.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatable<?> castProperty = (VComponentLocatable<?>) vComponent;
            return castProperty.getResources();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            final ObservableList<Resources> list;
            if (castDestination.getResources() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setResources(list);
            } else
            {
                list = castDestination.getResources();
            }
            list.add(new Resources((Resources) child));
        }
    },
    // Change management
    SEQUENCE ("SEQUENCE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Sequence.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getSequence();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Sequence propertyCopy = new Sequence((Sequence) child);
            castDestination.setSequence(propertyCopy);
        }
    },
    // Descriptive
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Status.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayable<?> castComponent = (VComponentDisplayable<?>) vComponent;
            return castComponent.getStatus();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Status propertyCopy = new Status((Status) child);
            castDestination.setStatus(propertyCopy);
        }
    },
    // Descriptive
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE,
                    ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Summary.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getSummary();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
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

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentDescribable<?> castDestination = (VComponentDescribable<?>) destination;
            Summary propertyCopy = new Summary((Summary) child);
            castDestination.setSummary(propertyCopy);
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VEvent castComponent = (VEvent) vComponent;
            return castComponent.getTimeTransparency();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VEvent castComponent = (VEvent) vComponent;
            if (castComponent.getTimeTransparency() == null)
            {
                castComponent.setTimeTransparency(TimeTransparency.parse(propertyContent));                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VEvent castDestination = (VEvent) destination;
            TimeTransparency propertyCopy = new TimeTransparency((TimeTransparency) child);
            castDestination.setTimeTransparency(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneIdentifier();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            if (castComponent.getTimeZoneIdentifier() == null)
            {
                castComponent.setTimeZoneIdentifier(TimeZoneIdentifier.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneIdentifier propertyCopy = new TimeZoneIdentifier((TimeZoneIdentifier) child);
            castDestination.setTimeZoneIdentifier(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castProperty = (StandardOrDaylight<?>) vComponent;
            return castProperty.getTimeZoneNames();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            final ObservableList<TimeZoneName> list;
            if (castComponent.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setTimeZoneNames(list);
            } else
            {
                list = castComponent.getTimeZoneNames();
            }
            list.add(TimeZoneName.parse(propertyContent));
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            final ObservableList<TimeZoneName> list;
            if (castDestination.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setTimeZoneNames(list);
            } else
            {
                list = castDestination.getTimeZoneNames();
            }
            list.add(new TimeZoneName((TimeZoneName) child));
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetFrom();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            if (castComponent.getTimeZoneOffsetFrom() == null)
            {
                castComponent.setTimeZoneOffsetFrom(TimeZoneOffsetFrom.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetFrom propertyCopy = new TimeZoneOffsetFrom((TimeZoneOffsetFrom) child);
            castDestination.setTimeZoneOffsetFrom(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetTo();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            if (castComponent.getTimeZoneOffsetTo() == null)
            {
                castComponent.setTimeZoneOffsetTo(TimeZoneOffsetTo.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetTo propertyCopy = new TimeZoneOffsetTo((TimeZoneOffsetTo) child);
            castDestination.setTimeZoneOffsetTo(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneURL();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            if (castComponent.getTimeZoneURL() == null)
            {
                castComponent.setTimeZoneURL(TimeZoneURL.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneURL propertyCopy = new TimeZoneURL((TimeZoneURL) child);
            castDestination.setTimeZoneURL(propertyCopy);
        }
    },
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.ALARM_TRIGGER_RELATIONSHIP, ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            Trigger.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getTrigger();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            if (castComponent.getTrigger() == null)
            {
                castComponent.setTrigger(Trigger.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Trigger<?> propertyCopy = new Trigger<>((Trigger<?>) child);
            castDestination.setTrigger(propertyCopy);
        }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            UniqueIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            return castComponent.getUniqueIdentifier();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            if (castComponent.getUniqueIdentifier() == null)
            {
                castComponent.setUniqueIdentifier(UniqueIdentifier.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            UniqueIdentifier propertyCopy = new UniqueIdentifier((UniqueIdentifier) child);
            castDestination.setUniqueIdentifier(propertyCopy);
        }
    },
    // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER), // allowed parameters
            UniformResourceLocator.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            return castComponent.getUniformResourceLocator();
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            VComponentPersonal<?> castComponent = (VComponentPersonal<?> ) vComponent;
            castComponent.setUniformResourceLocator(UniformResourceLocator.parse(propertyContent));
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            UniformResourceLocator propertyCopy = new UniformResourceLocator((UniformResourceLocator) child);
            castDestination.setUniformResourceLocator(propertyCopy);
        }
    },
    // Calendar property
    VERSION ("VERSION" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.OTHER) // allowed parameters
            , Version.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponent vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(Property<?> child, VComponent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    };
    
    private static Map<String, PropertyType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, PropertyType> makeEnumFromNameMap()
    {
        Map<String, PropertyType> map = new HashMap<>();
        PropertyType[] values = PropertyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static PropertyType enumFromName(String propertyName)
    {
        final PropertyType prop;
        if (propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString()))
        {
            prop = PropertyType.NON_STANDARD;
        } else if (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.contains(propertyName))
        {
            prop = PropertyType.IANA_PROPERTY;            
        } else
        {
            prop = enumFromNameMap.get(propertyName);   
        }
        return prop;
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Property>, PropertyType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Property>, PropertyType> makeEnumFromClassMap()
    {
        Map<Class<? extends Property>, PropertyType> map = new HashMap<>();
        PropertyType[] values = PropertyType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static PropertyType enumFromClass(Class<? extends VElement> myClass)
    {
        return enumFromClassMap.get(myClass);
    }
    
    private Class<? extends Property> myClass;
    public Class<? extends Property> getPropertyClass() { return myClass; }

    @Override
    public String toString() { return name; }
    private String name;
    
    private List<ValueType> valueTypes;
    public List<ValueType> allowedValueTypes() { return valueTypes; }

    private List<ParameterType> allowedParameters;
    public List<ParameterType> allowedParameters() { return allowedParameters; }
    
    PropertyType(String name, List<ValueType> valueTypes, List<ParameterType> allowedParameters, Class<? extends Property> myClass)
    {
        this.allowedParameters = allowedParameters;
        this.name = name;
        this.valueTypes = valueTypes;
        this.myClass = myClass;
    }
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Property<?> or List<Property<?>> 
     * @deprecated  not needed due to addition of Orderer, may be deleted */
    @Deprecated
    abstract public Object getProperty(VComponent vComponent);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public void parse(VComponent vComponent, String propertyContent);

    /** copies the associated property from the source component to the destination component */
    public void copyProperty(Property<?> child, VComponent destination) { throw new RuntimeException("not implemented");};
}
