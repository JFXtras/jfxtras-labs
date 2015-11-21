package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
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

/**
 * Parent calendar component, VEVENT
 * Defined in RFC 5545 iCalendar 3.6.1, page 52.
 * 
 * The status of following component properties from RFC 5545:
 * 
       3.8.1.  Descriptive Component Properties  . . . . . . . . . .  81
         3.8.1.1.  Attachment  . . . . . . . . . . . . . . . . . . .  81 - NO (from VComponent)
         3.8.1.2.  Categories  . . . . . . . . . . . . . . . . . . .  82 - Yes (from VComponent)
         3.8.1.3.  Classification  . . . . . . . . . . . . . . . . .  83 - TODO (from VComponent)
         3.8.1.4.  Comment . . . . . . . . . . . . . . . . . . . . .  84 - Yes (from VComponent)
         3.8.1.5.  Description . . . . . . . . . . . . . . . . . . .  85 - Yes
         3.8.1.6.  Geographic Position . . . . . . . . . . . . . . .  87 - NO
         3.8.1.7.  Location  . . . . . . . . . . . . . . . . . . . .  88 - Yes
         3.8.1.8.  Percent Complete  . . . . . . . . . . . . . . . .  89 - NO
         3.8.1.9.  Priority  . . . . . . . . . . . . . . . . . . . .  90 - NO
         3.8.1.10. Resources . . . . . . . . . . . . . . . . . . . .  92 - NO (from VComponent)
         3.8.1.11. Status  . . . . . . . . . . . . . . . . . . . . .  93 - TODO (from VComponent)
         3.8.1.12. Summary . . . . . . . . . . . . . . . . . . . . .  94 - Yes (from VComponent)
       3.8.2.  Date and Time Component Properties  . . . . . . . . .  95
         3.8.2.1.  Date-Time Completed . . . . . . . . . . . . . . .  95 - NO
         3.8.2.2.  Date-Time End . . . . . . . . . . . . . . . . . .  96 - Yes
         3.8.2.3.  Date-Time Due . . . . . . . . . . . . . . . . . .  97 - NO
         3.8.2.4.  Date-Time Start . . . . . . . . . . . . . . . . .  99 - Yes (from VComponent)
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
         3.8.4.1.  Attendee  . . . . . . . . . . . . . . . . . . . . 108 - NO (from VComponent)
         3.8.4.2.  Contact . . . . . . . . . . . . . . . . . . . . . 111 - TODO (from VComponent)
         3.8.4.3.  Organizer . . . . . . . . . . . . . . . . . . . . 113 - TODO (from VComponent)
         3.8.4.4.  Recurrence ID . . . . . . . . . . . . . . . . . . 114 - TODO (from VComponent)
         3.8.4.5.  Related To  . . . . . . . . . . . . . . . . . . . 117 - NO (from VComponent)
         3.8.4.6.  Uniform Resource Locator  . . . . . . . . . . . . 118 - NO (from VComponent)
         3.8.4.7.  Unique Identifier . . . . . . . . . . . . . . . . 119 - Yes (from VComponent)
       3.8.5.  Recurrence Component Properties . . . . . . . . . . . 120
         3.8.5.1.  Exception Date-Times  . . . . . . . . . . . . . . 120 - Yes, in EXDate class
         3.8.5.2.  Recurrence Date-Times . . . . . . . . . . . . . . 122 - TODO, in RDate class
         3.8.5.3.  Recurrence Rule . . . . . . . . . . . . . . . . . 124 - TODO, in RRule class
       3.8.6.  Alarm Component Properties  . . . . . . . . . . . . . 134
         3.8.6.1.  Action  . . . . . . . . . . . . . . . . . . . . . 134 - NO
         3.8.6.2.  Repeat Count  . . . . . . . . . . . . . . . . . . 135 - NO
         3.8.6.3.  Trigger . . . . . . . . . . . . . . . . . . . . . 135 - NO
       3.8.7.  Change Management Component Properties  . . . . . . . 138
         3.8.7.1.  Date-Time Created . . . . . . . . . . . . . . . . 138 - TODO (from VComponent)
         3.8.7.2.  Date-Time Stamp . . . . . . . . . . . . . . . . . 139 - TODO (from VComponent)
         3.8.7.3.  Last Modified . . . . . . . . . . . . . . . . . . 140 - TODO (from VComponent)
         3.8.7.4.  Sequence Number . . . . . . . . . . . . . . . . . 141 - TODO (from VComponent)
       3.8.8.  Miscellaneous Component Properties  . . . . . . . . . 142
         3.8.8.1.  IANA Properties . . . . . . . . . . . . . . . . . 142 - NO (from VComponent)
         3.8.8.2.  Non-Standard Properties . . . . . . . . . . . . . 142 - TODO (from VComponent, some X-properties may be defined here too)
         3.8.8.3.  Request Status  . . . . . . . . . . . . . . . . . 144 - NO (from VComponent)
 *
 */
