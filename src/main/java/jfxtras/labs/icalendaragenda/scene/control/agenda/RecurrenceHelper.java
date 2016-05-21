package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda.StartEndRange;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Handles edit and delete functionality of VEvents and VTodos
 * 
 * @author David Bal
 *
 */
public class RecurrenceHelper<R>
{   
    private final Collection<R> recurrences; // collection of recurrences
    private final Callback2<VComponentLocatable<?>, Temporal, R> recurrenceCallBack;
//    public final Map<VComponentNew<?>, List<R>> vComponentRecurrencetMap;    
    public final Map<Integer, List<R>> vComponentRecurrencetMap;    
    private final Map<Integer, VComponentDisplayable<?>> recurrenceVComponentMap; /* map matches appointment to VComponent that made it */
    
    private LocalDateTime startRange; // must be updated when range changes
    public void setStartRange(LocalDateTime startRange) { this.startRange = startRange; } 

    private LocalDateTime endRange; // must be updated when range changes
    public void setEndRange(LocalDateTime endRange) { this.endRange = endRange; } 


    public RecurrenceHelper(
            Collection<R> recurrences,
            Callback2<VComponentLocatable<?>, Temporal, R>  recurrenceCallBack,
//            Map<VComponentNew<?>, List<R>> vComponentRecurrencetMap,
            Map<Integer, List<R>> vComponentRecurrencetMap,
            Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap)
    {
        this.recurrences = recurrences;
        this.recurrenceCallBack = recurrenceCallBack;
        this.vComponentRecurrencetMap = vComponentRecurrencetMap;
        this.recurrenceVComponentMap = appointmentVComponentMap;
    }


