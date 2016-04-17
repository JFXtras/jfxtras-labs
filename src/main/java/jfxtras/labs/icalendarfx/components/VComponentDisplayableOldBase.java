package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.SetChangeListener;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.components.VComponentUtilities.VComponentPropertyOld;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;

/**
 * Abstract implementation of VComponent with all common methods for VEvent, VTodo, and VJournal
 * 
 * @author David Bal
 * @see VEvent
 *
 * @param <I> - recurrence instance type
 * @param <T> - Implementation class
 */
public abstract class VComponentDisplayableOldBase<I, T> implements VComponent<I>
{
    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    // TODO - NEED TO ACCEPT MULTIPLE CATEGORIES - CHANGE TO OBSERVABLE LIST OR SET OR USE COMMA-DELIMATED STRING - NEED TO PUT BOX AROUND APPOINTMENT GROUP FOR THE SELECTED ONE, BUT MULTIPLE CHECKS ARE ALLOWED
    @Override public ObjectProperty<Categories> categoriesProperty() { return categoriesProperty; }
    final private ObjectProperty<Categories> categoriesProperty = new SimpleObjectProperty<>(this, VComponentPropertyOld.CATEGORIES.toString());
    @Override public Categories getCategories() { return categoriesProperty.get(); }
    @Override public void setCategories(Categories value) { categoriesProperty.set(value); }
    public void setCategories(String value) { categoriesProperty.set(new Categories(value)); }
    public T withCategories(Categories categories) { setCategories(categories); return (T) this; }
    public T withCategories(String categories) { setCategories(categories); return (T) this; }
    
    /**
     *  COMMENT: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property specifies non-processing information intended
      to provide a comment to the calendar user.
     * Example:
     * COMMENT:The meeting really needs to include both ourselves
         and the customer. We can't hold this meeting without them.
         As a matter of fact\, the venue for the meeting ought to be at
         their site. - - John
     * */
    @Override
    public ObjectProperty<Comment> commentProperty()
    {
        if (comment == null) comment = new SimpleObjectProperty<Comment>(this, VComponentPropertyOld.COMMENT.toString(), _comment);
        return comment;
    }
    private ObjectProperty<Comment> comment;
    @Override public Comment getComment() { return (comment == null) ? _comment : comment.get(); }
    private Comment _comment;
    @Override
    public void setComment(Comment comment)
    {
        if (this.comment == null)
        {
            _comment = comment;
        } else
        {
            this.comment.set(comment);            
        }
    }
//    @Override
//    public StringProperty commentProperty()
//    {
//        if (comment == null) comment = new SimpleStringProperty(this, VComponentProperty.COMMENT.toString(), _comment);
//        return comment;
//    }
//    private StringProperty comment;
//    private String _comment;
//    @Override
//    public String getComment() { return (comment == null) ? _comment : comment.get(); }
//    @Override
//    public void setComment(String comment)
//    {
//        if (this.comment == null)
//        {
//            _comment = comment;
//        } else
//        {
//            this.comment.set(comment);            
//        }
//    }
//    public T withComment(String comment) { setComment(comment); return (T) this; }

    /**
     * CREATED: Date-Time Created, from RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     */
    @Override
    public ObjectProperty<ZonedDateTime> dateTimeCreatedProperty() { return dateTimeCreated; }
    final private ObjectProperty<ZonedDateTime> dateTimeCreated = new SimpleObjectProperty<>(this, VComponentPropertyOld.CREATED.toString());
    @Override
    public ZonedDateTime getDateTimeCreated() { return dateTimeCreated.get(); }
    @Override
    public void setDateTimeCreated(ZonedDateTime dtCreated)
    {
        if ((dtCreated != null) && ! (dtCreated.getOffset().equals(ZoneOffset.UTC)))
        {
            throw new DateTimeException("dateTimeStamp (DTSTAMP) must be specified in the UTC time format (Z)");
        }
        this.dateTimeCreated.set(dtCreated);
    }
    public T withDateTimeCreated(ZonedDateTime dtCreated) { setDateTimeCreated(dtCreated); return (T) this; }
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    @Override
    public ObjectProperty<ZonedDateTime> dateTimeStampProperty() { return dateTimeStamp; }
    final private ObjectProperty<ZonedDateTime> dateTimeStamp = new SimpleObjectProperty<>(this, VComponentPropertyOld.DATE_TIME_STAMP.toString());
    @Override
    public ZonedDateTime getDateTimeStamp() { return dateTimeStamp.get(); }
    @Override
    public void setDateTimeStamp(ZonedDateTime dtStamp)
    {
        if ((dtStamp != null) && ! (dtStamp.getOffset().equals(ZoneOffset.UTC)))
        {
            throw new DateTimeException("dateTimeStamp (DTSTAMP) must be specified in the UTC time format (Z)");
        }
        this.dateTimeStamp.set(dtStamp);
    }
    public T withDateTimeStamp(ZonedDateTime dtStamp) { setDateTimeStamp(dtStamp); return (T) this; }
    
    /* DTSTART temporal class and ZoneId
     * 
     * Used to ensure the following date-time properties use the same Temporal class
     * and ZoneId (if using ZonedDateTime, null otherwise)
     * DTEND
     * RECURRENCE-ID
     * EXDATE (underlying collection of Temporals)
     * RDATE (underlying collection of Temporals)
     */
    private DateTimeType lastDtStartDateTimeType;
    protected DateTimeType lastDtStartDateTimeType() { return lastDtStartDateTimeType; }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     * @SEE VDateTime
     */
    @Override
    public ObjectProperty<Temporal> dateTimeStartProperty() { return dateTimeStart; }
    final private ObjectProperty<Temporal> dateTimeStart = new SimpleObjectProperty<>(this, VComponentPropertyOld.DATE_TIME_START.toString());
    @Override public Temporal getDateTimeStart() { return dateTimeStart.get(); }
    @Override
    public void setDateTimeStart(Temporal dtStart)
    {
        // check Temporal class is LocalDate, LocalDateTime or ZonedDateTime - others are not supported
        DateTimeType myDateTimeType = DateTimeType.of(dtStart);
        boolean changed = (lastDtStartDateTimeType != null) && (myDateTimeType != lastDtStartDateTimeType);
        lastDtStartDateTimeType = myDateTimeType;
        dateTimeStart.set(dtStart);
        
        // if type has changed then make all date-time properties the same
        if (changed)
        {
            System.out.println("**********************start:" + dtStart);
            ensureDateTimeTypeConsistency(myDateTimeType, getZoneId());
        }
    }
    public T withDateTimeStart(Temporal dtStart) { setDateTimeStart(dtStart); return (T) this; }
    