public abstract class VEvent extends VComponent
{

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
//    public T withDescription(String value) { setDescription(value); return (T)this; } 
    
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
//    public T withDurationInSeconds(Integer value) { setDurationInSeconds(value); return (T)this; } 
//    public T withDurationInSeconds(String value) { setDurationInSeconds(value); return (T)this; } 
    
    /**
     * Date-Time End, DTEND from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * If entered this value is used to calculate the durationInSeconds, which is used
     * internally.
     */
    public void setDateTimeEnd(LocalDateTime dtEnd)
    {
        long seconds = ChronoUnit.SECONDS.between(getDateTimeStart(), dtEnd);
        System.out.println("seconds: " + seconds);
        setDurationInSeconds((int) seconds);
    }
    public void setDateTimeEnd(String dtEnd)
    {
        LocalDateTime dt = iCalendarDateTimeToLocalDateTime(dtEnd);
        setDateTimeEnd(dt);
    }
    public LocalDateTime getDateTimeEnd() { return getDateTimeStart().plusSeconds(getDurationInSeconds()); }
 
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
     * Start of range for which events are generated.  Should match the dates displayed on the calendar.
     */
    public LocalDateTime getDateTimeRangeStart() { return dateTimeRangeStart; }
    private LocalDateTime dateTimeRangeStart;
    public void setDateTimeRangeStart(LocalDateTime startDateTime) { this.dateTimeRangeStart = startDateTime; }
//    public T withDateTimeRangeStart(LocalDateTime startDateTime) { setDateTimeRangeStart(startDateTime); return (T)this; }
    
    /**
     * End of range for which events are generated.  Should match the dates displayed on the calendar.
     */
    public LocalDateTime getDateTimeRangeEnd() { return dateTimeRangeEnd; }
    private LocalDateTime dateTimeRangeEnd;
    public void setDateTimeRangeEnd(LocalDateTime endDateTime) { this.dateTimeRangeEnd = endDateTime; }
//    public T withDateTimeRangeEnd(LocalDateTime endDateTime) { setDateTimeRangeEnd(endDateTime); return (T)this; }
    
    
    // CONSTRUCTOR
    public VEvent(VEvent vevent)
    {
        super(vevent);
        copy(vevent, this);
    }
    
    public VEvent() { }
    
    /** Deep copy all fields from source to destination */
    private static void copy(VEvent source, VEvent destination)
    {
        destination.setDescription(source.getDescription());
        destination.setDurationInSeconds(source.getDurationInSeconds());
        if (source.getDateTimeRangeStart() != null) destination.setDateTimeRangeStart(source.getDateTimeRangeStart());
        if (source.getDateTimeRangeEnd() != null) destination.setDateTimeRangeEnd(source.getDateTimeRangeEnd());
    }

    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent destination)
    {
        copy(this, (VEvent) destination);
    }
    
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
    