    /**
     * Makes appointments from VEVENT or VTODO for Agenda
     * Appointments are made between displayed range
     * 
     * @param vComponentEdited - calendar component
     * @return created appointments
     */
    public List<R> makeRecurrences(VComponentLocatable<?> vComponentEdited)
    {
        if ((startRange == null) || (endRange == null))
        {
            throw new DateTimeException("Both startRange and endRange must not be null (" + startRange + ", " + endRange + ")");
        }
        List<R> newRecurrences = new ArrayList<>();
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = LocalDate.from(startRange);
            endRange2 = LocalDate.from(endRange);            
        } else
        {
            startRange2 = vComponentEdited.getDateTimeStart().getValue().with(startRange);
            endRange2 = vComponentEdited.getDateTimeStart().getValue().with(endRange);            
        }
        vComponentEdited.streamRecurrences(startRange2, endRange2)
            .forEach(startTemporal -> 
            {
                R recurrence = recurrenceCallBack.call(vComponentEdited, startTemporal);
                recurrenceVComponentMap.put(System.identityHashCode(recurrence), vComponentEdited);
                newRecurrences.add(recurrence);
            });
        return newRecurrences;
    }
    
    /** Edit VEvent or VTodo */
    public boolean handleEdit(
            VComponentLocatable<?> vComponentEdited
          , VComponentLocatable<?> vComponentOriginalCopy
          , Collection<VComponentLocatable<?>> vComponents
          , Temporal startOriginalRecurrence
          , Temporal startRecurrence
          , Temporal endRecurrence
//          , Collection<R> allRecurrences
//          , Collection<Object> componentRecurrences
          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
    {
        validateStartRecurrenceAndDTStart(vComponentEdited, startOriginalRecurrence, startRecurrence);
        final RecurrenceHelper.RRuleStatus rruleType = RRuleStatus.getRRuleType(vComponentEdited.getRecurrenceRule(), vComponentOriginalCopy.getRecurrenceRule());
        System.out.println("rruleType:" + rruleType);
        boolean incrementSequence = true;
        Collection<R> newRecurrences = null;
        Collection<R> allRecurrences = recurrences;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            becomeNonRecurring(vComponentEdited, vComponentOriginalCopy, startRecurrence, endRecurrence);
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
            if (! vComponentEdited.equals(vComponentOriginalCopy))
            {
                newRecurrences = updateRecurrences(vComponentEdited);
            }
            break;
        case WITH_EXISTING_REPEAT:
            // Find which properties changed
            List<PropertyType> changedProperties = findChangedProperties(
                    vComponentEdited,
                    vComponentOriginalCopy,
                    startOriginalRecurrence,
                    startRecurrence,
                    endRecurrence);
            /* Note:
             * time properties must be checked separately because changes are stored in startRecurrence and endRecurrence,
             * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
             * question is answered. */
//            changedProperties.addAll(changedStartAndEndDateTime(startOriginalRecurrence, startRecurrence, endRecurrence));
            // determine if any changed properties warrant dialog
//            changedPropertyNames.stream().forEach(a -> System.out.println("changed property:" + a));
            boolean provideDialog = requiresChangeDialog(changedProperties);
            if (changedProperties.size() > 0) // if changes occurred
            {
                List<VComponentLocatable<?>> relatedVComponents = Arrays.asList(vComponentEdited); // TODO - support related components
                final ChangeDialogOption changeResponse;
                if (provideDialog)
                {
                    Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(vComponentEdited, startOriginalRecurrence);
                    changeResponse = dialogCallback.call(choices);
                } else
                {
                    changeResponse = ChangeDialogOption.ALL;
                }
                switch (changeResponse)
                {
                case ALL:
                    if (relatedVComponents.size() == 1)
                    {
                        adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
//                        if (vComponentEdited.childComponentsWithRecurrenceIDs().size() > 0)
//                        {
                        // Adjust children components with RecurrenceIDs
                        vComponentEdited.childComponentsWithRecurrenceIDs()
                                .stream()
//                                .map(c -> c.getRecurrenceId())
                                .forEach(v ->
                                {
                                    Temporal newRecurreneId = adjustRecurrenceStart(v.getRecurrenceId().getValue(), startOriginalRecurrence, startRecurrence, endRecurrence);
                                    v.setRecurrenceId(newRecurreneId);
                                });
//                        }
                        newRecurrences = updateRecurrences(vComponentEdited);
                    } else
                    {
                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
                    }
                    break;
                case CANCEL:
                    vComponentEdited.copyComponentFrom(vComponentOriginalCopy);  // return to original
                    return false;
                case THIS_AND_FUTURE:
                    newRecurrences = editThisAndFuture(
                            vComponentEdited,
                            vComponentOriginalCopy,
                            vComponents,
                            startOriginalRecurrence,
                            startRecurrence,
                            endRecurrence
                            );
                    break;
                case ONE:
                    newRecurrences = editOne(
                            vComponentEdited,
                            vComponentOriginalCopy,
                            vComponents,
                            startOriginalRecurrence,
                            startRecurrence,
                            endRecurrence
                            );
                    break;
                default:
                    break;
                }
            }
        }
        if (! vComponentEdited.isValid()) throw new RuntimeException("Invalid component"); // TODO - MAKE ERROR STRING
        if (incrementSequence)
        {
            vComponentEdited.incrementSequence();
        }
        if (newRecurrences != null)
        {
//            allRecurrences.clear();
            allRecurrences.addAll(newRecurrences);
        }
        return true;
    }
    
    /** If startRecurrence isn't valid due to a RRULE change, change startRecurrence and
     * endRecurrence to closest valid values
     */
    // TODO - VERITFY THIS WORKS - changed from old version
    private static void validateStartRecurrenceAndDTStart(VComponentLocatable<?> vComponentEdited, Temporal startOriginalRecurrence, Temporal startRecurrence)
    {
//        boolean isStreamedValue;
        if (vComponentEdited.getRecurrenceRule() != null)
        {
            Temporal firstTemporal = vComponentEdited.getRecurrenceRule().getValue()
                    .streamRecurrences(vComponentEdited.getDateTimeStart().getValue())
                    .findFirst()
                    .get();
            if (! firstTemporal.equals(vComponentEdited.getDateTimeStart().getValue()))
            {
                vComponentEdited.setDateTimeStart(firstTemporal);
            }
        }
    }
    
    /* Adjust DTSTART and DTEND, DUE, or DURATION by recurrence's start and end date-time */
    private static void adjustDateTime(
            VComponentLocatable<?> vComponentEdited,
            Temporal startOriginalRecurrence,
            Temporal startRecurrence,
            Temporal endRecurrence)
    {
        Temporal newStart = adjustRecurrenceStart(
                vComponentEdited.getDateTimeStart().getValue(),
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence);
        vComponentEdited.setDateTimeStart(newStart);
//        System.out.println("new DTSTART2:" + newStart + " " + startRecurrence + " " + endRecurrence);
        vComponentEdited.setEndOrDuration(startRecurrence, endRecurrence);
//        endType().setDuration(this, startRecurrence, endRecurrence);
    }

    /* Adjust DTSTART of RECURRENCE-ID */
    private static Temporal adjustRecurrenceStart(Temporal initialStart
            , Temporal startOriginalRecurrence
            , Temporal startRecurrence
            , Temporal endRecurrence)
    {
        DateTimeType newDateTimeType = DateTimeType.of(startRecurrence);
        ZoneId zone = (startRecurrence instanceof ZonedDateTime) ? ZoneId.from(startRecurrence) : null;
        Temporal startAdjusted = newDateTimeType.from(initialStart, zone);
        Temporal startOriginalRecurrenceAdjusted = newDateTimeType.from(startOriginalRecurrence, zone);

        // Calculate shift from startAdjusted to make new DTSTART
        final TemporalAmount startShift;
        if (newDateTimeType == DateTimeType.DATE)
        {
            startShift = Period.between(LocalDate.from(startOriginalRecurrence), LocalDate.from(startRecurrence));
        } else
        {
            startShift = Duration.between(startOriginalRecurrenceAdjusted, startRecurrence);
        }
        return startAdjusted.plus(startShift);
    }
    
    private void becomeNonRecurring(
            VComponentLocatable<?> vComponentEdited,
            VComponentRepeatable<?> vComponentOriginalCopy,
            Temporal startRecurrence,
            Temporal endRecurrence)
    {
        vComponentEdited.setRecurrenceRule((RecurrenceRule3) null);
        vComponentEdited.setRecurrenceDates(null);
        vComponentEdited.setExceptionDates(null);
        if (vComponentOriginalCopy.getRecurrenceRule() != null)
        { // RRULE was removed, update DTSTART, DTEND or DURATION
            vComponentEdited.setDateTimeStart(startRecurrence);
            if (vComponentEdited.getDuration() != null)
            {
                TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startRecurrence, endRecurrence);
                vComponentEdited.setDuration(duration);
            } else
            {
                if (vComponentEdited instanceof VTodo)
                {
                    ((VTodo) vComponentEdited).setDateTimeDue(endRecurrence);
                } else if (vComponentEdited instanceof VEvent)
                {
                    ((VEvent) vComponentEdited).setDateTimeEnd(endRecurrence);
                }
            }
        }
    }
    
    private Collection<R> updateRecurrences(VComponentLocatable<?> vComponentEdited)
    {
        Collection<R> componentRecurrences = vComponentRecurrencetMap.get(vComponentEdited);
        Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        recurrencesTemp.addAll(recurrences);
        recurrencesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
//        instances().clear(); // clear VEvent of outdated appointments
        List<R> newRecurrences = makeRecurrences(vComponentEdited);
        recurrencesTemp.addAll(newRecurrences); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)
