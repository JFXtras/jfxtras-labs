package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
/**
 * Contains following properties:
 * @see RecurrenceRule
 * @see RecurrenceDates
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see DaylightSavingTime
 * @see StandardTime
 */
public abstract class VComponentRepeatableBase<T> extends VComponentPrimaryBase<T> implements VComponentRepeatable<T>
{
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public ObservableList<RecurrenceDates> getRecurrenceDates() { return recurrenceDates; }
    private ObservableList<RecurrenceDates> recurrenceDates;
    @Override
    public void setRecurrenceDates(ObservableList<RecurrenceDates> recurrenceDates)
    {
        this.recurrenceDates = recurrenceDates;
        recurrenceDates.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
        checkRecurrencesConsistency(recurrenceDates, null);
    }

    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override public ObjectProperty<RecurrenceRuleNew> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
        }
        return recurrenceRule;
    }
    @Override
    public RecurrenceRuleNew getRecurrenceRule() { return (recurrenceRule == null) ? null : recurrenceRuleProperty().get(); }
    private ObjectProperty<RecurrenceRuleNew> recurrenceRule;
    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }    
    
//    /**
//     * Start of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
//     * This property is not a part of the iCalendar standard
//     */
//    @Override
//    public Temporal getStartRange() { return startRange; }
//    private Temporal startRange;
//    @Override
//    public void setStartRange(Temporal startRange) { this.startRange = startRange; }
//    public T withStartRange(Temporal startRange) { setStartRange(startRange); return (T) this; }
//    
//    /**
//     * End of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
//     */
//    @Override
//    public Temporal getEndRange() { return endRange; }
//    private Temporal endRange;
//    @Override
//    public void setEndRange(Temporal endRange) { this.endRange = endRange; }
//    public T withEndRange(Temporal endRange) { setEndRange(endRange); return (T) this; }
    
    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        Stream<Temporal> inStream = VComponentRepeatable.super.streamRecurrences(start);
        if (getRecurrenceRule() == null)
        {
            return inStream; // no cache is no recurrence rule
        }
        return recurrenceStreamer().makeCache(inStream);   // make cache of start date/times
    }
    
//    /**
//     * Recurrence instances, represented as type R, that are bounded by {@link #startRange} and {@link #endRange}
//     * The elements of the list are created by calling {@link #makeRecurrences()}
//     */
//    @Override
//    public List<R> recurrences() { return recurrences; }
//    final private List<R> recurrences = new ArrayList<>();

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }
    
