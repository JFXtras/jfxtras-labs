package jfxtras.labs.icalendarfx.properties;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.StandardOrDaylight;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponentAttendee;
import jfxtras.labs.icalendarfx.components.VComponentBase;
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
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
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

public enum PropertyType
{
    // Alarm
    ACTION ("ACTION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VAlarm castSource = (VAlarm) source;
            VAlarm castDestination = (VAlarm) destination;
            Action property = new Action(castSource.getAction());
            castDestination.setAction(property);
        }
    },
    // property class
    ATTACHMENT ("ATTACH" // property name
            , Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY) // valid property value types, first is default
            , Arrays.asList(ParameterType.FORMAT_TYPE, ParameterType.INLINE_ENCODING, ParameterType.VALUE_DATA_TYPES) // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDescribable<?> castSource = (VComponentDescribable<?>) source;
            VComponentDescribable<?> castDestination = (VComponentDescribable<?>) destination;
            Attachment<?>[] propertyArray = castSource.getAttachments()
                    .stream()
                    .map(c -> new Attachment<>(c))
                    .toArray(size -> new Attachment<?>[size]);
            ObservableList<Attachment<?>> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setAttachments(properties);
        }
    },

    ATTENDEE ("ATTENDEE"    // property name
            , Arrays.asList(ValueType.CALENDAR_USER_ADDRESS) // valid property value types, first is default
            , Arrays.asList(ParameterType.COMMON_NAME, ParameterType.CALENDAR_USER_TYPE, ParameterType.DELEGATEES,
                    ParameterType.DELEGATORS, ParameterType.DIRECTORY_ENTRY_REFERENCE,
                    ParameterType.GROUP_OR_LIST_MEMBERSHIP, ParameterType.LANGUAGE, ParameterType.PARTICIPATION_ROLE,
                    ParameterType.PARTICIPATION_STATUS, ParameterType.RSVP_EXPECTATION, ParameterType.SENT_BY,
                    ParameterType.VALUE_DATA_TYPES) // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentAttendee<?> castSource = (VComponentAttendee<?>) source;
            VComponentAttendee<?> castDestination = (VComponentAttendee<?>) destination;
            Attendee[] propertyArray = castSource.getAttendees()
                    .stream()
                    .map(c -> new Attendee(c)) // copy each property
                    .toArray(size -> new Attendee[size]);
            ObservableList<Attendee> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setAttendees(properties);
        }
    },
    // Calendar property
    CALENDAR_SCALE ("CALSCALE" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES) // allowed parameters
            , CalendarScale.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Descriptive
    CATEGORIES ("CATEGORIES" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES) // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Categories[] propertyArray = castSource.getCategories()
                    .stream()
                    .map(c -> new Categories(c)) // copy each property
                    .toArray(size -> new Categories[size]);
            ObservableList<Categories> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setCategories(properties);
        }
    },
    // Descriptive
    CLASSIFICATION ("CLASS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Classification property = new Classification(castSource.getClassification());
            castDestination.setClassification(property);
        }
    },
    // Descriptive
    COMMENT ("COMMENT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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
        
        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPrimary<?> castSource = (VComponentPrimary<?>) source;
            VComponentPrimary<?> castDestination = (VComponentPrimary<?>) destination;
            Comment[] propertyArray = castSource.getComments()
                    .stream()
                    .map(c -> new Comment(c)) // copy each property
                    .toArray(size -> new Comment[size]);
            ObservableList<Comment> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setComments(properties);
        }
    },
    // Relationship
    CONTACT ("CONTACT", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            if (source instanceof VFreeBusy)
            { // VFreeBusy has one Contact
                VFreeBusy castSource = (VFreeBusy) source;
                VFreeBusy castDestination = (VFreeBusy) destination;
                Contact property = new Contact(castSource.getContact());
                castDestination.setContact(property);
            } else
            { // Other components have a list of Contacts
                VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
                VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
                Contact[] propertyArray = castSource.getContacts()
                        .stream()
                        .map(c -> new Contact(c)) // copy each property
                        .toArray(size -> new Contact[size]);
                ObservableList<Contact> list = FXCollections.observableArrayList(propertyArray);
                castDestination.setContacts(list);
            }
        }
    },
    // Date and Time
    DATE_TIME_COMPLETED ("COMPLETED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VTodo castSource = (VTodo) source;
            VTodo castDestination = (VTodo) destination;
            DateTimeCompleted property = new DateTimeCompleted(castSource.getDateTimeCompleted());
            castDestination.setDateTimeCompleted(property);
        }
    },
    // Change management
    DATE_TIME_CREATED ("CREATED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            DateTimeCreated property = new DateTimeCreated(castSource.getDateTimeCreated());
            castDestination.setDateTimeCreated(property);
        }
    },
    // Date and time
    DATE_TIME_DUE ("DUE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VTodo castSource = (VTodo) source;
            VTodo castDestination = (VTodo) destination;
            DateTimeDue<? extends Temporal> property = new DateTimeDue<>(castSource.getDateTimeDue());
            castDestination.setDateTimeDue(property);
        }
    },
    // Date and Time
    DATE_TIME_END ("DTEND", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDateTimeEnd<?> castSource = (VComponentDateTimeEnd<?>) source;
            VComponentDateTimeEnd<?> castDestination = (VComponentDateTimeEnd<?>) destination;
            DateTimeEnd<? extends Temporal> property = new DateTimeEnd<>(castSource.getDateTimeEnd());
            castDestination.setDateTimeEnd(property);
        }
    },
    // Change management
    DATE_TIME_STAMP ("DTSTAMP", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPersonal<?> castSource = (VComponentPersonal<?>) source;
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            DateTimeStamp property = new DateTimeStamp(castSource.getDateTimeStamp());
            castDestination.setDateTimeStamp(property);
        }
    },
    DATE_TIME_START ("DTSTART", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPrimary<?> castSource = (VComponentPrimary<?>) source;
            VComponentPrimary<?> castDestination = (VComponentPrimary<?>) destination;
            DateTimeStart<? extends Temporal> property = new DateTimeStart<>(castSource.getDateTimeStart());
            castDestination.setDateTimeStart(property);
        }
    },
    // Descriptive
    DESCRIPTION ("DESCRIPTION",
            Arrays.asList(ValueType.TEXT),
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE),
            Description.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
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
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            if (source instanceof VJournal)
            { // VJournal has list of Descriptions
                VJournal castSource = (VJournal) source;
                VJournal castDestination = (VJournal) destination;
                Description[] propertyArray = castSource.getDescriptions()
                        .stream()
                        .map(c -> new Description(c)) // copy each property
                        .toArray(size -> new Description[size]);
                ObservableList<Description> list = FXCollections.observableArrayList(propertyArray);
                castDestination.setDescriptions(list);
            } else
            { // Other components have only one Description              
                VComponentDescribable2<?> castSource = (VComponentDescribable2<?>) source;
                VComponentDescribable2<?> castDestination = (VComponentDescribable2<?>) destination;
                Description property = new Description(castSource.getDescription());
                castDestination.setDescription(property);
            }
        }
    },
    // Date and Time
    DURATION ("DURATION", // property name
            Arrays.asList(ValueType.DURATION), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDuration<?> castSource = (VComponentDuration<?>) source;
            VComponentDuration<?> castDestination = (VComponentDuration<?>) destination;
            DurationProp property = new DurationProp(castSource.getDuration());
            castDestination.setDuration(property);
        }
    },
    // Recurrence
    EXCEPTION_DATE_TIMES ("EXDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Exceptions<?>[] propertyArray = castSource.getExceptions()
                    .stream()
                    .map(c -> new Exceptions<>(c)) // copy each property
                    .toArray(size -> new Exceptions[size]);
            ObservableList<Exceptions<?>> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setExceptions(properties);
        }
    },
    // Date and Time
    FREE_BUSY_TIME ("FREEBUSY", // property name
            Arrays.asList(ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.FREE_BUSY_TIME_TYPE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VFreeBusy castSource = (VFreeBusy) source;
            VFreeBusy castDestination = (VFreeBusy) destination;
            FreeBusyTime property = new FreeBusyTime(castSource.getFreeBusyTime());
            castDestination.setFreeBusyTime(property);
        }
    },
    // Descriptive
    GEOGRAPHIC_POSITION ("GEO", // property name
            Arrays.asList(ValueType.TEXT), // In GeographicPosition there are two doubles for latitude and longitude
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentLocatable<?> castSource = (VComponentLocatable<?>) source;
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            GeographicPosition property = new GeographicPosition(castSource.getGeographicPosition());
            castDestination.setGeographicPosition(property);
        }
    },
    // Miscellaneous
    IANA_PROPERTY (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.get(0), /** property name (one in list of valid names at {@link #IANAProperty} */
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default (any value allowed)
            Arrays.asList(ParameterType.values()), // all parameters allowed
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            IANAProperty[] propertyArray = source.getIANAProperties()
                    .stream()
                    .map(c -> new IANAProperty(c)) // copy each property
                    .toArray(size -> new IANAProperty[size]);
            ObservableList<IANAProperty> properties = FXCollections.observableArrayList(propertyArray);
            destination.setIANAProperties(properties);
        }
    },
    // Change management
    LAST_MODIFIED ("LAST-MODIFIED", // property name
            Arrays.asList(ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentLastModified<?> castSource = (VComponentLastModified<?>) source;
            VComponentLastModified<?> castDestination = (VComponentLastModified<?>) destination;
            LastModified property = new LastModified(castSource.getDateTimeLastModified());
            castDestination.setDateTimeLastModified(property);
        }
    },
    // Descriptive
    LOCATION ("LOCATION", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentLocatable<?> castSource = (VComponentLocatable<?>) source;
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            Location property = new Location(castSource.getLocation());
            castDestination.setLocation(property);
        }
    },
    // Calendar property
    METHOD ("METHOD" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES) // allowed parameters
            , Method.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            NonStandardProperty[] propertyArray = source.getNonStandardProperties()
                    .stream()
                    .map(c -> new NonStandardProperty(c)) // copy each property
                    .toArray(size -> new NonStandardProperty[size]);
            ObservableList<NonStandardProperty> properties = FXCollections.observableArrayList(propertyArray);
            destination.setNonStandardProperties(properties);
        }
    },
    // Relationship
    ORGANIZER ("ORGANIZER", // name
            Arrays.asList(ValueType.CALENDAR_USER_ADDRESS), // valid property value types, first is default
            Arrays.asList(ParameterType.COMMON_NAME, ParameterType.DIRECTORY_ENTRY_REFERENCE, ParameterType.LANGUAGE,
                    ParameterType.SENT_BY), // allowed parameters
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
            if (castComponent.getOrganizer() == null)
            {
                castComponent.setOrganizer(Organizer.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPersonal<?> castSource = (VComponentPersonal<?>) source;
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            Organizer property = new Organizer(castSource.getOrganizer());
            castDestination.setOrganizer(property);
        }
    },
    // Descriptive
    PERCENT_COMPLETE ("PERCENT-COMPLETE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VTodo castSource = (VTodo) source;
            VTodo castDestination = (VTodo) destination;
            PercentComplete property = new PercentComplete(castSource.getPercentComplete());
            castDestination.setPercentComplete(property);
        }
    },
    // Descriptive
    PRIORITY ("PRIORITY", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentLocatable<?> castSource = (VComponentLocatable<?>) source;
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            Priority property = new Priority(castSource.getPriority());
            castDestination.setPriority(property);
        }
    },
    // Calendar property
    PRODUCT_IDENTIFIER ("PRODID" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES) // allowed parameters
            , ProductIdentifier.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    },
    // Recurrence
    RECURRENCE_DATE_TIMES ("RDATE", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE, ValueType.PERIOD), // valid property value types, first is default
            Arrays.asList(ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
            Recurrences.class)
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            return castComponent.getRecurrenceDates();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            VComponentRepeatable<?> castComponent = (VComponentRepeatable<?>) vComponent;
            final ObservableList<Recurrences<? extends Temporal>> list;
            if (castComponent.getRecurrenceDates() == null)
            {
                list = FXCollections.observableArrayList();
                castComponent.setRecurrenceDates(list);
            } else
            {
                list = castComponent.getRecurrenceDates();
            }
            list.add(Recurrences.parse(propertyContent));
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentRepeatable<?> castSource = (VComponentRepeatable<?>) source;
            VComponentRepeatable<?> castDestination = (VComponentRepeatable<?>) destination;
            Recurrences<?>[] propertyArray = castSource.getRecurrenceDates()
                    .stream()
                    .map(c -> new Recurrences<>(c)) // copy each property
                    .toArray(size -> new Recurrences[size]);
            ObservableList<Recurrences<?>> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setRecurrenceDates(properties);
        }
    },
    // Relationship
    RECURRENCE_IDENTIFIER ("RECURRENCE-ID", // property name
            Arrays.asList(ValueType.DATE_TIME, ValueType.DATE), // valid property value types, first is default
            Arrays.asList(ParameterType.RECURRENCE_IDENTIFIER_RANGE, ParameterType.TIME_ZONE_IDENTIFIER, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            RecurrenceId<? extends Temporal> property = new RecurrenceId<>(castSource.getRecurrenceId());
            castDestination.setRecurrenceId(property);
        }
    },
    // Recurrence
    RECURRENCE_RULE ("RRULE", // property name
            Arrays.asList(ValueType.RECURRENCE_RULE), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
            RecurrenceRuleNew.class) // property class
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
                castComponent.setRecurrenceRule(RecurrenceRuleNew.parse(propertyContent));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentRepeatable<?> castSource = (VComponentRepeatable<?>) source;
            VComponentRepeatable<?> castDestination = (VComponentRepeatable<?>) destination;
            RecurrenceRuleNew property = new RecurrenceRuleNew(castSource.getRecurrenceRule());
            castDestination.setRecurrenceRule(property);
        }
    },
    // Relationship
    RELATED_TO ("RELATED-TO", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.RELATIONSHIP_TYPE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            RelatedTo[] propertyArray = castSource.getRelatedTo()
                    .stream()
                    .map(c -> new RelatedTo(c)) // copy each property
                    .toArray(size -> new RelatedTo[size]);
            ObservableList<RelatedTo> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setRelatedTo(properties);
        }
    },
    // Alarm
    REPEAT_COUNT ("REPEAT", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VAlarm castSource = (VAlarm) source;
            VAlarm castDestination = (VAlarm) destination;
            RepeatCount property = new RepeatCount(castSource.getRepeatCount());
            castDestination.setRepeatCount(property);
        }
    },
    // Miscellaneous
    REQUEST_STATUS ("REQUEST-STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VAlarm castSource = (VAlarm) source;
            VAlarm castDestination = (VAlarm) destination;
            RepeatCount property = new RepeatCount(castSource.getRepeatCount());
            castDestination.setRepeatCount(property);
        }
    },
    // Descriptive
    RESOURCES ("RESOURCES", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentLocatable<?> castSource = (VComponentLocatable<?>) source;
            VComponentLocatable<?> castDestination = (VComponentLocatable<?>) destination;
            Resources[] propertyArray = castSource.getResources()
                    .stream()
                    .map(c -> new Resources(c)) // copy each property
                    .toArray(size -> new Resources[size]);
            ObservableList<Resources> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setResources(properties);
        }
    },
    // Change management
    SEQUENCE ("SEQUENCE", // property name
            Arrays.asList(ValueType.INTEGER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Sequence property = new Sequence(castSource.getSequence());
            castDestination.setSequence(property);
        }
    },
    // Descriptive
    STATUS ("STATUS", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDisplayable<?> castSource = (VComponentDisplayable<?>) source;
            VComponentDisplayable<?> castDestination = (VComponentDisplayable<?>) destination;
            Status property = new Status(castSource.getStatus());
            castDestination.setStatus(property);
        }
    },
    // Descriptive
    SUMMARY ("SUMMARY", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.ALTERNATE_TEXT_REPRESENTATION, ParameterType.LANGUAGE,
                    ParameterType.VALUE_DATA_TYPES), // allowed parameters
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

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentDescribable<?> castSource = (VComponentDescribable<?>) source;
            VComponentDescribable<?> castDestination = (VComponentDescribable<?>) destination;
            Summary property = new Summary(castSource.getSummary());
            castDestination.setSummary(property);
        }
    },
    // Date and Time
    TIME_TRANSPARENCY ("TRANSP", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
            TimeTransparency.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VEvent castComponent = (VEvent) vComponent;
            return castComponent.getTimeTransparency();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VEvent castSource = (VEvent) source;
            VEvent castDestination = (VEvent) destination;
            TimeTransparency property = new TimeTransparency(castSource.getTimeTransparency());
            castDestination.setTimeTransparency(property);
        }
    },
    // Time Zone
    TIME_ZONE_IDENTIFIER ("TZID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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
                castComponent.setTimeZoneIdentifier(TimeZoneIdentifier.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VTimeZone castSource = (VTimeZone) source;
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneIdentifier property = new TimeZoneIdentifier(castSource.getTimeZoneIdentifier());
            castDestination.setTimeZoneIdentifier(property);
        }
    },
    // Time Zone
    TIME_ZONE_NAME ("TZNAME", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.LANGUAGE, ParameterType.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneName.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrDaylight<?> castProperty = (StandardOrDaylight<?>) vComponent;
            return castProperty.getTimeZoneNames();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            StandardOrDaylight<?> castSource = (StandardOrDaylight<?>) source;
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneName[] propertyArray = castSource.getTimeZoneNames()
                    .stream()
                    .map(c -> new TimeZoneName(c)) // copy each property
                    .toArray(size -> new TimeZoneName[size]);
            ObservableList<TimeZoneName> properties = FXCollections.observableArrayList(propertyArray);
            castDestination.setTimeZoneNames(properties);
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_FROM ("TZOFFSETFROM", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetFrom.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetFrom();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            StandardOrDaylight<?> castSource = (StandardOrDaylight<?>) source;
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetFrom property = new TimeZoneOffsetFrom(castSource.getTimeZoneOffsetFrom());
            castDestination.setTimeZoneOffsetFrom(property);
        }
    },
    // Time Zone
    TIME_ZONE_OFFSET_TO ("TZOFFSETTO", // property name
            Arrays.asList(ValueType.UTC_OFFSET), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneOffsetTo.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            StandardOrDaylight<?> castComponent = (StandardOrDaylight<?>) vComponent;
            return castComponent.getTimeZoneOffsetTo();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            StandardOrDaylight<?> castSource = (StandardOrDaylight<?>) source;
            StandardOrDaylight<?> castDestination = (StandardOrDaylight<?>) destination;
            TimeZoneOffsetTo property = new TimeZoneOffsetTo(castSource.getTimeZoneOffsetTo());
            castDestination.setTimeZoneOffsetTo(property);
        }
    },
    // Time Zone
    TIME_ZONE_URL ("TZURL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
            TimeZoneURL.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            VTimeZone castComponent = (VTimeZone) vComponent;
            return castComponent.getTimeZoneURL();
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
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
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VTimeZone castSource = (VTimeZone) source;
            VTimeZone castDestination = (VTimeZone) destination;
            TimeZoneURL property = new TimeZoneURL(castSource.getTimeZoneURL());
            castDestination.setTimeZoneURL(property);
        }
    },
    // Alarm
    TRIGGER ("TRIGGER", // property name
            Arrays.asList(ValueType.DURATION, ValueType.DATE_TIME), // valid property value types, first is default
            Arrays.asList(ParameterType.ALARM_TRIGGER_RELATIONSHIP, ParameterType.VALUE_DATA_TYPES), // allowed parameters
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
                castComponent.setTrigger(Trigger.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VAlarm castSource = (VAlarm) source;
            VAlarm castDestination = (VAlarm) destination;
            Trigger<?> property = new Trigger<>(castSource.getTrigger());
            castDestination.setTrigger(property);
        }
    },
    // Relationship
    UNIQUE_IDENTIFIER ("UID", // property name
            Arrays.asList(ValueType.TEXT), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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
                castComponent.setUniqueIdentifier(UniqueIdentifier.parse(propertyContent));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPersonal<?> castSource = (VComponentPersonal<?>) source;
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            UniqueIdentifier property = new UniqueIdentifier(castSource.getUniqueIdentifier());
            castDestination.setUniqueIdentifier(property);
        }
    },
    // Relationship
    UNIFORM_RESOURCE_LOCATOR ("URL", // property name
            Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER), // valid property value types, first is default
            Arrays.asList(ParameterType.VALUE_DATA_TYPES), // allowed parameters
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
            castComponent.setUniformResourceLocator(UniformResourceLocator.parse(propertyContent));
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            VComponentPersonal<?> castSource = (VComponentPersonal<?>) source;
            VComponentPersonal<?> castDestination = (VComponentPersonal<?>) destination;
            UniformResourceLocator property = new UniformResourceLocator(castSource.getUniformResourceLocator());
            castDestination.setUniformResourceLocator(property);
        }
    },
    // Calendar property
    VERSION ("VERSION" // property name
            , Arrays.asList(ValueType.TEXT) // valid property value types, first is default
            , Arrays.asList(ParameterType.VALUE_DATA_TYPES) // allowed parameters
            , Version.class) // property class
    {
        @Override
        public Object getProperty(VComponentNew<?> vComponent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void parse(VComponentNew<?> vComponent, String propertyContent)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }

        @Override
        public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination)
        {
            throw new RuntimeException(toString() + " is a calendar property.  It can't be a component property.");
        }
    };
    
    // Map to match up name to enum List
    @Deprecated // go back to one name per proprety
    private static Map<String, List<PropertyType>> enumListFromNameMap = makeEnumListFromNameMap();
    private static Map<String, List<PropertyType>> makeEnumListFromNameMap()
    {
        Map<String, List<PropertyType>> map = new HashMap<>();
        Arrays.stream(PropertyType.values())
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
    public static List<PropertyType> enumListFromName(String propertyName)
    {
        return enumListFromNameMap.get(propertyName.toUpperCase());
    }
    
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
    public static PropertyType enumFromClass(Class<? extends Property> myClass)
    {
        PropertyType p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(PropertyType.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
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
    abstract public Object getProperty(VComponentNew<?> vComponent);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
    abstract public void parse(VComponentNew<?> vComponent, String propertyContent);

    /** copies the associated property from the source component to the destination component */
    abstract public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination);
}
