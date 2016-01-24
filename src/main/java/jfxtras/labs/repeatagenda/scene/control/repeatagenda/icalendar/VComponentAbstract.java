package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;

/**
 * Top-level calendar component in iCalendar as defined in section 3.6 of RFC 5545 for 
 * an event (VEvent), a to-do (VTodo) or a journal entry (VJournal).
 * The three other components defined in section 3.6 are time zone
 * information, free/busy time information, and alarm are not subclasses of VComponent
 * and are not implemented here.
 * 
 * The implementation status of the following calendar components is below.
 * VEvent - yes
 * VTodo - no
 * VJournal - no
 *
 * The implementation status of component properties:
       3.8.1.  Descriptive Component Properties  . . . . . . . . . .  81
         3.8.1.1.  Attachment  . . . . . . . . . . . . . . . . . . .  81 - NO
         3.8.1.2.  Categories  . . . . . . . . . . . . . . . . . . .  82 - Yes
         3.8.1.3.  Classification  . . . . . . . . . . . . . . . . .  83 - NO
         3.8.1.4.  Comment . . . . . . . . . . . . . . . . . . . . .  84 - Yes
         3.8.1.5.  Description . . . . . . . . . . . . . . . . . . .  85 - Yes (in VEvent)
         3.8.1.6.  Geographic Position . . . . . . . . . . . . . . .  87 - NO
         3.8.1.7.  Location  . . . . . . . . . . . . . . . . . . . .  88 - Yes (in VEvent)
         3.8.1.8.  Percent Complete  . . . . . . . . . . . . . . . .  89 - NO
         3.8.1.9.  Priority  . . . . . . . . . . . . . . . . . . . .  90 - NO
         3.8.1.10. Resources . . . . . . . . . . . . . . . . . . . .  92 - NO
         3.8.1.11. Status  . . . . . . . . . . . . . . . . . . . . .  93 - NO
         3.8.1.12. Summary . . . . . . . . . . . . . . . . . . . . .  94 - Yes
       3.8.2.  Date and Time Component Properties  . . . . . . . . .  95
         3.8.2.1.  Date-Time Completed . . . . . . . . . . . . . . .  95 - NO
         3.8.2.2.  Date-Time End . . . . . . . . . . . . . . . . . .  96 - Yes
         3.8.2.3.  Date-Time Due . . . . . . . . . . . . . . . . . .  97 - NO
         3.8.2.4.  Date-Time Start . . . . . . . . . . . . . . . . .  99 - Yes
         3.8.2.5.  Duration  . . . . . . . . . . . . . . . . . . . . 100 - Yes
         3.8.2.6.  Free/Busy Time  . . . . . . . . . . . . . . . . . 101 - NO
         3.8.2.7.  Time Transparency . . . . . . . . . . . . . . . . 102 - NO
       3.8.3.  Time Zone Component Properties  . . . . . . . . . . . 103 - NO
         3.8.3.1.  Time Zone Identifier  . . . . . . . . . . . . . . 103 - NO
         3.8.3.2.  Time Zone Name  . . . . . . . . . . . . . . . . . 105 - NO
         3.8.3.3.  Time Zone Offset From . . . . . . . . . . . . . . 106 - NO
         3.8.3.4.  Time Zone Offset To . . . . . . . . . . . . . . . 106 - NO
         3.8.3.5.  Time Zone URL . . . . . . . . . . . . . . . . . . 107 - NO
       3.8.4.  Relationship Component Properties . . . . . . . . . . 108
         3.8.4.1.  Attendee  . . . . . . . . . . . . . . . . . . . . 108 - NO
         3.8.4.2.  Contact . . . . . . . . . . . . . . . . . . . . . 111 - NO
         3.8.4.3.  Organizer . . . . . . . . . . . . . . . . . . . . 113 - NO
         3.8.4.4.  Recurrence ID . . . . . . . . . . . . . . . . . . 114 - Yes
         3.8.4.5.  Related To  . . . . . . . . . . . . . . . . . . . 117 - Yes
         3.8.4.6.  Uniform Resource Locator  . . . . . . . . . . . . 118 - NO
         3.8.4.7.  Unique Identifier . . . . . . . . . . . . . . . . 119 - Yes
       3.8.5.  Recurrence Component Properties . . . . . . . . . . . 120
         3.8.5.1.  Exception Date-Times  . . . . . . . . . . . . . . 120 - Yes
         3.8.5.2.  Recurrence Date-Times . . . . . . . . . . . . . . 122 - Yes
         3.8.5.3.  Recurrence Rule . . . . . . . . . . . . . . . . . 124 - Yes, in RRule class
       3.8.6.  Alarm Component Properties  . . . . . . . . . . . . . 134
         3.8.6.1.  Action  . . . . . . . . . . . . . . . . . . . . . 134 - NO
         3.8.6.2.  Repeat Count  . . . . . . . . . . . . . . . . . . 135 - NO
         3.8.6.3.  Trigger . . . . . . . . . . . . . . . . . . . . . 135 - NO
       3.8.7.  Change Management Component Properties  . . . . . . . 138
         3.8.7.1.  Date-Time Created . . . . . . . . . . . . . . . . 138 - Yes
         3.8.7.2.  Date-Time Stamp . . . . . . . . . . . . . . . . . 139 - Yes
         3.8.7.3.  Last Modified . . . . . . . . . . . . . . . . . . 140 - Yes
         3.8.7.4.  Sequence Number . . . . . . . . . . . . . . . . . 141 - Yes
       3.8.8.  Miscellaneous Component Properties  . . . . . . . . . 142
         3.8.8.1.  IANA Properties . . . . . . . . . . . . . . . . . 142 - NO
         3.8.8.2.  Non-Standard Properties . . . . . . . . . . . . . 142 - can't be implemented here.  must be in implementing class
         3.8.8.3.  Request Status  . . . . . . . . . . . . . . . . . 144 - NO
         
Alphabetical list of elements for VComponent (some not implemented)
ATTACH - not implemented
ATTENDEE - not implemented
CATEGORIES - yes
CLASS - not implemented
COMMENT - yes
CONTACT - not implemented
CREATED - yes
DTSTAMP - yes
DTSTART - yes
EXDATE - yes
IANA-PROP - not implemented
LAST-MOD - yes
ORGANIZER - not implemented
RDATE - yes
RECURRENCE-ID - yes
RELATED-TO - yes
RESOURCES - not implemented
RRULE - yes
RSTATUS - not implemented
SEQ - Yes
STATUS - not implemented
SUMMARY - yes
UID - yes
URL - not implemented
X-PROP - can't be implemented here.  must be in implementing class

Limitations: COMMENT, EXDATE, RDATE can only exist once per calendar component.  According
to iCalendar a number of properties, including those three, can exist more than once.  Fixing
this limitation is a future goal. - I plan on fixing this problem by combining multiple
instances into one property internally.

 * 
 * @author David Bal
 * @param <U>
 * @see VEvent
 *
 */
