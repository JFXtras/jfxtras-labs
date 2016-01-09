package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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
    final private SimpleLongProperty durationInNanos = new SimpleLongProperty(this, DURATION_NAME);
    public SimpleLongProperty durationInNanosProperty() { return durationInNanos; }
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
    public void setDurationInNanos(Long value)
    {
        if (endPriority == null) endPriority = EndPriority.DURATION; // set endPriority only first time when either dateTimeEnd or duration is set
        durationInNanos.setValue(value);
    }
    public void setDurationInNanos(String value)
    { // parse ISO.8601.2004 period string into period of seconds (no support for Y (years) or M (months).
        long nanos = 0;
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
        if (! tokenIterator.hasNext() || (! firstString.equals("P"))) throw new IllegalArgumentException("Invalid DURATION string (" + value + "). Must begin with a P");
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
    private ChangeListener<? super Temporal> dateTimeEndlistener;
    private ChangeListener<? super Temporal> dateTimeStartlistener;
    private ChangeListener<? super Number> durationlistener;
    
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
        if (endPriority == null) endPriority = EndPriority.DTEND; // set endPriority only first time when either dateTimeEnd or duration is set
        dateTimeEnd.set(dtEnd);
    }
    public Temporal getDateTimeEnd() { return dateTimeEnd.get(); }
    private boolean isDateTimeEndWholeDay() { return getDateTimeEnd() instanceof LocalDate; }
