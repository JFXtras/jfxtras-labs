package jfxtras.labs.icalendar;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
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
import jfxtras.labs.icalendar.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.icalendar.rrule.RRule;

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
 * @see VComponentBase
 * @see VEvent
 * @see VTodo // not implemented yet
 * @see VJournal // not implemented yet
 * */
public interface VComponent<I>
{
    final static DateTimeFormatter LOCAL_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();
    final static DateTimeFormatter LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();
    final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(LOCAL_DATE_FORMATTER)
            .appendLiteral('T')
            .append(LOCAL_TIME_FORMATTER)
            .toFormatter();
    final static DateTimeFormatter ZONED_DATE_TIME_UTC_FORMATTER = new DateTimeFormatterBuilder()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .appendOffsetId()
            .toFormatter();
    final static DateTimeFormatter ZONED_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .appendLiteral(':')
            .optionalEnd()
            .append(LOCAL_DATE_TIME_FORMATTER)
            .toFormatter();
    final static DateTimeFormatter ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .optionalStart()
            .parseCaseInsensitive()
            .appendLiteral("TZID=")
            .appendZoneRegionId()
            .optionalEnd()
            .toFormatter();

    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    String getCategories();
    StringProperty categoriesProperty();
    void setCategories(String value);
    
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
    String getComment();
    StringProperty commentProperty();
    void setComment(String value);
    
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
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.  Can be either type LocalDate or LocalDateTime
     */
    Temporal getDateTimeStart();
    ObjectProperty<Temporal> dateTimeStartProperty();
    void setDateTimeStart(Temporal dtStart);
    default DateTimeType getDateTimeType() { return DateTimeType.from(getDateTimeStart()); };

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
    RRule getRRule();
    ObjectProperty<RRule> rRuleProperty();
    void setRRule(RRule rRule);

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
    String getSummary();
    StringProperty summaryProperty();
    void setSummary(String value);
    
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
    
