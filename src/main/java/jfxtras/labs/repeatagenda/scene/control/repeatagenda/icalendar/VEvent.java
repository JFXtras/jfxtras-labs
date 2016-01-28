package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.RRuleType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;

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
 * @author David Bal
 * @see VEventImpl
 */
public abstract class VEvent<T> extends VComponentAbstract<T>
{   
    private static final long NANOS_IN_WEEK = Duration.ofDays(7).toNanos();
    private static final long NANOS_IN_HOUR = Duration.ofHours(1).toNanos();
    private static final long NANOS_IN_MINUTE = Duration.ofMinutes(1).toNanos();
    private static final long NANOS_IN_SECOND = Duration.ofSeconds(1).toNanos();
    
    private static final String DESCRIPTION_NAME = "DESCRIPTION";
    private static final String DURATION_NAME = "DURATION";
    private static final String DATE_TIME_END_NAME = "DTEND";
    /**
     * DESCRIPTION: RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     */
    public StringProperty descriptionProperty() { return description; }
    final private StringProperty description = new SimpleStringProperty(this, DESCRIPTION_NAME);
    public String getDescription() { return description.getValue(); }
    public void setDescription(String value) { description.setValue(value); }
//    public T withDescription(String value) { setDescription(value); return (T)this; } 
    
    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Internally stored a seconds.  Can be set an an integer of seconds or a string as defined by iCalendar which is
     * converted to seconds.  This value is used exclusively internally.  Any specified DTEND is converted to 
     * durationInSeconds,
     * */
    final private ObjectProperty<Long> durationInNanos = new SimpleObjectProperty<>(this, DURATION_NAME);
    public ObjectProperty<Long> durationInNanosProperty() { return durationInNanos; }
    public Long getDurationInNanos() { return durationInNanos.getValue(); }
    public String getDurationAsString()
    {
        StringBuilder duration = new StringBuilder("P");
        Long nanos = getDurationInNanos();

        Long weeks = nanos / NANOS_IN_WEEK;
        if (weeks > 0) duration.append(weeks + "W");
        nanos -= NANOS_IN_WEEK * weeks;

        Long days = nanos / NANOS_IN_DAY;
        if (days > 0) duration.append(days + "D");
        nanos -= NANOS_IN_DAY * days;

        Long hours = nanos / NANOS_IN_HOUR;
        boolean addedT = false;
        if (hours > 0)
        {
            addedT = true;
            duration.append("T");
            duration.append(hours + "H");
        }
        nanos -= NANOS_IN_HOUR * hours;

        Long minutes = nanos / NANOS_IN_MINUTE;
        if (minutes > 0)
        {
            if (! addedT) duration.append("T");
            addedT = true;
            duration.append(minutes + "M");
        }
        nanos -= NANOS_IN_MINUTE * minutes;

        if (nanos > 0)
        {
            if (! addedT) duration.append("T");
            addedT = true;
            Long seconds = nanos / NANOS_IN_SECOND;
            duration.append(seconds + "S");
        }

        return duration.toString();
    }
    public void setDurationInNanos(Long duration)
    {
        if (duration != null)
        {
            if (duration > 0) endPriority = EndPriority.DURATION;
            else if (duration == 0)
            {
                endPriority = null;
                duration = null;
            }
            else if (duration < 0) throw new IllegalArgumentException("Duration must be greater than zero.");
        }
        durationInNanos.set(duration);
    }
    public void setDurationInNanos(String duration)
    { // parse ISO.8601.2004 period string into period of seconds (no support for Y (years) or M (months).
        long nanos = 0;
        Pattern p = Pattern.compile("([0-9]+)|([A-Z])");
        Matcher m = p.matcher(duration);
        List<String> tokens = new ArrayList<String>();
        while (m.find())
        {
            String token = m.group(0);
            tokens.add(token);
        }
        Iterator<String> tokenIterator = tokens.iterator();
        String firstString = tokenIterator.next();
        if (! tokenIterator.hasNext() || (! firstString.equals("P"))) throw new IllegalArgumentException("Invalid DURATION string (" + duration + "). Must begin with a P");
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
                    nanos += n * NANOS_IN_WEEK;
                } else if (time.equals("D"))
                { // days
                    nanos += n * NANOS_IN_DAY;
                } else if (timeFlag && time.equals("H"))
                { // hours
                    nanos += n * NANOS_IN_HOUR;                   
                } else if (timeFlag && time.equals("M"))
                { // minutes
                    nanos += n * NANOS_IN_MINUTE;                                        
                } else if (timeFlag && time.equals("S"))
                { // seconds
                    nanos += n * NANOS_IN_SECOND;                    
                } else
                {
                    throw new IllegalArgumentException("Invalid DURATION string time element (" + time + "). Must begin with a P, or Time character T not found");
                }
            } else if (token.equals("T")) timeFlag = true; // proceeding elements will be hour, minute or second
        }
        durationInNanos.setValue(nanos);
    }
