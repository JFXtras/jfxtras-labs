package jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.RRule;

/**
 * Parent calendar component, VEVENT
 * Defined in RFC 5545 iCalendar 3.6.1, page 52.
 * 
 * The status of following component properties from RFC 5545:
 * 
       3.8.1.  Descriptive Component Properties  . . . . . . . . . .  81
         3.8.1.1.  Attachment  . . . . . . . . . . . . . . . . . . .  81 - NO
         3.8.1.2.  Categories  . . . . . . . . . . . . . . . . . . .  82 - Yes
         3.8.1.3.  Classification  . . . . . . . . . . . . . . . . .  83 - NO
         3.8.1.4.  Comment . . . . . . . . . . . . . . . . . . . . .  84 - Yes
         3.8.1.5.  Description . . . . . . . . . . . . . . . . . . .  85 - Yes
         3.8.1.6.  Geographic Position . . . . . . . . . . . . . . .  87 - NO
         3.8.1.7.  Location  . . . . . . . . . . . . . . . . . . . .  88 - Yes
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
         3.8.4.4.  Recurrence ID . . . . . . . . . . . . . . . . . . 114 - TODO
         3.8.4.5.  Related To  . . . . . . . . . . . . . . . . . . . 117 - NO
         3.8.4.6.  Uniform Resource Locator  . . . . . . . . . . . . 118 - NO
         3.8.4.7.  Unique Identifier . . . . . . . . . . . . . . . . 119 - Yes
       3.8.5.  Recurrence Component Properties . . . . . . . . . . . 120
         3.8.5.1.  Exception Date-Times  . . . . . . . . . . . . . . 120 - Yes, in EXDate
         3.8.5.2.  Recurrence Date-Times . . . . . . . . . . . . . . 122 - TODO, in RDate
         3.8.5.3.  Recurrence Rule . . . . . . . . . . . . . . . . . 124 - TODO, in RRule
       3.8.6.  Alarm Component Properties  . . . . . . . . . . . . . 134
         3.8.6.1.  Action  . . . . . . . . . . . . . . . . . . . . . 134 - NO
         3.8.6.2.  Repeat Count  . . . . . . . . . . . . . . . . . . 135 - NO
         3.8.6.3.  Trigger . . . . . . . . . . . . . . . . . . . . . 135 - NO
       3.8.7.  Change Management Component Properties  . . . . . . . 138
         3.8.7.1.  Date-Time Created . . . . . . . . . . . . . . . . 138 - TODO
         3.8.7.2.  Date-Time Stamp . . . . . . . . . . . . . . . . . 139 - TODO
         3.8.7.3.  Last Modified . . . . . . . . . . . . . . . . . . 140 - TODO
         3.8.7.4.  Sequence Number . . . . . . . . . . . . . . . . . 141 - TODO
       3.8.8.  Miscellaneous Component Properties  . . . . . . . . . 142
         3.8.8.1.  IANA Properties . . . . . . . . . . . . . . . . . 142 - NO
         3.8.8.2.  Non-Standard Properties . . . . . . . . . . . . . 142 - NO
         3.8.8.3.  Request Status  . . . . . . . . . . . . . . . . . 144 - NO
 * @param <T>

 * 
 *
 */
public abstract class VEvent<T extends VEvent>
{
    /**
     * Date-Time Start, DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     */
    final private ObjectProperty<LocalDateTime> dateTimeStart = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> dateTimeStartProperty() { return dateTimeStart; }
    public LocalDateTime getDateTimeStart() { return dateTimeStart.getValue(); }
    public void setDateTimeStart(LocalDateTime dtStart) { this.dateTimeStart.set(dtStart); }
    // TODO - ADD SET METHOD FOR ICALENDAR COMPLIANT STRINGS
    public T withDateTimeStart(LocalDateTime startDate) { setDateTimeStart(startDate); return (T)this; }

    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Internally stored a seconds.  Can be set an an integer of seconds or a string as defined by iCalendar which is
     * converted to seconds.  This value is used exclusively internally.  Any specified DTEND is converted to 
     * durationInSeconds,
     * */
    final private ObjectProperty<Integer> durationInSeconds = new SimpleObjectProperty<Integer>(this, "durationProperty");
    public ObjectProperty<Integer> durationInSecondsProperty() { return durationInSeconds; }
    public Integer getDurationInSeconds() { return durationInSeconds.getValue(); }
    public void setDurationInSeconds(Integer value) { durationInSeconds.setValue(value); }
    public void setDurationInSeconds(String value)
    { // parse ISO.8601.2004 period string into period of seconds (no support for Y (years) or M (months).
        int seconds = 0;
        Pattern p = Pattern.compile("([0-9]+)|([A-Z])");
        Matcher m = p.matcher(value);
        List<String> tokens = new ArrayList<String>();
        while (m.find())
        {
            String token = m.group(0);
            tokens.add(token);
        }
        Iterator<String> tokenIterator = tokens.iterator();
        String firstString = tokenIterator.next();
        if (! tokenIterator.hasNext() || (! firstString.equals("P"))) throw new InvalidParameterException("Invalid DURATION string (" + value + "). Must begin with a P");
        boolean timeFlag = false;
        while (tokenIterator.hasNext())
        {
            String token = tokenIterator.next();
            if (token.matches("\\d+"))
            { // first value is a number means I got a data element
                int n = Integer.parseInt(token);
                String time = tokenIterator.next();
                if (time.equals("W"))
                { // weeks
                    seconds += n * 7 * 24 * 60 * 60;
                } else if (time.equals("D"))
                { // days
                    seconds += n * 24 * 60 * 60;
                } else if (timeFlag && time.equals("H"))
                { // hours
                    seconds += n * 60 * 60;                    
                } else if (timeFlag && time.equals("M"))
                { // minutes
                    seconds += n * 60;                                        
                } else if (timeFlag && time.equals("S"))
                { // seconds
                    seconds += n;                    
                } else
                {
                    throw new InvalidParameterException("Invalid DURATION string time element (" + time + "). Must begin with a P");
                }
            } else if (token.equals("T")) timeFlag = true; // proceeding elements will be hour, minute or second
        }
        durationInSeconds.setValue(seconds);
    }
    public T withDurationInSeconds(Integer value) { setDurationInSeconds(value); return (T)this; } 
    public T withDurationInSeconds(String value) { setDurationInSeconds(value); return (T)this; } 
    
