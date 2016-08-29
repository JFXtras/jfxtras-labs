package jfxtras.labs.icalendarfx.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VElement;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.components.StandardOrDaylight;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentAttendee;
import jfxtras.labs.icalendarfx.components.VComponentBase;
import jfxtras.labs.icalendarfx.components.VComponentCommonBase;
import jfxtras.labs.icalendarfx.components.VComponentDateTimeEnd;
import jfxtras.labs.icalendarfx.components.VComponentDescribable;
import jfxtras.labs.icalendarfx.components.VComponentDescribable2;
import jfxtras.labs.icalendarfx.components.VComponentDisplayableBase;
import jfxtras.labs.icalendarfx.components.VComponentDuration;
import jfxtras.labs.icalendarfx.components.VComponentLastModified;
import jfxtras.labs.icalendarfx.components.VComponentLocatableBase;
import jfxtras.labs.icalendarfx.components.VComponentPersonalBase;
import jfxtras.labs.icalendarfx.components.VComponentPrimaryBase;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.ParameterType;
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Action.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getAction();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            Action child = Action.parse(propertyContent);
            VAlarm castComponent = (VAlarm) vParent;
            castComponent.setAction(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Action propertyCopy = new Action((Action) child);
            castDestination.setAction(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterType.FORMAT_TYPE, ParameterType.INLINE_ENCODING, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , Attachment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getAttachments();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vParent;
            final ObservableList<Attachment<?>> list;
            if (castComponent.getAttachments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttachments(list);
            } else
            {
                list = castComponent.getAttachments();
            }
            Attachment<?> child = Attachment.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
                    ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , Attendee.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentAttendee<?> castComponent = (VComponentAttendee<?>) vComponent;
            return castComponent.getAttendees();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentAttendee<?> castComponent = (VComponentAttendee<?>) vParent;
            final ObservableList<Attendee> list;
            if (castComponent.getAttendees() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setAttendees(list);
            } else
            {
                list = castComponent.getAttendees();
            }
            Attendee child = Attendee.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , CalendarScale.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Descriptive
    CATEGORIES ("CATEGORIES" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , Categories.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getCategories();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            final ObservableList<Categories> list;
            if (castComponent.getCategories() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setCategories(list);
            } else
            {
                list = castComponent.getCategories();
            }
            Categories child = Categories.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Classification.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getClassification();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            Classification child = Classification.parse(propertyContent);
            castComponent.setClassification(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
            Classification propertyCopy = new Classification((Classification) child);
            castDestination.setClassification(propertyCopy);
        }
    },
    // Descriptive
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES,
                    ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Comment.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPrimaryBase<?> castProperty = (VComponentPrimaryBase<?>) vComponent;
            return castProperty.getComments();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPrimaryBase<?> castComponent = (VComponentPrimaryBase<?>) vParent;
            final ObservableList<Comment> list;
            if (castComponent.getComments() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setComments(list);
            } else
            {
                list = castComponent.getComments();
            }
            Comment child = Comment.parse(propertyContent);
            list.add(child);
            return child;
        }
        
        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPrimaryBase<?> castDestination = (VComponentPrimaryBase<?>) destination;
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
                    ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
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
                VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
                return castComponent.getContacts();
            }
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            Contact child = Contact.parse(propertyContent);
            if (vParent instanceof VFreeBusy)
            {// VJournal has one Contact
                VFreeBusy castComponent = (VFreeBusy) vParent;
                castComponent.setContact(child);                
            } else
            { // Other components have a list of Contacts
                VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
                final ObservableList<Contact> list;
                if (castComponent.getContacts() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setContacts(list);
                } else
                {
                    list = castComponent.getContacts();
                }
                list.add(child);
            }
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            if (destination instanceof VFreeBusy)
            { // VFreeBusy has one Contact
                VFreeBusy castDestination = (VFreeBusy) destination;
                Contact propertyCopy = new Contact((Contact) child);
                castDestination.setContact(propertyCopy);
            } else
            { // Other components have a list of Contacts
                VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeCompleted.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeCompleted();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            DateTimeCompleted child = DateTimeCompleted.parse(propertyContent);
            castComponent.setDateTimeCompleted(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeCompleted propertyCopy = new DateTimeCompleted((DateTimeCompleted) child);
            castDestination.setDateTimeCompleted(propertyCopy);
        }
    },
    // Change management
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeCreated.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getDateTimeCreated();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            DateTimeCreated child = DateTimeCreated.parse(propertyContent);
            castComponent.setDateTimeCreated(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
            DateTimeCreated propertyCopy = new DateTimeCreated((DateTimeCreated) child);
            castDestination.setDateTimeCreated(propertyCopy);
        }
    },
    // Date and time
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeDue.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getDateTimeDue();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            DateTimeDue child = DateTimeDue.parse(propertyContent);
            castComponent.setDateTimeDue(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VTodo castDestination = (VTodo) destination;
            DateTimeDue propertyCopy = new DateTimeDue((DateTimeDue) child);
            castDestination.setDateTimeDue(propertyCopy);
        }
    },
    // Date and Time
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeEnd.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDateTimeEnd<?> castComponent = (VComponentDateTimeEnd<?>) vComponent;
            return castComponent.getDateTimeEnd();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDateTimeEnd<?> castComponent = (VComponentDateTimeEnd<?>) vParent;
            DateTimeEnd child = DateTimeEnd.parse(propertyContent);
            castComponent.setDateTimeEnd(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDateTimeEnd<?> castDestination = (VComponentDateTimeEnd<?>) destination;
            DateTimeEnd propertyCopy = new DateTimeEnd((DateTimeEnd) child);
            castDestination.setDateTimeEnd(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeStamp.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vComponent;
            return castComponent.getDateTimeStamp();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vParent;
            DateTimeStamp child = DateTimeStamp.parse(propertyContent);
            castComponent.setDateTimeStamp(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPersonalBase<?> castDestination = (VComponentPersonalBase<?>) destination;
            DateTimeStamp propertyCopy = new DateTimeStamp((DateTimeStamp) child);
            castDestination.setDateTimeStamp(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DateTimeStart.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPrimaryBase<?> castComponent = (VComponentPrimaryBase<?>) vComponent;
            return castComponent.getDateTimeStart();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPrimaryBase<?> castComponent = (VComponentPrimaryBase<?>) vParent;
            DateTimeStart child = DateTimeStart.parse(propertyContent);
            castComponent.setDateTimeStart(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPrimaryBase<?> castDestination = (VComponentPrimaryBase<?>) destination;
            DateTimeStart propertyCopy = new DateTimeStart((DateTimeStart) child);
            castDestination.setDateTimeStart(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent )
        {
            return (parent instanceof VEvent) ? true : false;
        }
    },
    // Descriptive
    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER),
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
        public VChild parse(VParent vParent, String propertyContent)
        {
            Description child = Description.parse(propertyContent);
            if (vParent instanceof VJournal)
            { // VJournal has list of Descriptions
                VJournal castComponent = (VJournal) vParent;
                final ObservableList<Description> list;
                if (castComponent.getDescriptions() == null)
                {
                    list = FXCollections.observableArrayList();
                    castComponent.setDescriptions(list);
                } else
                {
                    list = castComponent.getDescriptions();
                }
                list.add(child);
            } else
            { // Other components have only one Description
                VComponentDescribable2<?> castComponent = (VComponentDescribable2<?>) vParent;
                if (castComponent.getDescription() == null)
                {
                    castComponent.setDescription(child);                                
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
                }
            }
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            DurationProp.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDuration<?> castComponent = (VComponentDuration<?>) vComponent;
            return castComponent.getDuration();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            DurationProp child = DurationProp.parse(propertyContent);            
            VComponentDuration<?> castComponent = (VComponentDuration<?>) vParent;
            if (castComponent.getDuration() == null)
            {
                castComponent.setDuration(child);                                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDuration<?> castDestination = (VComponentDuration<?>) destination;
            DurationProp propertyCopy = new DurationProp((DurationProp) child);
            castDestination.setDuration(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            ExceptionDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getExceptionDates();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            ExceptionDates child = ExceptionDates.parse(propertyContent);
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            final ObservableList<ExceptionDates> list;
            if (castComponent.getExceptionDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setExceptionDates(list);
            } else
            {
                list = castComponent.getExceptionDates();
            }

            String error = VComponentRepeatable.checkPotentialRecurrencesConsistency(list, child);
            if (error == null)
            {
                list.add(child);
                return child;
            }
            return null; // invalid content
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
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
            Arrays.asList(ParameterType.FREE_BUSY_TIME_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            FreeBusyTime.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VFreeBusy castComponent = (VFreeBusy) vComponent;
            return castComponent.getFreeBusyTime();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VFreeBusy castComponent = (VFreeBusy) vParent;
            FreeBusyTime child = FreeBusyTime.parse(propertyContent);
            castComponent.setFreeBusyTime(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VFreeBusy castDestination = (VFreeBusy) destination;
            FreeBusyTime propertyCopy = new FreeBusyTime((FreeBusyTime) child);
            castDestination.setFreeBusyTime(propertyCopy);
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", // property name
            Arrays.asList(ValueType.TEXT), // In GeographicPosition there are two doubles for latitude and longitude
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            GeographicPosition.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vComponent;
            return castComponent.getGeographicPosition();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vParent;
            GeographicPosition child = GeographicPosition.parse(propertyContent);
            castComponent.setGeographicPosition(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentLocatableBase<?> castDestination = (VComponentLocatableBase<?>) destination;
            GeographicPosition propertyCopy = new GeographicPosition((GeographicPosition) child);
            castDestination.setGeographicPosition(propertyCopy);
        }
    },
    // Miscellaneous
    IANA_PROPERTY (null, // name specified in IANAProperty registeredIANAProperties
            Arrays.asList(ValueType.values()), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
            IANAProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentCommonBase<?> castComponent = (VComponentCommonBase<?>) vComponent;
            return castComponent.getIana();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentCommonBase<?> castComponent = (VComponentCommonBase<?>) vParent;
            final ObservableList<IANAProperty> list;
            if (castComponent.getIana() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setIana(list);
            } else
            {
                list = castComponent.getIana();
            }
            IANAProperty child = IANAProperty.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentCommonBase<?> castDestination = (VComponentCommonBase<?>) destination;
            final ObservableList<IANAProperty> list;
            if (castDestination.getIana() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setIana(list);
            } else
            {
                list = castDestination.getIana();
            }
            list.add(new IANAProperty((IANAProperty) child));
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            LastModified.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLastModified<?> castComponent = (VComponentLastModified<?>) vComponent;
            return castComponent.getDateTimeLastModified();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentLastModified<?> castComponent = (VComponentLastModified<?>) vParent;
            LastModified child = LastModified.parse(propertyContent);
            castComponent.setDateTimeLastModified(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
                    ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Location.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vComponent;
            return castComponent.getLocation();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vParent;
            Location child = Location.parse(propertyContent);
            castComponent.setLocation(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentLocatableBase<?> castDestination = (VComponentLocatableBase<?>) destination;
            Location propertyCopy = new Location((Location) child);
            castDestination.setLocation(propertyCopy);
        }
    },
    // Calendar property
    METHOD ("METHOD" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , Method.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Miscellaneous
    NON_STANDARD ("X-", // property name begins with X- prefix
            Arrays.asList(ValueType.values()), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
            NonStandardProperty.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentCommonBase<?> castComponent = (VComponentCommonBase<?>) vComponent;
            return castComponent.getNonStandard();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentCommonBase<?> castComponent = (VComponentCommonBase<?>) vParent;
            final ObservableList<NonStandardProperty> list;
            if (castComponent.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setNonStandard(list);
            } else
            {
                list = castComponent.getNonStandard();
            }
            NonStandardProperty child = NonStandardProperty.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentCommonBase<?> castDestination = (VComponentCommonBase<?>) destination;
            final ObservableList<NonStandardProperty> list;
            if (castDestination.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                castDestination.setNonStandard(list);
            } else
            {
                list = castDestination.getNonStandard();
            }
            list.add(new NonStandardProperty((NonStandardProperty) child));
        }
    },
    // Relationship
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterType.COMMON_NAME, ParameterType.DIRECTORY_ENTRY_REFERENCE, ParameterType.LANGUAGE,
                    ParameterType.SENT_BY, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Organizer.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vComponent;
            return castComponent.getOrganizer();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vParent;
            Organizer child = Organizer.parse(propertyContent);
            castComponent.setOrganizer(child);
            return child;
        }
        
        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPersonalBase<?> castDestination = (VComponentPersonalBase<?>) destination;
            Organizer propertyCopy = new Organizer((Organizer) child);
            castDestination.setOrganizer(propertyCopy);
        }
    },
    // Descriptive
    PERCENT_COMPLETE ("PERCENT-COMPLETE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            PercentComplete.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTodo castComponent = (VTodo) vComponent;
            return castComponent.getPercentComplete();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTodo castComponent = (VTodo) vParent;
            PercentComplete child = PercentComplete.parse(propertyContent);
            castComponent.setPercentComplete(child);
            return child;
        }
        
        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VTodo castDestination = (VTodo) destination;
            PercentComplete propertyCopy = new PercentComplete((PercentComplete) child);
            castDestination.setPercentComplete(propertyCopy);
        }
    },
    // Descriptive
    PRIORITY ("PRIORITY", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Priority.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vComponent;
            return castComponent.getPriority();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vParent;
            Priority child = Priority.parse(propertyContent);
            castComponent.setPriority(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentLocatableBase<?> castDestination = (VComponentLocatableBase<?>) destination;
            Priority propertyCopy = new Priority((Priority) child);
            castDestination.setPriority(propertyCopy);
        }
    },
    // Calendar property
    PRODUCT_IDENTIFIER ("PRODID" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , ProductIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RecurrenceDates.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceDates();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vParent;
            final ObservableList<RecurrenceDates> list;
            if (castComponent.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRecurrenceDates(list);
            } else
            {
                list = castComponent.getRecurrenceDates();
            }
            RecurrenceDates child = RecurrenceDates.parse(propertyContent);
            list.add(child);
            return child;
        }
        
        @Override
        public void copyProperty(VChild child, VParent destination)
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
                    ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RecurrenceId.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getRecurrenceId();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            RecurrenceId child = RecurrenceId.parse(propertyContent);
            castComponent.setRecurrenceId(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
            RecurrenceId propertyCopy = new RecurrenceId((RecurrenceId) child);
            castDestination.setRecurrenceId(propertyCopy);
        }
    },
    // Recurrence
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RecurrenceRule.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceRule();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vParent;
            RecurrenceRule child = RecurrenceRule.parse(propertyContent);
            castComponent.setRecurrenceRule(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentRepeatable<?> castDestination = (VComponentRepeatable<?>) destination;
            RecurrenceRule propertyCopy = new RecurrenceRule((RecurrenceRule) child);
            castDestination.setRecurrenceRule(propertyCopy);
        }
    },
    // Relationship
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.RELATIONSHIP_TYPE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RelatedTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getRelatedTo();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            final ObservableList<RelatedTo> list;
            if (castComponent.getRelatedTo() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRelatedTo(list);
            } else
            {
                list = castComponent.getRelatedTo();
            }
            RelatedTo child = RelatedTo.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RepeatCount.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getRepeatCount();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vParent;
            RepeatCount child = RepeatCount.parse(propertyContent);
            castComponent.setRepeatCount(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            RepeatCount propertyCopy = new RepeatCount((RepeatCount) child);
            castDestination.setRepeatCount(propertyCopy);
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            RequestStatus.class)
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vComponent;
            return castComponent.getRequestStatus();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?>) vParent;
            final ObservableList<RequestStatus> list;
            if (castComponent.getRequestStatus() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRequestStatus(list);
            } else
            {
                list = castComponent.getRequestStatus();
            }
            RequestStatus child = RequestStatus.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPersonalBase<?> castDestination = (VComponentPersonalBase<?>) destination;
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
                    ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Resources.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentLocatableBase<?> castProperty = (VComponentLocatableBase<?>) vComponent;
            return castProperty.getResources();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentLocatableBase<?> castComponent = (VComponentLocatableBase<?>) vParent;
            final ObservableList<Resources> list;
            if (castComponent.getResources() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setResources(list);
            } else
            {
                list = castComponent.getResources();
            }
            Resources child = Resources.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentLocatableBase<?> castDestination = (VComponentLocatableBase<?>) destination;
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Sequence.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getSequence();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            Sequence child = Sequence.parse(propertyContent);
            castComponent.setSequence(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
            Sequence propertyCopy = new Sequence((Sequence) child);
            castDestination.setSequence(propertyCopy);
        }
    },
    // Descriptive
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Status.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vComponent;
            return castComponent.getStatus();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDisplayableBase<?> castComponent = (VComponentDisplayableBase<?>) vParent;
            Status child = Status.parse(propertyContent);
            castComponent.setStatus(child);
            return child;            
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDisplayableBase<?> castDestination = (VComponentDisplayableBase<?>) destination;
            Status propertyCopy = new Status((Status) child);
            castDestination.setStatus(propertyCopy);
        }
    },
    // Descriptive
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE,
                    ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Summary.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vComponent;
            return castComponent.getSummary();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentDescribable<?> castComponent = (VComponentDescribable<?>) vParent;
            Summary child = Summary.parse(propertyContent);
            castComponent.setSummary(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentDescribable<?> castDestination = (VComponentDescribable<?>) destination;
            Summary propertyCopy = new Summary((Summary) child);
            castDestination.setSummary(propertyCopy);
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VEvent castComponent = (VEvent) vComponent;
            return castComponent.getTimeTransparency();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VEvent castComponent = (VEvent) vParent;
            TimeTransparency child = TimeTransparency.parse(propertyContent);
            castComponent.setTimeTransparency(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VEvent castDestination = (VEvent) destination;
            TimeTransparency propertyCopy = new TimeTransparency((TimeTransparency) child);
            castDestination.setTimeTransparency(propertyCopy);
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeZoneIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneIdentifier();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vParent;
            TimeZoneIdentifier child = TimeZoneIdentifier.parse(propertyContent);
            castComponent.setTimeZoneIdentifier(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneIdentifier propertyCopy = new TimeZoneIdentifier((TimeZoneIdentifier) child);
            castDestination.setTimeZoneIdentifier(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent )
        {
            return (parent instanceof VTimeZone) ? true : false;
        }
    },
    // Time Zone
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castProperty = (StandardOrDaylight<?>) vComponent;
            return castProperty.getTimeZoneNames();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            final ObservableList<TimeZoneName> list;
            if (castComponent.getTimeZoneNames() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setTimeZoneNames(list);
            } else
            {
                list = castComponent.getTimeZoneNames();
            }
            TimeZoneName child = TimeZoneName.parse(propertyContent);
            list.add(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetFrom();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            TimeZoneOffsetFrom child = TimeZoneOffsetFrom.parse(propertyContent);
            castComponent.setTimeZoneOffsetFrom(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetFrom propertyCopy = new TimeZoneOffsetFrom((TimeZoneOffsetFrom) child);
            castDestination.setTimeZoneOffsetFrom(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetTo();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vParent;
            TimeZoneOffsetTo child = TimeZoneOffsetTo.parse(propertyContent);
            castComponent.setTimeZoneOffsetTo(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetTo propertyCopy = new TimeZoneOffsetTo((TimeZoneOffsetTo) child);
            castDestination.setTimeZoneOffsetTo(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneURL();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VTimeZone castComponent = (VTimeZone) vParent;
            TimeZoneURL child = TimeZoneURL.parse(propertyContent);
            castComponent.setTimeZoneURL(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneURL propertyCopy = new TimeZoneURL((TimeZoneURL) child);
            castDestination.setTimeZoneURL(propertyCopy);
        }
    },
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.ALARM_TRIGGER_RELATIONSHIP, ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            Trigger.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VAlarm castComponent = (VAlarm) vComponent;
            return castComponent.getTrigger();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VAlarm castComponent = (VAlarm) vParent;
            Trigger<?> child = Trigger.parse(propertyContent);
            castComponent.setTrigger(child);
            return child;            
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VAlarm castDestination = (VAlarm) destination;
            Trigger<?> propertyCopy = new Trigger<>((Trigger<?>) child);
            castDestination.setTrigger(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            UniqueIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?> ) vComponent;
            return castComponent.getUniqueIdentifier();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?> ) vParent;
            UniqueIdentifier child = UniqueIdentifier.parse(propertyContent);
            castComponent.setUniqueIdentifier(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPersonalBase<?> castDestination = (VComponentPersonalBase<?>) destination;
            UniqueIdentifier propertyCopy = new UniqueIdentifier((UniqueIdentifier) child);
            castDestination.setUniqueIdentifier(propertyCopy);
        }
    },
    // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER), // allowed parameters
            UniformResourceLocator.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?> ) vComponent;
            return castComponent.getUniformResourceLocator();
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VComponentPersonalBase<?> castComponent = (VComponentPersonalBase<?> ) vParent;
            UniformResourceLocator child = UniformResourceLocator.parse(propertyContent);
            castComponent.setUniformResourceLocator(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
        {
            VComponentPersonalBase<?> castDestination = (VComponentPersonalBase<?>) destination;
            UniformResourceLocator propertyCopy = new UniformResourceLocator((UniformResourceLocator) child);
            castDestination.setUniformResourceLocator(propertyCopy);
        }
        
        @Override
        public boolean isRequired(VParent parent ) { return true; }
    },
    // Calendar property
    VERSION ("VERSION" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES, ParameterType.NON_STANDARD, ParameterType.IANA_PARAMETER) // allowed parameters
            , Version.class) // property class
    {
        @Override
        public Object getProperty(VComponent vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public VChild parse(VParent vParent, String propertyContent)
        {
            VCalendar vCalendar = (VCalendar) vParent;
            Version child = Version.parse(propertyContent);
            vCalendar.setVersion(child);
            return child;
        }

        @Override
        public void copyProperty(VChild child, VParent destination)
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
        if (propertyName.length() < 3) return null; // minimum property name is 3 characters
        if (propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString()))
        {
            prop = PropertyType.NON_STANDARD;
        } else if ((IANAProperty.getRegisteredIANAPropertys() != null) && IANAProperty.getRegisteredIANAPropertys().contains(propertyName))
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
    /** Returns associated Property<?> or List<Property<?>> */
    abstract public Object getProperty(VComponent vComponent);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public VChild parse(VParent vParent, String propertyContent);
//    abstract public VChild parse(VParent vParent, String propertyContent);

    /** copies the associated property from the source component to the destination component */
    abstract public void copyProperty(VChild child, VParent destination);
//    abstract public void copyProperty(VChild child, VParent destination);
    
    /** If property is required returns true, false otherwise */
    public boolean isRequired(VParent parent ) { return false; }
}