//    private ChangeListener<? super Temporal> dateTimeEndlistener;
    private ChangeListener<? super Temporal> dateTimeStartlistener;
//    private ChangeListener<? super Number> durationlistener;
    
    /**
     * DTEND, Date-Time End. from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * If entered this value is used to calculate the durationInSeconds, which is used
     * internally.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    final private ObjectProperty<Temporal> dateTimeEnd = new SimpleObjectProperty<>(this, DATE_TIME_END_NAME);
    public ObjectProperty<Temporal> dateTimeEndProperty() { return dateTimeEnd; }
    public void setDateTimeEnd(Temporal dtEnd)
    {
        if (dtEnd != null) endPriority = EndPriority.DTEND;
        else if (dtEnd == null) endPriority = null;
        dateTimeEnd.set(dtEnd);
    }
    public Temporal getDateTimeEnd() { return dateTimeEnd.get(); }
    private boolean isDateTimeEndWholeDay() { return getDateTimeEnd() instanceof LocalDate; }

    /** Indicates end option, DURATION or DTEND. */
    public EndPriority getEndPriority() { return endPriority; }
    private EndPriority endPriority;

    
    // CONSTRUCTORS
    public VEvent(VEvent<T> vevent)
    {
        super(vevent);
        copy(vevent, this);
        setupListeners();
    }
    
    public VEvent()
    {
        super();
        setupListeners();
    }


    //    public static final long DEFAULT_DURATION = 3600L * NANOS_IN_SECOND;
//    private LocalTime lastStartTime = LocalTime.of(10, 0);
//    public LocalTime getLastStartTime() { return lastStartTime; }
//    private long lastTimeBasedDuration = DEFAULT_DURATION; // Default to one hour duration
    