    /** Stream of dates or date/times that indicate the start of the event(s).
     * For a VEvent without RRULE the stream will contain only one date/time element.
     * A VEvent with a RRULE the stream contains more than one date/time element.  It will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
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
     */
    void handleEdit(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances
          , Callback<Map<ChangeDialogOption, String>, ChangeDialogOption> dialogCallback);
    
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
          , Callback<Map<ChangeDialogOption, String>, ChangeDialogOption> dialogCallback);
    
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
                            errorsBuilder.append(System.lineSeparator() + "Invalid RRule: UNTIL must be ZonedDateTime with UTC if DTSTART (dateTimeStart) is ZonedDateTime");
                        }
                    } else
                    {
                        errorsBuilder.append(System.lineSeparator() + "Invalid RRule: UNTIL must be ZonedDateTime wth UTC if DTSTART (dateTimeStart) is ZonedDateTime");
                    }
                }
            }
        }
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

        if (getExDate() != null)
        {
            DateTimeType exDateTimeType = DateTimeType.from(getExDate().getTemporals().iterator().next());
            if (getDateTimeType() != exDateTimeType) errorsBuilder.append(System.lineSeparator() + "Invalid VComponent.  DateTimeType of DTSTART (" + getDateTimeType() + ") and EXDATE (" + exDateTimeType + ") must be the same.");
        }
        if (getRDate() != null)
        {
            DateTimeType rDateTimeType = DateTimeType.from(getRDate().getTemporals().iterator().next());
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
    default boolean isValid() { return errorString().equals(""); };
    
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
    
    /** Returns true if startInstance is last in recurrence set,
     *  false there are recurrences after startInstance */
    default boolean isLastRecurrence(Temporal startInstance)
    {
        if ((getRRule() != null) && (getRRule().isInfinite())) { return false; }
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        while (i.hasNext())
        {
            Temporal myTemporal = i.next();
            System.out.println("test:" + myTemporal + " " + startInstance);
            if (myTemporal.equals(startInstance)) { return i.hasNext(); } // matched startInstance, does iterator have next?
            if (LocalDate.from(myTemporal).isAfter(LocalDate.from(startInstance))) break;
        }
        throw new RuntimeException("Instance is not in recurrence set:" + startInstance);
    }
    
    /** Returns true if VComponent has zero instances in recurrence set */
    default boolean isRecurrenceSetEmpty()
    {
        Iterator<Temporal> i = stream(getDateTimeStart()).iterator();
        return ! i.hasNext();
    }
    
//    /**
//     * Make easy-to-read date range string
//     * 
//     * For generating a string representing the whole segment the start parameter should be
//     * DTSTART.  For a this-and-future representation start should be the start of the
//     * selected instance.
//     * 
//     * If VComponent is part of a multi-part series only this segment is considered.
//     * Example: Dec 5, 2015 - Feb 6, 2016
//     *          Nov 12, 2015 - forever
//     *          
//     * return String representing start and end of VComponent.
//     */
//    default String rangeToString(Temporal start)
//    {
//        Temporal lastStart = lastStartTemporal();
//        if (start.equals(lastStart)) return temporalToStringPretty(start); // individual            
//        else if (lastStart == null) return temporalToStringPretty(start) + " - forever"; // infinite
//        else return temporalToStringPretty(start) + " - " + Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(lastStart); // has finite range (use only date for end)
//    }
    /**
     * Make easy-to-read date range string
     * Uses DTSTART as start.
     *      
     * If VComponent is part of a multi-part series only this segment is considered.
     * Example: Dec 5, 2015 - Feb 6, 2016
     *          Nov 12, 2015 - forever
     *          
     * return String representing start and end of VComponent.
     */
 // TODO - PUT THESE METHODS ELSEWHERE - NOT NEEDED BY ICALENDAR - USED BY ICALENDARAGENDA???
//    default String rangeToString() { return rangeToString(getDateTimeStart()); }

    /** returns the last date or date/time of the series.  If infinite returns null */
    default Temporal lastStartTemporal()
    {
        if (getRRule() != null)
        {
            if ((getRRule().getCount() == 0) && (getRRule().getUntil() == null))
            {
                return null; // infinite
            }
            else
            { // finite (find end)
                List<Temporal> instances = stream(getDateTimeStart()).collect(Collectors.toList());
                return instances.get(instances.size()-1);
            }
        } else if (getRDate() != null)
        { // has RDATE list finite (find end)
            List<Temporal> instances = stream(getDateTimeStart()).collect(Collectors.toList());
            return instances.get(instances.size()-1);
        } else return getDateTimeStart(); // individual            
    }
    
    /*
     * UTILITY METHODS
     * 
     * Below methods are used to handle dateTimeEnd and dateTimeStart as Temporal objects.  LocalDate
     * objects are compared at start-of-day.
     */
    
    final static Comparator<Temporal> TEMPORAL_COMPARATOR = (t1, t2) -> 
    {
        LocalDateTime ld1 = (t1.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t1) : LocalDate.from(t1).atStartOfDay();
        LocalDateTime ld2 = (t2.isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(t2) : LocalDate.from(t2).atStartOfDay();
        return ld1.compareTo(ld2);
    };
    
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
    
    /** Parse iCalendar date or date/time string into LocalDate, LocalDateTime or ZonedDateTime for following formats:
     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
     * 
     * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE")) before the date-time portion of the string.
     * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
     * 
     * Based on ISO.8601.2004
     */
    static Temporal parseTemporal(String temporalString)
    {
        final String form1 = "^[0-9]{8}T([0-9]{6})";
        final String form2 = "^[0-9]{8}T([0-9]{6})Z";
        final String form3 = "^(TZID=.*:)[0-9]{8}T([0-9]{6})";
        final String form4 = "^(VALUE=DATE:)?[0-9]{8}";
        if (temporalString.matches("^VALUE=DATE-TIME:.*")) // remove optional VALUE=DATE-TIME
        {
            temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE-TIME:")+"VALUE=DATE-TIME:".length()).trim();
        }
        if (temporalString.matches(form1))
        {
            return LocalDateTime.parse(temporalString, LOCAL_DATE_TIME_FORMATTER);
        } else if (temporalString.matches(form2))
        {
            return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_UTC_FORMATTER);
        } else if (temporalString.matches(form3))
        {
            return ZonedDateTime.parse(temporalString, ZONED_DATE_TIME_FORMATTER);            
        } else if (temporalString.matches(form4))
        {
            if (temporalString.matches("^VALUE=DATE:.*"))
            {
                temporalString = temporalString.substring(temporalString.indexOf("VALUE=DATE:")+"VALUE=DATE:".length()).trim();
            }
            return LocalDate.parse(temporalString, LOCAL_DATE_FORMATTER);
        } else
        {
            throw new IllegalArgumentException("String does not match any DATE or DATE-TIME patterns: " + temporalString);
        }
    }
    
    /**
     * Convert temporal, either LocalDate or LocalDateTime to appropriate iCalendar string
     * Examples:
     * 19980119T020000
     * 19980119
     * 
     * @param temporal LocalDate or LocalDateTime
     * @return iCalendar date or date/time string based on ISO.8601.2004
     */
    static String temporalToString(Temporal temporal)
    {
        if (temporal == null) return null;
        if (temporal instanceof ZonedDateTime)
        {
            ZoneOffset offset = ((ZonedDateTime) temporal).getOffset();
            if (offset == ZoneOffset.UTC)
            {
                return ZONED_DATE_TIME_UTC_FORMATTER.format(temporal);
            } else
            {
                return LOCAL_DATE_TIME_FORMATTER.format(temporal); // don't use ZONED_DATE_TIME_FORMATTER because time zone is added to property tag
            }
        } else if (temporal instanceof LocalDateTime)
        {
            return LOCAL_DATE_TIME_FORMATTER.format(temporal);
        } else if (temporal instanceof LocalDate)
        {
            return LOCAL_DATE_FORMATTER.format(temporal);
        } else
        {
            throw new DateTimeException("Invalid temporal type:" + temporal.getClass().getSimpleName());
        }
    }
    
//    /**
//     * Returns LocalDateTime from Temporal that is an instance of either LocalDate or LocalDateTime
//     * If the parameter is type LocalDate the returned LocalDateTime is atStartofDay.
//     * If the parameter is type ZonedDateTime the zoneID is changed to ZoneId.systemDefault() before taking the
//     * LocalDateTime.
//     * 
//     * @param temporal - either LocalDate or LocalDateTime type
//     * @return LocalDateTime
//     */
//    static LocalDateTime localDateTimeFromTemporal(Temporal temporal)
//    {
//        if (temporal == null) return null;
//        if (temporal.isSupported(ChronoUnit.NANOS))
//        {
//            if (temporal instanceof ZonedDateTime)
//            {
//                ZonedDateTime z = ((ZonedDateTime) temporal).withZoneSameInstant(ZoneId.systemDefault());
//                return LocalDateTime.from(z);                
//            } else if (temporal instanceof LocalDateTime)
//            {
//                return LocalDateTime.from(temporal);                
//            } else throw new DateTimeException("Invalid temporal type:" + temporal.getClass().getSimpleName());
//        } else
//        {
//            return LocalDate.from(temporal).atStartOfDay();
//        }
//    }
    
    /** Determines if Temporal is before t2
     * Works for LocalDate or LocalDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is before t2, false otherwise
     */
    static boolean isBefore(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            LocalDateTime d1 = DateTimeType.localDateTimeFromTemporal(t1);
            LocalDateTime d2 = DateTimeType.localDateTimeFromTemporal(t2);
            return d1.isBefore(d2);
        } throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }

    /** Determines if Temporal is after t2
     * Works for LocalDate or LocalDateTime
     * 
     * @param t1 first Temporal
     * @param t2 second Temporal (to compare with t1)
     * @return true if t1 is after t2, false otherwise
     */
    static boolean isAfter(Temporal t1, Temporal t2)
    {
        if (t1.getClass().equals(t2.getClass()))
        {
            LocalDateTime d1 = DateTimeType.localDateTimeFromTemporal(t1);
            LocalDateTime d2 = DateTimeType.localDateTimeFromTemporal(t2);
            return d1.isAfter(d2);
        } throw new DateTimeException("For comparision, Temporal classes must be equal (" + t1.getClass().getSimpleName() + ", " + t2.getClass().getSimpleName() + ")");
    }

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