    /**
     * Changes Temporal type of some properties to match the input parameter.  The input
     * parameter should be based on the DTSTART property.
     * 
     * This method runs when dateTimeStart (DTSTART) changes DateTimeType
     * 
     * @param dateTimeType - new DateTimeType
     * @param zone - ZoneId of dateTimeType, null if not applicable
     */
    void ensureDateTimeTypeConsistency(DateTimeType dateTimeType, ZoneId zone)
    {
        // RECURRENCE-ID (of children)
        System.out.println("ensureDateTimeTypeConsistency:" + dateTimeType);
        if (getRRule() != null && getRRule().recurrences() != null)
        {
            getRRule().recurrences().forEach(v ->
            {
//                Temporal newDateTimeRecurrence = DateTimeType.changeTemporal(v.getDateTimeRecurrence(), dateTimeType);
                Temporal newDateTimeRecurrence = dateTimeType.from(v.getDateTimeRecurrence(), zone);
                v.setDateTimeRecurrence(newDateTimeRecurrence);
            });
        }
        
        // EXDATE
        if (getExDate() != null)
        {
            Temporal firstTemporal = getExDate().getTemporals().iterator().next();
            DateTimeType exDateDateTimeType = DateTimeType.of(firstTemporal);
            if (dateTimeType != exDateDateTimeType)
            {
                Set<Temporal> newExDateTemporals = getExDate().getTemporals()
                        .stream()
//                        .map(t -> DateTimeType.changeTemporal(t, dateTimeType))
                        .map(t -> dateTimeType.from(t, zone))
                        .collect(Collectors.toSet());
                getExDate().getTemporals().clear();
                getExDate().getTemporals().addAll(newExDateTemporals);
            }
        }
        
        // RDATE
        if (getRDate() != null)
        {
            Temporal firstTemporal = getRDate().getTemporals().iterator().next();
            DateTimeType rDateDateTimeType = DateTimeType.of(firstTemporal);
            if (dateTimeType != rDateDateTimeType)
            {
                Set<Temporal> newRDateTemporals = getRDate().getTemporals()
                        .stream()
//                        .map(t -> DateTimeType.changeTemporal(t, dateTimeType))
                        .map(t -> dateTimeType.from(t, zone))
                        .collect(Collectors.toSet());
                getRDate().getTemporals().clear();
                getRDate().getTemporals().addAll(newRDateTemporals);
            }
        }
        
        // RANGE
        if (getStartRange() != null)
        {
            setStartRange(getStartRange());
        }
        if (getEndRange() != null)
        {
            setEndRange(getEndRange());
        }
    }
    
    // Listener for EXDATE and RDATE - checks if added Temporals match DTSTART type // TODO - IS THIS NECESSARY?
    private final SetChangeListener<? super Temporal> recurrenceListener = (SetChangeListener<? super Temporal>) (SetChangeListener.Change<? extends Temporal> change) ->
    {
        if (change.wasAdded())
        {
            Temporal newTemporal = change.getElementAdded();
            DateTimeType myDateTimeType = DateTimeType.of(newTemporal);
            if ((lastDtStartDateTimeType() != null) && (myDateTimeType != lastDtStartDateTimeType()))
            {
                throw new DateTimeException("Temporal must have the same DateTimeType as DTSTART, (" + myDateTimeType + ", " + lastDtStartDateTimeType() + ", respectively");
            }
        }
    };
    
    /**
     * EXDATE: Set of date/times exceptions for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Is rarely used, so employs lazy initialization.
     */
    @Override
    public ObjectProperty<ExDate> exDateProperty()
    {
        if (exDate == null) exDate = new SimpleObjectProperty<>(this, VComponentPropertyOld.EXCEPTIONS.toString(), _exDate);
        return exDate;
    }
    private ObjectProperty<ExDate> exDate;
    private ExDate _exDate;
    @Override
    public ExDate getExDate() { return (exDate == null) ? _exDate : exDate.getValue(); }
    @Override
    public void setExDate(ExDate exDate)
    {
        if (this.exDate == null)
        {
            _exDate = exDate;
        } else
        {
            this.exDate.set(exDate);
            // ensure Temporals added to ExDate are the same as DTSTART
            exDate.getTemporals().removeListener(recurrenceListener);
            exDate.getTemporals().addListener(recurrenceListener);
        }
    }
    public T withExDate(ExDate exDate) { setExDate(exDate); return (T) this; }
    /** true = put all Temporals on one line, false = use one line for each Temporal */
    @Override
    public boolean isExDatesOnOneLine() { return exDatesOnOneLine; }
    private boolean exDatesOnOneLine = false;
    /** true = put all Temporals on one line, false = use one line for each Temporal */
    public void setExDatesOnOneLine(boolean b) { exDatesOnOneLine = b; }
    
    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    @Override
    public ObjectProperty<ZonedDateTime> dateTimeLastModifiedProperty() { return dateTimeLastModified; }
    final private ObjectProperty<ZonedDateTime> dateTimeLastModified = new SimpleObjectProperty<ZonedDateTime>(this, VComponentPropertyOld.LAST_MODIFIED.toString());
    @Override
    public ZonedDateTime getDateTimeLastModified() { return dateTimeLastModified.getValue(); }
    @Override
    public void setDateTimeLastModified(ZonedDateTime dtLastModified)
    {
        if ((dtLastModified != null) && ! dtLastModified.getOffset().equals(ZoneOffset.UTC))
        {
            throw new DateTimeException("dateTimeStamp (DTSTAMP) must be specified in the UTC time format (Z)");
        }
        this.dateTimeLastModified.set(dtLastModified);
    }
    public T withDateTimeLastModified(ZonedDateTime dtLastModified) { setDateTimeLastModified(dtLastModified); return (T) this; }