//        System.out.println("puting:" + vComponentEdited.hashCode() + " " + newRecurrences);
//        vComponentRecurrencetMap.put(vComponentEdited, newRecurrences);
        vComponentRecurrencetMap.put(System.identityHashCode(vComponentEdited), newRecurrences);
//        System.out.println("contains key:" + vComponentRecurrencetMap.containsKey(vComponentEdited));
       return recurrencesTemp;
    }

//    private Collection<?> updateRecurrences(VComponentLocatable<?> vComponentEdited, Collection<?> recurrences)
//   {
//       Collection<Object> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//       recurrencesTemp.addAll(recurrences);
//       recurrencesTemp.removeIf(a -> updateInstances().stream().anyMatch(a2 -> a2 == a));
//       recurrences().clear(); // clear VEvent of outdated appointments
//       recurrencesTemp.addAll(makeRecurrences(vComponentEdited)); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
//       return recurrencesTemp;
//   }
    
    /**
     * Generates a list of iCalendar property names that have different values from the 
     * input parameter
     * 
     * equal checks are encapsulated inside the enum VComponentProperty
     */
    public static List<PropertyType> findChangedProperties(
            VComponentLocatable<?> vComponentEdited,
            VComponentLocatable<?> vComponentOriginalCopy,
            Temporal startOriginalInstance,
            Temporal startInstance,
            Temporal endInstance)
    {
        List<PropertyType> changedProperties = new ArrayList<>();
        vComponentEdited.properties()
                .stream()
                .map(p -> p.propertyType())
                .forEach(t ->
                {
                    Object p1 = t.getProperty(vComponentEdited);
                    Object p2 = t.getProperty(vComponentOriginalCopy);
                    if (! p1.equals(p2))
                    {
                        changedProperties.add(t);
                    }
                });
        
        /* Note:
         * time properties must be checked separately because changes are stored in startRecurrence and endRecurrence,
         * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
         * question is answered. */
        if (! startOriginalInstance.equals(startInstance))
        {
            changedProperties.add(PropertyType.DATE_TIME_START);
        }
        
        TemporalAmount durationNew = DateTimeUtilities.temporalAmountBetween(startInstance, endInstance);
        TemporalAmount durationOriginal = vComponentEdited.getActualDuration();
        if (! durationOriginal.equals(durationNew))
        {
            if (vComponentEdited instanceof VEvent)
            {
                if (! (((VEvent) vComponentEdited).getDateTimeEnd() == null))
                {
                    changedProperties.add(PropertyType.DATE_TIME_END);                    
                }
            } else if (vComponentEdited instanceof VTodo)
            {
                if (! (((VTodo) vComponentEdited).getDateTimeDue() == null))
                {
                    changedProperties.add(PropertyType.DATE_TIME_DUE);                    
                }                
            }
            boolean isDurationNull = vComponentEdited.getDuration() == null;
            if (! isDurationNull)
            {
                changedProperties.add(PropertyType.DURATION);                    
            }
        }   
        
        return changedProperties;
    }


   // TODO - DOUBLE CHECK THIS LIST - WHY NO DESCRIPTION, FOR EXAMPLE?
   private final static List<PropertyType> DIALOG_REQUIRED_PROPERTIES = Arrays.asList(
           PropertyType.CATEGORIES,
           PropertyType.COMMENT,
           PropertyType.DATE_TIME_START,
           PropertyType.ORGANIZER,
           PropertyType.SUMMARY
           );
   
   /**
    * Return true if ANY changed property requires a dialog, false otherwise
    * 
    * @param changedPropertyNames - list from {@link #findChangedProperties(VComponent)}
    * @return
    */
   boolean requiresChangeDialog(List<PropertyType> changedPropertyNames)
   {
       return changedPropertyNames.stream()
               .map(p -> DIALOG_REQUIRED_PROPERTIES.contains(p))
               .anyMatch(b -> b == true);
   }
   
   /**
    * Changing this and future instances in VComponent is done by ending the previous
    * VComponent with a UNTIL date or date/time and starting a new VComponent from 
    * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
    * VComponents.  vEventNew has new settings, vEvent has former settings.
    * 
    * @see VComponent#handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
    * 
    * @param vComponentEdited - has new settings
    * @param vComponentOriginalCopy - has former settings
    * @param vComponents - list of all components
    * @param startOriginalRecurrence - start date/time before edit
    * @param startRecurrence - edited start date/time
    * @param endRecurrence - edited end date/time
    * @return
    */
   private Collection<R> editThisAndFuture(
           VComponentLocatable<?> vComponentEdited,
           VComponentLocatable<?> vComponentOriginalCopy,
           Collection<VComponentLocatable<?>> vComponents,
           Temporal startOriginalRecurrence,
           Temporal startRecurrence,
           Temporal endRecurrence
           )
   {
       // adjust original VEvent
       // TODO - THIS DOESN'T MAKE SENSE - BELOW COUNT CHANGE CONTRADICTS
       if (vComponentOriginalCopy.getRecurrenceRule().getValue().getCount() != null)
       {
           vComponentOriginalCopy.getRecurrenceRule().getValue().setCount(null);
       }
       final Temporal untilNew;
       if (vComponentEdited.isWholeDay())
       {
           untilNew = LocalDate.from(startOriginalRecurrence).minus(1, ChronoUnit.DAYS);
       } else
       {
//           Temporal temporal = startOriginalInstance.minus(1, ChronoUnit.NANOS);
//           untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(temporal);
//           Temporal previousRecurrence = vComponentEdited.previousStreamValue(startRecurrence);
//           if (startRecurrence instanceof LocalDateTime)
//           {
//               LocalDateTime.from(previousRecurrence).atZone(DateTimeUtilities.DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
//           } else if (startRecurrence instanceof ZonedDateTime)
//           {
//               
//           }
//           return LocalDateTime.from(temporal).atZone(DEFAULT_ZONE).withZoneSameInstant(ZoneId.of("Z"));
//       case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
//           return ZonedDateTime.from(temporal).withZoneSameInstant(ZoneId.of("Z"));
           untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(vComponentEdited.previousStreamValue(startRecurrence));
//           untilNew = DateTimeType.DATE_WITH_UTC_TIME.from(previousStreamValue(startInstance));
       }
       vComponentOriginalCopy.getRecurrenceRule().getValue().setUntil(untilNew);
       
       
       vComponentEdited.setDateTimeStart(startRecurrence);
       adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
//       adjustDateTime(startRecurrence, startRecurrence, endInstance);
       vComponentEdited.setUniqueIdentifier(); // ADD UID CALLBACK
       // only supports one RELATED-TO value
       String relatedUID = (vComponentOriginalCopy.getRelatedTo() == null) ?
               vComponentOriginalCopy.getUniqueIdentifier().getValue() : vComponentOriginalCopy.getRelatedTo().get(0).getValue();
       vComponentEdited.withRelatedTo(relatedUID);
       vComponentEdited.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
       
       // remove EXDATEs that are out of bounds
       if (vComponentEdited.getExceptionDates() != null)
       {
           final Iterator<Temporal> exceptionDateIterator = vComponentEdited.getExceptionDates()
                   .stream()
                   .flatMap(e -> e.getValue().stream())
                   .iterator();
           while (exceptionDateIterator.hasNext())
           {
               Temporal t = exceptionDateIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result < 0)
               {
                   exceptionDateIterator.remove();
               }
           }
       }
       if (vComponentOriginalCopy.getExceptionDates() != null)
       {
           final Iterator<Temporal> exceptionDateIterator = vComponentOriginalCopy.getExceptionDates()
                   .stream()
                   .flatMap(e -> e.getValue().stream())
                   .iterator();
           while (exceptionDateIterator.hasNext())
           {
               Temporal t = exceptionDateIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result > 0)
               {
                   exceptionDateIterator.remove();
               }
           }
       }
       
       // remove RDATEs that are out of bounds
       if (vComponentEdited.getRecurrenceDates() != null)
       {
           final Iterator<Temporal> recurrenceDateIterator = vComponentEdited.getRecurrenceDates()
                   .stream()
                   .flatMap(e -> e.getValue().stream())
                   .iterator();
           while (recurrenceDateIterator.hasNext())
           {
               Temporal t = recurrenceDateIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result < 0)
               {
                   recurrenceDateIterator.remove();
               }
           }
       }
       if (vComponentOriginalCopy.getRecurrenceDates() != null)
       {
           final Iterator<Temporal> recurrenceDateIterator = vComponentOriginalCopy.getRecurrenceDates()
                   .stream()
                   .flatMap(e -> e.getValue().stream())
                   .iterator();
           while (recurrenceDateIterator.hasNext())
           {
               Temporal t = recurrenceDateIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result > 0)
               {
                   recurrenceDateIterator.remove();
               }
           }
       }
       
       // remove RECURRENCE-ID components that are out of bounds
       if (vComponentEdited.childComponentsWithRecurrenceIDs() != null)
       {
           final Iterator<Temporal> recurrenceIDIterator = vComponentEdited.childComponentsWithRecurrenceIDs()
                   .stream()
                   .map(e -> (Temporal) e.getRecurrenceId().getValue())
                   .iterator();
           while (recurrenceIDIterator.hasNext())
           {
               Temporal t = recurrenceIDIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result < 0)
               {
                   recurrenceIDIterator.remove();
               }
           }
       }
       if (vComponentOriginalCopy.getRecurrenceDates() != null)
       {
           final Iterator<Temporal> recurrenceIDIterator = vComponentOriginalCopy.childComponentsWithRecurrenceIDs()
                   .stream()
                   .map(e -> (Temporal) e.getRecurrenceId().getValue())
                   .iterator();
           while (recurrenceIDIterator.hasNext())
           {
               Temporal t = recurrenceIDIterator.next();
               int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, startRecurrence);
               if (result > 0)
               {
                   recurrenceIDIterator.remove();
               }
           }
       }
       
       // Modify COUNT for the edited vEvent
       if (vComponentEdited.getRecurrenceRule().getValue().getCount() != null)
       {
           int countInOrginal = (int) vComponentOriginalCopy.streamRecurrences().count();
           int countInNew = vComponentEdited.getRecurrenceRule().getValue().getCount().getValue() - countInOrginal;
           vComponentEdited.getRecurrenceRule().getValue().setCount(countInNew);
       }
       
       if (! vComponentOriginalCopy.isValid()) throw new RuntimeException("Invalid component");
       vComponents.add(vComponentOriginalCopy);

       // Remove old appointments, add back ones