//    // TODO - PROBABLY THESE METHODS SHOULD GO TO ICALENDARUTILITIES
//    /**
//     * Makes easy-to-read string of date range for the VComponents
//     * For ALL edit option (one VComponent)
//     * 
//     * @param vComponent
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static <U> String rangeToString(VComponent<U> vComponent)
//    {
//        return rangeToString(Arrays.asList(vComponent));
//    }
//    /**
//     * Makes easy-to-read string of date range for the VComponents
//     * Beginning of range is parameter start
//     * For ALL edit option (one VComponent)
//     * 
//     * @param vComponent
//     * @param start - Temporal start date or date/time
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static <U> String rangeToString(VComponent<U> vComponent, Temporal start)
//    {
//        return rangeToString(Arrays.asList(vComponent), start);
//    }
//    /**
//     * For ALL edit option (list of VComponents)
//     * 
//     * @param relatives - list of all related VComponents
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static <U> String rangeToString(Collection<VComponent<U>> relatives)
//    {
//        return rangeToString(relatives, null);
//    }
//    /**
//     * For THIS_AND_FUTURE_ALL edit option
//     * 
//     * @param relatives - list of all related VComponents
//     * @param start - Temporal start date or date/time
//     * @return - easy-to-read string of date range for the VComponents
//     */
//    static <U> String rangeToString(Collection<VComponent<U>> relatives, Temporal start)
//    {
//        if (relatives.size() == 0) return null;
//        Iterator<VComponent<U>> i = relatives.iterator();
//        VComponent<U> v1 = i.next();
//        Temporal start2 = (start == null) ? v1.getDateTimeStart() : start; // set initial start
//        Temporal end = v1.lastStartTemporal();
//        if (i.hasNext())
//        {
//            VComponent<U> v = i.next();
//            if (start != null) start2 = (isBefore(v.getDateTimeStart(), start2)) ? v.getDateTimeStart() : start2;
//            if (end != null) // null means infinite
//            {
//                Temporal myEnd = v.lastStartTemporal();
//                if (myEnd == null) end = null;
//                else end = (isAfter(myEnd, end)) ? v.lastStartTemporal() : end;
//            }
//        }
//        if (start2.equals(end)) return temporalToStringPretty(start2); // individual            
//        else if (end == null) return temporalToStringPretty(start2) + " - forever"; // infinite
//        else return temporalToStringPretty(start2) + " - " + Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY.format(end); // has finite range (only returns date for end of range)
//    }
    
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
            else count += v.getRRule().stream(v.getDateTimeStart()).count();
            if (count == -1) break;
        }
        if (count == 0) throw new RuntimeException("Invalid VComponent: no instances in recurrence set");
        return count;
    }
    
    /**
     * Produces property name and attribute, if necessary.
     * For example:
     * LocalDate : DTSTART;VALUE=DATE:
     * LocalDateTime : DTSTART:
     * ZonedDateTime (UTC) : DTSTART:
     * ZonedDateTime : DTEND;TZID=America/New_York:
     * 
     * @param propertyName
     * @param t - temporal of LocalDate, LocalDateTime or ZonedDateTime
     * @return
     */
    static String makeDateTimePropertyTag(String propertyName, Temporal t)
    {
        if (t instanceof ZonedDateTime)
        {
            String zone = VComponent.ZONE_FORMATTER.format(t);
            if (zone.isEmpty())
            {
                return propertyName + ":";                
            } else
            {
                return propertyName + ";" + zone + ":";                
            }
        } else
        {
            String prefex = (t instanceof LocalDate) ? ";VALUE=DATE:" : ":";
            return propertyName + prefex;
        }
    }

    /**
     * A convenience class to represent start and end date-time pairs
     * 
     */
   static public class StartEndPair
   {
       public StartEndPair(Temporal start, Temporal end)
       {
           if (start.getClass() != end.getClass()) { throw new RuntimeException("Temporal classes of start and end must be the same."); }
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