    /**
     * Date-Time End, DTEND from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * If entered this value is used to calculate the durationInSeconds, which is used
     * internally.
     */
    public void setDateTimeEnd(LocalDateTime dtEnd)
    {
        long seconds = ChronoUnit.SECONDS.between(getDateTimeStart(), dtEnd);
        setDurationInSeconds((int) seconds);
    }
    public void setDateTimeEnd(String dtEnd)
    {
        LocalDateTime dt = iCalendarDateTimeToLocalDateTime(dtEnd);
        setDateTimeEnd(dt);
    }
    public LocalDateTime getDateTimeEnd() { return getDateTimeStart().plusSeconds(getDurationInSeconds()); }

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
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
            date = LocalDate.parse(dateToken, dateFormatter);
        } else throw new InvalidParameterException("Invalid Date-Time string: " + dt);           
        if (tokens.size() == 2)
        { // find date if another token is available
            String timeToken = tokens.get(1);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
            LocalTime time = LocalTime.parse(timeToken, timeFormatter);
            return LocalDateTime.of(date, time);
        }
        return date.atStartOfDay();
    }
    
    /**
     * Unique identifier, UID as defined by RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * Included is an example UID generator.  Other UID generators can be provided by
     * setting the UID callback.
     * Uses lazy initialization of property because UID doesn't often require advanced features.
     */
    public StringProperty UIDProperty()
    {
        if (uid == null) uid = new SimpleStringProperty(this, "UID", _uid);
        return uid;
    }
    private StringProperty uid;
    public String getUID() { return (uid == null) ? _uid : uid.getValue(); }
    private String _uid;
    public void setUID(String s)
    {
        if (uid == null)
        {
            _uid = s;
        } else
        {
            uid.set(s);
        }
    }
    public VEvent withUID(String uid) { setUID(uid); return this; }

    /** Callback for creating unique uid values */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static String datePattern = "yyyyMMdd";
    private static String timePattern = "HHmmss";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern + "'T'" + timePattern);
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = formatter.format(LocalDateTime.now());
        String domain = "jfxtras-agenda";
        return dateTime + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    /** assign uid by calling the uidGeneratorCallback */
    public void makeUid()
    {
        setUID(uidGeneratorCallback.call(null));
    }
 
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
    public SimpleStringProperty commentProperty() { return commentProperty; }
    final private SimpleStringProperty commentProperty = new SimpleStringProperty(this, "comment");
    public String getComment() { return commentProperty.getValue(); }
    public void setComment(String value) { commentProperty.setValue(value); }
    public VEvent withComment(String value) { setComment(value); return this; } 
    
    
    /**
     *  SUMMARY: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property defines a short summary or subject for the calendar component 
     * Example:
     * SUMMARY:Department Party
     * */
    public SimpleStringProperty summaryProperty() { return summaryProperty; }
    final private SimpleStringProperty summaryProperty = new SimpleStringProperty(this, "summary");
    public String getSummary() { return summaryProperty.getValue(); }
    public void setSummary(String value) { summaryProperty.setValue(value); }
    public T withSummary(String value) { setSummary(value); return (T)this; } 
    
    /**
     * DESCRIPTION: RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     */
    public StringProperty descriptionProperty() { return descriptionProperty; }
    final private StringProperty descriptionProperty = new SimpleStringProperty(this, "description");
    public String getDescription() { return descriptionProperty.getValue(); }
    public void setDescription(String value) { descriptionProperty.setValue(value); }
    public T withDescription(String value) { setDescription(value); return (T)this; } 
    
    /**
     * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public StringProperty locationProperty() { return locationProperty; }
    final private StringProperty locationProperty = new SimpleStringProperty(this, "location");
    public String getLocation() { return locationProperty.getValue(); }
    public void setLocation(String value) { locationProperty.setValue(value); }
    public T withLocation(String value) { setLocation(value); return (T)this; } 
    
    /**
     * CATEGORIES: RFC 5545 iCalendar 3.8.1.12. page 81
     * This property defines the categories for a calendar component.
     * Example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    public SimpleStringProperty categoriesProperty() { return categoriesProperty; }
    final private SimpleStringProperty categoriesProperty = new SimpleStringProperty(this, "appointmentGroup");
    public String getCategories() { return categoriesProperty.getValue(); }
    public void setCategories(String value) { categoriesProperty.setValue(value); }
    public T withCategories(String value) { setCategories(value); return (T)this; }
 
    // NO NEED - 
//    /** Appointment-specific data - only uses data fields. Repeat related and date/time objects are null */
//    // TODO - HOW TO ENSURE USAGE OF ICALENDAR DATA FIELDS YET PROVIDE AVAILABILITY OF CUSTOM FIELDS AND
//    // COMPATIBILITY WITH AGENDA?
////    DESCRIPTION:This is a message for everyone
////    SUMMARY:The event
////    LOCATION:here
////    CATEGORIES:3.8.1.2 page 81 - like appointmentGroup
//    // COMMENT 3.8.1.4 page 83
//    private RepeatableAppointment appointmentData = null; //AppointmentFactory.newAppointment();
//    public RepeatableAppointment getAppointmentData() { return appointmentData; }
//    public void setAppointmentData(RepeatableAppointment appointment) { appointmentData = appointment; }
//    public VEvent withAppointmentData(RepeatableAppointment appointment) { setAppointmentData(appointment); return this; }
    
    /**
     * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
     * If event is not repeating value is null
     */
    public RRule getRRule() { return rRule; }
    private RRule rRule;
    public void setRRule(RRule rRule) { this.rRule = rRule; }

    /**
     * Start of range for which events are generated.  Should match the dates displayed on the calendar.
     */
    public LocalDateTime getDateTimeRangeStart() { return dateTimeRangeStart; }
    private LocalDateTime dateTimeRangeStart;
    public void setDateTimeRangeStart(LocalDateTime startDateTime) { this.dateTimeRangeStart = startDateTime; }
    public T withDateTimeRangeStart(LocalDateTime startDateTime) { setDateTimeRangeStart(startDateTime); return (T)this; }
    
    /**
     * End of range for which events are generated.  Should match the dates displayed on the calendar.
     */
    public LocalDateTime getDateTimeRangeEnd() { return dateTimeRangeEnd; }
    private LocalDateTime dateTimeRangeEnd;
    public void setDateTimeRangeEnd(LocalDateTime endDateTime) { this.dateTimeRangeEnd = endDateTime; }
    public T withDateTimeRangeEnd(LocalDateTime endDateTime) { setDateTimeRangeEnd(endDateTime); return (T)this; }
    
    /** Stream of date/times that indicate the start of the event(s).
     * For a VEvent without RRULE the stream will contain only one date/time element.
     * A VEvent with a RRULE the stream contains more than one date/time element.  It will be infinite 
     * if COUNT or UNTIL is not present.  The stream has an end when COUNT or UNTIL condition is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        if (getRRule() == null)
        { // if individual event
            if (! startDateTime.isBefore(getDateTimeStart()))
            {
            return Arrays.asList(getDateTimeStart()).stream();
            } else
            { // if dateTimeStart is before startDateTime
                return new ArrayList<LocalDateTime>().stream(); // empty stream
            }
        } else
        { // if has recurrence rule
            // filter away too early (with Java 9 takeWhile these statements can be combined into one chained statement for greater elegance)
            Stream<LocalDateTime> filteredStream = getRRule()
                    .stream(startDateTime)
                    .filter(a -> (getDateTimeRangeStart() == null) ? true : ! a.isBefore(getDateTimeRangeStart()));
            // stop when too late
            return takeWhile(filteredStream, a -> (getDateTimeRangeEnd() == null) ? true : ! a.isAfter(getDateTimeRangeEnd()));
        }
    };
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    public static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
          return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
            boolean stillGoing = true;
            @Override public boolean tryAdvance(Consumer<? super T> consumer) {
              if (stillGoing) {
                boolean hadNext = splitr.tryAdvance(elem -> {
                  if (predicate.test(elem)) {
                    consumer.accept(elem);
                  } else {
                    stillGoing = false;
                  }
                });
                return hadNext && stillGoing;
              }
              return false;
            }
          };
        }

        static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
           return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
        }
    
}
