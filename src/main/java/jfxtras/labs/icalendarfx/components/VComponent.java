package jfxtras.labs.icalendarfx.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;

/** Interface for VEVENT, VTODO, VJOURNAL calendar components. 
 *
 * This defines a top-level calendar component from iCalendar as defined in section 3.6 
 * of RFC 5545 for an event (VEvent), a to-do (VTodo) or a journal entry (VJournal).
 * 
 * The three other components defined in section 3.6 are time zone
 * information, free/busy time information, and alarm.  Those three components
 * are not subclasses of VComponent and are not implemented here.
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
         3.8.4.3.  Organizer . . . . . . . . . . . . . . . . . . . . 113 - Yes
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
LAST-MODIFIED - yes
ORGANIZER - yes
RDATE - yes
RECURRENCE-ID - yes
RELATED-TO - yes
RESOURCES - not implemented
RRULE - yes
RSTATUS - not implemented
SEQUENCE - Yes
STATUS - not implemented
SUMMARY - yes
UID - yes
URL - not implemented
X-PROP - can't be implemented here.  must be in implementing class

Limitations: CATEGORIES, COMMENT, RELATED-TO can only exist once per calendar component.  According
to iCalendar a number of properties, including these can exist more than once.  Fixing
this limitation is a future goal. - I plan on fixing this problem by combining multiple
instances into one property internally.

 * @param <I> - type of recurrence instance, such as an appointment implementation
 * @see VComponentDisplayableOldBase
 * @see VEvent
 * @see VTodoOld // not implemented yet
 * @see VJournalOld // not implemented yet
 * 
 * @author David Bal
 * */
public interface VComponent<I>
{
    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    Categories getCategories();
    ObjectProperty<Categories> categoriesProperty();
    void setCategories(Categories categories);
    
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
//    String getComment();
//    StringProperty commentProperty();
//    void setComment(String value);
    Comment getComment();
    ObjectProperty<Comment> commentProperty();
    void setComment(Comment comment);
//    StringProperty commentTextProperty();
//    void setCommentText(String value);
    
    /**
     * CREATED: Date-Time Created, from RFC 5545 iCalendar 3.8.7.1 page 136
     * This property specifies the date and time that the calendar information was created.
     * This is analogous to the creation date and time for a file in the file system.
     * The value MUST be specified in the UTC time format.
     */
    ZonedDateTime getDateTimeCreated();
    ObjectProperty<ZonedDateTime> dateTimeCreatedProperty();
    void setDateTimeCreated(ZonedDateTime dtCreated);
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     * The value MUST be specified in the UTC time format.
     */
    ZonedDateTime getDateTimeStamp();
    ObjectProperty<ZonedDateTime> dateTimeStampProperty();
    void setDateTimeStamp(ZonedDateTime dtStamp);
    
    /**
     * DTSTART: Date-Time Start
     * RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.  Can be either type LocalDate or LocalDateTime
     */
    Temporal getDateTimeStart();
    ObjectProperty<Temporal> dateTimeStartProperty();
    void setDateTimeStart(Temporal dtStart);
    default DateTimeType getDateTimeType() { return DateTimeType.of(getDateTimeStart()); };
    default ZoneId getZoneId()
    {
        if (getDateTimeType() == DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE)
        {
            return ((ZonedDateTime) getDateTimeStart()).getZone();
        }
        return null;
    }

    /** Component is whole day if dateTimeStart (DTSTART) only contains a date (no time) */
    default boolean isWholeDay() { return getDateTimeType() == DateTimeType.DATE; }
        
    /**
     * EXDATE: Set of date/times exceptions for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Is rarely used, so employs lazy initialization.
     */
    ExDate getExDate();
    ObjectProperty<ExDate> exDateProperty();
    void setExDate(ExDate exDate);
    boolean isExDatesOnOneLine();

    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    ZonedDateTime getDateTimeLastModified();
    ObjectProperty<ZonedDateTime> dateTimeLastModifiedProperty();
    void setDateTimeLastModified(ZonedDateTime dtLastModified);
    
