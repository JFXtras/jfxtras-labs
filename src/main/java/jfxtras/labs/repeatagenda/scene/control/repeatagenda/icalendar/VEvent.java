package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
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
public abstract class VEvent<I> extends VComponentBaseAbstract<I>
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
    public StringProperty descriptionProperty() { return description; }
    final private StringProperty description = new SimpleStringProperty(this, VEventProperty.DESCRIPTION.toString());
    public String getDescription() { return description.getValue(); }
    public void setDescription(String value) { description.setValue(value); }
    
    /** 
     * DURATION from RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * */
    final private ObjectProperty<TemporalAmount> duration = new SimpleObjectProperty<>(this, VEventProperty.DURATION.toString());
    public ObjectProperty<TemporalAmount> durationProperty() { return duration; }
    public TemporalAmount getDuration() { return duration.getValue(); }
    public void setDuration(TemporalAmount duration)
    {
        if (duration == null)
        {
            endPriority = null;
        } else
        {
            endPriority = EndPriority.DURATION;
        }
        this.duration.set(duration);
    }
    
    /**
     * DTEND, Date-Time End. from RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * Can't be used if DURATION is used.  Must be one or the other.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    final private ObjectProperty<Temporal> dateTimeEnd = new SimpleObjectProperty<>(this, VEventProperty.DATE_TIME_END.toString());
    public ObjectProperty<Temporal> dateTimeEndProperty() { return dateTimeEnd; }
    public void setDateTimeEnd(Temporal dtEnd)
    {
        if (dtEnd == null)
        {
            endPriority = null;
        } else
        {
            DateTimeType myDateTimeType = DateTimeType.dateTimeTypeFromTemporal(dtEnd);
            if ((lastDtStartDateTimeType() != null) && (myDateTimeType != lastDtStartDateTimeType()))
            {
                throw new DateTimeException("DTEND must have the same DateTimeType as DTSTART, (" + myDateTimeType + " and " + lastDtStartDateTimeType() + ", respectively");
            }
            endPriority = EndPriority.DTEND;
         }
        dateTimeEnd.set(dtEnd);
    }
    public Temporal getDateTimeEnd() { return dateTimeEnd.get(); }

    /** Indicates end option, DURATION or DTEND. */
    public EndPriority endPriority() { return endPriority; }
    EndPriority endPriority;

    /**
     * LOCATION: RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public StringProperty locationProperty() { return locationProperty; }
    final private StringProperty locationProperty = new SimpleStringProperty(this, VEventProperty.LOCATION.toString());
    public String getLocation() { return locationProperty.getValue(); }
    public void setLocation(String value) { locationProperty.setValue(value); }
    
    @Override
    void ensureDateTimeTypeConsistency(DateTimeType dateTimeType)
    {
        // DTEND
        if ((getDateTimeEnd() != null) && (dateTimeType != DateTimeType.dateTimeTypeFromTemporal(getDateTimeEnd())))
        {
            // convert to new Temporal type
            Temporal newDateTimeEnd = DateTimeType.changeTemporal(getDateTimeEnd(), dateTimeType);
            setDateTimeEnd(newDateTimeEnd);
        }
        super.ensureDateTimeTypeConsistency(dateTimeType);
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
                        VEventProperty p = VEventProperty.propertyFromString(s);
                        return (p != null) ? p.isDialogRequired() : false;
                    })
                    .anyMatch(b -> b == true);
        }
    }

    @Override
    List<String> findChangedProperties(VComponent<I> vComponentOriginal)
    {
        List<String> changedProperties = new ArrayList<>();
        changedProperties.addAll(super.findChangedProperties(vComponentOriginal));
        Arrays.stream(VEventProperty.values())
                .forEach(p -> 
                {
                    boolean equals = p.isPropertyEqual(this, (VEvent<?>) vComponentOriginal);
                    if (! equals)
                    {
                        changedProperties.add(p.toString());
                    }
                });        
        return changedProperties;
    }
    
    // CONSTRUCTORS
    public VEvent(VEvent<I> vevent)
    {
        super(vevent);
        copy(vevent, this);
    }
    
    public VEvent()
    {
        super();
    }
    
    
    // HANDLE EDIT METHODS
    @Override
    protected void becomingIndividual(VComponent<I> vComponentOriginal, Temporal startInstance, Temporal endInstance)
    {
        super.becomingIndividual(vComponentOriginal, startInstance, endInstance);
        if ((vComponentOriginal.getRRule() != null) && (endPriority() == EndPriority.DTEND))
        { // RRULE was removed, update DTEND
            setDateTimeEnd(endInstance);
        }
    }

    @Override // edit end date or date/time
    protected void editOne(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
          , Temporal endInstance
          , Collection<I> instances)
    {
        // Apply dayShift, if any
        switch (endPriority())
        {
        case DTEND:
            Period dayShift = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(startInstance));
            Temporal newEnd = getDateTimeEnd().plus(dayShift);
            setDateTimeEnd(newEnd);
            break;
        case DURATION:
            setDuration(Duration.between(startInstance, endInstance));
            break;
        }
        super.editOne(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
    }

    @Override // edit end date or date/time
    protected void editThisAndFuture(
            VComponent<I> vComponentOriginal
          , Collection<VComponent<I>> vComponents
          , Temporal startOriginalInstance
          , Temporal startInstance
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
        Temporal endNew = getDateTimeEnd().plus(duration);
        setDateTimeEnd(endNew);
        super.editThisAndFuture(vComponentOriginal, vComponents, startOriginalInstance, startInstance, instances);
    }
    
    /** Deep copy all fields from source to destination.  Used both by copyTo method and copy constructor. 
     * */
    private static void copy(VEvent<?> source, VEvent<?> destination)
    {
        destination.setDescription(source.getDescription());
        destination.setDuration(source.getDuration());
        destination.setDateTimeEnd(source.getDateTimeEnd());
        destination.setLocation(source.getLocation());
        destination.endPriority = source.endPriority();
    }

    /** Deep copy all fields from this to destination */
    @Override
    public void copyTo(VComponent<I> destination)
    {
        super.copyTo(destination);
        copy(this, (VEvent<?>) destination);
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
        Arrays.stream(VEventProperty.values())
                .forEach(p ->
                {
                    String newLine = p.makeContentLine(this);
                    if (newLine != null)
                    {
                        properties.add(newLine);
                    }
                });
        return properties;
    }
    
    /**
     * Needed by parse methods in subclasses 
     * 
     * Convert a list of strings containing properties of a iCalendar component and
     * populate its properties.  Used to make a new object from a List<String>.
     * 
     * @param vEvent
     * @param strings - list of properties
     * @return VComponent with parsed properties added
     */