//    // its already edited by RepeatableController
//    // changes to be made if ONE or FUTURE is selected.
//    // change back if CANCEL
//    public <T> WindowCloseType edit(
//              LocalDateTime dateTimeStart // start date/time of edited recurrence
//            , VEvent vEventOld // change back if cancel
//            , Collection<T> appointments // remove affected appointments
//            , Collection<VEvent> vevents // add new VEvents if change to one or future
//            , Callback<ChangeDialogOptions[], ChangeDialogOptions> changeDialogCallback // force change selection for testing
//            , Callback<Collection<VEvent>, Void> writeVEventsCallback) // I/O callback
//    {
//        if (this.equals(vEventOld)) return WindowCloseType.CLOSE_WITHOUT_CHANGE;
//        final RRuleType rruleType = getVEventType(vEventOld.getRRule());
//        System.out.println("rruleType " + rruleType);
//        boolean editedFlag = false;
//        switch (rruleType)
//        {
//        case HAD_REPEAT_BECOMING_INDIVIDUAL:
//            this.setRRule(null);
//        case WITH_NEW_REPEAT:
//        case INDIVIDUAL:
//            editedFlag = true;
//            break;
//        case WITH_EXISTING_REPEAT:
//            // Check if changes between vEvent and vEventOld exist apart from RRule
//            VEvent tempVEvent = VEventFactory.newVEvent(vEventOld);
//            tempVEvent.setRRule(getRRule());
//            boolean onlyRRuleChanged = this.equals(tempVEvent);
//
//            ChangeDialogOptions[] choices = null;
//            if (onlyRRuleChanged) choices = new ChangeDialogOptions[] {ChangeDialogOptions.ALL, ChangeDialogOptions.FUTURE};
//            ChangeDialogOptions changeResponse = changeDialogCallback.call(choices);
//            switch (changeResponse)
//            {
//            case ALL:
//                break;
//            case CANCEL:
//                break;
//            case FUTURE:
//                break;
//            case ONE:
//                // Make new individual VEvent, save settings to it.  Add date to original as recurrence.
//                VEvent newVEvent = VEventFactory.newVEvent(this);
//                newVEvent.setRRule(null);
//                newVEvent.setDateTimeStart(dateTimeStart);
//                vevents.remove(this);
//                vevents.add(vEventOld);
////                veventRefreshAppointments.call()
////                this = vEventOld;
//                vEventOld.copyTo(this);
//                break;
//            }
//            break;
//        default:
//            break;
//        }
//        // TODO - THIS MAY MEAN THIS HAS TO GO BACK TO IMPL - CAN USE CALLBACK
//        // DOESN'T KNOW ABOUT APPOINTMENTS HERE
//        
//        if (editedFlag) // make these changes as long as CANCEL is not selected
//        { // remove appointments from mail collection made by VEvent
//            Iterator<T> i = appointments.iterator();
//            while (i.hasNext())
//            {
//                T a = i.next();
//                if (appointments().contains(a)) i.remove();
//            }
//            appointments().clear(); // clear VEvent's collection of appointments
//            appointments.addAll(makeAppointments()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
//            return WindowCloseType.CLOSE_WITH_CHANGE;
//        } else
//        {
//            return WindowCloseType.CLOSE_WITHOUT_CHANGE;
//        }
//    }
//    
//    private RRuleType getVEventType(RRule rruleOld)
//    {
//
//        if (getRRule() == null)
//        {
//            if (rruleOld == null)
//            { // doesn't have repeat or have old repeat either
//                return RRuleType.INDIVIDUAL;
//            } else {
//                return RRuleType.HAD_REPEAT_BECOMING_INDIVIDUAL;
//            }
//        } else
//        { // RRule != null
//            if (rruleOld == null)
//            {
//                return RRuleType.WITH_NEW_REPEAT;                
//            } else {
//                return RRuleType.WITH_EXISTING_REPEAT;
//            }
//        }
//    }
    
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
