package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.change.Sequence;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Status;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

public abstract class VComponentDisplayableBase<T> extends VComponentPersonalBase<T> implements VComponentDisplayable<T>, VComponentRepeatable<T>, VComponentDescribable<T>
{
    /**
     * ATTACH
     * Attachment
     * RFC 5545, 3.8.1.1, page 80
     * 
     * This property provides the capability to associate a document object with a calendar component.
     * 
     * Examples:
     * ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
     *  reports/r-960812.ps
     * */
    @Override
    public ObservableList<Attachment<?>> getAttachments() { return attachments; }
    private ObservableList<Attachment<?>> attachments;
    @Override
    public void setAttachments(ObservableList<Attachment<?>> attachments)
    {
        if (attachments != null)
        {
            registerSortOrderProperty(attachments);
        } else
        {
            unregisterSortOrderProperty(this.attachments);
        }
        this.attachments = attachments;
    }
    
    /**
     * CATEGORIES:
     * RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    @Override
    public ObservableList<Categories> getCategories() { return categories; }
    private ObservableList<Categories> categories;
    @Override
    public void setCategories(ObservableList<Categories> categories)
    {
        if (categories != null)
        {
            registerSortOrderProperty(categories);
        } else
        {
            unregisterSortOrderProperty(this.categories);
        }
        this.categories = categories;
    }
    
    /**
     * CLASS
     * Classification
     * RFC 5545, 3.8.1.3, page 82
     * 
     * This property defines the access classification for a calendar component.
     * 
     * Example:
     * CLASS:PUBLIC
     */
    @Override
    public ObjectProperty<Classification> classificationProperty()
    {
        if (classification == null)
        {
            classification = new SimpleObjectProperty<>(this, PropertyType.CLASSIFICATION.toString());
            registerSortOrderProperty(classification);
        }
        return classification;
    }
    @Override
    public Classification getClassification() { return (classification == null) ? null : classificationProperty().get(); }
    private ObjectProperty<Classification> classification;
    
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
    @Override
    public ObservableList<Contact> getContacts() { return contacts; }
    private ObservableList<Contact> contacts;
    @Override
    public void setContacts(ObservableList<Contact> contacts)
    {
        if (contacts != null)
        {
            registerSortOrderProperty(contacts);
        } else
        {
            unregisterSortOrderProperty(this.contacts);
        }
        this.contacts = contacts;
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
    @Override
    public ObjectProperty<DateTimeCreated> dateTimeCreatedProperty()
    {
        if (dateTimeCreated == null)
        {
            dateTimeCreated = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_CREATED.toString());
            registerSortOrderProperty(dateTimeCreated);
        }
        return dateTimeCreated;
    }
    @Override
    public DateTimeCreated getDateTimeCreated() { return (dateTimeCreated == null) ? null : dateTimeCreatedProperty().get(); }
    private ObjectProperty<DateTimeCreated> dateTimeCreated;
    
   /** 
    * EXDATE
    * Exception Date-Times
    * RFC 5545 iCalendar 3.8.5.1, page 117.
    * 
    * This property defines the list of DATE-TIME exceptions for
    * recurring events, to-dos, journal entries, or time zone definitions.
    */ 
    @Override
    public ObservableList<ExceptionDates> getExceptionDates()
    {
        return exceptions;
    }
    private ObservableList<ExceptionDates> exceptions;
    @Override
    public void setExceptionDates(ObservableList<ExceptionDates> exceptions)
    {
        if (exceptions != null)
        {
            registerSortOrderProperty(exceptions);
            exceptions.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
            checkRecurrencesConsistency(exceptions, null); // test current data
        } else
        {
            unregisterSortOrderProperty(this.exceptions);
        }
        this.exceptions = exceptions;
    }
    
