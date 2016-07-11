package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Calendar component that is displayable in a graphic.
 * 
 * @author David Bal
 *
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public interface VComponentDisplayable<T> extends VComponentPersonal<T>, VComponentRepeatable<T>, VComponentDescribable<T>, VComponentLastModified<T>
{
    /**
     * CATEGORIES:
     * RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    ObservableList<Categories> getCategories();
    void setCategories(ObservableList<Categories> properties);
    default T withCategories(ObservableList<Categories> categories) { setCategories(categories); return (T) this; }
    default T withCategories(String...categories)
    {
        // TODO - JOINING MAY BE A PROBLEM - PERHAPS BETTER TO GET LIST AND ADD INDIVIDUALLY - CHECK
        String c = Arrays.stream(categories).collect(Collectors.joining(","));
        PropertyType.CATEGORIES.parse(this, c);
        return (T) this;
    }
    default T withCategories(Categories...categories)
    {
        if (getCategories() == null)
        {
            setCategories(FXCollections.observableArrayList(categories));
        } else
        {
            getCategories().addAll(categories);
        }
        return (T) this;
    }
    
    /**
     * CLASS: Classification
     * RFC 5545 iCalendar 3.8.1.3. page 82
     * This property defines the access status for a calendar component.
     * "PUBLIC" / "PRIVATE" / "CONFIDENTIAL"
     * Default is PUBLIC
     * 
     * Example:
     * CLASS:PUBLIC
     */
    ObjectProperty<Classification> classificationProperty();
    Classification getClassification();
    default void setClassification(String classification)
    {
        if (getClassification() == null)
        {
            setClassification(Classification.parse(classification));
        } else
        {
            Classification temp = Classification.parse(classification);
            getClassification().setValue(temp.getValue());
        }
    }
    default void setClassification(Classification classification) { classificationProperty().set(classification); }
    default void setClassification(ClassificationType classification)
    {
        if (getClassification() == null)
        {
            setClassification(new Classification(classification));            
        } else
        {
            getClassification().setValue(classification);
        }
    }
    default T withClassification(Classification classification)
    {
        if (getClassification() == null)
        {
            setClassification(classification);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withClassification(ClassificationType classification)
    {
        if (getClassification() == null)
        {
            setClassification(classification);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withClassification(String classification)
    {
        if (getClassification() == null)
        {
            setClassification(classification);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    /**
     * CONTACT:
     * RFC 5545 iCalendar 3.8.4.2. page 109
     * This property is used to represent contact information or
     * alternately a reference to contact information associated with the
     * calendar component.
     * 
     * Example:
     * CONTACT;ALTREP="ldap://example.com:6666/o=ABC%20Industries\,
     *  c=US???(cn=Jim%20Dolittle)":Jim Dolittle\, ABC Industries\,
     *  +1-919-555-1234
     */
    ObservableList<Contact> getContacts();
    void setContacts(ObservableList<Contact> properties);
    default T withContacts(ObservableList<Contact> contacts) { setContacts(contacts); return (T) this; }
    default T withContacts(String...contacts)
    {
        Arrays.stream(contacts).forEach(c -> PropertyType.CONTACT.parse(this, c));
        return (T) this;
    }
    default T withContacts(Contact...contacts)
    {
        if (getContacts() == null)
        {
            setContacts(FXCollections.observableArrayList(contacts));
        } else
        {
            getContacts().addAll(contacts);
        }
        return (T) this;
    }
    
    /**
     * CREATED: Date-Time Created
     * RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     * The value MUST be specified in the UTC time format.
     * 
     * Example:
     * CREATED:19960329T133000Z
     */
    ObjectProperty<DateTimeCreated> dateTimeCreatedProperty();
    DateTimeCreated getDateTimeCreated();
    default void setDateTimeCreated(String dtCreated)
    {
        if (getDateTimeCreated() == null)
        {
            setDateTimeCreated(DateTimeCreated.parse(dtCreated));
        } else
        {
            DateTimeCreated temp = DateTimeCreated.parse(dtCreated);
            setDateTimeCreated(temp);
        }
    }
    default void setDateTimeCreated(DateTimeCreated dtCreated) { dateTimeCreatedProperty().set(dtCreated); }
    default void setDateTimeCreated(ZonedDateTime dtCreated)
    {
        if (getDateTimeCreated() == null)
        {
            setDateTimeCreated(new DateTimeCreated(dtCreated));
        } else
        {
            getDateTimeCreated().setValue(dtCreated);
        }
    }
    default T withDateTimeCreated(ZonedDateTime dtCreated)
    {
        if (getDateTimeCreated() == null)
        {
            setDateTimeCreated(dtCreated);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeCreated(String dtCreated)
    {
        if (getDateTimeCreated() == null)
        {
            setDateTimeCreated(dtCreated);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeCreated(DateTimeCreated dtCreated)
    {
        if (getDateTimeCreated() == null)
        {
            setDateTimeCreated(dtCreated);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    /**
     * EXDATE: Exception Date-Times
     * Set of date/times exceptions for recurring events, to-dos, journal entries.
     * These are date/times that would be instances, but instead are skipped.
     * 3.8.5.1, RFC 5545 iCalendar, page 118
     * 
     * Example:
     * EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z
     * 
     */
    ObservableList<ExceptionDates> getExceptionDates();
    void setExceptionDates(ObservableList<ExceptionDates> exceptions);
    default T withExceptionDates(ObservableList<ExceptionDates> exceptions)
    {
        setExceptionDates(exceptions);
        return (T) this;
    }
    default T withExceptionDates(String...exceptions)
    {
        Arrays.stream(exceptions).forEach(s -> PropertyType.EXCEPTION_DATE_TIMES.parse(this, s));   
        return (T) this;
    }
    default T withExceptionDates(Temporal...exceptions)
    {
        final ObservableList<ExceptionDates> list;
        if (getExceptionDates() == null)
        {
            list = FXCollections.observableArrayList();
            setExceptionDates(list);
        } else
        {
            list = getExceptionDates();
        }
        list.add(new ExceptionDates(FXCollections.observableSet(exceptions)));
        return (T) this;
    }
    default T withExceptionDates(ExceptionDates...exceptions)
    {
        if (getExceptionDates() == null)
        {
            setExceptionDates(FXCollections.observableArrayList());
            Arrays.stream(exceptions).forEach(e -> getExceptionDates().add(e)); // add one at a time to ensure date-time type compliance
        } else
        {
            getExceptionDates().addAll(exceptions);
        }
        return (T) this;
    }
    
    /**
     * RECURRENCE-ID: Recurrence Identifier
     * RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance before an edit that changed the value.
     * 
     * Example:
     * RECURRENCE-ID;VALUE=DATE:19960401
     */
    ObjectProperty<RecurrenceId> recurrenceIdProperty();
    RecurrenceId getRecurrenceId();
    default void setRecurrenceId(RecurrenceId recurrenceId) { recurrenceIdProperty().set(recurrenceId); }
    default void setRecurrenceId(String recurrenceId)
    {
        if (getRecurrenceId() == null)
        {
            setRecurrenceId(RecurrenceId.parse(recurrenceId));
        } else
        {
            RecurrenceId temp = RecurrenceId.parse(recurrenceId);
            if (temp.getValue().getClass().equals(getRecurrenceId().getValue().getClass()))
            {
                getRecurrenceId().setValue(temp.getValue());
            } else
            {
                setRecurrenceId(temp);
            }
        }
    }
    default void setRecurrenceId(Temporal temporal)
    {
        if ((getRecurrenceId() == null) || ! getRecurrenceId().getValue().getClass().equals(temporal.getClass()))
        {
            if ((temporal instanceof LocalDate) || (temporal instanceof LocalDateTime) || (temporal instanceof ZonedDateTime))
            {
                if (getRecurrenceId() == null)
                {
                    setRecurrenceId(new RecurrenceId(temporal));
                } else
                {
                    getRecurrenceId().setValue(temporal);
                }
            } else
            {
                throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                        + temporal.getClass().getSimpleName() + " is not supported");
            }
        } else
        {
            getRecurrenceId().setValue(temporal);
        }
    }
    default T withRecurrenceId(RecurrenceId recurrenceId)
    {
        if (getRecurrenceId() == null)
        {
            setRecurrenceId(recurrenceId);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withRecurrenceId(String recurrenceId)
    {
        if (getRecurrenceId() == null)
        {
            setRecurrenceId(recurrenceId);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withRecurrenceId(Temporal recurrenceId)
    {
        if (getRecurrenceId() == null)
        {
            setRecurrenceId(recurrenceId);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    /** Ensures RecurrenceId has same date-time type as DateTimeStart.  Should be put in listener
     *  after recurrenceIdProperty() is initialized */
    default void checkRecurrenceIdConsistency()
    {
        if ((getRecurrenceId() != null) && (getDateTimeStart() != null))
        {
            DateTimeType recurrenceIdType = DateTimeUtilities.DateTimeType.of(getRecurrenceId().getValue());
//            System.out.println("getParent():" + getParent());
//            System.out.println("this:" + this);
//            System.out.println("getRecurrenceId().getValue():" + getRecurrenceId().getValue());
            List<VComponentDisplayable<?>> relatedComponents = ((VCalendar) getParent()).uidComponentsMap().get(getUniqueIdentifier().getValue());
            VComponentDisplayable<?> parentComponent = relatedComponents.stream()
                    .filter(v -> v.getRecurrenceId() == null)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("no parent component found"));
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(parentComponent.getDateTimeStart().getValue());
            if (recurrenceIdType != dateTimeStartType)
            {
                throw new DateTimeException("RecurrenceId DateTimeType (" + recurrenceIdType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }
    }
    
    /**
     * RELATED-TO:
     * 3.8.4.5, RFC 5545 iCalendar, page 115
     * This property is used to represent a relationship or reference between
     * one calendar component and another.  By default, the property value points to another
     * calendar component's UID that has a PARENT relationship to the referencing object.
     * This field is null unless the object contains as RECURRENCE-ID value.
     * 
     * Example:
     * RELATED-TO:19960401-080045-4000F192713-0052@example.com
     */
    ObservableList<RelatedTo> getRelatedTo();
    void setRelatedTo(ObservableList<RelatedTo> properties);
    default T withRelatedTo(ObservableList<RelatedTo> relatedTo) { setRelatedTo(relatedTo); return (T) this; }
    default T withRelatedTo(String...relatedTo)
    {
        Arrays.stream(relatedTo).forEach(c -> PropertyType.RELATED_TO.parse(this, c));
        return (T) this;
    }
    default T withRelatedTo(RelatedTo...relatedTo)
    {
        if (getRelatedTo() == null)
        {
            setRelatedTo(FXCollections.observableArrayList(relatedTo));
        } else
        {
            getRelatedTo().addAll(relatedTo);
        }
        return (T) this;
    }
    
    /**
     * SEQUENCE:
     * RFC 5545 iCalendar 3.8.7.4. page 138
     * This property defines the revision sequence number of the calendar component within a sequence of revisions.
     * Example:  The following is an example of this property for a calendar
     * component that was just created by the "Organizer":
     *
     * SEQUENCE:0
     *
     * The following is an example of this property for a calendar
     * component that has been revised two different times by the
     * "Organizer":
     *
     * SEQUENCE:2
     */
    ObjectProperty<Sequence> sequenceProperty();
    Sequence getSequence();
    default void setSequence(String sequence)
    {
        if (getSequence() == null)
        {
            setSequence(Sequence.parse(sequence));
        } else
        {
            Sequence temp = Sequence.parse(sequence);
            getSequence().setValue(temp.getValue());
        }
    }
    default void setSequence(Integer sequence)
    {
        if (getSequence() == null)
        {
            setSequence(new Sequence(sequence));
        } else
        {
            getSequence().setValue(sequence);
        }
    }
    default void setSequence(Sequence sequence) { sequenceProperty().set(sequence); }
    default T withSequence(Sequence sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withSequence(Integer sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withSequence(String sequence)
    {
        if (getSequence() == null)
        {
            setSequence(sequence);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default void incrementSequence()
    {
        if (getSequence() != null)
        {
            setSequence(getSequence().getValue()+1);            
        } else
        {
            setSequence(1);            
        }
    }
    
    /**
     * STATUS
     * RFC 5545 iCalendar 3.8.1.11. page 92
     * 
     * This property defines the overall status or confirmation for the calendar component.
     * 
     * Example:
     * STATUS:TENTATIVE
     */
    ObjectProperty<Status> statusProperty();
    Status getStatus();
    default void setStatus(String status)
    {
        if (getStatus() == null)
        {
            setStatus(Status.parse(status));
        } else
        {
            Status temp = Status.parse(status);
            getStatus().setValue(temp.getValue());
        }
    }
    default void setStatus(Status status) { statusProperty().set(status); }
    default void setStatus(StatusType status)
    {
        if (getStatus() == null)
        {
            setStatus(new Status(status));
        } else
        {
            getStatus().setValue(status);
        }
    }
    default T withStatus(Status status)
    {
        if (getStatus() == null)
        {
            setStatus(status);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withStatus(StatusType status)
    {
        if (getStatus() == null)
        {
            setStatus(status);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withStatus(String status)
    {
        if (getStatus() == null)
        {
            setStatus(status);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    @Override
    default void checkDateTimeStartConsistency()
    {
        VComponentRepeatable.super.checkDateTimeStartConsistency();
        if ((getExceptionDates() != null) && (getDateTimeStart() != null))
        {
            Temporal firstException = getExceptionDates().get(0).getValue().iterator().next();
            DateTimeType exceptionType = DateTimeUtilities.DateTimeType.of(firstException);
            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
            if (exceptionType != dateTimeStartType)
            {
                throw new DateTimeException("Exceptions DateTimeType (" + exceptionType +
                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
            }
        }
        checkRecurrenceIdConsistency();
//        if ((getRecurrenceId() != null) && (getDateTimeStart() != null))
//        {
//            DateTimeType recurrenceIdType = DateTimeUtilities.DateTimeType.of(getRecurrenceId().getValue());
//            DateTimeType dateTimeStartType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
//            if (recurrenceIdType != dateTimeStartType)
//            {
//                throw new DateTimeException("RecurrenceId DateTimeType (" + recurrenceIdType +
//                        ") must be same as the DateTimeType of DateTimeStart (" + dateTimeStartType + ")");
//            }
//        }        
    }

//    @Override
//    DisplayRevisable<?, T> newRevisor();
    
    /**
     * List of child components having RecurrenceIDs and matching UID to a parent component
     * Note: This list is maintained by a listener in {@link VCalendar#displayableListChangeListener}
     * Do not modify the list.
     * 
     * @return - list of child components having RecurrenceIDs
     */
    List<VComponentDisplayable<?>> childComponents();
//    /** Returns false if there are no child components currently in list.  Run this method instead of childComponents().size() because
//     * the latter will generate the list if it doesn't exist which may not be desired.
//     */
//    boolean hasChildComponents();
    
    /** Callback to make list of child components (those with RECURRENCE-ID and same UID) 
     * Callback assigned in {@link VCalendar#displayableListChangeListener }.  It should not be assigned elsewhere. */
    Callback<VComponentDisplayable<?>, List<VComponentDisplayable<?>>> getChildComponentsListCallBack();
    /** Callback to make list of child components (those with RECURRENCE-ID and same UID) */
    void setChildComponentsListCallBack(Callback<VComponentDisplayable<?>, List<VComponentDisplayable<?>>> childComponentsListCallBack);
}