//       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//       recurrencesTemp.addAll(recurrences);
//       recurrencesTemp.removeIf(a -> vComponentOriginalCopy.recurrences().stream().anyMatch(a2 -> a2 == a));
//       vComponentOriginalCopy.recurrences().clear(); // clear vEvent outdated collection of appointments
//       recurrencesTemp.addAll(vComponentOriginalCopy.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
//       recurrences().clear(); // clear vEvent's outdated collection of appointments
//       recurrencesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
//       return recurrencesTemp;
//       instances.clear();
//       instances.addAll(instancesTemp);
       
       // UPDATE RECURRENCES
       Collection<R> componentRecurrences = vComponentRecurrencetMap.get(vComponentEdited);
       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
       recurrencesTemp.addAll(recurrences);
       recurrencesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
//       instances().clear(); // clear VEvent of outdated appointments
       List<R> newRecurrences = makeRecurrences(vComponentOriginalCopy);
//       vComponentRecurrencetMap.put(vComponentOriginalCopy, newRecurrences);
       vComponentRecurrencetMap.put(System.identityHashCode(vComponentOriginalCopy), newRecurrences);
       recurrencesTemp.addAll(newRecurrences); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)
       
       return recurrencesTemp;
   }
   
   /**
    * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
    * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
    * 
    * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
    */
   private Collection<R> editOne(
           VComponentLocatable<?> vComponentEdited,
           VComponentLocatable<?> vComponentOriginalCopy,
           Collection<VComponentLocatable<?>> vComponents,
           Temporal startOriginalRecurrence,
           Temporal startRecurrence,
           Temporal endRecurrence
           )
   {
       // Remove RRule and set parent component
       vComponentEdited.setRecurrenceRule((RecurrenceRule3) null);
//       setParent(vComponentOriginalCopy);

       // Apply dayShift, account for editing recurrence beyond first
       Period dayShift = Period.between(LocalDate.from(vComponentEdited.getDateTimeStart().getValue()),
               LocalDate.from(startOriginalRecurrence));
       Temporal newStart = vComponentEdited.getDateTimeStart().getValue().plus(dayShift);
       vComponentEdited.setDateTimeStart(newStart);
       adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
       vComponentEdited.setRecurrenceId(startOriginalRecurrence);
       vComponentEdited.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
  
       // Add recurrence to original vEvent
       vComponentOriginalCopy.childComponentsWithRecurrenceIDs().add(vComponentEdited);
       
       // Check for validity
       if (! vComponentEdited.isValid()) { throw new RuntimeException("Invalid component"); }
//       System.out.println("here:" + vComponentOriginalCopy);
       if (! vComponentOriginalCopy.isValid()) { throw new RuntimeException("Invalid component"); }
       
       // Remove old recurrences, add back ones
       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
       recurrencesTemp.addAll(recurrences);
       Collection<R> componentRecurrences = vComponentRecurrencetMap.get(vComponentEdited);
       recurrencesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
//       vComponentOriginalCopy.recurrences().clear(); // clear vEventOriginal outdated collection of recurrences
       List<R> newRecurrences = makeRecurrences(vComponentOriginalCopy);
//       vComponentRecurrencetMap.put(vComponentOriginalCopy, newRecurrences);
       vComponentRecurrencetMap.put(System.identityHashCode(vComponentOriginalCopy), newRecurrences);
       recurrencesTemp.addAll(newRecurrences); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)