public abstract class VComponentAbstract<T> implements VComponent<T>
{
    private static final String CATEGORIES_NAME = "CATEGORIES";
    private static final String COMMENT_NAME = "COMMENT";
    private static final String CREATED_NAME = "CREATED";
    private static final String DATE_TIME_STAMP_NAME = "DTSTAMP";
    private static final String DATE_TIME_START_NAME = "DTSTART";
    private static final String EXCEPTION_DATE_TIMES_NAME = "EXDATE";
    private static final String LAST_MODIFIED_NAME = "LAST-MODIFIED";
    private static final String LOCATION_NAME = "LOCATION";
    private static final String RECURRENCE_DATE_TIMES_NAME = "RDATE";
    private static final String RECURRENCE_ID_NAME = "RECURRENCE-ID";
    private static final String RECURRENCE_RULE_NAME = "RRULE";
    private static final String RELATED_TO_NAME = "RELATED-TO";
    private static final String SEQUENCE_NAME = "SEQUENCE";
    private static final String SUMMARY_NAME = "SUMMARY";
    private static final String UNIQUE_IDENTIFIER_NAME = "UID";
    
//    public String name() { return null; } // not called for abstract class

    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    public StringProperty categoriesProperty() { return categoriesProperty; }
    final private StringProperty categoriesProperty = new SimpleStringProperty(this, CATEGORIES_NAME);
    public String getCategories() { return categoriesProperty.get(); }
    public void setCategories(String value) { categoriesProperty.set(value); }
//    public T withCategories(String value) { setCategories(value); return (T)this; }
    
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
    public StringProperty commentProperty()
    {
        if (comment == null) comment = new SimpleStringProperty(this, COMMENT_NAME, _comment);
        return comment;
    }
    private StringProperty comment;
    private String _comment;
    public String getComment() { return (comment == null) ? _comment : comment.get(); }
    public void setComment(String comment)
    {
        if (this.comment == null)
        {
            _comment = comment;
        } else
        {
            this.comment.set(comment);            
        }
    }
//    public VEvent withComment(String value) { setComment(value); return this; }