//    private boolean useDateTimeEnd = true; // when true toString will output DTEND, default to true

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
    // private variables needed to changing Temporal class (LocalDate to LocalDateTime or opposite)
    private EndPriority endPriority;
    public static final long DEFAULT_DURATION = 3600L * NANOS_IN_SECOND;
    private LocalTime lastStartTime = LocalTime.of(10, 0);
    public LocalTime getLastStartTime() { return lastStartTime; }
    private long lastTimeBasedDuration = DEFAULT_DURATION; // Default to one hour duration
    
    // Change start, keep same Temporal type
    // this method is called by dateTimeStartlistener
    private void changeStartSameClass(Temporal newValue)
    {
        if (getDurationInNanos() == 0)
        {
            if (getDateTimeEnd() != null)
            { // set duration for the first time from dateTimeStart and dateTimeEnd
                final long nanos;
                if (newValue instanceof LocalDateTime)
                {
                    nanos = ChronoUnit.NANOS.between(newValue, getDateTimeEnd());
                } else if (newValue instanceof LocalDate)
                {
                    int days = Period.between((LocalDate) getDateTimeStart(), (LocalDate) getDateTimeEnd()).getDays();
                    nanos = (long) days * NANOS_IN_DAY;
                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");

                durationInNanosProperty().removeListener(durationlistener);
                setDurationInNanos(nanos);
                durationInNanosProperty().addListener(durationlistener);
            }
        } else
        {
            final Temporal dtEnd;
            if (newValue instanceof LocalDateTime)
            {
                dtEnd = newValue.plus(getDurationInNanos(), ChronoUnit.NANOS);
            } else if (newValue instanceof LocalDate)
            {
                dtEnd = newValue.plus(getDurationInNanos()/NANOS_IN_DAY, ChronoUnit.DAYS);
            } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newValue.getClass().getSimpleName() + ")");
            dateTimeEndProperty().removeListener(dateTimeEndlistener);
            setDateTimeEnd(dtEnd);
            dateTimeEndProperty().addListener(dateTimeEndlistener);
        }
    }
    
    // Change start, change Temporal type from LocalDateTime to LocalDate (changes to whole-day)
    // this method is called by dateTimeStartlistener
    private void changeStartToLocalDate(LocalDate newValue)
    {
        // Change dateTimeEnd
        if (getDurationInNanos() > 0)
        {
            long daysToAdd = getDurationInNanos()/NANOS_IN_DAY;
            lastTimeBasedDuration = getDurationInNanos();
            Temporal dtEnd = newValue.plus(daysToAdd+1, ChronoUnit.DAYS);
            setDateTimeEnd(dtEnd);
        }
        
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
        // Change dateTimeEnd
        if (getDurationInNanos() > 0)
        {
            Temporal dtEnd = LocalDateTime.from(newValue).plus(lastTimeBasedDuration, ChronoUnit.NANOS);
            setDateTimeEnd(dtEnd);
        }
        
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
    
    
    /* add listeners for dateTimeStart, dateTimeEnd and duration */
    private void setupListeners()
    {
        dateTimeEndlistener = (obs, oldSel, newSel) ->
        { // listener to synch dateTimeEnd and durationInSeconds
            if (getDateTimeStart() != null)
            {
                final long nanos;
                if (getDateTimeStart() instanceof LocalDateTime)
                {
                    nanos = ChronoUnit.NANOS.between(getDateTimeStart(), newSel);
                } else if (getDateTimeStart() instanceof LocalDate)
                {
                    int days = Period.between((LocalDate) getDateTimeStart(), (LocalDate) newSel).getDays();
                    nanos = (long) days * NANOS_IN_DAY;
                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + newSel.getClass().getSimpleName() + ")");

                durationInNanosProperty().removeListener(durationlistener);
                setDurationInNanos(nanos);
                durationInNanosProperty().addListener(durationlistener);
            }
        };
        
        // MAYBE I CAN HAVE THREE LISTENERS - ONE TO CHECK FOR TYPE CHANGING AND SWAP ONE FOR LOCALDATE AND OTHER LOCALDATETIME - 
        dateTimeStartlistener = (obs, oldValue, newValue) ->
        { // listener to synch dateTimeStart and durationInSeconds
            Class<? extends Temporal> oldClass = (oldValue == null) ? null : oldValue.getClass();
            Class<? extends Temporal> newClass = newValue.getClass();
            System.out.println("new Start:" + newValue);
            if (oldClass == null || (oldClass == newClass))
            {
                changeStartSameClass(newValue);
            } else
            {
                if (newClass.equals(LocalDate.class)) // change to LocalDate
                {
                    lastStartTime = LocalTime.from(oldValue);
                    changeStartToLocalDate((LocalDate) newValue);
                } else if (newClass.equals(LocalDateTime.class)) // change to LocalDateTime
                {
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
        
        durationlistener = (obs, oldSel, newSel) ->
        { // listener to synch dateTimeEnd and durationInSeconds.  dateTimeStart is left in place, dateTimeEnd moves to match duration.
            if (getDateTimeStart() != null)
            {
                final Temporal dtEnd;
                if (getDateTimeStart() instanceof LocalDateTime)
                {
                    dtEnd = getDateTimeStart().plus((long) newSel, ChronoUnit.NANOS);
                } else if (getDateTimeStart() instanceof LocalDate)
                {
                    dtEnd = getDateTimeStart().plus(((long) newSel)/NANOS_IN_DAY, ChronoUnit.DAYS);
                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported (" + getDateTimeStart().getClass().getSimpleName() + ")");
                dateTimeEndProperty().removeListener(dateTimeEndlistener);
                setDateTimeEnd(dtEnd);
                dateTimeEndProperty().addListener(dateTimeEndlistener);
            }
        };
        
        dateTimeEndProperty().addListener(dateTimeEndlistener); // synch duration with dateTimeEnd
        dateTimeStartProperty().addListener(dateTimeStartlistener); // synch duration with dateTimeStart
        durationInNanosProperty().addListener(durationlistener); // synch duration with dateTimeEnd
    }
    
    /** Deep copy all fields from source to destination.  Used both by copyTo method and copy constructor. */
    private static void copy(VEvent source, VEvent destination)
    {
        destination.setDescription(source.getDescription());
        destination.setDurationInNanos(source.getDurationInNanos());
        destination.setDateTimeStart(source.getDateTimeStart());
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
        Map<Property, String> properties = makePropertiesMap();
        String propertiesString = properties.entrySet()
                .stream() 
                .map(p -> p.getKey().getName() + ":" + p.getValue() + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }

    @Override
    protected Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();
        properties.putAll(super.makePropertiesMap());
        if ((getDescription() != null) && (! getDescription().equals(""))) properties.put(descriptionProperty(), getDescription());
        String endPrefix = (getDateTimeEnd() instanceof LocalDate) ? "VALUE=DATE:" : "";
        if (endPriority == EndPriority.DTEND) properties.put(dateTimeEndProperty(), endPrefix + VComponent.temporalToString(getDateTimeEnd()));
        if (endPriority == EndPriority.DURATION) properties.put(durationInNanosProperty(), getDurationAsString());
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
        boolean durationNull = getDurationInNanos() == 0;
        boolean endDateTimeNull = getDateTimeEnd() == null;
        if (durationNull && endDateTimeNull && getDateTimeStart() != null && ! isDateTimeStartWholeDay()) errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DURATION and DTEND can not be null.");

        Boolean s = (getDateTimeStart() == null) ? null: isDateTimeStartWholeDay();
        Boolean e = (getDateTimeEnd() == null) ? null: isDateTimeEndWholeDay();
        if ((s != null) && (e != null) && ((s && !e) || (!s && e))) errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DTSTART and DTEND must be both whole day or neither can be");

        Class<? extends Temporal> startClass = getDateTimeStart().getClass();
        Class<? extends Temporal> endClass = getDateTimeEnd().getClass();
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
                if (vEvent.getDurationInNanos() == 0)
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
    
    private static enum EndPriority
    {
        DURATION
      , DTEND;
    }
}