    /**
     *  ORGANIZER: RFC 5545 iCalendar 3.8.4.3. page 111
     * This property defines the organizer for a calendar component
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     * 
     * The property is stored as a simple string.  The implementation is
     * responsible to extract any contained data elements such as CN, DIR, SENT-BY
     * */
    @Override
    public StringProperty organizerProperty()
    {
        if (organizer == null) organizer = new SimpleStringProperty(this, VComponentPropertyOld.ORGANIZER.toString(), _organizer);
        return organizer;
    }
    private StringProperty organizer;
    private String _organizer;
    @Override
    public String getOrganizer() { return (organizer == null) ? _organizer : organizer.get(); }
    @Override
    public void setOrganizer(String organizer)
    {
        if (this.organizer == null)
        {
            _organizer = organizer;
        } else
        {
            this.organizer.set(organizer);            
        }
    }
    public T withOrganizer(String organizer) { setOrganizer(organizer); return (T) this; }

    /**
     * RDATE: Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
    */
    // Is rarely used, so employs lazy initialization.
    @Override
    public ObjectProperty<RDate> rDateProperty()
    {
        if (rDate == null) rDate = new SimpleObjectProperty<RDate>(this, VComponentPropertyOld.RECURRENCES.toString(), _rDate);
        return rDate;
    }
    private ObjectProperty<RDate> rDate;
    private RDate _rDate;
    @Override
    public RDate getRDate() { return (rDate == null) ? _rDate : rDate.getValue(); }
    @Override
    public void setRDate(RDate rDate)
    {
        if (this.rDate == null)
        {
            _rDate = rDate;
        } else
        {
            this.rDate.set(rDate);
            // ensure Temporals added to RDate are the same as DTSTART
//            rDate.getTemporals().removeListener(recurrenceListener); // TODO - WHAT ARE THESE DOING?
//            rDate.getTemporals().addListener(recurrenceListener);
        }
    }
    public T withRDate(RDate rDate) { setRDate(rDate); return (T) this; }

    
    /**
     * RELATED-TO: This property is used to represent a relationship or reference between
     * one calendar component and another.  By default, the property value points to another
     * calendar component's UID that has a PARENT relationship to the referencing object.
     * This field is null unless the object contains as RECURRENCE-ID value.
     * 3.8.4.5, RFC 5545 iCalendar
     */
    @Override
    public StringProperty relatedToProperty() { return relatedTo; }
    final private StringProperty relatedTo = new SimpleStringProperty(this, VComponentPropertyOld.RELATED_TO.toString());
    @Override
    public String getRelatedTo() { return relatedTo.getValue(); }
    @Override
    public void setRelatedTo(String uid) { relatedTo.set(uid); }
    public T withRelatedTo(String uid) { setRelatedTo(uid); return (T) this; }

    
    // TODO - I may implement Google UID recurrence for segments of recurrence set
    /**
     * Use Google UID extension instead of RELATED-TO to express 
     */
    @Override
    public boolean isGoogleRecurrenceUID() { return googleRecurrenceUID; };
    private boolean googleRecurrenceUID = false; // default to not using Google system
    @Override
    public void setGoogleRecurrenceUID(boolean b) { googleRecurrenceUID = b; };
    
    /**
     * RECURRENCE-ID: Date-Time recurrence, from RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance.
     */
    @Override
    public ObjectProperty<Temporal> dateTimeRecurrenceProperty()
    {
        if (dateTimeRecurrence == null) dateTimeRecurrence = new SimpleObjectProperty<>(this, VComponentPropertyOld.RECURRENCE_ID.toString(), _dateTimeRecurrence);
        return dateTimeRecurrence;
    }
    private ObjectProperty<Temporal> dateTimeRecurrence;
    private Temporal _dateTimeRecurrence;
    @Override public Temporal getDateTimeRecurrence()
    {
        return (dateTimeRecurrence == null) ? _dateTimeRecurrence : dateTimeRecurrence.get();
    }
    @Override public void setDateTimeRecurrence(Temporal dtRecurrence)
    {
        DateTimeType myDateTimeType = DateTimeType.of(dtRecurrence);
         // parent should be set before dateTimeRecurrence or this check can't be performed
        if ((getParent() != null) && (myDateTimeType != getParent().getDateTimeType()))
        {
            throw new DateTimeException("RECURRENCE-ID must have the same DateTimeType as the parent's DTSTART, (" + myDateTimeType + " & " + getParent().getDateTimeType() + ", respectively");
        }

        if (dateTimeRecurrence == null)
        {
            _dateTimeRecurrence = dtRecurrence;
        } else
        {
            dateTimeRecurrence.set(dtRecurrence);
        }
    }
    public T withDateTimeRecurrence(Temporal dtRecurrence) { setDateTimeRecurrence(dtRecurrence); return (T) this; }

    
    @Override // TODO - MAKE OBJECT PROPERTY???
    public VComponent<I> getParent() { return parent; }
    private VComponent<I> parent;
    @Override
    public void setParent(VComponent<I> parent) { this.parent = parent; }
    public T withParent(VComponent<I> parent) { setParent(parent); return (T) this; }

    
    /**
     * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
     * If event is not repeating value is null
     */
    @Override
    public ObjectProperty<RecurrenceRule> rRuleProperty()
    {
        if (rRule == null) rRule = new SimpleObjectProperty<RecurrenceRule>(this, VComponentPropertyOld.RECURRENCE_RULE.toString(), _rRule);
        return rRule;
    }
    private ObjectProperty<RecurrenceRule> rRule;
    private RecurrenceRule _rRule;
    @Override
    public RecurrenceRule getRRule() { return (rRule == null) ? _rRule : rRule.get(); }
    @Override
    public void setRRule(RecurrenceRule rRule)
    {
        if (this.rRule == null)
        {
            _rRule = rRule;
        } else
        {
            this.rRule.set(rRule);
        }
    }
    public T withRRule(RecurrenceRule rRule) { setRRule(rRule); return (T) this; }
    
