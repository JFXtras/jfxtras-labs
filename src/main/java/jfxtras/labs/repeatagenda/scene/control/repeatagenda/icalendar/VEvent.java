package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
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
    private ChangeListener<? super Temporal> dateTimeStartlistener;
    
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

    /** Indicates end option, DURATION or DTEND. */
    public EndPriority endPriority() { return endPriority; }
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
    
    // Change start, change Temporal type from LocalDateTime to LocalDate (changes to whole-day)
    // this method is called by dateTimeStartlistener
    private void changeStartToLocalDate(LocalDate newValue)
    {
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
        dateTimeStartlistener = (obs, oldValue, newValue) ->
        { // listener to synch dateTimeStart and durationInSeconds
            Class<? extends Temporal> oldClass = (oldValue == null) ? null : oldValue.getClass();
            Class<? extends Temporal> newClass = newValue.getClass();
            if ((oldClass != null) && (newClass != oldClass))
            {
                if (newClass.equals(LocalDate.class)) // change to LocalDate
                {
                    changeStartToLocalDate((LocalDate) newValue);
                } else if (newClass.equals(LocalDateTime.class)) // change to LocalDateTime
                {
                    changeStartToLocalDateTime((LocalDateTime) newValue);
                }
            }
        };
        dateTimeStartProperty().addListener(dateTimeStartlistener); // synch duration with dateTimeStart
    }

    @Override
    protected void becomingIndividual(VComponent<T> vComponentOriginal, Temporal startInstance, Temporal endInstance)
    {
        super.becomingIndividual(vComponentOriginal, startInstance, endInstance);
        if ((vComponentOriginal.getRRule() != null) && (endPriority() == EndPriority.DTEND))
        { // RRULE was removed, update DTEND
            setDateTimeEnd(endInstance);
        }
    }

    @Override // edit end date or date/time
    protected void editOne(
            VComponent<T> vComponentOriginal
          , Collection<VComponent<T>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<T> instances)
    {
        switch (endPriority())
        {
        case DTEND:
            Temporal endNew = (isWholeDay()) ?  LocalDate.from(endInstance) : endInstance;
            setDateTimeEnd(endNew);
            break;
        case DURATION:
            long nanos = ChronoUnit.NANOS.between(startInstance, endInstance);
            setDurationInNanos(nanos);
            break;
        default:
            break;        
        }
        super.editOne(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
    }

    @Override // edit end date or date/time
    protected void editThisAndFuture(
            VComponent<T> vComponentOriginal
          , Collection<VComponent<T>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Collection<T> instances)
    {
        long shift = ChronoUnit.DAYS.between(getDateTimeStart(), startInstance);
        Temporal endNew = getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
        setDateTimeEnd(endNew);
        System.out.println("endNew:" + endNew);
        super.editThisAndFuture(vComponentOriginal, vComponents, startOriginalInstance, startInstance, instances);
    }
    
    /** Deep copy all fields from source to destination.  Used both by copyTo method and copy constructor. 
     * */
    private static void copy(VEvent<?> source, VEvent<?> destination)
    {
        destination.setDescription(source.getDescription());
        destination.setDurationInNanos(source.getDurationInNanos());
        destination.setDateTimeEnd(source.getDateTimeEnd());
        destination.endPriority = source.endPriority();
    }

    /** Deep copy all fields from this to destination */
    @Override
    public void copyTo(VComponent<T> destination)
    {
        super.copyTo(destination);
        copy(this, (VEvent<?>) destination);
    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        @SuppressWarnings("unchecked")
//        VEvent<T> testObj = (VEvent<T>) obj;
//
//        boolean descriptionEquals = (getDescription() == null) ? (testObj.getDescription() == null)
//                : getDescription().equals(testObj.getDescription());
//        final boolean endEquals;
//        switch (endPriority())
//        {
//        case DTEND:
//            endEquals = getDateTimeEnd().equals(testObj.getDateTimeEnd());
//            break;
//        case DURATION:
//            endEquals = getDurationInNanos().equals(testObj.getDurationInNanos());
//            break;
//        default:
//            endEquals = false; // shouldn't get here
//            break;
//        }
//        System.out.println("VEvent: " + descriptionEquals + " " + endEquals);
//        // don't need to check getDateTimeEnd because it is bound to duration
//        return super.equals(obj) && descriptionEquals && endEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        return super.hashCode();
//    }
    
    /** Make iCalendar compliant string of VEvent calendar component.
     * This method should be overridden by an implementing class if that
     * class contains any extra properties. */
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + "[" + toComponentText() + "]";
    }
    
    @Override
    public String toComponentText()
    {
        List<String> properties = makePropertiesList();
        String propertiesString = properties.stream()
                .map(p -> p + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }

    @Override
    protected List<String> makePropertiesList()
    {
        List<String> properties = new ArrayList<>();
        properties.addAll(super.makePropertiesList());
        if ((getDescription() != null) && (! getDescription().equals("")))
        {
            properties.add(descriptionProperty().getName() + ":" + getDescription());
        }
        if (endPriority != null)
        {
            switch (endPriority)
            {
            case DTEND:
                String tag = makeDateTimePropertyTag(dateTimeEndProperty().getName(), getDateTimeEnd());
                properties.add(tag + VComponent.temporalToString(getDateTimeEnd()));
                break;
            case DURATION:
                properties.add(durationInNanosProperty().getName() + ":" + getDurationAsString());
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
        boolean isDurationNull = getDurationInNanos() == null;
        boolean isEndDateTimeNull = getDateTimeEnd() == null;
        if (isDurationNull && isEndDateTimeNull)
        {
            errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DURATION and DTEND can not be null.");
        }

        if (! isEndDateTimeNull)
        {
            Class<? extends Temporal> startClass = getDateTimeStart().getClass();
            Class<? extends Temporal> endClass = getDateTimeEnd().getClass();
            if (! startClass.equals(endClass))
            {
                errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  DTSTART and DTEND must be same Temporal type");
            }
        }
        
        return errorsBuilder.toString();
    }
    
    /** This method should be called by a method in the implementing class the
     * makes a new object and passes it here as vEvent.
     * @param <U>
     * @param vEvent
     * @param strings
     * @return
     */
    protected static VEvent<?> parseVEvent(VEvent<?> vEvent, List<String> strings)
    {
        // Test for BEGIN:VEVENT and END:VEVENT, then remove
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
            if (value.isEmpty())
            { // skip empty properties
                continue;
            }
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
            } else if (property.matches("^" + DATE_TIME_END_NAME + ".*"))
            { // DTEND
                if (vEvent.getDateTimeEnd() == null)
                {
                    if (durationFound == false)
                    {
                        dTEndFound = true;
                        vEvent.endPriority = EndPriority.DTEND;
                        String dateTimeString = (property.contains(";")) ? line.substring(property.indexOf(";")+1) : value;
                        Temporal dateTime = VComponent.parseTemporal(dateTimeString);
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
        return (VEvent<?>) VComponentAbstract.parseVComponent(vEvent, strings);
    }
    
    public static enum EndPriority
    {
        DURATION
      , DTEND;
    }
}