//    @Override
//    public boolean handleEdit(
//            VComponentRepeatable<T> vComponentOriginal
//          , Collection<VComponentRepeatable<T>> vComponents
//          , Temporal startOriginalInstance
//          , Temporal startInstance
//          , Temporal endInstance
//          , Collection<Object> instances
//          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
//    {
//        validateStartInstanceAndDTStart(startOriginalInstance, startInstance, endInstance);
//        final RRuleStatus rruleType = RRuleStatus.getRRuleType(getRecurrenceRule().getValue(), vComponentOriginal.getRecurrenceRule().getValue());
//        System.out.println("rruleType:" + rruleType);
//        boolean incrementSequence = true;
//        Collection<?> newInstances = null;
//        switch (rruleType)
//        {
//        case HAD_REPEAT_BECOMING_INDIVIDUAL:
//            becomingIndividual(vComponentOriginal, startInstance, endInstance);
//            // fall through
//        case WITH_NEW_REPEAT: // no dialog
//        case INDIVIDUAL:
//            adjustDateTime(startOriginalInstance, startInstance, endInstance);
//            if (! this.equals(vComponentOriginal)) { newInstances = updateInstances(instances); }
//            break;
//        case WITH_EXISTING_REPEAT:
//            // Find which properties changed
//            List<String> changedPropertyNames = findChangedProperties(vComponentOriginal);
//            /* Note:
//             * time properties must be checked separately because changes are stored in startInstance and endInstance,
//             * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
//             * question is answered. */
//            changedPropertyNames.addAll(changedStartAndEndDateTime(startOriginalInstance, startInstance, endInstance));
//            // determine if any changed properties warrant dialog
////            changedPropertyNames.stream().forEach(a -> System.out.println("changed property:" + a));
//            boolean provideDialog = requiresChangeDialog(changedPropertyNames);
//            if (changedPropertyNames.size() > 0) // if changes occurred
//            {
//                List<VComponent<I>> relatedVComponents = Arrays.asList(this); // TODO - support related components
//                final ChangeDialogOption changeResponse;
//                if (provideDialog)
//                {
//                    Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(this, startOriginalInstance);
//                    changeResponse = dialogCallback.call(choices);
//                } else
//                {
//                    changeResponse = ChangeDialogOption.ALL;
//                }
//                switch (changeResponse)
//                {
//                case ALL:
//                    if (relatedVComponents.size() == 1)
//                    {
//                        adjustDateTime(startOriginalInstance, startInstance, endInstance);
//                        if ((getRRule() != null) && (getRRule().recurrences().size() > 0))
//                        {
//                            getRRule().recurrences().forEach(v ->
//                            {
//                                Temporal newRecurreneId = adjustRecurrenceStart(v.getDateTimeRecurrence(), startOriginalInstance, startInstance, endInstance);
//                                v.setDateTimeRecurrence(newRecurreneId);
//                            });
//                        }
//                        newInstances = updateInstances(instances);
//                    } else
//                    {
//                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
//                    }
//                    break;
//                case CANCEL:
//                    vComponentOriginal.copyTo(this); // return to original
//                    return false;
//                case THIS_AND_FUTURE:
//                    newInstances = editThisAndFuture(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
//                    break;
//                case ONE:
//                    newInstances = editOne(vComponentOriginal, vComponents, startOriginalInstance, startInstance, endInstance, instances);
//                    break;
//                default:
//                    break;
//                }
//            }
//        }
//        if (! isValid()) throw new RuntimeException(errorString());
//        if (incrementSequence) { incrementSequence(); }
////        if (newInstances != null)
////        {
////            instances.clear();
////            instances.addAll(newInstances);
////        }
//        return true;
//    }
//    
//    /** If startInstance isn't valid due to a RRULE change, change startInstance and
//     * endInstance to closest valid values
//     */
//    void validateStartInstanceAndDTStart(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
//    {
//        // only done in subclasses
//    }    
//    
//    /** returns list of date-time properties that have been edited (DTSTART) */
//    protected Collection<String> changedStartAndEndDateTime(Temporal startOriginalInstance, Temporal startInstance, Temporal endInstance)
//    {
//        Collection<String> changedProperties = new ArrayList<>();
//        if (! startOriginalInstance.equals(startInstance)) { changedProperties.add(VComponentPropertyOld.DATE_TIME_START.toString()); }
//        return changedProperties;
//    }
//    /* Adjust DTSTART by instance start and end date-time */
//    void adjustDateTime(Temporal startOriginalInstance
//            , Temporal startInstance
//            , Temporal endInstance)
//    {
//        Temporal newStart = adjustRecurrenceStart(getDateTimeStart(), startOriginalInstance, startInstance, endInstance);
//        System.out.println("new DTSTART:" + newStart);
//        setDateTimeStart(newStart);
//    }
//    
//    /* Adjust DTSTART of RECURRENCE-ID */
//    private static Temporal adjustRecurrenceStart(Temporal initialStart
//            , Temporal startOriginalInstance
//            , Temporal startInstance
//            , Temporal endInstance)
//    {
//        DateTimeType newDateTimeType = DateTimeType.of(startInstance);
//        ZoneId zone = (startInstance instanceof ZonedDateTime) ? ZoneId.from(startInstance) : null;
//        Temporal startAdjusted = newDateTimeType.from(initialStart, zone);
//        Temporal startOriginalInstanceAdjusted = newDateTimeType.from(startOriginalInstance, zone);
//
//        // Calculate shift from startAdjusted to make new DTSTART
//        final TemporalAmount startShift;
//        if (newDateTimeType == DateTimeType.DATE)
//        {
//            startShift = Period.between(LocalDate.from(startOriginalInstance), LocalDate.from(startInstance));
//        } else
//        {
//            startShift = Duration.between(startOriginalInstanceAdjusted, startInstance);
//        }
//        return startAdjusted.plus(startShift);
//    }
//    
//    /**
//     * Return true if ANY changed property requires a dialog, false otherwise
//     * 
//     * @param changedPropertyNames - list from {@link #findChangedProperties(VComponent)}
//     * @return
//     */
//    boolean requiresChangeDialog(List<String> changedPropertyNames)
//    {
//        return changedPropertyNames.stream()
//                .map(s ->  
//                {
//                    VComponentPropertyOld p = VComponentPropertyOld.propertyFromName(s);
//                    return (p != null) ? p.isDialogRequired() : false;
//                })
//                .anyMatch(b -> b == true);
//    }
//    
//    /**
//     * Generates a list of iCalendar property names that have different values from the 
//     * input parameter
//     * 
//     * equal checks are encapsulated inside the enum VComponentProperty
//     */
//    List<String> findChangedProperties(VComponent<I> vComponentOriginal)
//    {
//        List<String> changedProperties = new ArrayList<>();
//        Arrays.stream(VComponentPropertyOld.values())
//                .filter(p -> ! p.equals(VComponentPropertyOld.DATE_TIME_START)) // DATE_TIME_START change calculated in changedStartAndEndDateTime
//                .forEach(p -> 
//                {
//                    boolean equals = p.isPropertyEqual(this, vComponentOriginal);
//                    if (! equals)
//                    {
//                        changedProperties.add(p.toString());
//                    }
//                });        
//        return changedProperties;
//    }
//    
//    private Collection<Object> updateInstances(Collection<Object> instances)
//    {
//        Collection<Object> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//        instancesTemp.addAll(instances);
////        instancesTemp.removeIf(a -> instances().stream().anyMatch(a2 -> a2 == a));
////        instances().clear(); // clear VEvent of outdated appointments
////        instancesTemp.addAll(makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
//        // TODO - MAKE SURE LISTENER UPDATES WEAK MAP OF INSTANCES AND RUNS MAKEINSTANCES
//        return instancesTemp;
//    }
//    
//    /**
//     * Part of handleEdit.
//     * Changes a VComponent with a RRULE to be an individual,
//     * 
//     * @param vComponentOriginal
//     * @param startInstance
//     * @param endInstance
//     * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection, Callback)
//     */
//    protected void becomingIndividual(VComponentRepeatable<T> vComponentOriginal, Temporal startInstance, Temporal endInstance)
//    {
//        setRecurrenceRule((RecurrenceRule3) null);
//        setRecurrenceDates(null);
////        setExDate(null); TODO - PUT IN SUBCLASS
//        if (vComponentOriginal.getRecurrenceRule() != null)
//        { // RRULE was removed, update DTSTART
//            setDateTimeStart(startInstance);
//        }
//    }
//    
//    /**
//     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
//     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
//     * 
//     * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
//     */
//    protected Collection<I> editOne(
//            VComponent<I> vComponentOriginal
//          , Collection<VComponent<I>> vComponents
//          , Temporal startOriginalInstance
//          , Temporal startInstance
//          , Temporal endInstance
//          , Collection<I> instances)
//    {
//        // Remove RRule and set parent component
//        setRRule(null);
//        setParent(vComponentOriginal);
//
//        // Apply dayShift, account for editing instance beyond first
//        Period dayShift = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(startOriginalInstance));
//        Temporal newStart = getDateTimeStart().plus(dayShift);
//        setDateTimeStart(newStart);
//        adjustDateTime(startOriginalInstance, startInstance, endInstance);
//        setDateTimeRecurrence(startOriginalInstance);
//        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
//   
//        // Add recurrence to original vEvent
//        vComponentOriginal.getRRule().recurrences().add(this);
//        
//        // Check for validity
//        if (! isValid()) { throw new RuntimeException(errorString()); }
////        System.out.println("here:" + vComponentOriginal);
//        if (! vComponentOriginal.isValid()) { throw new RuntimeException(vComponentOriginal.errorString()); }
//        
//        // TODO - PUT THIS CODE IN IMPLEMENTATION
//        // Remove old instances, add back ones
////        Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to instances
////        instancesTemp.addAll(instances);
////        instancesTemp.removeIf(a -> vComponentOriginal.instances().stream().anyMatch(a2 -> a2 == a));
////        vComponentOriginal.instances().clear(); // clear vEventOriginal outdated collection of instances
////        instancesTemp.addAll(vComponentOriginal.makeInstances()); // make new instances and add to main collection (added to vEventNew's collection in makeinstances)
////        instances().clear(); // clear vEvent outdated collection of instances
////        instancesTemp.addAll(makeInstances()); // add vEventOld part of new instances
////        vComponents.add(vComponentOriginal);
////        return instancesTemp;
//    }
//    
//    /**
//     * Changing this and future instances in VComponent is done by ending the previous
//     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
//     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
//     * VComponents.  vEventNew has new settings, vEvent has former settings.
//     * @param endInstance 
//     * @param <T>
//     * 
//     * @see VComponent#handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
//     */
//    protected Collection<I> editThisAndFuture(
//            VComponent<I> vComponentOriginal
//          , Collection<VComponent<I>> vComponents
//          , Temporal startOriginalInstance
//          , Temporal startInstance
//          , Temporal endInstance
//          , Collection<I> instances)
//    {
//        // adjust original VEvent
//        if (vComponentOriginal.getRRule().getCount() > 0)
//        {
//            vComponentOriginal.getRRule().setCount(0);
//        }
//        final Temporal untilNew;
//        if (isWholeDay())
//        {
//            untilNew = LocalDate.from(startOriginalInstance).minus(1, ChronoUnit.DAYS);
//        } else
//        {
////            Temporal temporal = startOriginalInstance.minus(1, ChronoUnit.NANOS);
////            untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(temporal);
//            untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(previousStreamValue(startInstance));
//        }
//        vComponentOriginal.getRRule().setUntil(untilNew);
//        
//        
//        setDateTimeStart(startInstance);
//        adjustDateTime(startInstance, startInstance, endInstance);
//        setUniqueIdentifier();
//        String relatedUID = (vComponentOriginal.getRelatedTo() == null) ? vComponentOriginal.getUniqueIdentifier() : vComponentOriginal.getRelatedTo();
//        setRelatedTo(relatedUID);
//        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
//        
//        // Split EXDates dates between this and newVEvent
//        if (getExDate() != null)
//        {
//            getExDate().getTemporals().clear();
//            final Iterator<Temporal> exceptionIterator = getExDate().getTemporals().iterator();
//            while (exceptionIterator.hasNext())
//            {
//                Temporal d = exceptionIterator.next();
//                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(d, startInstance);
//                if (result < 0)
//                {
//                    exceptionIterator.remove();
//                } else {
//                    getExDate().getTemporals().add(d);
//                }
//            }
//            if (getExDate().getTemporals().isEmpty()) setExDate(null);
//            if (getExDate().getTemporals().isEmpty()) setExDate(null);
//        }
//
//        // Split recurrence date/times between this and newVEvent
//        if (getRDate() != null)
//        {
//            getRDate().getTemporals().clear();
//            final Iterator<Temporal> recurrenceIterator = getRDate().getTemporals().iterator();
//            while (recurrenceIterator.hasNext())
//            {
//                Temporal d = recurrenceIterator.next();
//                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(d, startInstance);
//                if (result < 0)
//                {
//                    recurrenceIterator.remove();
//                } else {
//                    getRDate().getTemporals().add(d);
//                }
//            }
//            if (getRDate().getTemporals().isEmpty()) setRDate(null);
//            if (getRDate().getTemporals().isEmpty()) setRDate(null);
//        }
//
//        // Split instance dates between this and newVEvent
//        if (getRRule().recurrences() != null)
//        {
//            getRRule().recurrences().clear();
//            final Iterator<VComponent<?>> recurrenceIterator = getRRule().recurrences().iterator();
//            while (recurrenceIterator.hasNext())
//            {
//                VComponent<?> d = recurrenceIterator.next();
//                if (DateTimeUtilities.isBefore(d.getDateTimeRecurrence(), startInstance))
//                {
//                    recurrenceIterator.remove();
//                } else {
//                    getRRule().recurrences().add(d);
//                }
//            }
//        }
//        
//        // Modify COUNT for the edited vEvent
//        if (getRRule().getCount() > 0)
//        {
//            int countInOrginal = vComponentOriginal.makeInstances().size();
//            int countInNew = getRRule().getCount() - countInOrginal;
//            getRRule().setCount(countInNew);
//        }
////        adjustDateTime(startOriginalInstance, startInstance, endInstance);        
//        
//        if (! vComponentOriginal.isValid()) throw new RuntimeException(vComponentOriginal.errorString());
//        vComponents.add(vComponentOriginal);
//
//        // Remove old appointments, add back ones
//        Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//        instancesTemp.addAll(instances);
//        instancesTemp.removeIf(a -> vComponentOriginal.instances().stream().anyMatch(a2 -> a2 == a));
//        vComponentOriginal.instances().clear(); // clear vEvent outdated collection of appointments
//        instancesTemp.addAll(vComponentOriginal.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
//        instances().clear(); // clear vEvent's outdated collection of appointments
//        instancesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
//        return instancesTemp;
////        instances.clear();
////        instances.addAll(instancesTemp);
//    }
//     
//    
//    @Override
//    public void handleDelete(
//            Collection<VComponentRepeatable<T>> vComponents
//          , Temporal startInstance
//          , Object instance
//          , Collection<Object> instances
//          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
//    {
//        int count = this.instances().size();
//        if (count == 1)
//        {
//            vComponents.remove(this);
//            instances.remove(instance);
//        } else // more than one instance
//        {
//            //  TODO - COMBINE makeDialogChoices INTO dialogCallback
//            Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(this, startInstance);
////            Map<ChangeDialogOption, String> choices = makeDialogChoices(startInstance);
//            ChangeDialogOption changeResponse = dialogCallback.call(choices);
//            switch (changeResponse)
//            {
//            case ALL:
//                List<VComponent<?>> relatedVComponents = new ArrayList<>();
//                if (this.getDateTimeRecurrence() == null)
//                { // is parent
//                    relatedVComponents.addAll(this.getRRule().recurrences());
//                    relatedVComponents.add(this);
//                } else
//                { // is child (recurrence).  Find parent delete all children
//                    relatedVComponents.addAll(this.getParent().getRRule().recurrences());
//                    relatedVComponents.add(this.getParent());
//                }
//                relatedVComponents.stream().forEach(v -> vComponents.remove(v));
//                vComponents.removeAll(relatedVComponents);
//                List<?> appointmentsToRemove = relatedVComponents.stream()
//                        .flatMap(v -> v.instances().stream())
//                        .collect(Collectors.toList());
//                instances.removeAll(appointmentsToRemove);
//                break;
//            case CANCEL:
//                break;
//            case ONE:
//                if (getExDate() == null) { setExDate(new ExDate(startInstance)); }
//                else { getExDate().getTemporals().add(startInstance); }
//                instances.removeIf(a -> a.equals(instance));
//                break;
//            case THIS_AND_FUTURE:
//                if (getRRule().getCount() != 0) { getRRule().setCount(0); }
////                Temporal previousDay = startInstance.minus(1, ChronoUnit.DAYS);
//                final Temporal untilNew;
//                if (isWholeDay())
//                {
//                    untilNew = LocalDate.from(startInstance).minus(1, ChronoUnit.DAYS);
//                } else
//                {
//                    // UNTIL IS INCLUSIVE - FIND PREVIOUS INSTANCE DATE-TIME - CONVERT TO UTC
//                    untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(previousStreamValue(startInstance));
//                    System.out.println("start instance:" + startInstance + " " + untilNew);
////                    Temporal temporal = startInstance.minus(1, ChronoUnit.NANOS);
////                    untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(temporal);
//                }
//                getRRule().setUntil(untilNew);
//
//                // Remove old appointments, add back ones
//                Collection<I> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//                instancesTemp.addAll(instances);
//                instancesTemp.removeIf(a -> instances().stream().anyMatch(a2 -> a2 == a));
//                instances().clear(); // clear this's outdated collection of appointments
//                instancesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
//                instances.clear();
//                instances.addAll(instancesTemp);
//                break;
//            default:
//                break;
//            }
//        }
//    }
    
    private enum RRuleStatus
    {
        INDIVIDUAL ,
        WITH_EXISTING_REPEAT ,
        WITH_NEW_REPEAT, 
        HAD_REPEAT_BECOMING_INDIVIDUAL;
      
        public static RRuleStatus getRRuleType(RecurrenceRule3 rruleNew, RecurrenceRule3 rruleOld)
        {
            if (rruleNew == null)
            {
                if (rruleOld == null)
                { // doesn't have repeat or have old repeat either
                    return RRuleStatus.INDIVIDUAL;
                } else {
                    return RRuleStatus.HAD_REPEAT_BECOMING_INDIVIDUAL;
                }
            } else
            { // RRule != null
                if (rruleOld == null)
                {
                    return RRuleStatus.WITH_NEW_REPEAT;                
                } else
                {
                    return RRuleStatus.WITH_EXISTING_REPEAT;
                }
            }
        }
    }
}
