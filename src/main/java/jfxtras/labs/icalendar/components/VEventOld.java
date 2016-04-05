package jfxtras.labs.icalendar.components;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.components.VEventUtilities.VEventProperty;
import jfxtras.labs.icalendar.properties.PropertyEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndZonedDateTime;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.VEventImpl;

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
public abstract class VEventOld<I, T> extends VComponentDisplayableBase<I, T>
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
    public ObjectProperty<Description> descriptionProperty() { return description; }
    final private ObjectProperty<Description> description = new SimpleObjectProperty<>(this, PropertyEnum.DESCRIPTION.toString());
    public Description getDescription() { return description.getValue(); }
    public void setDescription(Description description) { this.description.set(description); }
    public void setDescription(String description) { this.description.set(new Description(description)); }
    public T withDescription(Description description) { setDescription(description); return (T) this; }
    public T withDescription(String description) { setDescription(description); return (T) this; }
    
    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * */
    final private ObjectProperty<TemporalAmount> duration = new SimpleObjectProperty<>(this, PropertyEnum.DURATION.toString());
    public ObjectProperty<TemporalAmount> durationProperty() { return duration; }
    public TemporalAmount getDuration() { return duration.getValue(); }
    public void setDuration(TemporalAmount duration)
    {
        if (duration == null)
        {
            endPriority = null;
        } else
        {
            endPriority = EndType.DURATION;
        }
        this.duration.set(duration);
    }
    public T withDuration(TemporalAmount duration) { setDuration(duration); return (T) this; }

    
    /**
     * DTEND, Date-Time End. from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * Can't be used if DURATION is used.  Must be one or the other.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    final private ObjectProperty<DTEndZonedDateTime> dateTimeEnd = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_END.toString());
    public ObjectProperty<DTEndZonedDateTime> dateTimeEndProperty() { return dateTimeEnd; }
    public void setDateTimeEnd(Temporal dtEnd) { new DTEndZonedDateTime(dtEnd); }
    public void setDateTimeEnd(DTEndZonedDateTime dtEnd)
    {
        // TODO - I THINK BELOW LOGIC IS REDUNDANT - ALSO FOUND IN PropertyTimeBase - remove here if true
        if (dtEnd == null)
        {
            endPriority = null;
        } else
        {
            DateTimeType myDateTimeType = DateTimeType.of(dtEnd.getValue());
            if ((lastDtStartDateTimeType() != null) && (myDateTimeType != lastDtStartDateTimeType()))
            {
                throw new DateTimeException("DTEND must have the same DateTimeType as DTSTART, (" + myDateTimeType + " and " + lastDtStartDateTimeType() + ", respectively");
            }
            endPriority = EndType.DTEND;
         }
        dateTimeEnd.set(dtEnd);
    }
    public DTEndZonedDateTime getDateTimeEnd() { return dateTimeEnd.get(); }
    public T withDateTimeEnd(DTEndZonedDateTime dtEnd) { setDateTimeEnd(dtEnd); return (T) this; }
    public T withDateTimeEnd(Temporal dtEnd) { setDateTimeEnd(new DTEndZonedDateTime().withValue(dtEnd)); return (T) this; }

    /** Indicates end option, DURATION or DTEND. 
     * Getter and setter methods in EndPriority enum */
    public EndType endType() { return endPriority; }
    EndType endPriority;

    /**
     * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public StringProperty locationProperty() { return locationProperty; }
    final private StringProperty locationProperty = new SimpleStringProperty(this, PropertyEnum.LOCATION.toString());
    public String getLocation() { return locationProperty.getValue(); }
    public void setLocation(String location) { locationProperty.setValue(location); }
    public T withLocation(String location) { setLocation(location); return (T) this; }

    @Override
    void ensureDateTimeTypeConsistency(DateTimeType dateTimeType, ZoneId zone)
    {
        // DTEND
        if ((getDateTimeEnd() != null) && (dateTimeType != DateTimeType.of(getDateTimeEnd().getValue())))
        {
            // convert to new Temporal type
//            Temporal newDateTimeEnd = DateTimeType.changeTemporal(getDateTimeEnd(), dateTimeType);
            Temporal newDateTimeEnd = dateTimeType.from(getDateTimeEnd().getValue(), zone);
            getDateTimeEnd().setValue(newDateTimeEnd);
//            setDateTimeEnd(newDateTimeEnd);
        }
        super.ensureDateTimeTypeConsistency(dateTimeType, zone);
    }
    
    @Override
    boolean requiresChangeDialog(List<String> changedPropertyNames)
    {
        if (super.requiresChangeDialog(changedPropertyNames))
        {
            return true;
        } else
        {
            return changedPropertyNames.stream()
                    .map(s ->  
                    {
                        VEventProperty p = VEventProperty.propertyFromName(s);
                        return (p != null) ? p.isDialogRequired() : false;
                    })
                    .anyMatch(b -> b == true);
        }
    }

    @Override
    List<String> findChangedProperties(VComponentDisplayableOld<I> vComponentOriginal)
    {
        List<String> changedProperties = new ArrayList<>();
        changedProperties.addAll(super.findChangedProperties(vComponentOriginal));
        Arrays.stream(VEventProperty.values())
                .filter(p -> ! p.equals(VEventProperty.DATE_TIME_END)) // DATE_TIME_END change calculated in changedStartAndEndDateTime
                .filter(p -> ! p.equals(VEventProperty.DURATION)) // DURATION change calculated in changedStartAndEndDateTime
                .forEach(p -> 
                {
                    boolean equals = p.isPropertyEqual(this, (VEventOld<?,?>) vComponentOriginal);
                    if (! equals)
                    {
                        changedProperties.add(p.toString());
                    }
                });        
        return changedProperties;
    }
    
    // CONSTRUCTORS
    public VEventOld(VEventOld<I,T> vevent)
    {
        super(vevent);
        copy(vevent, this);
    }
    
    public VEventOld()
    {
        super();
    }
    
    // HANDLE EDIT METHODS
    @Override
    void validateStartInstanceAndDTStart(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
    {
        // Test if DTSTART is not valid, get new one
        if (! isStreamValue(getDateTimeStart()))
        {            
            Optional<Temporal> optionalAfter = stream(getDateTimeStart()).findFirst();
            final Temporal newTestedStart;
            if (optionalAfter.isPresent())
            {
                newTestedStart = optionalAfter.get();
            } else
            {
                throw new RuntimeException("No valid DTSTART in VComponent");
            }
            TemporalAmount duration = endType().getDuration(this);
//            Temporal newTestedEnd = newTestedStart.plus(duration);
            setDateTimeStart(newTestedStart);
            endType().setDuration(this, duration);
//            setDateTimeEnd(newTestedEnd);
        }
//        return super.validateStartInstanceAndDTStart(startOriginalInstance, startInstance, endInstance);
    }
        
    @Override
    protected void becomingIndividual(VComponentDisplayableOld<I> vComponentOriginal, Temporal startInstance, Temporal endInstance)
    {
        super.becomingIndividual(vComponentOriginal, startInstance, endInstance);
        if ((vComponentOriginal.getRRule() != null) && (endType() == EndType.DTEND))
        { // RRULE was removed, update DTEND
//            setDateTimeEnd(endInstance);            
            getDateTimeEnd().setValue(endInstance);
        }
    }
    
    /** returns list of date-time properties that have been edited (DURATION or DTEND) */
    @Override
    protected Collection<String> changedStartAndEndDateTime(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
    {
        Collection<String> changedProperties = super.changedStartAndEndDateTime(startOriginalInstance, startInstance, endInstance);
        TemporalAmount durationNew = DateTimeUtilities.durationBetween(startInstance, endInstance);
        TemporalAmount durationOriginal = endType().getDuration(this);
        if (! durationOriginal.equals(durationNew)) { changedProperties.add(endType().toString()); }
        return changedProperties;
    }
    
    /* Adjust DTEND by instance start and end date-time */
    @Override
    protected void adjustDateTime(Temporal startOriginalInstance
            , Temporal startInstance
            , Temporal endInstance)
    {
        super.adjustDateTime(startOriginalInstance, startInstance, endInstance);
        endType().setDuration(this, startInstance, endInstance);
    }

    @Override // edit end date or date/time
    protected Collection<I> editOne(
            VComponentDisplayableOld<I> vComponentOriginal
          , Collection<VComponentDisplayableOld<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances)
    {
        // Apply dayShift, if any

        switch (endType())  // TODO - CAN THIS BE PUT INTO EndPriority enum?
        {
        case DTEND:
            Period dayShift = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(startInstance));
            Temporal newEnd = getDateTimeEnd().getValue().plus(dayShift);
            getDateTimeEnd().setValue(newEnd);
//            setDateTimeEnd(newEnd);
            break;
        case DURATION:
            endType().setDuration(this, startInstance, endInstance);
//            setDuration(Duration.between(startInstance, endInstance));
            break;
        }
//        endType().setDuration(this, startInclusive, endExclusive);
        return super.editOne(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
    }

    @Override // edit end date or date/time
    protected Collection<I> editThisAndFuture(
            VComponentDisplayableOld<I> vComponentOriginal
          , Collection<VComponentDisplayableOld<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances)
    {
        final TemporalAmount duration;
        if (isWholeDay())
        {
            duration = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(startInstance));
        } else
        {
            duration = Duration.between(getDateTimeStart(), startInstance);
        }
        Temporal endNew = getDateTimeEnd().getValue().plus(duration);
        getDateTimeEnd().setValue(endNew);
//        setDateTimeEnd(endNew);
        return super.editThisAndFuture(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
    }
    
    /** Deep copy all fields from source to destination.  Used both by copyTo method and copy constructor. 
     * */
    @Deprecated // loop through VEventProperty copy methods directly
    private static void copy(VEventOld<?,?> source, VEventOld<?,?> destination)
    {
        Arrays.stream(VEventProperty.values())
                .forEach(p ->
                {
                    p.copyProperty(source, destination);
                });
        destination.endPriority = source.endType();
    }

    /** Deep copy all fields from this to destination */
    @Override
    public void copyTo(VComponentDisplayableOld<I> destination)
    {
        super.copyTo(destination);
        copy(this, (VEventOld<?,?>) destination);
    }
    
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
        List<String> properties = makeContentLines();
        String propertiesString = properties.stream()
                .map(p -> p + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }

    @Override
    protected List<String> makeContentLines()
    {
        List<String> properties = new ArrayList<>();
        properties.addAll(super.makeContentLines());
        Arrays.stream(VEventProperty.values())
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
    
    @Override
    public Stream<Temporal> streamLimitedByRange()
    {
        if ((getStartRange() == null) || (getEndRange() == null)) throw new RuntimeException("Can't make instances without setting date/time range first");
        TemporalAmount amount = endType().getDuration(this);
        Stream<Temporal> removedTooEarly = stream(getStartRange().minus(amount)).filter(d -> 
        {
            TemporalAmount duration = endType().getDuration(this);
            Temporal plus = d.plus(duration);
            return DateTimeUtilities.isAfter(plus, getStartRange()); 
        }); // inclusive
        Stream<Temporal> removedTooLate = ICalendarUtilities.takeWhile(removedTooEarly, a -> DateTimeUtilities.isBefore(a, getEndRange())); // exclusive
        return removedTooLate;
    }
    
    /**
     * Checks that one, and only one, of DTEND or DURATION is set.
     */
    @Override
    public String errorString()
    {
        StringBuilder errorsBuilder = new StringBuilder(super.errorString());

        if ((getDateTimeEnd() != null) && (! DateTimeUtilities.isAfter(getDateTimeEnd().getValue(), getDateTimeStart())))
        {
            errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  DTEND (" + getDateTimeEnd()
            + ") must be after DTSTART (" + getDateTimeStart() + ")");
        }
        
        // Note: Check for invalid condition where both DURATION and DTEND not being null is done in parseVEvent.
        // It is not checked here due to bindings between both DURATION and DTEND.
        boolean isDurationNull = getDuration() == null;
        boolean isEndDateTimeNull = getDateTimeEnd() == null;
        if (isDurationNull && isEndDateTimeNull)
        {
            errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  Both DURATION and DTEND can not be null.");
        }

        if (! isEndDateTimeNull)
        {
            Class<? extends Temporal> startClass = getDateTimeStart().getClass();
            Class<? extends Temporal> endClass = getDateTimeEnd().getValue().getClass();
            if (! startClass.equals(endClass))
            {
                errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  DTSTART and DTEND must be same Temporal type");
            }
        }
        
        return errorsBuilder.toString();
    }
    
    public enum EndType
    {
        DURATION
        {
            @Override
            public void setDuration(VEventOld<?,?> vEvent, Temporal startInclusive, Temporal endExclusive)
            {
                TemporalAmount duration = DateTimeUtilities.durationBetween(startInclusive, endExclusive);
                vEvent.setDuration(duration);
            }

            @Override
            public TemporalAmount getDuration(VEventOld<?,?> vEvent)
            {
                return vEvent.getDuration();
            }

            @Override
            public void setDuration(VEventOld<?, ?> vEvent, TemporalAmount amount)
            {
                vEvent.setDuration(amount);
            }
        }
      , DTEND
      {
        @Override
        public void setDuration(VEventOld<?,?> vEvent, Temporal startInclusive, Temporal endExclusive)
        {
            TemporalAmount duration = DateTimeUtilities.durationBetween(startInclusive, endExclusive);
            Temporal dtEnd = vEvent.getDateTimeStart().plus(duration);
//            vEvent.setDateTimeEnd(dtEnd);
            vEvent.getDateTimeEnd().setValue(dtEnd);
        }

        @Override
        public TemporalAmount getDuration(VEventOld<?,?> vEvent)
        {
            if (vEvent.isWholeDay())
            {
                return Period.between(LocalDate.from(vEvent.getDateTimeStart()), LocalDate.from(vEvent.getDateTimeEnd().getValue()));
            } else
            {
                return Duration.between(vEvent.getDateTimeStart(), vEvent.getDateTimeEnd().getValue());
            }
        }

        @Override
        public void setDuration(VEventOld<?, ?> vEvent, TemporalAmount amount)
        {
            vEvent.getDateTimeEnd().setValue(vEvent.getDateTimeStart().plus(amount));
        }
    };

    /**
     * Sets the DURATION or DTEND, of the vEvent depending on the EndType value vEvent contains.  If the EndType is DTEND it
     * is calculated by adding the amount to DTSTART.  Otherwise, DURATION is simply set to amount
     * 
     * @param vEvent - VEvent to modify
     * @param amount - TemporalAmount for duration or calculations for DTSTART
     */
    public abstract void setDuration(VEventOld<?,?> vEvent, TemporalAmount amount);
     /**
     * Sets the DURATION or DTEND, of the vEvent depending on the EndType value vEvent contains.  The duration or
     * period is calculated from the startInclusive and endExclusive parameters.
     * 
     * @param vEvent - VEvent to modify
     * @param startInclusive
     * @param endExclusive
     */
    public abstract void setDuration(VEventOld<?,?> vEvent, Temporal startInclusive, Temporal endExclusive);

    /**
     * Gets duration, either from DURATION property, or by calculating duration between
     * DTSTART and DTEND
     * 
     * @param vEvent
     * @return
     */
    public abstract TemporalAmount getDuration(VEventOld<?,?> vEvent);    
    }
}
