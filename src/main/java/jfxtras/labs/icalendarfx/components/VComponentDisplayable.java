package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Calendar component that is displayable in a graphic.  Has methods to generate recurrence
 * instances.
 * 
 * @author David Bal
 *
 * @param <I> class of recurrence instance
 * @see VEvent
 * @see VTodoOld
 * @see VJournal
 */
public interface VComponentDisplayable<T,I> extends VComponentPersonal<T>, VComponentRepeatable<T>, VComponentDescribable<T>, VComponentLastModified<T>
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
        String commaSeparatedList = Arrays.stream(categories).collect(Collectors.joining(","));
        PropertyEnum.CATEGORIES.parse(this, commaSeparatedList);
        return (T) this;
    }
    
    /**
     * CLASS: Classification
     * RFC 5545 iCalendar 3.8.1.3. page 82
     * This property defines the access classification for a calendar component.
     * "PUBLIC" / "PRIVATE" / "CONFIDENTIAL"
     * Default is PUBLIC
     * 
     * Example:
     * CLASS:PUBLIC
     */
    ObjectProperty<Classification> classificationProperty();
    default Classification getClassification() { return classificationProperty().get(); }
    default void setClassification(String contact) { setClassification(new Classification(contact)); }
    default void setClassification(Classification contact) { classificationProperty().set(contact); }
    default void setClassification(ClassificationType contact) { setClassification(new Classification(contact)); }
    default T withClassification(Classification contact) { setClassification(contact); return (T) this; }
    default T withClassification(ClassificationType contact) { setClassification(contact); return (T) this; }
    default T withClassification(String contact) { PropertyEnum.CLASSIFICATION.parse(this, contact); return (T) this; }
    
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
    ObservableList<Contact> getContact();
    void setContact(ObservableList<Contact> properties);
    default T withContact(ObservableList<Contact> contact) { setContact(contact); return (T) this; }
    default T withContact(String...contact)
    {
        Arrays.stream(contact).forEach(c -> PropertyEnum.CONTACT.parse(this, c));
        return (T) this;
    }