    /**
     *  ORGANIZER: RFC 5545 iCalendar 3.8.4.3. page 111
     * This property defines the organizer for a calendar component
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     * 
     * The property is stored as a simple string.  The implementation is
     * responsible to extract any contained data elements such as CN, DIR, SENT-BY
     * */
    String getOrganizer();
    StringProperty organizerProperty();
    void setOrganizer(String value);
    
    /**
     * RDATE: Set of date/times for recurring events, to-dos, journal entries.
     * 3.8.5.2, RFC 5545 iCalendar
     */
    RDate getRDate();
    ObjectProperty<RDate> rDateProperty();
    void setRDate(RDate rDate);
    
    /**
     * RELATED-TO: This property is used to represent a relationship or reference between
     * one calendar component and another.  By default, the property value points to another
     * calendar component's UID that has a PARENT relationship to the referencing object.
     * This field is null unless the object contains as RECURRENCE-ID value.
     * 3.8.4.5, RFC 5545 iCalendar
     */
    String getRelatedTo();
    StringProperty relatedToProperty();
    void setRelatedTo(String uid);
    
    // TODO - NOT IMPLEMENTED YET
    /**
     * Use Google UID extension instead of RELATED-TO to express 
     */
    boolean isGoogleRecurrenceUID();
    void setGoogleRecurrenceUID(boolean b);

    /**
     * RECURRENCE-ID: date or date-time recurrence, from RFC 5545 iCalendar 3.8.4.4 page 112
     * The property value is the original value of the "DTSTART" property of the 
     * recurrence instance.
     */
    Temporal getDateTimeRecurrence();
    ObjectProperty<Temporal> dateTimeRecurrenceProperty();
    void setDateTimeRecurrence(Temporal dtRecurrence);
    
    /** If VComponent has RECURRENCE-ID this returns its parent component */
    VComponent<I> getParent();
    void setParent(VComponent<I> v);
    
    /**
     * RRULE, Recurrence Rule as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     */
    RecurrenceRule getRRule();
    ObjectProperty<RecurrenceRule> rRuleProperty();
    void setRRule(RecurrenceRule rRule);

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
    int getSequence();
    IntegerProperty sequenceProperty();
    void setSequence(int value);
    default void incrementSequence() { setSequence(getSequence()+1); }
    
    /**
     *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    Summary getSummary();
    ObjectProperty<Summary> summaryProperty();
    void setSummary(Summary summary);
//    String getSummary();
//    StringProperty summaryProperty();
//    void setSummary(String value);
    
    /**
     * UID, Unique identifier, as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     */
    String getUniqueIdentifier();
    StringProperty uniqueIdentifierProperty();
    void setUniqueIdentifier(String s);
    
    /** Callback for creating unique uid values  */
    Callback<Void, String> getUidGeneratorCallback();
    void setUidGeneratorCallback(Callback<Void, String> uidCallback);
    
    /** Stream of dates or date-times that indicate the series of start date-times of the event(s).
     * iCalendar calls this series the recurrence set.
     * For a VEvent without RRULE or RDATE the stream will contain only one element.
     * In a VEvent with a RRULE the stream should contain more than one date/time element.  It is possible
     * to define a single-event RRULE, but it is not advisable.  The stream will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * The stream starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence.
     * 
     * Start date/times are only produced between the ranges set by setDateTimeRanges
     * 
     * @param startTemporal - start dates or date/times produced after this date.  If not on an occurrence,
     * it will be adjusted to be the next occurrence
     * @return - stream of start dates or date/times for the recurrence set
     */
    Stream<Temporal> stream(Temporal startTemporal);

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
       
    /**
     * returns string of line-separated properties defining calendar component.
     * 
     * Example:<br>
     * BEGIN:VEVENT<br>
     * DTSTART;TZID=America/Los_Angeles:20160214T080000<br>
     * DTEND;TZID=America/Los_Angeles:20160214T110000<br>
     * RRULE:FREQ=WEEKLY;BYDAY=SU,TU,FR<br>
     * DTSTAMP:20160214T022532Z<br>
     * UID:im8hmpakeigu3d85j3vq9q8bcc@google.com<br>
     * CREATED:20160214T022525Z<br>
     * LAST-MODIFIED:20160214T022525Z<br>
     * SUMMARY:test2<br>
     * END:VEVENT
     * 
     */
    String toComponentText();
    
