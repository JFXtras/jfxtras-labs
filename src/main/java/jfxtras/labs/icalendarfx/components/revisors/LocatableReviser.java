package jfxtras.labs.icalendarfx.components.revisors;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayableBase;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class LocatableReviser<T, U extends VComponentLocatable<U>> extends DisplayableReviser<T, U> implements LocateRevisable<T, U>
{
    public LocatableReviser(U component)
    {
        super(component);
    }

    @Override public Temporal getEndRecurrence() { return endRecurrence; }
    private Temporal endRecurrence;
    @Override public void setEndRecurrence(Temporal startRecurrence) { this.endRecurrence = startRecurrence; }
    
    @Override
    boolean isValid()
    {
        if (getEndRecurrence() == null)
        {
            System.out.println("endRecurrence must not be null");
            return false;
        }
        return super.isValid();
    }
    
    @Override
    void becomeNonRecurring()
    {
        super.becomeNonRecurring();
        if (getVComponentOriginal().getRecurrenceRule() != null)
        { // RRULE was removed, update DTSTART, DTEND or DURATION
            getVComponentEdited().setDateTimeStart(getStartRecurrence());
            if (getVComponentEdited().getDuration() != null)
            {
                TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
                getVComponentEdited().setDuration(duration);
            }
        }
    }
   
   /**
    * Return true if ANY changed property requires a dialog, false otherwise
    * 
    * @param changedPropertyNames - list from {@link #findChangedProperties(VComponent)}
    * @return
    */
   static boolean requiresChangeDialog(List<PropertyType> changedPropertyNames)
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
 * @param <T>
 * @param <U>
    * 
    * @see VComponent#handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
    * 
    * @param vComponentEdited - has new settings
    * @param vComponentOriginal - has former settings
    * @param vComponents - list of all components
    * @param startOriginalRecurrence - start date/time before edit
    * @param startRecurrence - edited start date/time
    * @param endRecurrence - edited end date/time
    * @return
    */
   private static <T extends VComponentDisplayableBase<?>, U extends Temporal> void editThisAndFuture(
           T vComponentOriginal,
           T vComponentEdited,
           Collection<T> vComponents,
           Temporal startOriginalRecurrence,
           U startRecurrence,
           U endRecurrence
//           Temporal dateTimeStartNew
//           TemporalAmount shiftAmount
           )
   {
       // adjust original VEvent - remove COUNT, replace with UNTIL
       if (vComponentOriginal.getRecurrenceRule().getValue().getCount() != null)
       {
           vComponentOriginal.getRecurrenceRule().getValue().setCount(null);
       }
//       final Temporal untilNew = LocalDateTime.from(startRecurrence).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));        
       
       final Temporal untilNew;
       if (vComponentEdited.isWholeDay())
       {
           untilNew = vComponentEdited.previousStreamValue(startRecurrence);
       } else
       {
           Temporal previousRecurrence = vComponentEdited.previousStreamValue(startRecurrence);
           if (startRecurrence instanceof LocalDateTime)
           {
               untilNew = LocalDateTime.from(previousRecurrence).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
           } else if (startRecurrence instanceof ZonedDateTime)
           {
               untilNew = ((ZonedDateTime) previousRecurrence).withZoneSameInstant(ZoneId.of("Z"));
           } else
           {
               throw new DateTimeException("Unsupported Temporal type:" + previousRecurrence.getClass());
           }
           vComponentOriginal.getRecurrenceRule().getValue().setUntil(untilNew);
       }
    
       
       
       // Adjust start and end
//       Temporal startOriginalRecurrence = startRecurrence.plus(shiftAmount);
       Period dateTimeStartShift = Period.between(LocalDate.from(vComponentEdited.getDateTimeStart().getValue()),
               LocalDate.from(vComponentOriginal.getDateTimeStart().getValue()));
//       TemporalAmount amount = DateTimeUtilities.temporalAmountBetween(startOriginalRecurrence, startRecurrence); // TODO - make work for different Temporal classes
       vComponentEdited.setDateTimeStart(startOriginalRecurrence.plus(dateTimeStartShift));
       vComponentEdited.adjustDateTime(startOriginalRecurrence, startRecurrence, endRecurrence); // TODO - is this worthy of being in a method?
//       adjustDateTime(startRecurrence, startRecurrence, endInstance);
       vComponentEdited.setUniqueIdentifier(); // ADD UID CALLBACK
       // only supports one RELATED-TO value
       String relatedUID = (vComponentOriginal.getRelatedTo() == null) ?
               vComponentOriginal.getUniqueIdentifier().getValue() : vComponentOriginal.getRelatedTo().get(0).getValue();
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
       if (vComponentOriginal.getExceptionDates() != null)
       {
           final Iterator<Temporal> exceptionDateIterator = vComponentOriginal.getExceptionDates()
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
       if (vComponentOriginal.getRecurrenceDates() != null)
       {
           final Iterator<Temporal> recurrenceDateIterator = vComponentOriginal.getRecurrenceDates()
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
       if (vComponentEdited.childComponents() != null)
       {
           final Iterator<Temporal> recurrenceIDIterator = vComponentEdited.childComponents()
                   .stream()
                   .map(e -> e.getRecurrenceId().getValue())
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
       if (vComponentOriginal.getRecurrenceDates() != null)
       {
           final Iterator<Temporal> recurrenceIDIterator = vComponentOriginal.childComponents()
                   .stream()
                   .map(e -> e.getRecurrenceId().getValue())
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
//       System.out.println(vComponentOriginal.getRecurrenceRule().getValue());
//       System.out.println(vComponentEdited.getRecurrenceRule().getValue());
       if (vComponentEdited.getRecurrenceRule().getValue().getCount() != null)
       {
           int countInOrginal = (int) vComponentOriginal.streamRecurrences().count();
           int countInNew = vComponentEdited.getRecurrenceRule().getValue().getCount().getValue() - countInOrginal;
           vComponentEdited.getRecurrenceRule().getValue().setCount(countInNew);
       }
       
       if (! vComponentOriginal.isValid())
       {
           throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                   vComponentOriginal.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                   vComponentOriginal.toContent());
       }
       vComponents.add(vComponentOriginal);
       

       // Remove old appointments, add back ones
//       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//       recurrencesTemp.addAll(recurrences);
//       recurrencesTemp.removeIf(a -> vComponentOriginal.recurrences().stream().anyMatch(a2 -> a2 == a));
//       vComponentOriginal.recurrences().clear(); // clear vEvent outdated collection of appointments
//       recurrencesTemp.addAll(vComponentOriginal.makeInstances()); // make new appointments and add to main collection (added to vEvent's collection in makeAppointments)
//       recurrences().clear(); // clear vEvent's outdated collection of appointments
//       recurrencesTemp.addAll(makeInstances()); // add vEventOld part of new appointments
//       return recurrencesTemp;
//       instances.clear();
//       instances.addAll(instancesTemp);
       
       // UPDATE RECURRENCES
       // TODO - MOVE TO LISTENERS IN AGENDA
//       Collection<R> componentRecurrences = vComponentRecurrencetMap.get(vComponentEditedCopy);
//       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//       recurrencesTemp.addAll(recurrences);
//       recurrencesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
//       List<R> newRecurrences = makeRecurrences(vComponentOriginal);
//       vComponentRecurrencetMap.put(System.identityHashCode(vComponentOriginal), newRecurrences);
//       recurrencesTemp.addAll(newRecurrences); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)
       
//       return recurrencesTemp;
   }
   
   /**
    * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
    * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
 * @param <U>
    * 
    * @see #handleEdit(VComponent, Collection, Temporal, Temporal, Temporal, Collection)
    */
   private static <T extends VComponentDisplayableBase<?>, U extends Temporal> void editOne(
           T vComponentOriginal,
           T vComponentEditedCopy,
           Collection<T> vComponents,
           Temporal startOriginalRecurrence,
           U startRecurrence,
           U endRecurrence
//           TemporalAmount shiftAmount
           )
   {
       // Remove RRule and set parent component
       vComponentEditedCopy.setRecurrenceRule((RecurrenceRule) null);
//       setParent(vComponentOriginal);

       // Apply dayShift, account for editing recurrence beyond first
       Period shiftAmount = Period.between(LocalDate.from(vComponentEditedCopy.getDateTimeStart().getValue()),
                 LocalDate.from(startOriginalRecurrence));
       Temporal newStart = vComponentEditedCopy.getDateTimeStart().getValue().plus(shiftAmount);
       vComponentEditedCopy.setDateTimeStart(newStart);
       vComponentEditedCopy.adjustDateTime(startOriginalRecurrence, startRecurrence, endRecurrence);
//       Temporal startOriginalRecurrence = startRecurrence.plus(shiftAmount);
       vComponentEditedCopy.setRecurrenceId(startOriginalRecurrence);
       vComponentEditedCopy.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
  
       // Add recurrence to original vEvent - done automatically
//       vComponentOriginal.childComponents().add(vComponentEditedCopy);
       
       // Check for validity
       if (! vComponentEditedCopy.isValid()) { throw new RuntimeException("Invalid component"); }
//       System.out.println("here:" + vComponentOriginal);
       if (! vComponentOriginal.isValid()) { throw new RuntimeException("Invalid component"); }
       
       // Remove old recurrences, add back ones
//       Collection<R> recurrencesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//       recurrencesTemp.addAll(recurrences);
//       Collection<R> componentRecurrences = vComponentRecurrencetMap.get(vComponentEditedCopy);
//       recurrencesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
//       List<R> newRecurrences = makeRecurrences(vComponentOriginal);
//       vComponentRecurrencetMap.put(System.identityHashCode(vComponentOriginal), newRecurrences);
//       recurrencesTemp.addAll(newRecurrences); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)

//       recurrences().clear(); // clear vEvent outdated collection of recurrences
//       recurrencesTemp.addAll(makeRecurrences()); // add vEventOld part of new recurrences
       vComponents.add(vComponentOriginal);
//       return recurrencesTemp;
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
      
        public static RRuleStatus getRRuleType(RecurrenceRule rruleEdited, RecurrenceRule rruleOriginal)
        {
            if (rruleOriginal == null)
            {
                if (rruleEdited == null)
                { // edited doesn't have repeat or original have repeat either
                    return RRuleStatus.INDIVIDUAL;
                } else {
                    return RRuleStatus.HAD_REPEAT_BECOMING_INDIVIDUAL;
                }
            } else
            { // RRule != null
                if (rruleEdited == null)
                {
                    return RRuleStatus.WITH_NEW_REPEAT;                
                } else
                {
                    return RRuleStatus.WITH_EXISTING_REPEAT;
                }
            }
        }
    }

     /*
      * When one of these properties the change-scope dialog should be activated to determine if the changes should be applied
      * to ONE, ALL, or THIS_AND_FUTURE recurrences.  If other properties are modified the changes should be applied without a dialog.
      * 
      * Currently, only the following are implemented in the control:
      * DATE_TIME_START
      * DATE_TIME_END
      * DESCRIPTION
      * LOCATION
      * RECURRENCE_RULE
      * SUMMARY
      */
     private final static List<PropertyType> DIALOG_REQUIRED_PROPERTIES = Arrays.asList(             
             PropertyType.ATTACHMENT,
             PropertyType.ATTENDEE,
             PropertyType.CATEGORIES,
             PropertyType.COMMENT,
             PropertyType.CONTACT,
             PropertyType.DATE_TIME_START,
             PropertyType.DATE_TIME_END,
             PropertyType.DESCRIPTION,
             PropertyType.DURATION,
             PropertyType.GEOGRAPHIC_POSITION,
             PropertyType.LOCATION,
             PropertyType.PRIORITY,
             PropertyType.RESOURCES,
             PropertyType.RECURRENCE_RULE,
             PropertyType.STATUS,
             PropertyType.SUMMARY,
             PropertyType.UNIFORM_RESOURCE_LOCATOR
             );



}