//    // Change start, keep same Temporal type
//    // this method is called by dateTimeStartlistener
//    @Deprecated
//    private void changeStartSameClass(Temporal newValue)
//    {
//        if (getDurationInNanos() == 0)
//        {
//            if (getDateTimeEnd() != null)
//            { // set duration for the first time from dateTimeStart and dateTimeEnd
//                final long nanos;
//                if (newValue instanceof LocalDateTime)
//                {
//                    nanos = ChronoUnit.NANOS.between(newValue, getDateTimeEnd());
//                } else if (newValue instanceof LocalDate)
//                {
//                    int days = Period.between((LocalDate) getDateTimeStart(), (LocalDate) getDateTimeEnd()).getDays();
//                    nanos = (long) days * NANOS_IN_DAY;
//                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");
//
//                durationInNanosProperty().removeListener(durationlistener);
//                setDurationInNanos(nanos);
//                durationInNanosProperty().addListener(durationlistener);
//            }
//        } else
//        {
//            final Temporal dtEnd;
//            if (newValue instanceof LocalDateTime)
//            {
//                dtEnd = newValue.plus(getDurationInNanos(), ChronoUnit.NANOS);
//            } else if (newValue instanceof LocalDate)
//            {
//                dtEnd = newValue.plus(getDurationInNanos()/NANOS_IN_DAY, ChronoUnit.DAYS);
//            } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");
//            System.out.println("new dtEnd:" + dtEnd);
////            dateTimeEndProperty().removeListener(dateTimeEndlistener);
//            setDateTimeEnd(dtEnd);
////            dateTimeEndProperty().addListener(dateTimeEndlistener);
//        }
//    }
    
    // Change start, change Temporal type from LocalDateTime to LocalDate (changes to whole-day)
    // this method is called by dateTimeStartlistener
    private void changeStartToLocalDate(LocalDate newValue)
    {
//        // Change dateTimeEnd
//        if (endPriority == EndPriority.DTEND)
//        {
//            long daysToAdd = getDurationInNanos()/NANOS_IN_DAY;
//            lastTimeBasedDuration = getDurationInNanos();
//            Temporal dtEnd = newValue.plus(daysToAdd+1, ChronoUnit.DAYS);
//            setDateTimeEnd(dtEnd);
//        }
        
        // Change ExDates to LocalDate
        if (getExDate() != null)
        {
            Set<LocalDate> newExDates = getExDate().getTemporals()
                    .stream()
                    .map(t -> LocalDate.from(t))
                    .collect(Collectors.toSet());
            getExDate().getTemporals().clear();
            getExDate().getTemporals().addAll(newExDates);
        }

        // Change RDates to LocalDate
        if (getRDate() != null)
        {
            Set<LocalDate> newRDates = getRDate().getTemporals()
                    .stream()
                    .map(t -> LocalDate.from(t))
                    .collect(Collectors.toSet());
            getExDate().getTemporals().clear();
            getRDate().getTemporals().addAll(newRDates);
        }
        
        // Change Until to LocalDate
        if (getRRule() != null)
        {
            Temporal until = getRRule().getUntil();
            if (until != null) getRRule().setUntil(LocalDate.from(until));
        }
    }

    // Change start, change Temporal type from LocalDate to LocalDateTime
    // this method is called by dateTimeStartlistener
    private void changeStartToLocalDateTime(LocalDateTime newValue)
    {        
        LocalTime time = LocalTime.from(newValue);
        // Change ExDates to LocalDate
        if (getExDate() != null)
        {
            Set<LocalDateTime> newExDates = getExDate().getTemporals()
                    .stream()
                    .map(t -> LocalDate.from(t).atTime(time))
                    .collect(Collectors.toSet());
            getExDate().getTemporals().clear();
            getExDate().getTemporals().addAll(newExDates);
        }

        // Change RDates to LocalDate
        if (getRDate() != null)
        {
            Set<LocalDateTime> newRDates = getRDate().getTemporals()
                    .stream()
                    .map(t -> LocalDate.from(t).atTime(time))
                    .collect(Collectors.toSet());
            getExDate().getTemporals().clear();
            getRDate().getTemporals().addAll(newRDates);
        }
        
        // Change Until to LocalDate
        if (getRRule() != null)
        {
            Temporal until = getRRule().getUntil();
            if (until != null) getRRule().setUntil(LocalDate.from(until).atTime(time));
        }        
    }
    
    
    /* add listeners for dateTimeStart, dateTimeEnd and duration */
    private void setupListeners()
    {
//        dateTimeEndlistener = (obs, oldSel, newSel) ->
//        { // listener to synch dateTimeEnd and durationInSeconds
//            if (getDateTimeStart() != null)
//            {
//                final long nanos;
//                if (getDateTimeStart() instanceof LocalDateTime)
//                {
//                    nanos = ChronoUnit.NANOS.between(getDateTimeStart(), newSel);
//                } else if (getDateTimeStart() instanceof LocalDate)
//                {
//                    int days = Period.between((LocalDate) getDateTimeStart(), (LocalDate) newSel).getDays();
//                    nanos = (long) days * NANOS_IN_DAY;
//                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newSel.getClass().getSimpleName() + ")");
//                durationInNanosProperty().removeListener(durationlistener);
//                setDurationInNanos(nanos);
//                durationInNanosProperty().addListener(durationlistener);
//            }
//        };
        
        // MAYBE I CAN HAVE THREE LISTENERS - ONE TO CHECK FOR TYPE CHANGING AND SWAP ONE FOR LOCALDATE AND OTHER LOCALDATETIME - 
        // TODO - SHOULD THESE LISTENERS DISAPPEAR?  SHOULD THE IMPLEMENTATION HANDLE THIS? - CONTROLLER 
        // I NEED DURATION THOUGH
        dateTimeStartlistener = (obs, oldValue, newValue) ->
        { // listener to synch dateTimeStart and durationInSeconds
            Class<? extends Temporal> oldClass = (oldValue == null) ? null : oldValue.getClass();
            Class<? extends Temporal> newClass = newValue.getClass();
            System.out.println("new Start:" + newValue);
            if ((oldClass != null) && (newClass != oldClass))
            {
                if (newClass.equals(LocalDate.class)) // change to LocalDate
                {
                    System.out.println("change to localDate:");
//                    lastStartTime = LocalTime.from(oldValue);
                    changeStartToLocalDate((LocalDate) newValue);
                } else if (newClass.equals(LocalDateTime.class)) // change to LocalDateTime
                {
                    System.out.println("change to localDateTime:");
                    changeStartToLocalDateTime((LocalDateTime) newValue);
                }
            }
        };
//        // dateTimeStart controls the type of all other Temporal fields.  When it changes dateTimeEnd must change too.
//        // TODO - VERIFY OTHER TEMPORALS - EXCEPTIONS, UNTIL, RECURRENCES CHANGE TOO
//        // SHOULD I HAVE ANOTHER LISTENER THAT ONLY HANDLES TYPE CHANGES?
//        ChangeListener<? super Temporal> dateTimeStartlistener2 = (obs, oldValue, newValue) ->
//        { // listener to synch dateTimeStart and durationInSeconds
//            if (getDurationInNanos() == 0)
//            {
//                if (getDateTimeEnd() != null)
//                { // set duration for the first time from dateTimeStart and dateTimeEnd
//                    final long nanos;
//                    if (newValue instanceof LocalDateTime)
//                    {
//                        nanos = ChronoUnit.NANOS.between(newValue, getDateTimeEnd());
//                    } else if (newValue instanceof LocalDate)
//                    {
//                        int days = Period.between((LocalDate) getDateTimeStart(), (LocalDate) getDateTimeEnd()).getDays();
//                        nanos = (long) days * NANOS_IN_DAY;
//                    } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");
//
//                    durationInNanosProperty().removeListener(durationlistener);
//                    setDurationInNanos(nanos);
//                    durationInNanosProperty().addListener(durationlistener);
//                }
//            } else
//            { // has duration set previously
//                final Temporal dtEnd;
//                final boolean removeListener;
//                if (newValue instanceof LocalDateTime)
//                {
//                    if (getDateTimeEnd() instanceof LocalDateTime)
//                    {
//                        dtEnd = newValue.plus(getDurationInNanos(), ChronoUnit.NANOS);
//                        removeListener = true;
//                    } else if (getDateTimeEnd() instanceof LocalDate)
//                    { // change from LocalDate to LocalDateTime
//                        dtEnd = LocalDateTime.from(newValue).plus(lastTimeBasedDuration, ChronoUnit.NANOS);
//                        System.out.println("new end:" + dtEnd);
//                        removeListener = false;
//                    } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported");
//                } else if (newValue instanceof LocalDate)
//                {
//                    long amountToAdd = getDurationInNanos()/NANOS_IN_DAY;
//                    if (getDateTimeEnd() instanceof LocalDateTime)
//                    {
//                        lastStartTime = LocalTime.from(oldValue);
//                        lastTimeBasedDuration = getDurationInNanos();
//                        dtEnd = newValue.plus(amountToAdd+1, ChronoUnit.DAYS);
//                        removeListener = false;
//                    } else if (getDateTimeEnd() instanceof LocalDate)
//                    {
//                        dtEnd = newValue.plus(amountToAdd, ChronoUnit.DAYS);
//                        removeListener = true;
//                    } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported");
//                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");
//                if (removeListener) dateTimeEndProperty().removeListener(dateTimeEndlistener);
//                setDateTimeEnd(dtEnd);
//                if (removeListener) dateTimeEndProperty().addListener(dateTimeEndlistener);
//            }
//        };
        
//        durationlistener = (obs, oldSel, newSel) ->
//        { // listener to synch dateTimeEnd and durationInSeconds.  dateTimeStart is left in place, dateTimeEnd moves to match duration.
//            if (getDateTimeStart() != null)
//            {
//                final Temporal dtEnd;
//                if (getDateTimeStart() instanceof LocalDateTime)
//                {
//                    dtEnd = getDateTimeStart().plus((long) newSel, ChronoUnit.NANOS);
//                } else if (getDateTimeStart() instanceof LocalDate)
//                {
//                    dtEnd = getDateTimeStart().plus(((long) newSel)/NANOS_IN_DAY, ChronoUnit.DAYS);
//                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + getDateTimeStart().getClass().getSimpleName() + ")");
//                dateTimeEndProperty().removeListener(dateTimeEndlistener);
//                setDateTimeEnd(dtEnd);
//                dateTimeEndProperty().addListener(dateTimeEndlistener);
//            }
//        };
        
//        dateTimeEndProperty().addListener(dateTimeEndlistener); // synch duration with dateTimeEnd
        dateTimeStartProperty().addListener(dateTimeStartlistener); // synch duration with dateTimeStart
//        durationInNanosProperty().addListener(durationlistener); // synch duration with dateTimeEnd
    }
    
    /** Deep copy all fields from source to destination.  Used both by copyTo method and copy constructor. 
     * @param <U> instance type
     * */
    private static <U> void copy(VEvent<U> source, VEvent<U> destination)
    {
        destination.setDescription(source.getDescription());
        destination.setDurationInNanos(source.getDurationInNanos());
        destination.setDateTimeEnd(source.getDateTimeEnd());
    }

    /** Deep copy all fields from this to destination */
    @Override
    public void copyTo(VComponent<T> destination)
    {
        super.copyTo(destination);
        copy(this, (VEvent<T>) destination);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        @SuppressWarnings("unchecked")
        VEvent<T> testObj = (VEvent<T>) obj;

        boolean descriptionEquals = (getDescription() == null) ?
                (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
        boolean durationEquals = (getDurationInNanos() == null) ?
                (testObj.getDurationInNanos() == null) : getDurationInNanos().equals(testObj.getDurationInNanos());
        System.out.println("VEvent: " + descriptionEquals + " " + durationEquals);
        // don't need to check getDateTimeEnd because it is bound to duration
        return super.equals(obj) && descriptionEquals && durationEquals;
    }
    
    /** Make iCalendar compliant string of VEvent calendar component.
     * This method should be overridden by an implementing class if that
     * class contains any extra properties. */
    @Override
    public String toString()
    {
        @SuppressWarnings("rawtypes")
        Map<Property, String> properties = makePropertiesMap();
        String propertiesString = properties.entrySet()
                .stream() 
                .map(p -> p.getKey().getName() + ":" + p.getValue() + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();
        properties.putAll(super.makePropertiesMap());
        if ((getDescription() != null) && (! getDescription().equals(""))) properties.put(descriptionProperty(), getDescription());
        if (endPriority != null)
        {
            switch (endPriority)
            {
            case DTEND:
                String endPrefix = (getDateTimeEnd() instanceof LocalDate) ? "VALUE=DATE:" : "";
                properties.put(dateTimeEndProperty(), endPrefix + VComponent.temporalToString(getDateTimeEnd()));
                break;
            case DURATION:
                properties.put(durationInNanosProperty(), getDurationAsString());
                break;
            }
        }
        return properties;
    }
    
    @Override
    public String makeErrorString()
    {
        StringBuilder errorsBuilder = new StringBuilder(super.makeErrorString());

        if ((getDateTimeEnd() != null) && (! VComponent.isAfter(getDateTimeEnd(), getDateTimeStart())))
            errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  DTEND (" + getDateTimeEnd()
            + ") must be after DTSTART (" + getDateTimeStart() + ")");
        
        // Note: Check for invalid condition where both DURATION and DTEND not being null is done in parseVEvent.
        // It is not checked here due to bindings between both DURATION and DTEND.
        boolean durationNull = getDurationInNanos() == null;
        boolean endDateTimeNull = getDateTimeEnd() == null;
        if (durationNull && endDateTimeNull && getDateTimeStart() != null && ! isDateTimeStartWholeDay()) errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DURATION and DTEND can not be null.");

        Boolean s = (getDateTimeStart() == null) ? null: isDateTimeStartWholeDay();
        Boolean e = (getDateTimeEnd() == null) ? null: isDateTimeEndWholeDay();
        if ((s != null) && (e != null) && ((s && !e) || (!s && e))) errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DTSTART and DTEND must be both whole day or neither can be");

        Class<? extends Temporal> startClass = getDateTimeStart().getClass();
        Class<? extends Temporal> endClass = (getDateTimeEnd() != null) ? getDateTimeEnd().getClass() : startClass;
        if (! startClass.equals(endClass)) errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DTSTART and DTEND must have the same Temporal type (" + startClass.getSimpleName() + "," + endClass.getSimpleName() + ")");
        
        return errorsBuilder.toString();
    }
    
    /** This method should be called by a method in the implementing class the
     * makes a new object and passes it here as vEvent.
     * @param <U>
     * @param vEvent
     * @param strings
     * @return
     */
    protected static <U> VEvent<U> parseVEvent(VEvent<U> vEvent, List<String> strings)
    {
        // Test for correct beginning and end, then remove
        if (! strings.get(0).equals("BEGIN:VEVENT"))
        {
            throw new IllegalArgumentException("Invalid calendar component. First element must be BEGIN:VEVENT");
        } else
        {
            strings.remove(0);
        }
        if (! strings.get(strings.size()-1).equals("END:VEVENT"))
        {
            throw new IllegalArgumentException("Invalid calendar component. Last element must be END:VEVENT");
        } else
        {
            strings.remove(strings.size()-1);
        }
        
        Iterator<String> stringsIterator = strings.iterator();
        boolean dTEndFound = false;
        boolean durationFound = false;
        while (stringsIterator.hasNext())
        {
            String line = stringsIterator.next();
            String property = line.substring(0, line.indexOf(":"));
            String value = line.substring(line.indexOf(":") + 1).trim();
            if (property.equals(DESCRIPTION_NAME))
            { // DESCRIPTION
                    if (vEvent.getDescription() == null)
                    {
                        vEvent.setDescription(value);
                        stringsIterator.remove();
                    } else
                    {
                        throw new IllegalArgumentException("Invalid VEvent: DESCRIPTION can only be specified once");
                    }
            } else if (property.equals(DURATION_NAME))
            { // DURATION
                if (vEvent.getDurationInNanos() == null)
                {
                    if (dTEndFound == false)
                    {
                        durationFound = true;
                        vEvent.endPriority = EndPriority.DURATION;
//                        vEvent.useDuration = true;
//                        vEvent.useDateTimeEnd = false;
                        vEvent.setDurationInNanos(value);
                        stringsIterator.remove();
                    } else
                    {
                        throw new IllegalArgumentException("Invalid VEvent: Can't contain both DTEND and DURATION.");
                    }
                } else
                {
                    throw new IllegalArgumentException("Invalid VEvent: DURATION can only be specified once.");                    
                }
            } else if (property.equals(DATE_TIME_END_NAME))
            { // DTEND
                if (vEvent.getDateTimeEnd() == null)
                {
                    if (durationFound == false)
                    {
                        dTEndFound = true;
                        vEvent.endPriority = EndPriority.DTEND;
//                        vEvent.useDuration = false;
//                        vEvent.useDateTimeEnd = true;
//                        VDateTime dateTime = VDateTime.parseString(value);
                        Temporal dateTime = VComponent.parseTemporal(value);
                        System.out.println("dateTime:" + dateTime);
                        vEvent.setDateTimeEnd(dateTime);
                        stringsIterator.remove();
                    } else
                    {
                        throw new IllegalArgumentException("Invalid VEvent: Can't contain both DTEND and DURATION.");
                    }
                } else
                {
                    throw new IllegalArgumentException("Invalid VEvent: DTEND can only be specified once.");                                        
                }
            }
        }
        return (VEvent<U>) VComponentAbstract.parseVComponent(vEvent, strings);
    }
    
    /**
     * Saves changes to a VEvent after modifying an instance
     * If the VEvent has a repeat rule a dialog prompting the user to change one, this-and-future or all
     * events in the series.
     * 
     * Note: Doesn't work for a VComponent DTEND is needed, which is not in VComponent.
     * It is possible to provide similar functionality for different VComponents with modifications.
     * 
     * @param vEvent
     * @param vEventOriginal
     * @param vComponents
     * @param startOriginalInstance
     * @param startInstance
     * @param endInstance
     * @param instances
     */
    public static <U> void saveChange(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<U> instances)
    {
        final RRuleType rruleType = ICalendarUtilities.getRRuleType(vEvent.getRRule(), vEventOriginal.getRRule());
        boolean incrementSequence = true;
        System.out.println("rrule: " + rruleType);
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            vEvent.setRRule(null);
            vEvent.setRDate(null);
            vEvent.setExDate(null);
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            if (! vEvent.equals(vEventOriginal)) updateAppointments(vEvent, instances);
            break;
        case WITH_EXISTING_REPEAT:
            if (! vEvent.equals(vEventOriginal)) // if changes occurred
            {
                List<VComponent<U>> relatedVComponents = VComponent.findRelatedVComponents(vComponents, vEvent);
                Map<ChangeDialogOption, String> choices = new LinkedHashMap<>();
                String one = VComponent.temporalToStringPretty(startInstance);
                choices.put(ChangeDialogOption.ONE, one);
                if (! vEvent.isIndividual())
                {
                    {
                        String future = VComponent.relativesRangeToString(relatedVComponents, startInstance);
                        choices.put(ChangeDialogOption.THIS_AND_FUTURE, future);
                    }
                    String all = VComponent.relativesRangeToString(relatedVComponents);
                    choices.put(ChangeDialogOption.ALL, all);
                }

                
                ChangeDialogOption changeResponse = ICalendarUtilities.repeatChangeDialog(choices);
                switch (changeResponse)
                {
                case ALL:
                    Collection<VComponent<U>> editList = VComponent.findRelatedVComponents(vComponents, vEvent);
                    if (relatedVComponents.size() == 1) updateAppointments(vEvent, instances);
                    else
                    {
                        relatedVComponents.stream().forEach(v -> 
                        {
                            // Copy ExDates
                            if (v.getExDate() != null)
                            {
                                if (vEvent.getExDate() == null)
                                { // make new EXDate object for destination if necessary
                                    try {
                                        EXDate newEXDate = v.getExDate().getClass().newInstance();
                                        vEvent.setExDate(newEXDate);
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                              }
                              v.getExDate().getTemporals().addAll(v.getExDate().getTemporals());
                            }
                            // update start and end dates
                            if (VComponent.isBefore(v.getDateTimeStart(), vEvent.getDateTimeStart()))
                            {
                                final Temporal startNew;
                                final Temporal endNew;
                                if (vEvent.getDateTimeStart() instanceof LocalDateTime)
                                {
                                    LocalTime startTime = LocalTime.from(vEvent.getDateTimeStart());
                                    startNew = LocalDate.from(v.getDateTimeStart()).atTime(startTime);
                                    long shift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), startNew);
                                    endNew = vEvent.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
                                } else if (vEvent.getDateTimeStart() instanceof LocalDate)
                                {
                                    startNew = v.getDateTimeStart();
                                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
                                vEvent.setDateTimeStart(startNew);
                            }
                        });
                    }
                    break;
                case CANCEL:
                    vEventOriginal.copyTo(vEvent); // return to original vEvent
                    incrementSequence = false;
                    break;
                case THIS_AND_FUTURE:
                    editThisAndFuture(vEvent, vEventOriginal, vComponents, startOriginalInstance, startInstance, instances);
                    break;
                case ONE:
                    editOne(vEvent, vEventOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
                    break;
                default:
                    break;
                }
            }
        }
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        if (incrementSequence) vEvent.incrementSequence();
//        System.out.println(vEvent);
    }
    
    private static <U> void updateAppointments(VComponent<U> vEvent, Collection<U> instances)
    {
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
        vEvent.instances().clear(); // clear VEvent's outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
        instances.clear();
        instances.addAll(instancesTemp);
    }
    
    /*
     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
     */
    private static <U> void editOne(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<U> instances)
    {
        if (vEvent.isWholeDay())
        {
            LocalDate start = LocalDate.from(startInstance);
            vEvent.setDateTimeStart(start);
            LocalDate end = LocalDate.from(endInstance);
            vEvent.setDateTimeEnd(end);
        } else
        {
            vEvent.setDateTimeStart(startInstance);
            vEvent.setDateTimeEnd(endInstance);
        }
        vEvent.setRRule(null);
        vEvent.setDateTimeRecurrence(startOriginalInstance);
        vEvent.setDateTimeStamp(LocalDateTime.now());
   
              // Add recurrence to original vEvent
        vEventOriginal.getRRule().getRecurrences().add(vEvent);

        // Check for validity
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        if (! vEventOriginal.isValid()) throw new RuntimeException(vEventOriginal.makeErrorString());
        
        // Remove old appointments, add back ones
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEventOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vEventOriginal.instances().clear(); // clear vEventOriginal outdated collection of appointments
        instancesTemp.addAll(vEventOriginal.makeInstances()); // make new appointments and add to main collection (added to vEventNew's collection in makeAppointments)
        vEvent.instances().clear(); // clear vEvent outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
        instances.clear();
        instances.addAll(instancesTemp);
        vComponents.add(vEventOriginal); // TODO - LET LISTENER ADD NEW APPOINTMENTS OR ADD THEM HERE?

        System.out.println(vEventOriginal);
    }
    
    /*
     * Changing this and future instances in VComponent is done by ending the previous
     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.  vEventNew has new settings, vEvent has former settings.
     */
    private static <U> void editThisAndFuture(
            VEvent<U> vEvent
          , VEvent<U> vEventOriginal
          , Collection<VComponent<U>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Collection<U> instances)
    {
        // adjust original VEvent
        if (vEventOriginal.getRRule().getCount() != null) vEventOriginal.getRRule().setCount(0);
        Temporal previousDay = startOriginalInstance.minus(1, ChronoUnit.DAYS);
        Temporal untilNew = (vEvent.isWholeDay()) ? LocalDate.from(previousDay).atTime(23, 59, 59) : previousDay; // use last second of previous day, like Yahoo
        vEventOriginal.getRRule().setUntil(untilNew);
        
        // Adjust new VEvent
        long shift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), startInstance);
        Temporal endNew = vEvent.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
        vEvent.setDateTimeEnd(endNew);
        vEvent.setDateTimeStart(startInstance);
        vEvent.setUniqueIdentifier();
        String relatedUID = (vEventOriginal.getRelatedTo() == null) ? vEventOriginal.getUniqueIdentifier() : vEventOriginal.getRelatedTo();
        vEvent.setRelatedTo(relatedUID);
        vEvent.setDateTimeStamp(LocalDateTime.now());
        System.out.println("unti2l:" + vEvent.getRRule().getUntil());
        
        // Split EXDates dates between this and newVEvent
        if (vEvent.getExDate() != null)
        {
            vEvent.getExDate().getTemporals().clear();
            final Iterator<Temporal> exceptionIterator = vEvent.getExDate().getTemporals().iterator();
            while (exceptionIterator.hasNext())
            {
                Temporal d = exceptionIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    exceptionIterator.remove();
                } else {
                    vEvent.getExDate().getTemporals().add(d);
                }
            }
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
        }

        // Split recurrence date/times between this and newVEvent
        if (vEvent.getRDate() != null)
        {
            vEvent.getRDate().getTemporals().clear();
            final Iterator<Temporal> recurrenceIterator = vEvent.getRDate().getTemporals().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, startInstance);
                if (result < 0)
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRDate().getTemporals().add(d);
                }
            }
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
        }

        // Split instance dates between this and newVEvent
        if (vEvent.getRRule().getRecurrences() != null)
        {
            vEvent.getRRule().getRecurrences().clear();
            final Iterator<VComponent<?>> recurrenceIterator = vEvent.getRRule().getRecurrences().iterator();
            while (recurrenceIterator.hasNext())
            {
                VComponent<?> d = recurrenceIterator.next();
                if (VComponent.isBefore(d.getDateTimeRecurrence(), startInstance))
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRRule().getRecurrences().add(d);
                }
            }
        }
        
        // Modify COUNT for the edited vEvent
        if (vEvent.getRRule().getCount() > 0)
        {
            int countInOrginal = vEventOriginal.makeInstances().size();
            int countInNew = vEvent.getRRule().getCount() - countInOrginal;
//            final int newCount = (int) vEvent.instances()
//                    .stream()
//                    .map(a -> a.getStartLocalDateTime())
//                    .filter(d -> ! VComponent.isBefore(d, startInstance))
//                    .count();
            vEvent.getRRule().setCount(countInNew);
        }
        
        if (! vEventOriginal.isValid()) throw new RuntimeException(vEventOriginal.makeErrorString());
        vComponents.add(vEventOriginal);

        // Remove old appointments, add back ones
        Collection<U> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        instancesTemp.addAll(instances);
        instancesTemp.removeIf(a -> vEventOriginal.instances().stream().anyMatch(a2 -> a2 == a));
        vEventOriginal.instances().clear(); // clear vEvent outdated collection of appointments
        instancesTemp.addAll(vEventOriginal.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
        vEvent.instances().clear(); // clear vEvent's outdated collection of appointments
        instancesTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
        instances.clear();
        instances.addAll(instancesTemp);
        
//        vComponents.stream().forEach(System.out::println);
//        System.out.println("vEvent:" + vEvent);
//        System.out.println("vComponents:" + vComponents.size());
    }
    
    public static enum EndPriority
    {
        DURATION
      , DTEND;
    }
}