    /**
    * LAST-MODIFIED
    * RFC 5545, 3.8.7.3, page 138
    * 
    * This property specifies the date and time that the
    * information associated with the calendar component was last
    * revised in the calendar store.
    *
    * Note: This is analogous to the modification date and time for a
    * file in the file system.
    * 
    * The value MUST be specified as a date with UTC time.
    * 
    * Example:
    * LAST-MODIFIED:19960817T133000Z
    */
    @Override
    public ObjectProperty<LastModified> dateTimeLastModifiedProperty()
    {
        if (lastModified == null)
        {
            lastModified = new SimpleObjectProperty<>(this, PropertyType.LAST_MODIFIED.toString());
            registerSortOrderProperty(lastModified);
        }
        return lastModified;
    }
    @Override
    public LastModified getDateTimeLastModified() { return (lastModified == null) ? null : dateTimeLastModifiedProperty().get(); }
    private ObjectProperty<LastModified> lastModified;
    
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public ObservableList<RecurrenceDates> getRecurrenceDates() { return recurrenceDates; }
    private ObservableList<RecurrenceDates> recurrenceDates;
    @Override
    public void setRecurrenceDates(ObservableList<RecurrenceDates> recurrenceDates)
    {
        if (recurrenceDates != null)
        {
            registerSortOrderProperty(recurrenceDates);
            recurrenceDates.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
            checkRecurrencesConsistency(recurrenceDates, null);
        } else
        {
            unregisterSortOrderProperty(this.recurrenceDates);
        }
        this.recurrenceDates = recurrenceDates;
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
    @Override
    public ObjectProperty<RecurrenceId> recurrenceIdProperty()
    {
        if (recurrenceId == null)
        {
            recurrenceId = new SimpleObjectProperty<>(this, PropertyType.RECURRENCE_IDENTIFIER.toString());
            registerSortOrderProperty(recurrenceId);
            recurrenceId.addListener((observable, oldValue, newValue) -> checkRecurrenceIdConsistency());
        }
        return recurrenceId;
    }
    @Override
    public RecurrenceId getRecurrenceId() { return (recurrenceId == null) ? null : recurrenceIdProperty().get(); }
    private ObjectProperty<RecurrenceId> recurrenceId;

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
    @Override
    public ObservableList<RelatedTo> getRelatedTo() { return relatedTo; }
    private ObservableList<RelatedTo> relatedTo;
    @Override
    public void setRelatedTo(ObservableList<RelatedTo> relatedTo)
    {
        if (relatedTo != null)
        {
            registerSortOrderProperty(relatedTo);
        } else
        {
            unregisterSortOrderProperty(this.relatedTo);
        }
        this.relatedTo = relatedTo;
    }
    
    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
            registerSortOrderProperty(recurrenceRule);
        }
        return recurrenceRule;
    }
    @Override
    public RecurrenceRule getRecurrenceRule() { return (recurrenceRule == null) ? null : recurrenceRuleProperty().get(); }
    private ObjectProperty<RecurrenceRule> recurrenceRule;
 
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
    @Override
    public ObjectProperty<Sequence> sequenceProperty()
    {
        if (sequence == null)
        {
            sequence = new SimpleObjectProperty<>(this, PropertyType.SEQUENCE.toString());
            registerSortOrderProperty(sequence);
        }
        return sequence;
    }
    @Override
    public Sequence getSequence() { return (sequence == null) ? null : sequenceProperty().get(); }
    private ObjectProperty<Sequence> sequence;

    /**
     * SUMMARY
     * RFC 5545 iCalendar 3.8.1.12. page 93
     * 
     * This property defines a short summary or subject for the calendar component.
     * 
     * Example:
     * SUMMARY:Department Party
     */
    @Override public ObjectProperty<Summary> summaryProperty()
    {
        if (summary == null)
        {
            summary = new SimpleObjectProperty<>(this, PropertyType.SUMMARY.toString());
            registerSortOrderProperty(summary);
        }
        return summary;
    }
    @Override
    public Summary getSummary() { return (summary == null) ? null : summaryProperty().get(); }
    private ObjectProperty<Summary> summary;
    
    /**
     * STATUS
     * RFC 5545 iCalendar 3.8.1.11. page 92
     * 
     * This property defines the overall status or confirmation for the calendar component.
     * 
     * Example:
     * STATUS:TENTATIVE
     */
    @Override
    public ObjectProperty<Status> statusProperty()
    {
        if (status == null)
        {
            status = new SimpleObjectProperty<>(this, PropertyType.STATUS.toString());
            registerSortOrderProperty(status);
        }
        return status;
    }
    @Override
    public Status getStatus() { return (status == null) ? null : statusProperty().get(); }
    private ObjectProperty<Status> status;

    
    /*
     * CONSTRUCTORS
     */
    public VComponentDisplayableBase() { super(); }
    
    public VComponentDisplayableBase(String contentLines)
    {
        super(contentLines);
    }
    
    public VComponentDisplayableBase(VComponentDisplayableBase<T> source)
    {
        super(source);
    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        // get stream with recurrence rule (RRULE) and recurrence date (RDATE)
        Stream<Temporal> inStream = VComponentDisplayable.super.streamRecurrences(start);

        // assign temporal comparator to match start type
        final Comparator<Temporal> temporalComparator = DateTimeUtilities.getTemporalComparator(start);
        
        // Handle Recurrence IDs
        final Stream<Temporal> stream2;
        List<VComponentDisplayable<?>> children = childComponents();
        if (children != null)
        {
            System.out.println("childComponents():" + childComponents().size() + " " + this);
            childComponents().stream().forEach(System.out::println);
            // If present, remove recurrence ID original values
            List<Temporal> recurrenceIDTemporals = childComponents()
                    .stream()
                    .peek(a -> System.out.println(a.getRecurrenceId()))
                    .map(c -> c.getRecurrenceId().getValue())
                    .collect(Collectors.toList());
            stream2 = inStream.filter(t -> ! recurrenceIDTemporals.contains(t));
//            // If present, add replacement recurrences from child components - DON'T DO - ADDED IN WHEN OTHER COMPONENT IS PROCESSED
//            stream3 = RecurrenceStreamer.merge(
//                    stream2,
//                    childComponentsWithRecurrenceIDs()
//                            .stream()
//                            .flatMap(c ->  c.streamRecurrences(start))
//                            .sorted(temporalComparator),
//                    temporalComparator);
        } else
        {
            stream2 = inStream;
        }
        
        // If present, remove exceptions
        final Stream<Temporal> stream3;
        if (getExceptionDates() != null)
        {
            List<Temporal> exceptions = getExceptionDates()
                    .stream()
                    .flatMap(r -> r.getValue().stream())
                    .map(v -> v)
                    .sorted(temporalComparator)
                    .collect(Collectors.toList());
            stream3 = stream2.filter(d -> ! exceptions.contains(d));
        } else
        {
            stream3 = stream2;
        }
        
        if (getRecurrenceRule() == null)
        {
            return stream3; // no cache is no recurrence rule
        }
        return recurrenceStreamer().makeCache(stream3);  // make cache of start date/times
    }

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }

    /*
     * CHILDREN COMPONENTS - (RECURRENCE-IDs AND MATCHING UID)
     */
    /**  Callback to make list of child components (those with RECURRENCE-ID and same UID)
     * Callback assigned in {@link VCalendar#displayableListChangeListener } */
    private Callback<VComponentDisplayable<?>, List<VComponentDisplayable<?>>> makeChildComponentsListCallBack;
    @Override
    public Callback<VComponentDisplayable<?>, List<VComponentDisplayable<?>>> getChildComponentsListCallBack()
    {
        return makeChildComponentsListCallBack;
    }
    @Override
    public void setChildComponentsListCallBack(Callback<VComponentDisplayable<?>, List<VComponentDisplayable<?>>> makeChildComponentsListCallBack)
    {
        this.makeChildComponentsListCallBack = makeChildComponentsListCallBack;
    }

    @Override
    public List<VComponentDisplayable<?>> childComponents()
    {
        if ((getRecurrenceId() == null) && (getChildComponentsListCallBack() != null))
        {
            return getChildComponentsListCallBack().call(this);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        DateTimeType startType = DateTimeUtilities.DateTimeType.of(getDateTimeStart().getValue());
        if (getExceptionDates() != null)
        {
            // assumes all exceptions are same Temporal type.  There is a listener to guarantee that assumption.
            Temporal e1 = getExceptionDates().get(0).getValue().iterator().next();
            DateTimeType exceptionType = DateTimeUtilities.DateTimeType.of(e1);
            boolean isExceptionTypeMatch = startType == exceptionType;
            if (! isExceptionTypeMatch)
            {
                errors.add("The value type of EXDATE elements MUST be the same as the DTSTART property (" + exceptionType + ", " + startType);
            }
        }

        if (getRecurrenceId() != null)
        {
            DateTimeType recurrenceIdType = DateTimeUtilities.DateTimeType.of(getRecurrenceId().getValue());
            boolean isRecurrenceIdTypeMatch = startType == recurrenceIdType;
            if (! isRecurrenceIdTypeMatch)
            {
                errors.add("The value type of RECURRENCE-ID MUST be the same as the DTSTART property (" + recurrenceIdType + ", " + startType);
            }
        }
        
        if (getRecurrenceDates() != null)
        {
            Temporal r1 = getRecurrenceDates().get(0).getValue().iterator().next();
            DateTimeType recurrenceType = DateTimeUtilities.DateTimeType.of(r1);
            boolean isRecurrenceTypeMatch = startType == recurrenceType;
            if (! isRecurrenceTypeMatch)
            {
                errors.add("The value type of RDATE elements MUST be the same as the DTSTART property (" + recurrenceType + ", " + startType);
            }
        }
        
        if (getRecurrenceRule() != null)
        {
            errors.addAll(getRecurrenceRule().errors());
        }      
        return errors;
    }
}