    /**
     * Handles how an edited VComponent is processed.  For a VComponent with a recurrence rule (RRULE)
     * the user is given a dialog to select ONE, THIS_AND_FUTURE, or ALL instances to edit.
     * For a VComponent without a RRULE there is no dialog.
     * 
     * This VComponent should have all changes made to it by the controller, except date-time changes
     * that depend on the answer to the dialog question
     * 
     * @param vComponentOriginal - copy of this VComponent before changes
     * @param vComponents - collection of all VComponents
     * @param startOriginalInstance - date or date/time of selected instance before changes
     * @param startInstance - date or date/time of selected instance after changes
     * @param endInstance - date or date/time of selected instance after changes (null for VTODO and VJOURNAL)
     * @param instances - all instances being rendered by all VComponents
     * @param dialogCallback - callback to generate dialog to select ONE, THIS_AND_FUTURE, or ALL.
     *    Note: Can use a stub for testing (e.g. (m) -> ChangeDialogOption.ALL).
     * @return - true if changed, false otherwise
     */
    boolean handleEdit(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances
          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback);
    
    /**
     * Deletes a VComponent.  For a VComponent with a recurrence rule (RRULE) the user is given a dialog
     * to select ONE, THIS_AND_FUTURE, or ALL instances to delete.
     * 
     * @param vComponents - collection of all VComponents
     * @param startInstance - start date or date/time of instance
     * @param instance - selected recurrence instance
     * @param instances - collection of all instances across all VComponents
     * @param dialogCallback - callback to generate dialog to select ONE, THIS_AND_FUTURE, or ALL.
     *    Note: Can use a stub for testing (e.g. (m) -> ChangeDialogOption.ALL).
     */
    void handleDelete(
            Collection<VComponent<I>> vComponents
          , Temporal startInstance
          , I instance
          , Collection<I> instances
          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback);
    
    /**
     * Copies this object into destination object
     * 
     * @param destination
     */
    // TODO - MOVE TO VCOMPONENT PROPERTY ENUM
    void copyTo(VComponent<I> destination);
    
    // DEFAULT METHODS
    // TODO - CONSIDER MOVING SOME OF THE BELOW METHODS TO ABSTRACT
    