//    static VEvent<?> parseVEvent(VEvent<?> vEvent, List<String> strings)
//    {
//        // Test for BEGIN:VEVENT and END:VEVENT, then remove those lines
//        if (! strings.get(0).equals("BEGIN:VEVENT"))
//        {
//            throw new IllegalArgumentException("Invalid VEVENT. First line must be BEGIN:VEVENT");
//        } else
//        {
//            strings.remove(0);
//        }
//        if (! strings.get(strings.size()-1).equals("END:VEVENT"))
//        {
//            throw new IllegalArgumentException("Invalid VEVENT. Last line must be END:VEVENT");
//        } else
//        {
//            strings.remove(strings.size()-1);
//        }
//        
//        Iterator<String> lineIterator = strings.iterator();
//        while (lineIterator.hasNext())
//        {
//            String line = lineIterator.next();
//            // identify iCalendar property ending index (property name must start at the beginning of the line)
//            int propertyValueSeparatorIndex = 0;
//            for (int i=0; i<line.length(); i++)
//            {
//                if ((line.charAt(i) == ';') || (line.charAt(i) == ':'))
//                {
//                    propertyValueSeparatorIndex = i;
//                    break;
//                }
//            }
//            if (propertyValueSeparatorIndex == 0)
//            {
//                continue; // line doesn't contain a property, get next one
//            }
//            String propertyName = line.substring(0, propertyValueSeparatorIndex);
//            String value = line.substring(propertyValueSeparatorIndex + 1).trim();
//            if (value.isEmpty())
//            { // skip empty properties
//                continue;
//            }
//            VEventProperty property = VEventProperty.propertyFromString(propertyName);
//            boolean propertyFound = property.setVComponent(vEvent, value); // runs method in enum to set vComponent
//            if (propertyFound)
//            {
//                lineIterator.remove();                
//            }
//        }
//        return (VEvent<?>) VComponentBaseAbstract.parseVComponent(vEvent, strings);
//    }
    
    public static VEvent<?> parseVEvent(VEvent<?> vEvent, String string)
    {
        Iterator<Pair<String, String>> i = ICalendarUtilities.ComponentStringToPropertyNameAndValueList(string).iterator();
        while (i.hasNext())
        {
            Pair<String, String> linePair = i.next();
            String propertyName = linePair.getKey();
            String value = linePair.getValue();

            // VComponent properties
            VComponentProperty vComponentProperty = VComponentProperty.propertyFromString(propertyName);
            if (vComponentProperty != null)
            {
                vComponentProperty.setVComponent(vEvent, value);
                continue;
            }
            
            // VEvent properties
            VEventProperty vEventProperty = VEventProperty.propertyFromString(propertyName);
            if (vEventProperty != null)
            {
                vEventProperty.setVComponent(vEvent, value);
            }
        }
        return vEvent;
    }

    
//    /** Make new VEventImpl and populate properties by parsing a list of property strings 
//     * */
//    private static VEvent<?> parse(List<String> strings, List<AppointmentGroup> appointmentGroups)
//    {
//        VEventImpl vEvent = new VEventImpl(appointmentGroups);
//        // TODO - NEED DTSTART FIRST - HOW?
//        // split into property name, value
//        // sort by some order - DTSTART on top
//        // write parse line method in VEVent and VComponent
//        // parse one line at a time in order VComponent, VEvent
//        // put these methods in VEvent - nothing specific here
//        
//        VEvent.parseVEvent(vEvent, strings); // parse VEvent properties into vEvent
//        return vEvent;
//    }
    
//    /** Make new VEventImpl and populate properties by parsing a string with properties separated
//     * a by lineSeparator (new line) */
//    public static VEvent<?> parse(String string, List<AppointmentGroup> appointmentGroups)
//    {
//        List<String> stringsList = Arrays
//                .stream(string.split(System.lineSeparator()))
//                .collect(Collectors.toList());
//        return parse(stringsList, appointmentGroups);
//    }
    
    /**
     * Checks that one, and only one, of DTEND or DURATION is set.
     */
    @Override
    public String errorString()
    {
        StringBuilder errorsBuilder = new StringBuilder(super.errorString());

        if ((getDateTimeEnd() != null) && (! VComponent.isAfter(getDateTimeEnd(), getDateTimeStart())))
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
            Class<? extends Temporal> endClass = getDateTimeEnd().getClass();
            if (! startClass.equals(endClass))
            {
                errorsBuilder.append(System.lineSeparator() + "Invalid VEvent.  DTSTART and DTEND must be same Temporal type");
            }
        }
        
        return errorsBuilder.toString();
    }
    
    public enum EndPriority
    {
        DURATION
      , DTEND;
    }
}