    /**
     *  SEQUENCE: RFC 5545 iCalendar 3.8.7.4. page 138
     * This property defines the revision sequence number of the calendar component within a sequence of revisions.
     * Example:  The following is an example of this property for a calendar
      component that was just created by the "Organizer":

       SEQUENCE:0

      The following is an example of this property for a calendar
      component that has been revised two different times by the
      "Organizer":

       SEQUENCE:2
     */
    @Override
    public IntegerProperty sequenceProperty() { return sequenceProperty; }
    final private IntegerProperty sequenceProperty = new SimpleIntegerProperty(this, VComponentPropertyOld.SEQUENCE.toString(), 0);
    @Override
    public int getSequence() { return sequenceProperty.get(); }
    @Override
    public void setSequence(int value)
    {
        if (value < 0) throw new IllegalArgumentException("Sequence value must be greater than zero");
        if (value < getSequence()) throw new IllegalArgumentException("New sequence value must be greater than previous value");
        sequenceProperty.set(value);
    }
    public T withSequence(int value) { setSequence(value); return (T) this; }
    
    /**
     *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    @Override
    public ObjectProperty<Summary> summaryProperty() { return summary; }
    private ObjectProperty<Summary> summary = new SimpleObjectProperty<>(this, VComponentPropertyOld.SUMMARY.toString());
    @Override
    public Summary getSummary() { return summary.get(); }
    @Override
    public void setSummary(Summary summary) { this.summary.set(summary); }
    public void setSummary(String summary) { this.summary.set(new Summary(summary)); }
    public T withSummary(Summary summary) { setSummary(summary); return (T) this; }
    public T withSummary(String summary) { setSummary(summary); return (T) this; }
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     */
    @Override
    public StringProperty uniqueIdentifierProperty() { return uniqueIdentifier; }
    final private StringProperty uniqueIdentifier = new SimpleStringProperty(this, VComponentPropertyOld.UNIQUE_IDENTIFIER.toString());
    @Override
    public String getUniqueIdentifier() { return uniqueIdentifier.getValue(); }
    @Override
    public void setUniqueIdentifier(String uid)
    {
        if ((uid != null) && (uid.matches("(.*)google\\.com")) && (uid.matches("(.*)_R(.*)")))
        {
            String uid2 = uid.substring(0, uid.indexOf("_")) + "@google.com";
            String recurrence = uid.substring(uid.indexOf("_"), uid.indexOf("@")); // not currently used
            setRelatedTo(uid2);
        }
        uniqueIdentifier.set(uid);
    }
    /** Set uniqueIdentifier by calling uidGeneratorCallback */
    public void setUniqueIdentifier() { setUniqueIdentifier(getUidGeneratorCallback().call(null)); }
    public T withUniqueIdentifier(String uid) { setUniqueIdentifier(uid); return (T) this; }

    
    /** Callback for creating unique uid values  */
    @Override
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    @Override
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
    /**
     * Start of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     * This property is not a part of the iCalendar standard
     */
    @Override
    public Temporal getStartRange() { return startRange; }
    private Temporal startRange;
    @Override
    public void setStartRange(Temporal start){ startRange = getDateTimeType().from(start, getZoneId()); }
    public T withStartRange(Temporal start) { setStartRange(start); return (T) this; }
    
    /**
     * End of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     */
    @Override
    public Temporal getEndRange() { return endRange; }
    private Temporal endRange;
    @Override
    public void setEndRange(Temporal end) { endRange = getDateTimeType().from(end, getZoneId()); }
    public T withEndRange(Temporal end) { setEndRange(end); return (T) this; }

    /**
     * The currently generated instances of the recurrence set.
     * 3.8.5.2 defines the recurrence set as the complete set of recurrence instances for a
     * calendar component.  As many RRule definitions are infinite sets, a complete representation
     * is not possible.  The set only contains the events inside the bounds of 
     */
    @Override
    public Collection<I> instances() { return instances; }
    final private Collection<I> instances = new ArrayList<>();

    // CONSTRUCTORS
    /** Copy constructor */
    public VComponentDisplayableOldBase(VComponentDisplayableOldBase<I, T> vcomponent)
    {
        copy(vcomponent, this);
    }
    
    public VComponentDisplayableOldBase() { }
    