    /**
     * If VComponent is not valid returns string of errors.  If it's valid then returns an empty string
     * See {@link #isValid()} for valid criteria
     */
    default String errorString()
    {
        StringBuilder errorsBuilder = new StringBuilder();
        if (getDateTimeStart() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTART must not be null.");
        if (getDateTimeStamp() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTAMP must not be null.");
        if (getUniqueIdentifier() == null) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  UID must not be null.");
        if (getRRule() != null)
        {
            errorsBuilder.append(getRRule().makeErrorString(this));
            /*
             * RFC 5545, page 41
             * If the "DTSTART" property is specified as a date with UTC
             * time or a date with local time and time zone reference, then the
             * UNTIL rule part MUST be specified as a date with UTC time.
             */
            if (getRRule().getUntil() != null)
            {
                if ((getDateTimeStart() instanceof ZonedDateTime))
                {
                    if ((getRRule().getUntil() instanceof ZonedDateTime))
                    {
                        if (! ((ZonedDateTime) getRRule().getUntil()).getOffset().equals(ZoneOffset.UTC))
                        {
                            errorsBuilder.append(System.lineSeparator() + "Invalid RRule: UNTIL must be DateTimeType DATE_WITH_UTC_TIME (not " + DateTimeType.of(getRRule().getUntil()) + ") if DTSTART (dateTimeStart) is ZonedDateTime");
                        }
                    } else
                    {
                        errorsBuilder.append(System.lineSeparator() + "Invalid RRule: UNTIL must be ZonedDateTime (not " + getRRule().getUntil().getClass().getSimpleName() + ") with UTC if DTSTART (dateTimeStart) is ZonedDateTime");
                    }
                }
            }
            Temporal t1 = getRRule().getFrequency().stream(getDateTimeStart()).findFirst().get();
            final Temporal first;
            if (getExDate() != null)
            {
                Temporal t2 = Collections.min(getExDate().getTemporals(), DateTimeUtilities.TEMPORAL_COMPARATOR);
                first = (DateTimeUtilities.isBefore(t1, t2)) ? t1 : t2;
            } else
            {
                first = t1;
            }           
            if (! first.equals(getDateTimeStart())) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DTSTART (" + getDateTimeStart() + ") must be first occurrence (" + first + ")");
        }

        if (getExDate() != null)
        {
            DateTimeType exDateTimeType = DateTimeType.of(getExDate().getTemporals().iterator().next());
            if (getDateTimeType() != exDateTimeType) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DateTimeType of DTSTART (" + getDateTimeType() + ") and EXDATE (" + exDateTimeType + ") must be the same.");
        }
        if (getRDate() != null)
        {
            DateTimeType rDateTimeType = DateTimeType.of(getRDate().getTemporals().iterator().next());
            if (getDateTimeType() != rDateTimeType) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DateTimeType of DTSTART (" + getDateTimeType() + ") and RDATE (" + rDateTimeType + ") must be the same.");
        }
        
        return errorsBuilder.toString();
    }
    
    /**
     * Checks to see if VComponent is has all required properties filled.  Also checks
     * to ensure all properties contain valid values.
     *
     * Requires properties:
     * DTSTAMP
     * UID
     *
     * Optional, can only occur once: // TODO - VERIFY ALL BELOW ITEMS DONE ONLY ONCE
     * CLASS
     * CREATED
     * DTSTART
     * LAST-MODIFIED
     * ORGANIZER
     * RECURID
     * RRULE
     * SEQUENCE
     * STATUS
     * SUMMARY
     * URL
     * 
     * @return true for valid VComponent, false for invalid one
     */
    default boolean isValid()
    {
        return errorString().equals("");
    };
    
    /** Returns true if VComponent is an individual (only one instance in recurrence set),
     *  false if has more than 1 instance */
    default boolean isIndividual()
    {
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        if (i.hasNext())
        {
            i.next();
            if (i.hasNext()) return false; // has at least two elements
            else return true; // has only one element
        } else throw new RuntimeException("VComponent stream has no elements");
    }
    
    /** Returns true if startInstance is first in recurrence set, false otherwise */
    default boolean isFirstRecurrence(Temporal startInstance)
    {
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        if (i.hasNext())
        {
            Temporal myTemporal = i.next();
            return (myTemporal.equals(startInstance));
        } else
        {
            return false;
        }
    }
    
    /** Returns true if startInstance is last in recurrence set,
     *  false there are recurrences after startInstance */
    default boolean isLastRecurrence(Temporal startInstance)
    {
        if ((getRRule() != null) && (getRRule().isInfinite()))
        {
            return false;
        }
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        while (i.hasNext())
        {
            Temporal myTemporal = i.next();
            if (myTemporal.equals(startInstance)) { return ! i.hasNext(); } // matched startInstance, does iterator have next?
            if (LocalDate.from(myTemporal).isAfter(LocalDate.from(startInstance))) break;
        }
        throw new RuntimeException("Instance is not in recurrence set:" + startInstance);
    }
    
    /** returns the last date or date/time of the series.  If infinite returns null */
    default Temporal lastRecurrence()
    {
        if ((getRRule() != null) && (getRRule().isInfinite()))
        {
            return null;
        } else
        {
            Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
            Temporal myTemporal = null;
            while (i.hasNext())
            {
                myTemporal = i.next();
            }
            return myTemporal;
        }       
    }
    
    /** Returns true if VComponent has zero instances in recurrence set */
    default boolean isRecurrenceSetEmpty()
    {
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        return ! i.hasNext();
    }
        
    /** Returns true if temporal is in vComponent's stream of start date-time
     * values, false otherwise.
     */
    default boolean isStreamValue(Temporal temporal)
    {
        Iterator<Temporal> startInstanceIterator = stream(temporal).iterator();
        while (startInstanceIterator.hasNext())
        {
            Temporal myStartInstance = startInstanceIterator.next();
            if (myStartInstance.equals(temporal))
            {
                return true;
            }
            if (DateTimeUtilities.isAfter(myStartInstance, temporal))
            {
                return false;
            }
        }
        return false;
    }
    
    /*
     * STATIC UTILITY METHODS
     * 
     */
    
    /**
     * Sorts VComponents by DTSTART date/time
     */
    final static Comparator<? super VComponent<?>> VCOMPONENT_COMPARATOR = (v1, v2) -> 
    {
        Temporal t1 = v1.getDateTimeStart();
        LocalDateTime ld1 = (t1.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t1) : LocalDate.from(t1).atStartOfDay();
        Temporal t2 = v2.getDateTimeStart();
        LocalDateTime ld2 = (t2.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t2) : LocalDate.from(t2).atStartOfDay();
        return ld1.compareTo(ld2);
    };


    /**
     * Return list of all related VComponents that make up entire recurrence set.
     * List also contains all VComponents that share the same UID.
     * 
     * Used to edit or delete entire recurrence set.
     * 
     * @param vComponents : collection of all VComponents
     * @param vComponent : VComponent to match to parent, children and branches
     * @return
     */
    static <U> List<VComponent<U>> findRelatedVComponents(Collection<VComponent<U>> vComponents, VComponent<U> vComponent)
    {
        String uid = vComponent.getUniqueIdentifier();
        return vComponents.stream()
                .filter(v -> v.getUniqueIdentifier().equals(uid))
                .collect(Collectors.toList());
    }
    
    /**
     * Return list of all related VComponents that make up entire recurrence set.
     * List also contains input parameter vComponent, parent, children,
     * or branches.
     * 
     * Used to edit or delete entire recurrence set.
     * 
     * @param vComponents : collection of all VComponents
     * @param vComponent : VComponent to match to parent, children and branches
     * @return
     */
    @Deprecated // matched by relatedTo - too inclusive.  Will be probably deleted in future.
    static <U> List<VComponent<U>> findRelatedVComponents2(Collection<VComponent<U>> vComponents, VComponent<U> vComponent)
    {
        final String uid = (vComponent.getRelatedTo() != null) ? vComponent.getRelatedTo() : vComponent.getUniqueIdentifier();
        System.out.println("uid:" + uid + " " + vComponents.size());

        return vComponents.stream()
                .filter( v ->
                {
                    boolean isChild = (v.getUniqueIdentifier() != null) ? v.getUniqueIdentifier().equals(uid) : false;
                    boolean isBranch2 = (v.getRelatedTo() != null) ? v.getRelatedTo().equals(uid) : false;
                    return isChild || isBranch2;
                })
                .sorted(VCOMPONENT_COMPARATOR)
                .collect(Collectors.toList());
    }
    
    /**
     * Counts number of instances in recurrence set.  returns -1 for infinite.
     * Recurrence set of collection of VComponents making up a series.
     * 
     * @param relatives
     * @return
     */
    static <U> int countVComponents(Collection<VComponent<U>> relatives)
    {        
        int count=0;
        Iterator<VComponent<U>> i = relatives.iterator();
        while (i.hasNext())
        {
            VComponent<U> v = i.next();
            if (v.getRRule() == null && v.getRDate() == null) count++; // individual
            else if ((v.getRRule().getUntil() == null) && (v.getRRule().getCount() == 0)) count = -1; // infinite
            else count += v.getRRule().streamRecurrence(v.getDateTimeStart()).count();
            if (count == -1) break;
        }
        if (count == 0) throw new RuntimeException("Invalid VComponent: no instances in recurrence set");
        return count;
    }

    /**
     * A convenience class to represent start and end date-time pairs
     * 
     */
   static public class StartEndRange
   {
       public StartEndRange(Temporal start, Temporal end)
       {
           if ((start != null) && (end != null) && (start.getClass() != end.getClass())) { throw new RuntimeException("Temporal classes of start and end must be the same."); }
           this.start = start;
           this.end = end;
       }
       
       public Temporal getDateTimeStart() { return start; }
       private final Temporal start;
       
       public Temporal getDateTimeEnd() { return end; }
       private final Temporal end; 
       
       @Override
       public String toString() { return super.toString() + " " + start + " to " + end; }
       
   }

}