    /**
     * CREATED: Date-Time Created, from RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     */
    public ObjectProperty<LocalDateTime> dateTimeCreatedProperty() { return dateTimeCreated; }
    final private ObjectProperty<LocalDateTime> dateTimeCreated = new SimpleObjectProperty<>(this, CREATED_NAME);
    public LocalDateTime getDateTimeCreated() { return dateTimeCreated.get(); }
    public void setDateTimeCreated(LocalDateTime dtCreated) { this.dateTimeCreated.set(dtCreated); }
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    public ObjectProperty<LocalDateTime> dateTimeStampProperty() { return dateTimeStamp; }
    final private ObjectProperty<LocalDateTime> dateTimeStamp = new SimpleObjectProperty<>(this, DATE_TIME_STAMP_NAME);
    public LocalDateTime getDateTimeStamp() { return dateTimeStamp.get(); }
    public void setDateTimeStamp(LocalDateTime dtStamp) { this.dateTimeStamp.set(dtStamp); }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     * @SEE VDateTime
     */
    public ObjectProperty<Temporal> dateTimeStartProperty() { return dateTimeStart; }
    final private ObjectProperty<Temporal> dateTimeStart = new SimpleObjectProperty<>(this, DATE_TIME_START_NAME);
    @Override public Temporal getDateTimeStart() { return dateTimeStart.get(); }
    @Override public void setDateTimeStart(Temporal dtStart) { VComponent.super.setDateTimeStart(dtStart); dateTimeStart.set(dtStart); }
    boolean isDateTimeStartWholeDay() { return dateTimeStart.get() instanceof LocalDate; }
    
    /**
     * EXDATE: Set of date/times exceptions for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Is rarely used, so employs lazy initialization.
     */
    public ObjectProperty<EXDate> exDateProperty()
    {
        if (exDate == null) exDate = new SimpleObjectProperty<>(this, EXCEPTION_DATE_TIMES_NAME, _exDate);
        return exDate;
    }
    private ObjectProperty<EXDate> exDate;
    private EXDate _exDate;
    public EXDate getExDate() { return (exDate == null) ? _exDate : exDate.getValue(); }
    public void setExDate(EXDate exDate)
    {
        if (this.exDate == null)
        {
            _exDate = exDate;
        } else
        {
            this.exDate.set(exDate);
        }
    }

    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     */
    public ObjectProperty<LocalDateTime> dateTimeLastModifiedProperty() { return dateTimeLastModified; }
    final private ObjectProperty<LocalDateTime> dateTimeLastModified = new SimpleObjectProperty<LocalDateTime>(this, LAST_MODIFIED_NAME);
    public LocalDateTime getDateTimeLastModified() { return dateTimeLastModified.getValue(); }
    public void setDateTimeLastModified(LocalDateTime dtLastModified) { this.dateTimeLastModified.set(dtLastModified); }
    