    @Override
    public boolean handleEdit(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances
          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
    {
        validateStartInstanceAndDTStart(startOriginalInstance, startInstance, endInstance);
        final RRuleStatus rruleType = RRuleStatus.getRRuleType(getRRule(), vComponentOriginal.getRRule());
        System.out.println("rruleType:" + rruleType);
        boolean incrementSequence = true;
        Collection<I> newInstances = null;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            becomingIndividual(vComponentOriginal, startInstance, endInstance);
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            adjustDateTime(startOriginalInstance, startInstance, endInstance);
            if (! this.equals(vComponentOriginal)) { newInstances = updateInstances(instances); }
            break;
        case WITH_EXISTING_REPEAT:
            // Find which properties changed
            List<String> changedPropertyNames = findChangedProperties(vComponentOriginal);
            /* Note:
             * time properties must be checked separately because changes are stored in startInstance and endInstance,
             * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
             * question is answered. */
            changedPropertyNames.addAll(changedStartAndEndDateTime(startOriginalInstance, startInstance, endInstance));
            // determine if any changed properties warrant dialog
//            changedPropertyNames.stream().forEach(a -> System.out.println("changed property:" + a));
            boolean provideDialog = requiresChangeDialog(changedPropertyNames);
            if (changedPropertyNames.size() > 0) // if changes occurred
            {
                List<VComponent<I>> relatedVComponents = Arrays.asList(this); // TODO - support related components
                final ChangeDialogOption changeResponse;
                if (provideDialog)
                {
                    Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(this, startOriginalInstance);
                    changeResponse = dialogCallback.call(choices);
                } else
                {
                    changeResponse = ChangeDialogOption.ALL;
                }
                switch (changeResponse)
                {
                case ALL:
                    if (relatedVComponents.size() == 1)
                    {
                        adjustDateTime(startOriginalInstance, startInstance, endInstance);
                        if ((getRRule() != null) && (getRRule().recurrences().size() > 0))
                        {
                            getRRule().recurrences().forEach(v ->
                            {
                                Temporal newRecurreneId = adjustRecurrenceStart(v.getDateTimeRecurrence(), startOriginalInstance, startInstance, endInstance);
                                v.setDateTimeRecurrence(newRecurreneId);
                            });
                        }
                        newInstances = updateInstances(instances);
                    } else
                    {
                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
                    }
                    break;
                case CANCEL:
                    vComponentOriginal.copyTo(this); // return to original
                    return false;
                case THIS_AND_FUTURE:
                    newInstances = editThisAndFuture(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
                    break;
                case ONE:
                    newInstances = editOne(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
                    break;
                default:
                    break;
                }
            }
        }
        if (! isValid()) throw new RuntimeException(errorString());
        if (incrementSequence) { incrementSequence(); }
        if (newInstances != null)
        {
            instances.clear();
            instances.addAll(newInstances);
        }
        return true;
    }
    
    /** If startInstance isn't valid due to a RRULE change, change startInstance and
     * endInstance to closest valid values
     */
    void validateStartInstanceAndDTStart(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
    {
        // only done in subclasses
    }    
    
    /** returns list of date-time properties that have been edited (DTSTART) */
    protected Collection<String> changedStartAndEndDateTime(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
    {
        Collection<String> changedProperties = new ArrayList<>();
        if (! startOriginalInstance.equals(startInstance)) { changedProperties.add(VComponentPropertyOld.DATE_TIME_START.toString()); }
        return changedProperties;
    }
    /* Adjust DTSTART by instance start and end date-time */
    void adjustDateTime(Temporal startOriginalInstance
            , Temporal startInstance
            , Temporal endInstance)
    {
        Temporal newStart = adjustRecurrenceStart(getDateTimeStart(), startOriginalInstance, startInstance, endInstance);
        System.out.println("new DTSTART:" + newStart);
        setDateTimeStart(newStart);
    }
    
    /* Adjust DTSTART of RECURRENCE-ID */
    private static Temporal adjustRecurrenceStart(Temporal initialStart
            , Temporal startOriginalInstance
            , Temporal startInstance
            , Temporal endInstance)
    {
        DateTimeType newDateTimeType = DateTimeType.of(startInstance);
        ZoneId zone = (startInstance instanceof ZonedDateTime) ? ZoneId.from(startInstance) : null;
        Temporal startAdjusted = newDateTimeType.from(initialStart, zone);
        Temporal startOriginalInstanceAdjusted = newDateTimeType.from(startOriginalInstance, zone);

        // Calculate shift from startAdjusted to make new DTSTART
        final TemporalAmount startShift;
        if (newDateTimeType == DateTimeType.DATE)
        {
            startShift = Period.between(LocalDate.from(startOriginalInstance), LocalDate.from(startInstance));
        } else
        {
            startShift = Duration.between(startOriginalInstanceAdjusted, startInstance);
        }
        return startAdjusted.plus(startShift);
    }
    
    /**
     * Return true if ANY changed property requires a dialog, false otherwise
     * 
     * @param changedPropertyNames - list from {@link #findChangedProperties(VComponent)}
     * @return
     */
    boolean requiresChangeDialog(List<String> changedPropertyNames)
    {
        return changedPropertyNames.stream()
                .map(s ->  
                {
                    VComponentPropertyOld p = VComponentPropertyOld.propertyFromName(s);
                    return (p != null) ? p.isDialogRequired() : false;
                })
                .anyMatch(b -> b == true);
    }
    
    /**
     * Generates a list of iCalendar property names that have different values from the 
     * input parameter
     * 
     * equal checks are encapsulated inside the enum VComponentProperty
     */
    List<String> findChangedProperties(VComponent<I> vComponentOriginal)
    {
        List<String> changedProperties = new ArrayList<>();
        Arrays.stream(VComponentPropertyOld.values())
                .filter(p -> ! p.equals(VComponentPropertyOld.DATE_TIME_START)) // DATE_TIME_START change calculated in changedStartAndEndDateTime
                .forEach(p -> 
                {
                    boolean equals = p.isPropertyEqual(this, vComponentOriginal);
                    if (! equals)
                    {
                        changedProperties.add(p.toString());
                    }
                });        
        return changedProperties;
    }
    
    private Collection<I> updateInstances(Collection<I> instances)
    {
        Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> instances().stream().anyMatch(a2 -> a2 == a));
        instances().clear(); // clear VEvent of outdated appointments
        instancesTemp.addAll(makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
        return instancesTemp;
    }
    
    /**
     * Part of handleEdit.
     * Changes a VComponent with a RRULE to be an individual,
     * 
     * @param vComponentOriginal
     * @param startInstance
     * @param endInstance
     * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection, Callback)
     */
    protected void becomingIndividual(VComponent<I> vComponentOriginal, Temporal startInstance, Temporal endInstance)
    {
        setRRule(null);
        setRDate(null);
        setExDate(null);
        if (vComponentOriginal.getRRule() != null)
        { // RRULE was removed, update DTSTART
            setDateTimeStart(startInstance);
        }
    }
    
    /**
     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
     * 
     * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
     */
    protected Collection<I> editOne(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances)
    {
        // Remove RRule and set parent component
        setRRule(null);
        setParent(vComponentOriginal);

        // Apply dayShift, account for editing instance beyond first
        Period dayShift = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(startOriginalInstance));
        Temporal newStart = getDateTimeStart().plus(dayShift);
        setDateTimeStart(newStart);
        adjustDateTime(startOriginalInstance, startInstance, endInstance);
        setDateTimeRecurrence(startOriginalInstance);
        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
   
        // Add recurrence to original vEvent
        vComponentOriginal.getRRule().recurrences().add(this);
        
        // Check for validity
        if (! isValid()) { throw new RuntimeException(errorString()); }
//        System.out.println("here:" + vComponentOriginal);
        if (! vComponentOriginal.isValid()) { throw new RuntimeException(vComponentOriginal.errorString()); }
        
        // Remove old instances, add back ones
        Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to instances
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vComponentOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vComponentOriginal.instances().clear(); // clear vEventOriginal outdated collection of instances
        instancesTemp.addAll(vComponentOriginal.makeInstances()); // make new instances and add to main collection (added to vEventNew's collection in makeinstances)
        instances().clear(); // clear vEvent outdated collection of instances
        instancesTemp.addAll(makeInstances()); // add vEventOld part of new instances
        vComponents.add(vComponentOriginal);
        return instancesTemp;
    }
    
    /**
     * Changing this and future instances in VComponent is done by ending the previous
     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.  vEventNew has new settings, vEvent has former settings.
     * @param endInstance 
     * @param <T>
     * 
     * @see VComponent#handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
     */
    protected Collection<I> editThisAndFuture(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances)
    {
        // adjust original VEvent
        if (vComponentOriginal.getRRule().getCount() > 0)
        {
            vComponentOriginal.getRRule().setCount(0);
        }
        final Temporal untilNew;
        if (isWholeDay())
        {
            untilNew = LocalDate.from(startOriginalInstance).minus(1, ChronoUnit.DAYS);
        } else
        {
//            Temporal temporal = startOriginalInstance.minus(1, ChronoUnit.NANOS);
//            untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(temporal);
            untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(previousStreamValue(startInstance));
        }
        vComponentOriginal.getRRule().setUntil(untilNew);
        
        
        setDateTimeStart(startInstance);
        adjustDateTime(startInstance, startInstance, endInstance);
        setUniqueIdentifier();
        String relatedUID = (vComponentOriginal.getRelatedTo() == null) ? vComponentOriginal.getUniqueIdentifier() : vComponentOriginal.getRelatedTo();
        setRelatedTo(relatedUID);
        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        
        // Split EXDates dates between this and newVEvent
        if (getExDate() != null)
        {
            getExDate().getTemporals().clear();
            final Iterator<Temporal> exceptionIterator = getExDate().getTemporals().iterator();
            while (exceptionIterator.hasNext())
            {
                Temporal d = exceptionIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    exceptionIterator.remove();
                } else {
                    getExDate().getTemporals().add(d);
                }
            }
            if (getExDate().getTemporals().isEmpty()) setExDate(null);
            if (getExDate().getTemporals().isEmpty()) setExDate(null);
        }

        // Split recurrence date/times between this and newVEvent
        if (getRDate() != null)
        {
            getRDate().getTemporals().clear();
            final Iterator<Temporal> recurrenceIterator = getRDate().getTemporals().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    recurrenceIterator.remove();
                } else {
                    getRDate().getTemporals().add(d);
                }
            }
            if (getRDate().getTemporals().isEmpty()) setRDate(null);
            if (getRDate().getTemporals().isEmpty()) setRDate(null);
        }

        // Split instance dates between this and newVEvent
        if (getRRule().recurrences() != null)
        {
            getRRule().recurrences().clear();
            final Iterator<VComponent<?>> recurrenceIterator = getRRule().recurrences().iterator();
            while (recurrenceIterator.hasNext())
            {
                VComponent<?> d = recurrenceIterator.next();
                if (DateTimeUtilities.isBefore(d.getDateTimeRecurrence(), startInstance))
                {
                    recurrenceIterator.remove();
                } else {
                    getRRule().recurrences().add(d);
                }
            }
        }
        
        // Modify COUNT for the edited vEvent
        if (getRRule().getCount() > 0)
        {
            int countInOrginal = vComponentOriginal.makeInstances().size();
            int countInNew = getRRule().getCount() - countInOrginal;
            getRRule().setCount(countInNew);
        }
//        adjustDateTime(startOriginalInstance, startInstance, endInstance);        
        
        if (! vComponentOriginal.isValid()) throw new RuntimeException(vComponentOriginal.errorString());
        vComponents.add(vComponentOriginal);

        // Remove old appointments, add back ones
        Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vComponentOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vComponentOriginal.instances().clear(); // clear vEvent outdated collection of appointments
        instancesTemp.addAll(vComponentOriginal.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
        instances().clear(); // clear vEvent's outdated collection of appointments
        instancesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
        return instancesTemp;
//        instances.clear();
//        instances.addAll(instancesTemp);
    }
     
    
    @Override
    public void handleDelete(
            Collection<VComponent<I>> vComponents
          , Temporal startInstance
          , I instance
          , Collection<I> instances
          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
    {
        int count = this.instances().size();
        if (count == 1)
        {
            vComponents.remove(this);
            instances.remove(instance);
        } else // more than one instance
        {
            //  TODO - COMBINE makeDialogChoices INTO dialogCallback
            Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(this, startInstance);
//            Map<ChangeDialogOption, String> choices = makeDialogChoices(startInstance);
            ChangeDialogOption changeResponse = dialogCallback.call(choices);
            switch (changeResponse)
            {
            case ALL:
                List<VComponent<?>> relatedVComponents = new ArrayList<>();
                if (this.getDateTimeRecurrence() == null)
                { // is parent
                    relatedVComponents.addAll(this.getRRule().recurrences());
                    relatedVComponents.add(this);
                } else
                { // is child (recurrence).  Find parent delete all children
                    relatedVComponents.addAll(this.getParent().getRRule().recurrences());
                    relatedVComponents.add(this.getParent());
                }
                relatedVComponents.stream().forEach(v -> vComponents.remove(v));
                vComponents.removeAll(relatedVComponents);
                List<?> appointmentsToRemove = relatedVComponents.stream()
                        .flatMap(v -> v.instances().stream())
                        .collect(Collectors.toList());
                instances.removeAll(appointmentsToRemove);
                break;
            case CANCEL:
                break;
            case ONE:
                if (getExDate() == null) { setExDate(new ExDate(startInstance)); }
                else { getExDate().getTemporals().add(startInstance); }
                instances.removeIf(a -> a.equals(instance));
                break;
            case THIS_AND_FUTURE:
                if (getRRule().getCount() != 0) { getRRule().setCount(0); }
//                Temporal previousDay = startInstance.minus(1, ChronoUnit.DAYS);
                final Temporal untilNew;
                if (isWholeDay())
                {
                    untilNew = LocalDate.from(startInstance).minus(1, ChronoUnit.DAYS);
                } else
                {
                    // UNTIL IS INCLUSIVE - FIND PREVIOUS INSTANCE DATE-TIME - CONVERT TO UTC
                    untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(previousStreamValue(startInstance));
                    System.out.println("start instance:" + startInstance + " " + untilNew);
//                    Temporal temporal = startInstance.minus(1, ChronoUnit.NANOS);
//                    untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(temporal);
                }
                getRRule().setUntil(untilNew);

                // Remove old appointments, add back ones
                Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
                instancesTemp.addAll(instances);
                instancesTemp.removeIf(a -> instances().stream().anyMatch(a2 -> a2 == a));
                instances().clear(); // clear this's outdated collection of appointments
                instancesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
                instances.clear();
                instances.addAll(instancesTemp);
                break;
            default:
                break;
            }
        }
    }
    
    /** Deep copy all fields from source to destination 
     * @param <J>*/
    private static <J> void copy(VComponentDisplayableOldBase<J,?> source, VComponentDisplayableOldBase<J,?> destination)
    {
        Arrays.stream(VComponentPropertyOld.values())
        .forEach(p ->
        {
            p.copyProperty(source, destination);
        });
        
        if (source.getStartRange() != null)
        {
            destination.setStartRange(source.getStartRange());
        }
        if (source.getEndRange() != null)
        {
            destination.setEndRange(source.getEndRange());
        }
        source.instances().stream().forEach(a -> destination.instances().add(a));
        destination.setUidGeneratorCallback(source.getUidGeneratorCallback());
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent<I> destination)
    {
        copy(this, (VComponentDisplayableOldBase<I,?>) destination);
    }

    /**
     * Needed by toString in subclasses.
     *  
     * Make list of properties and string values for toString method in subclasses (like VEvent)
     * Used by toString method in subclasses */
    List<String> makeContentLines()
    {
        List<String> properties = new ArrayList<>();
        Arrays.stream(VComponentPropertyOld.values())
                .forEach(p ->
                {
                    String newLine = p.toPropertyString(this);
                    if (newLine != null)
                    {
                        properties.add(newLine);
                    }
                });
        return properties;
    }
    
    // Variables for start date or date/time cache used as starting Temporal for stream
    private static final int CACHE_RANGE = 51; // number of values in cache
    private static final int CACHE_SKIP = 21; // store every nth value in the cache
    private int skipCounter = 0; // counter that increments up to CACHE_SKIP, indicates time to record a value, then resets to 0
    private Temporal[] temporalCache; // the start date or date/time cache
    private Temporal dateTimeStartLast; // last dateTimeStart, when changes indicates clearing the cache is necessary
    private RecurrenceRule rRuleLast; // last rRule, when changes indicates clearing the cache is necessary
    private int cacheStart = 0; // start index where cache values are stored (starts in middle)
    private int cacheEnd = 0; // end index where cache values are stored

    /**
     * finds previous stream Temporal before input parameter value
     * 
     * @param value
     * @return
     */
    public Temporal previousStreamValue(Temporal value)
    {
        final Temporal start; 
        if (cacheEnd == 0)
        {
            start = getDateTimeStart();
        } else
        { // try to get start from cache
            Temporal m  = null;
            for (int i=cacheEnd; i>cacheStart; i--)
            {
                if (DateTimeUtilities.isBefore(temporalCache[i], value))
                {
                    m = temporalCache[i];
                    break;
                }
            }
            start = (m != null) ? m : getDateTimeStart();
        }
        Iterator<Temporal> i = streamNoCache(start).iterator();
        Temporal lastT = null;
        while (i.hasNext())
        {
            Temporal t = i.next();
            if (! DateTimeUtilities.isBefore(t, value)) break;
            lastT = t;
        }
        return lastT;
    }
    
    @Override
    public Stream<Temporal> stream(Temporal start)
    {
        // adjust start to ensure its not before dateTimeStart
        final Temporal start2 = (DateTimeUtilities.isBefore(start, getDateTimeStart())) ? getDateTimeStart() : start;
        final Stream<Temporal> stream1; // individual or rrule stream
        final Temporal earliestCacheValue;
        final Temporal latestCacheValue;
        
        if (getRRule() == null)
        { // if individual event
            stream1 = Arrays.asList(getDateTimeStart())
                    .stream()
                    .filter(d -> ! DateTimeUtilities.isBefore(d, start2));
            earliestCacheValue = null;
            latestCacheValue = null;
        } else
        {
            // check if cache needs to be cleared (changes to RRULE or DTSTART)
            if ((dateTimeStartLast != null) && (rRuleLast != null))
            {
                boolean startChanged = ! getDateTimeStart().equals(dateTimeStartLast);
                boolean rRuleChanged = ! getRRule().equals(rRuleLast);
                if (startChanged || rRuleChanged)
                {
                    temporalCache = null;
                    cacheStart = 0;
                    cacheEnd = 0;
                    skipCounter = 0;
                    dateTimeStartLast = getDateTimeStart();
                    rRuleLast = getRRule();
                }
            } else
            { // save current DTSTART and RRULE for next time
                dateTimeStartLast = getDateTimeStart();
                rRuleLast = getRRule();
            }
            
            final Temporal match;
            
            // use cache if available to find matching start date or date/time
            if (temporalCache != null)
            {
                // Reorder cache to maintain centered and sorted
                final int len = temporalCache.length;
                final Temporal[] p1;
                final Temporal[] p2;
                if (cacheEnd < cacheStart) // high values have wrapped from end to beginning
                {
                    p1 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                    p2 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
                } else if (cacheEnd > cacheStart) // low values have wrapped from beginning to end
                {
                    p2 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                    p1 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
                } else
                {
                    p1 = null;
                    p2 = null;
                }
                if (p1 != null)
                { // copy elements to accommodate wrap and restore sort order
                    int p1Index = 0;
                    int p2Index = 0;
                    for (int i=0; i<len; i++)
                    {
                        if (p1Index < p1.length)
                        {
                            temporalCache[i] = p1[p1Index];
                            p1Index++;
                        } else if (p2Index < p2.length)
                        {
                            temporalCache[i] = p2[p2Index];
                            p2Index++;
                        } else
                        {
                            cacheEnd = i;
                            break;
                        }
                    }
                }
    
                // Find match in cache
                latestCacheValue = temporalCache[cacheEnd];
                if ((! DateTimeUtilities.isBefore(start2, temporalCache[cacheStart])))
                {
                    Temporal m = latestCacheValue;
                    for (int i=cacheStart; i<cacheEnd+1; i++)
                    {
                        if (DateTimeUtilities.isAfter(temporalCache[i], start2))
                        {
                            m = temporalCache[i-1];
                            break;
                        }
                    }
                    match = m;
                } else
                { // all cached values too late - start over
                    cacheStart = 0;
                    cacheEnd = 0;
                    temporalCache[cacheStart] = getDateTimeStart();
                    match = getDateTimeStart();
                }
                earliestCacheValue = temporalCache[cacheStart];
            } else
            { // no previous cache.  initialize new array with dateTimeStart as first value.
                temporalCache = new Temporal[CACHE_RANGE];
                temporalCache[cacheStart] = getDateTimeStart();
                match = getDateTimeStart();
                earliestCacheValue = getDateTimeStart();
                latestCacheValue = getDateTimeStart();
            }
            stream1 = getRRule().streamRecurrence(match);
        }
        
        Stream<Temporal> stream2 = (getRDate() == null) ? stream1 : getRDate().stream(stream1, start2); // add recurrence list
        Stream<Temporal> stream3 = (getExDate() == null) ? stream2 : getExDate().stream(stream2, start2); // remove exceptions
        Stream<Temporal> stream4 = stream3
                .peek(t ->
                { // save new values in cache
                    if (getRRule() != null)
                    {
                        if (DateTimeUtilities.isBefore(t, earliestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheStart--;
                                if (cacheStart < 0) cacheStart = CACHE_RANGE - 1;
                                if (cacheStart == cacheEnd) cacheEnd--; // just overwrote oldest value - push cacheEnd down
                                temporalCache[cacheStart] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        if (DateTimeUtilities.isAfter(t, latestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheEnd++;
                                if (cacheEnd == CACHE_RANGE) cacheEnd = 0;
                                if (cacheStart == cacheEnd) cacheStart++; // just overwrote oldest value - push cacheStart up
                                temporalCache[cacheEnd] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        // check if start or end needs to wrap
                        if (cacheEnd < 0) cacheEnd = CACHE_RANGE - 1;
                        if (cacheStart == CACHE_RANGE) cacheStart = 0;
                    }
                })
                .filter(t -> ! DateTimeUtilities.isBefore(t, start2)); // remove too early events;

        return stream4;
    }

    /** Stream of date/times that indicate the start of the event(s).
     * For a VEvent without RRULE the stream will contain only one date/time element.
     * A VEvent with a RRULE the stream contains more than one date/time element.  It will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. 
     * 
     * @param start - starting date or date/time for which occurrence start date or date/time
     * are generated by the returned stream
     * @return stream of starting dates or date/times for occurrences after rangeStart
     */
    private Stream<Temporal> streamNoCache(Temporal start)
    {
        final Stream<Temporal> stream1;
        if (getRRule() == null)
        { // if individual event
            stream1 = Arrays.asList(getDateTimeStart())
                    .stream()
                    .filter(d -> ! DateTimeUtilities.isBefore(d, start));
        } else
        { // if has recurrence rule
            stream1 = getRRule().streamRecurrence(getDateTimeStart());
        }
        Stream<Temporal> stream2 = (getRDate() == null) ? stream1 : getRDate().stream(stream1, start); // add recurrence list
        Stream<Temporal> stream3 = (getExDate() == null) ? stream2 : getExDate().stream(stream2, start); // remove exceptions
        return stream3.filter(t -> ! DateTimeUtilities.isBefore(t, start));
    }
    
    private enum RRuleStatus
    {
        INDIVIDUAL ,
        WITH_EXISTING_REPEAT ,
        WITH_NEW_REPEAT, 
        HAD_REPEAT_BECOMING_INDIVIDUAL;
      
        public static RRuleStatus getRRuleType(RecurrenceRule rruleNew, RecurrenceRule rruleOld)
        {
            if (rruleNew == null)
            {
                if (rruleOld == null)
                { // doesn't have repeat or have old repeat either
                    return RRuleStatus.INDIVIDUAL;
                } else {
                    return RRuleStatus.HAD_REPEAT_BECOMING_INDIVIDUAL;
                }
            } else
            { // RRule != null
                if (rruleOld == null)
                {
                    return RRuleStatus.WITH_NEW_REPEAT;                
                } else
                {
                    return RRuleStatus.WITH_EXISTING_REPEAT;
                }
            }
        }
    }
}