//    // TODO - MAKE LIST
//    ObjectProperty<Contact> contactProperty();
//    default Contact getContact() { return contactProperty().get(); }
//    default void setContact(String contact) { setContact(new Contact(contact)); }
//    default void setContact(Contact contact) { contactProperty().set(contact); }
//    default T withContact(Contact contact) { setContact(contact); return (T) this; }
//    default T withContact(String contact) { PropertyEnum.CONTACT.parse(this, contact); return (T) this; }

    
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
    default DateTimeCreated getDateTimeCreated() { return dateTimeCreatedProperty().get(); }
    default void setDateTimeCreated(DateTimeCreated dtCreated) { dateTimeCreatedProperty().set(dtCreated); }
    default T withDateTimeCreated(ZonedDateTime zonedDateTime) { setDateTimeCreated(new DateTimeCreated(zonedDateTime)); return (T) this; }
    default T withDateTimeCreated(String zonedDateTime) { setDateTimeCreated(new DateTimeCreated(zonedDateTime)); return (T) this; }
    default T withDateTimeCreated(DateTimeCreated dtCreated) { setDateTimeCreated(dtCreated); return (T) this; }
    
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
    ObservableList<Exceptions<? extends Temporal>> getExceptions();
    void setExceptions(ObservableList<Exceptions<? extends Temporal>> exceptions);
    default T withExceptions(ObservableList<Exceptions<? extends Temporal>> exceptions)
    {
        setExceptions(exceptions);
        return (T) this;
    }
    default T withExceptions(String...exceptions)
    {
        Arrays.stream(exceptions).forEach(s -> PropertyEnum.EXCEPTION_DATE_TIMES.parse(this, s));   
        return (T) this;
    }
    default T withExceptions(Temporal...exceptions)
    {
        if (exceptions.length > 0)
        {
            final ObservableList<Exceptions<? extends Temporal>> list;
            if (getExceptions() == null)
            {
                list = FXCollections.observableArrayList();
                setExceptions(list);
            } else
            {
                list = getExceptions();
            }
            
            Temporal t = exceptions[0];
            if (t instanceof LocalDate)
            {
                Set<LocalDate> exceptions2 = Arrays.stream(exceptions).map(r -> (LocalDate) r).collect(Collectors.toSet());
                getExceptions().add(new Exceptions<LocalDate>(FXCollections.observableSet(exceptions2)));
            } else if (t instanceof LocalDateTime)
            {
                getExceptions().add(new Exceptions<LocalDateTime>(FXCollections.observableSet((LocalDateTime[]) exceptions)));
            } else if (t instanceof ZonedDateTime)
            {
                getExceptions().add(new Exceptions<ZonedDateTime>(FXCollections.observableSet((ZonedDateTime[]) exceptions)));
            }
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
    ObjectProperty<RecurrenceId<? extends Temporal>> recurrenceIdProperty();
    default RecurrenceId<? extends Temporal> getRecurrenceId() { return recurrenceIdProperty().get(); }
    default void setRecurrenceId(RecurrenceId<? extends Temporal> dtStart) { recurrenceIdProperty().set(dtStart); }
    default void setRecurrenceId(Temporal temporal)
    {
        if (temporal instanceof LocalDate)
        {
            setRecurrenceId(new RecurrenceId<LocalDate>((LocalDate) temporal));            
        } else if (temporal instanceof LocalDateTime)
        {
            setRecurrenceId(new RecurrenceId<LocalDateTime>((LocalDateTime) temporal));            
        } else if (temporal instanceof ZonedDateTime)
        {
            setRecurrenceId(new RecurrenceId<ZonedDateTime>((ZonedDateTime) temporal));            
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + temporal.getClass().getSimpleName() + " is not supported");
        }
    }
    default T withRecurrenceId(RecurrenceId<? extends Temporal> dtStart) { setRecurrenceId(dtStart); return (T) this; }
    default T withRecurrenceId(String dtStart) { return withRecurrenceId(DateTimeUtilities.temporalFromString(dtStart)); }
    default T withRecurrenceId(Temporal temporal) { setRecurrenceId(temporal); return (T) this; }
    
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
        Arrays.stream(relatedTo).forEach(c -> PropertyEnum.RELATED_TO.parse(this, c));
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
    default Sequence getSequence() { return sequenceProperty().get(); }
    default void setSequence(String sequence) { setSequence(new Sequence(sequence)); }
    default void setSequence(Integer sequence) { setSequence(new Sequence(sequence)); }
    default void setSequence(Sequence sequence) { sequenceProperty().set(sequence); }
    default T withSequence(Sequence sequence) { setSequence(sequence); return (T) this; }
    default T withSequence(Integer sequence) { setSequence(sequence); return (T) this; }
    default T withSequence(String sequence) { PropertyEnum.SEQUENCE.parse(this, sequence); return (T) this; }

    
    /**
     * STATUS:
     * RFC 5545 iCalendar 3.8.1.11. page 92
     * This property defines the overall status or confirmation
     * for the calendar component.
     */
    String getStatus();
    StringProperty statusProperty();
    void setStatus(String classification);
    
    /**
     * Produces a stream of start dates or date/times by calling {@link #stream(Temporal)} using {@link #getStartRange()}
     * as the temporal parameter, minus the duration if the VComponent has one.  This stream is used
     * by {@link #makeInstances()} to produce the displayed instances of the recurrence set.
     * 
     * @return - stream of start dates or date/times between {@link #getStartRange()} and {@link #getEndRange()}
     */
    Stream<Temporal> streamLimitedByRange();
    
    /**
     * Start of range for which recurrence instances are generated.
     * Should match the start date displayed on the calendar.
     * This is not a part of an iCalendar VComponent.
     */
    Temporal getStartRange();
    /**
     * Start of range for which recurrence instances are generated.
     * Should match the start date displayed on the calendar.
     * This is not a part of an iCalendar VComponent.
     */
    void setStartRange(Temporal start);
    /**
     * End of range for which recurrence instances are generated.
     * Should match the end date displayed on the calendar.
     * This is not a part of an iCalendar VComponent.
     */
    Temporal getEndRange();
    /**
     * End of range for which recurrence instances are generated.
     * Should match the end date displayed on the calendar.
     * This is not a part of an iCalendar VComponent.
     */
    void setEndRange(Temporal end);

    /**
     * Returns the collection of recurrence instances of calendar component of type T that exists
     * between dateTimeRangeStart and dateTimeRangeEnd based on VComponent.
     * Recurrence set is defined in RFC 5545 iCalendar page 121 as follows 
     * "The recurrence set is the complete set of recurrence instances for a calendar component.  
     * The recurrence set is generated by considering the initial "DTSTART" property along with
     * the "RRULE", "RDATE", and "EXDATE" properties contained within the recurring component."
     *  
     * @param start - beginning of time frame to make instances
     * @param end - end of time frame to make instances
     * @return
     */
    Collection<I> makeInstances(Temporal start, Temporal end);
    /**
     * Returns the collection of recurrence instances of calendar component of type T that exists
     * between dateTimeRangeStart and dateTimeRangeEnd based on VComponent.
     * Recurrence set is defined in RFC 5545 iCalendar page 121 as follows 
     * "The recurrence set is the complete set of recurrence instances for a calendar component.  
     * The recurrence set is generated by considering the initial "DTSTART" property along with
     * the "RRULE", "RDATE", and "EXDATE" properties contained within the recurring component."
     * 
     * Uses start and end values from a previous call to makeInstances(Temporal start, Temporal end)
     * If there are no start and end values an exception is thrown.
     *  
     * @return
     */
    Collection<I> makeInstances();
    /**
     * Returns existing instances in the Recurrence Set (defined in RFC 5545 iCalendar page 121)
     * made by the last call of makeRecurrenceSet
     * @param <T> type of recurrence instance, such as an appointment implementation
     * 
     * @return - current instances of the Recurrence Set
     * @see makeRecurrenceSet
     */
    Collection<I> instances();
}