    /**
     * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public StringProperty locationProperty(){ return locationProperty; }
    final private StringProperty locationProperty = new SimpleStringProperty(this, LOCATION_NAME);
    public String getLocation() { return locationProperty.getValue(); }
    public void setLocation(String value) { locationProperty.setValue(value); }
//    public T withLocation(String value) { setLocation(value); return (T)this; }

    /**
     * RDATE: Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
    */
    // Is rarely used, so employs lazy initialization.
    public ObjectProperty<RDate> rDateProperty()
    {
        if (rDate == null) rDate = new SimpleObjectProperty<RDate>(this, RECURRENCE_DATE_TIMES_NAME, _rDate);
        return rDate;
    }
    private ObjectProperty<RDate> rDate;
    private RDate _rDate;
    public RDate getRDate() { return (rDate == null) ? _rDate : rDate.getValue(); }
    public void setRDate(RDate rDate)
    {
        if (this.rDate == null)
        {
            _rDate = rDate;
        } else
        {
            this.rDate.set(rDate);
        }
    }

    public StringProperty relatedToProperty() { return relatedTo; }
    final private StringProperty relatedTo = new SimpleStringProperty(this, RELATED_TO_NAME);
    public String getRelatedTo() { return relatedTo.getValue(); }
    public void setRelatedTo(String s) { relatedTo.set(s); }
    
    /**
     * Use Google UID extension instead of RELATED-TO to express 
     */
    public boolean isGoogleRecurrenceUID() { return googleRecurrenceUID; };
    private boolean googleRecurrenceUID = false; // default to not using Google system
    public void setGoogleRecurrenceUID(boolean b) { googleRecurrenceUID = b; };
    
    /**
     * RECURRENCE-ID: Date-Time recurrence, from RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance.
     */
    public ObjectProperty<Temporal> dateTimeRecurrenceProperty()
    {
        if (dateTimeRecurrence == null) dateTimeRecurrence = new SimpleObjectProperty<>(this, RECURRENCE_ID_NAME, _dateTimeRecurrence);
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
        if (dateTimeRecurrence == null)
        {
            _dateTimeRecurrence = dtRecurrence;
        } else
        {
            dateTimeRecurrence.set(dtRecurrence);
        }
    }
    
    /**
     * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
     * If event is not repeating value is null
     */
    public ObjectProperty<RRule> rRuleProperty()
    {
        if (rRule == null) rRule = new SimpleObjectProperty<RRule>(this, RECURRENCE_RULE_NAME, _rRule);
        return rRule;
    }
    private ObjectProperty<RRule> rRule;
    private RRule _rRule;
    public RRule getRRule() { return (rRule == null) ? _rRule : rRule.get(); }
    public void setRRule(RRule rRule)
    {
        if (this.rRule == null)
        {
            _rRule = rRule;
        } else
        {
            this.rRule.set(rRule);
        }
    }
    
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
    public IntegerProperty sequenceProperty() { return sequenceProperty; }
    final private IntegerProperty sequenceProperty = new SimpleIntegerProperty(this, SEQUENCE_NAME, 0);
    public int getSequence() { return sequenceProperty.get(); }
    public void setSequence(int value)
    {
        if (value < 0) throw new IllegalArgumentException("Sequence value must be greater than zero");
        if (value < getSequence()) throw new IllegalArgumentException("New sequence value must be greater than previous value");
        sequenceProperty.set(value);
    }
    
    /**
     *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    public StringProperty summaryProperty() { return summaryProperty; }
    final private StringProperty summaryProperty = new SimpleStringProperty(this, SUMMARY_NAME);
    public String getSummary() { return summaryProperty.get(); }
    public void setSummary(String value) { summaryProperty.set(value); }
//    public T withSummary(String value) { setSummary(value); return (T)this; } 
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     */
    public StringProperty uniqueIdentifierProperty() { return uniqueIdentifier; }
    final private StringProperty uniqueIdentifier = new SimpleStringProperty(this, UNIQUE_IDENTIFIER_NAME);
    public String getUniqueIdentifier() { return uniqueIdentifier.getValue(); }
    public void setUniqueIdentifier(String s) { uniqueIdentifier.set(s); }
    /** Set uniqueIdentifier by calling uidGeneratorCallback */
    public void setUniqueIdentifier() { setUniqueIdentifier(getUidGeneratorCallback().call(null)); } 
//    public T withUniqueIdentifier(String uid) { setUniqueIdentifier(uid); return (T)this; }
    