//       recurrences().clear(); // clear vEvent outdated collection of recurrences
//       recurrencesTemp.addAll(makeRecurrences()); // add vEventOld part of new recurrences
       vComponents.add(vComponentOriginalCopy);
       return recurrencesTemp;
   }
   
//   /**
//    * Options available when editing or deleting a repeatable appointment.
//    * Sometimes all options are not available.  For example, a one-part repeating
//    * event doesn't have the SEGMENT option.
//    */
//   public enum ChangeDialogOption
//   {
//       ONE                  // individual instance
//     , ALL                  // entire series
//     , THIS_AND_FUTURE      // selected instance and all in the future
//     , CANCEL;              // do nothing
//       
//       public static Map<ChangeDialogOption, StartEndRange> makeDialogChoices(VComponentLocatable<?> vComponent, Temporal startInstance)
//       {
//           Map<ChangeDialogOption, StartEndRange> choices = new LinkedHashMap<>();
//           choices.put(ChangeDialogOption.ONE, new StartEndRange(startInstance, startInstance));
//           Temporal end = vComponent.lastRecurrence();
//           if (! vComponent.isIndividual())
//           {
//               if (! vComponent.isLastRecurrence(startInstance))
//               {
//                   Temporal start = (startInstance == null) ? vComponent.getDateTimeStart() : startInstance; // set initial start
//                   choices.put(ChangeDialogOption.THIS_AND_FUTURE, new StartEndRange(start, end));
//               }
//               choices.put(ChangeDialogOption.ALL, new StartEndRange(vComponent.getDateTimeStart(), end));
//           }
//           return choices;
//       }        
//   }
   