    /** Callback for creating unique uid values  */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
    /** Method to convert DTSTART or DTEND to LocalDateTime
     * Currently ignores time zones */
    public LocalDateTime iCalendarDateTimeToLocalDateTime(String dt)
    {
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher m = p.matcher(dt);
        List<String> tokens = new ArrayList<String>();
        while (m.find())
        {
            String token = m.group(0);
            tokens.add(token);
        }
        LocalDate date;
        if (tokens.size() > 0)
        {
            String dateToken = tokens.get(0);
            date = LocalDate.parse(dateToken, VComponent.DATE_FORMATTER);
        } else throw new DateTimeException("Invalid Date-Time string: " + dt);           
        if (tokens.size() == 2)
        { // find date if another token is available
            String timeToken = tokens.get(1);
            LocalTime time = LocalTime.parse(timeToken, VComponent.TIME_FORMATTER);
            return LocalDateTime.of(date, time);
        }
        return date.atStartOfDay();
    }

    // CONSTRUCTORS
    /** Copy constructor */
    public VComponentAbstract(VComponentAbstract<T> vcomponent)
    {
        copy(vcomponent, this);
    }
    
    public VComponentAbstract()
    {
    }

    /** Deep copy all fields from source to destination */
    private static void copy(VComponentAbstract<?> source, VComponentAbstract<?> destination)
    {
        destination.setCategories(source.getCategories());
        destination.setComment(source.getComment());
        destination.setDateTimeStamp(source.getDateTimeStamp());
        destination.setDateTimeStart(source.getDateTimeStart());
        destination.setLocation(source.getLocation());
        destination.setRelatedTo(source.getRelatedTo());
        destination.setSequence(source.getSequence());
        destination.setSummary(source.getSummary());
        destination.setUniqueIdentifier(source.getUniqueIdentifier());
        if (source.getRRule() != null)
        {
            if (destination.getRRule() == null)
            { // make new RRule object for destination if necessary
                try {
                    RRule newRRule = source.getRRule().getClass().newInstance();
                    destination.setRRule(newRRule);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getRRule().copyTo(destination.getRRule());
        }
        if (source.getExDate() != null)
        {
            if (destination.getExDate() == null)
            { // make new EXDate object for destination if necessary
                try {
                    EXDate newEXDate = source.getExDate().getClass().newInstance();
                    destination.setExDate(newEXDate);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getExDate().copyTo(destination.getExDate());
        }
        if (source.getRDate() != null)
        {
            if (destination.getRDate() == null)
            { // make new RDate object for destination if necessary
                try {
                    RDate newRDate = source.getRDate().getClass().newInstance();
                    destination.setRDate(newRDate);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            source.getRDate().copyTo(destination.getRDate());
        }
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent<T> destination)
    {
        copy(this, (VComponentAbstract<T>) destination);
    }

    @Override
    public boolean equals(Object obj)
    {
        @SuppressWarnings("unchecked")
        VComponentAbstract<T> testObj = (VComponentAbstract<T>) obj;

        boolean categoriesEquals = (getCategories() == null) ?
                (testObj.getCategories() == null) : getCategories().equals(testObj.getCategories());
        boolean commentEquals = (getComment() == null) ?
                (testObj.getComment() == null) : getComment().equals(testObj.getComment());
        boolean dateTimeStampEquals = (getDateTimeStamp() == null) ?
                (testObj.getDateTimeStamp() == null) : getDateTimeStamp().equals(testObj.getDateTimeStamp());
        boolean dateTimeStartEquals = (getDateTimeStart() == null) ?
                (testObj.getDateTimeStart() == null) : getDateTimeStart().equals(testObj.getDateTimeStart());
        boolean locationEquals = (getLocation() == null) ?
                (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
        boolean sequenceEquals = getSequence() == testObj.getSequence();
        boolean summaryEquals = (getSummary() == null) ?
                (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
        boolean uniqueIdentifierEquals = (getUniqueIdentifier() == null) ?
                (testObj.getUniqueIdentifier() == null) : getUniqueIdentifier().equals(testObj.getUniqueIdentifier());
        boolean relatedToEquals = (getRelatedTo() == null) ?
                (testObj.getRelatedTo() == null) : getRelatedTo().equals(testObj.getRelatedTo());
        boolean rruleEquals = (getRRule() == null) ?
                (testObj.getRRule() == null) : getRRule().equals(testObj.getRRule());
        boolean eXDatesEquals = (getExDate() == null) ?
                (testObj.getExDate() == null) : getExDate().equals(testObj.getExDate());
        boolean rDatesEquals = (getRDate() == null) ?
                (testObj.getRDate() == null) : getRDate().equals(testObj.getRDate());
        System.out.println("Vcomponent equals: " + categoriesEquals + " " + commentEquals + " " + dateTimeStampEquals + " " + dateTimeStartEquals + " " 
                + locationEquals + " " + summaryEquals + " " + uniqueIdentifierEquals + " " + rruleEquals + " " + eXDatesEquals + " " + rDatesEquals);
        return categoriesEquals && commentEquals && dateTimeStampEquals && dateTimeStartEquals && locationEquals
                && summaryEquals && uniqueIdentifierEquals && rruleEquals && eXDatesEquals && rDatesEquals && relatedToEquals
                && sequenceEquals;
    }

    /** Make map of properties and string values for toString method in subclasses (like VEvent)
     * Used by toString method in subclasses */
    @SuppressWarnings("rawtypes")
    Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();

        if (getCategories() != null) properties.put(categoriesProperty(), getCategories().toString());
        if (getComment() != null) properties.put(commentProperty(), getComment().toString());
        if (getDateTimeCreated() != null) properties.put(dateTimeCreatedProperty(), VComponent.DATE_TIME_FORMATTER.format(getDateTimeCreated()));
        if (getDateTimeStamp() != null) properties.put(dateTimeStampProperty(), VComponent.DATE_TIME_FORMATTER.format(getDateTimeStamp())); // required property
        if (getDateTimeRecurrence() != null) properties.put(dateTimeRecurrenceProperty(), VComponent.DATE_TIME_FORMATTER.format(getDateTimeRecurrence()));
        String startPrefix = (getDateTimeStart() instanceof LocalDate) ? "VALUE=DATE:" : "";
        if (getDateTimeStart() != null) properties.put(dateTimeStartProperty(), startPrefix + VComponent.temporalToString(getDateTimeStart()));
        if (getDateTimeLastModified() != null) properties.put(dateTimeLastModifiedProperty(), VComponent.DATE_TIME_FORMATTER.format(getDateTimeLastModified()));
        if (getExDate() != null) properties.put(exDateProperty(), getExDate().toString());
        if (getLocation() != null) properties.put(locationProperty(), getLocation().toString());
        if (getRelatedTo() != null) properties.put(relatedToProperty(), getRelatedTo().toString());
        if (getRDate() != null) properties.put(rDateProperty(), getRDate().toString());
        if (getRRule() != null) properties.put(rRuleProperty(), getRRule().toString());
        if (getSequence() != 0) properties.put(sequenceProperty(), Integer.toString(getSequence()));
        if (getSummary() != null) properties.put(summaryProperty(), getSummary().toString());
        if (getUniqueIdentifier() != null) properties.put(uniqueIdentifierProperty(), getUniqueIdentifier()); // required property
        return properties;
    }
    
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid.
     */
    public String makeErrorString()
    {
        StringBuilder errorsBuilder = new StringBuilder();
        if (getDateTimeStart() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTART must not be null.");
        if (getDateTimeStamp() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTAMP must not be null.");
        if (getUniqueIdentifier() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  UID must not be null.");
        if (getRRule() != null) errorsBuilder.append(getRRule().makeErrorString(this));
        Temporal t1 = stream(getDateTimeStart()).findFirst().get();
        final Temporal first;
        if (getExDate() != null)
        {
            Temporal t2 = Collections.min(getExDate().getTemporals(), VComponent.TEMPORAL_COMPARATOR);
            first = (VComponent.isBefore(t1, t2)) ? t1 : t2;
        } else
        {
            first = t1;
        }           
        if (! first.equals(getDateTimeStart())) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTART (" + getDateTimeStart() + ") must be first occurrence (" + first + ")");
        Class<? extends Temporal> startClass = first.getClass();
        Class<? extends Temporal> untilClass = ((getRRule() != null) && (getRRule().getUntil() != null))
                ? getRRule().getUntil().getClass() : startClass;
        Class<? extends Temporal> eXDateClass = (getExDate() != null) ? getExDate().temporalClass() : startClass;
        Class<? extends Temporal> rDateClass = (getRDate() != null) ? getRDate().temporalClass() : startClass;
        if (startClass != untilClass) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  Temporal class type of DTSTART (" + startClass + ") and UNTIL (" + untilClass + ") must be the same.");
        if (startClass != eXDateClass) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  Temporal class type of DTSTART (" + startClass + ") and EXDATE (" + eXDateClass + ") must be the same.");
        if (startClass != rDateClass) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  Temporal class type of DTSTART (" + startClass + ") and RDATE (" + rDateClass + ") must be the same.");
        
        return errorsBuilder.toString();
    }
    
    /** Convert a list of strings containing properties of a iCalendar component and
     * populate its properties.  Used to make a new object from a List<String>.
     * 
     * @param vComponent
     * @param strings - list of properties
     * @return
     */
    protected static <U> VComponentAbstract<U> parseVComponent(VComponentAbstract<U> vComponent, List<String> strings)
    {
        Iterator<String> stringsIterator = strings.iterator();
        while (stringsIterator.hasNext())
        {
            String line = stringsIterator.next();
            String property = line.substring(0, line.indexOf(":"));
            String value = line.substring(line.indexOf(":") + 1).trim();
            if (property.equals(CATEGORIES_NAME))
            { // CATEGORIES
                vComponent.setCategories(value);
                stringsIterator.remove();
            } else if (property.equals(COMMENT_NAME))
            { // COMMENT
                vComponent.setComment(value);
                stringsIterator.remove();
            } else if (property.equals(CREATED_NAME))
            { // CREATED
                LocalDateTime dateTime = LocalDateTime.parse(value,VComponent.DATE_TIME_FORMATTER);
                vComponent.setDateTimeCreated(dateTime);
                stringsIterator.remove();
            } else if (property.equals(DATE_TIME_STAMP_NAME))
            { // DTSTAMP
                LocalDateTime dateTime = LocalDateTime.parse(value,VComponent.DATE_TIME_FORMATTER);
                vComponent.setDateTimeStamp(dateTime);
                stringsIterator.remove();
            } else if (property.equals(DATE_TIME_START_NAME))
            { // DTSTART
                Temporal dateTime = VComponent.parseTemporal(value);
                vComponent.setDateTimeStart(dateTime);
                stringsIterator.remove();
            } else if (property.equals(EXCEPTION_DATE_TIMES_NAME))
            { // EXDATE
                Collection<Temporal> dateTimeCollection = RecurrenceComponentAbstract.parseDates(value);
                if (vComponent.getExDate() == null)
                {
                    vComponent.setExDate(new EXDate());
                }                  
                vComponent.getExDate().getTemporals().addAll(dateTimeCollection);
                stringsIterator.remove();
            } else if (property.equals(LAST_MODIFIED_NAME))
            { // LAST-MODIFIED
                LocalDateTime dateTime = LocalDateTime.parse(value,VComponent.DATE_TIME_FORMATTER);
                vComponent.setDateTimeLastModified(dateTime);
                stringsIterator.remove();
            } else if (property.equals(RECURRENCE_DATE_TIMES_NAME))
            { // RDATE
                Collection<Temporal> dateTimeCollection = RecurrenceComponentAbstract.parseDates(value);
                if (vComponent.getRDate() == null)
                {
                    vComponent.setRDate(new RDate());
                }                  
                vComponent.getRDate().getTemporals().addAll(dateTimeCollection);
                stringsIterator.remove();
            } else if (property.equals(RECURRENCE_ID_NAME))
            { // RECURRENCE-ID
                LocalDateTime dateTime = LocalDateTime.parse(value,VComponent.DATE_TIME_FORMATTER);
                vComponent.setDateTimeRecurrence(dateTime);
                stringsIterator.remove();
            } else if (property.equals(RELATED_TO_NAME))
            { // RELATED-TO
                vComponent.setRelatedTo(value);
                stringsIterator.remove();
            } else if (property.equals(RECURRENCE_RULE_NAME))
            { // RRULE
                vComponent.setRRule(RRule.parseRRule(value));
                stringsIterator.remove();
            } else if (property.equals(SEQUENCE_NAME))
            { // SEQUENCE
                vComponent.setSequence(Integer.parseInt(value));
                stringsIterator.remove();
            } else if (property.equals(SUMMARY_NAME))
            { // SUMMARY
                vComponent.setSummary(value);
                stringsIterator.remove();
            } else if (property.equals(UNIQUE_IDENTIFIER_NAME))
            { // UID
                vComponent.setUniqueIdentifier(value);
                stringsIterator.remove();
            }
        }
        return vComponent;
    }

    // Variables for start date or date/time cache used as starting Temporal for stream
    private static final int CACHE_RANGE = 51; // number of values in cache
    private static final int CACHE_SKIP = 21; // store every nth value in the cache
    private int skipCounter = 0; // counter that increments up to CACHE_SKIP, indicates time to record a value, then resets to 0
    private Temporal[] temporalCache; // the start date or date/time cache
    private Temporal dateTimeStartLast; // last dateTimeStart, when changes indicates clearing the cache is necessary
    private RRule rRuleLast; // last rRule, when changes indicates clearing the cache is necessary
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
                if (VComponent.isBefore(temporalCache[i], value))
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
            if (! VComponent.isBefore(t, value)) break;
            lastT = t;
        }
        return lastT;
    }
    
    @Override
    public Stream<Temporal> stream(Temporal start)
    {
        // adjust start to ensure its not before dateTimeStart
        final Temporal start2 = (VComponent.isBefore(start, getDateTimeStart())) ? getDateTimeStart() : start;
     
        final Stream<Temporal> stream1; // individual or rrule stream
        final Temporal earliestCacheValue;
        final Temporal latestCacheValue;
        
        if (getRRule() == null)
        { // if individual event
            stream1 = Arrays.asList(getDateTimeStart())
                    .stream()
                    .filter(d -> ! VComponent.isBefore(d, start2));
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
                if ((! VComponent.isBefore(start2, temporalCache[cacheStart])))
                {
                    Temporal m = latestCacheValue;
                    for (int i=cacheStart; i<cacheEnd+1; i++)
                    {
                        if (VComponent.isAfter(temporalCache[i], start2))
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
            stream1 = getRRule().stream(match);
        }
        
        Stream<Temporal> stream2 = (getRDate() == null) ? stream1 : getRDate().stream(stream1, start2); // add recurrence list
        Stream<Temporal> stream3 = (getExDate() == null) ? stream2 : getExDate().stream(stream2, start2); // remove exceptions
        Stream<Temporal> stream4 = stream3
                .peek(t ->
                { // save new values in cache
                    if (getRRule() != null)
                    {
                        if (VComponent.isBefore(t, earliestCacheValue))
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
                        if (VComponent.isAfter(t, latestCacheValue))
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
                .filter(t -> ! VComponent.isBefore(t, start2)); // remove too early events;

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
                    .filter(d -> ! VComponent.isBefore(d, start));
        } else
        { // if has recurrence rule
            stream1 = getRRule().stream(getDateTimeStart());
        }
        Stream<Temporal> stream2 = (getRDate() == null) ? stream1 : getRDate().stream(stream1, start); // add recurrence list
        Stream<Temporal> stream3 = (getExDate() == null) ? stream2 : getExDate().stream(stream2, start); // remove exceptions
        return stream3.filter(t -> ! VComponent.isBefore(t, start));
    }
}