//    @Deprecated // TODO - consider replace with simple booleans
     enum RRuleStatus
    {
        INDIVIDUAL ,
        WITH_EXISTING_REPEAT ,
        WITH_NEW_REPEAT, 
        HAD_REPEAT_BECOMING_INDIVIDUAL;
      
        public static RRuleStatus getRRuleType(RecurrenceRuleNew rruleNew, RecurrenceRuleNew rruleOld)
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

     /**
      * Options available when editing or deleting a repeatable appointment.
      * Sometimes all options are not available.  For example, a one-part repeating
      * event doesn't have the SEGMENT option.
      */
     public enum ChangeDialogOption
     {
         ONE                  // individual instance
       , ALL                  // entire series
       , THIS_AND_FUTURE      // selected instance and all in the future
       , CANCEL;              // do nothing
         
         public static Map<ChangeDialogOption, StartEndRange> makeDialogChoices(VComponentLocatable<?> vComponent, Temporal startInstance)
         {
             Map<ChangeDialogOption, StartEndRange> choices = new LinkedHashMap<>();
             choices.put(ChangeDialogOption.ONE, new StartEndRange(startInstance, startInstance));
             Temporal lastRecurrence = vComponent.lastRecurrence();
             if (! (vComponent.getRecurrenceRule() == null))
             {
                 if ((lastRecurrence == null) || (! lastRecurrence.equals(startInstance)))
                 {
                     Temporal start = (startInstance == null) ? vComponent.getDateTimeStart().getValue() : startInstance; // set initial start
                     choices.put(ChangeDialogOption.THIS_AND_FUTURE, new StartEndRange(start, lastRecurrence));
                 }
                 choices.put(ChangeDialogOption.ALL, new StartEndRange(vComponent.getDateTimeStart().getValue(), lastRecurrence));
             }
             return choices;
         }        
     }
     
     /** Based on {@link Callback<P,R>} */
     @FunctionalInterface
     public interface Callback2<P1, P2, R> {
         /**
          * The <code>call</code> method is called when required, and is given 
          * two arguments of type P1 and P2, with a requirement that an object of type R
          * is returned.
          *
          * @param param1 The first argument upon which the returned value should be
          *      determined.
          * @param param1 The second argument upon which the returned value should be
          *      determined.
          * @return An object of type R that may be determined based on the provided
          *      parameter value.
          */
         public R call(P1 param1, P2 param2);
     }
}
