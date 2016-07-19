package jfxtras.labs.icalendarfx.components.revisors;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class ReviserDisplayable<T, U extends VComponentDisplayable<U>> implements Reviser
{
    public ReviserDisplayable(U vComponent)
    {
        setVComponentEdited(vComponent);
    }

    public U getVComponentEdited() { return vComponentEdited; }
    private U vComponentEdited;
    public void setVComponentEdited(U vComponentEdited) { this.vComponentEdited = vComponentEdited; }
    public T withVComponentEdited(U vComponentEdited) { setVComponentEdited(vComponentEdited); return (T) this; }

    public U getVComponentOriginal() { return vComponentOriginal; }
    private U vComponentOriginal;
    public void setVComponentOriginal(U vComponentOriginal) { this.vComponentOriginal = vComponentOriginal; }
    public T withVComponentOriginal(U vComponentOriginal) { setVComponentOriginal(vComponentOriginal); return (T) this; }

    public List<U> getVComponents() { return vComponents; }
    private List<U> vComponents;
    /** Can be null if only the returned changed components are only desired */
    public void setVComponents(List<U> vComponents) { this.vComponents = vComponents; }
    public T withVComponents(List<U> vComponents) { setVComponents(vComponents); return (T) this; }

    public Temporal getStartOriginalRecurrence() { return startOriginalRecurrence; }
    private Temporal startOriginalRecurrence;
    public void setStartOriginalRecurrence(Temporal startOriginalRecurrence) { this.startOriginalRecurrence = startOriginalRecurrence; }
    public T withStartOriginalRecurrence(Temporal startOriginalRecurrence) { setStartOriginalRecurrence(startOriginalRecurrence); return (T) this; }
    
    public Temporal getStartRecurrence() { return startRecurrence; }
    private Temporal startRecurrence;
    public void setStartRecurrence(Temporal startRecurrence) { this.startRecurrence = startRecurrence; }
    public T withStartRecurrence(Temporal startRecurrence) { setStartRecurrence(startRecurrence); return (T) this; }
    
    public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> getDialogCallback() { return dialogCallback; }
    private Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback;    
    public void setDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback) { this.dialogCallback = dialogCallback; }
    public T withDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback)
    {
        setDialogCallback(dialogCallback);
        return (T) this;
    }

    boolean isValid()
    {
        if (getVComponentEdited() == null)
        {
            System.out.println("vComponentEdited must not be null");
            return false;
        }
        if (getVComponentOriginal() == null)
        {
            System.out.println("vComponentOriginal must not be null");
            return false;
        }
        if (getStartOriginalRecurrence() == null)
        {
            System.out.println("startOriginalRecurrence must not be null");
            return false;
        }
        if (getStartRecurrence() == null)
        {
            System.out.println("startRecurrence must not be null");
            return false;
        }
        if (getDialogCallback() == null)
        {
            System.out.println("dialogCallback must not be null");
            return false;
        }
        return true;   
    }
    
    /** Main method to edit VEvent or VTodo or VJournal */
    @Override
    public List<U> revise()
    {
        if (! isValid())
        {
            throw new RuntimeException("Invalid parameters for component revision:");
        }
        
        // Copy edited component for further changes (i.e. UID, date/time)
        U vComponentEditedCopy = null;
        try
        {
            vComponentEditedCopy = (U) getVComponentEdited().getClass().newInstance();
            vComponentEditedCopy.copyFrom(getVComponentEdited());
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        U vComponentOriginalCopy = getVComponentOriginal();
        Temporal startRecurrence = getStartRecurrence();
        Temporal startOriginalRecurrence = getStartOriginalRecurrence();

        if (! vComponentOriginalCopy.isValid())
        {
            throw new RuntimeException("Can't revise. Original component is invalid:" + System.lineSeparator() + 
                    vComponentEditedCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponentEditedCopy.toContent());
        }
        
        List<U> revisedVComponents = new ArrayList<>(Arrays.asList(vComponentEditedCopy)); // new components that should be added to main list
        validateStartRecurrenceAndDTStart(vComponentEditedCopy, getStartRecurrence());
        final RRuleStatus rruleType = RRuleStatus.getRRuleType(vComponentOriginalCopy.getRecurrenceRule(), vComponentEditedCopy.getRecurrenceRule());
        boolean incrementSequence = true;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            becomeNonRecurring(vComponentEditedCopy);
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            adjustStartAndEnd(vComponentEditedCopy, vComponentOriginalCopy);
//            adjustDateTime(vComponentEditedCopy);
            break;
        case WITH_EXISTING_REPEAT:
            // Find which properties changed
            List<PropertyType> changedProperties = findChangedProperties(vComponentEditedCopy, vComponentOriginalCopy);
            /* Note:
             * time properties must be checked separately because changes are stored in startRecurrence and endRecurrence,
             * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
             * question is answered. */
            // determine if any changed properties warrant dialog
            boolean provideDialog = changedProperties.stream()
                .map(p -> dialogRequiredProperties().contains(p))
                .anyMatch(b -> b == true);
            if (changedProperties.size() > 0) // if changes occurred
            {
                List<U> relatedVComponents = Arrays.asList(vComponentEditedCopy); // TODO - support related components
                final ChangeDialogOption changeResponse;
                if (provideDialog)
                {
                    Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = ChangeDialogOption.makeDialogChoices(
                            vComponentOriginalCopy,
                            vComponentEditedCopy,
                            startOriginalRecurrence,
                            changedProperties);
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
                        adjustDateTime(vComponentEditedCopy);
                        // Adjust children components with RecurrenceIDs
                        vComponentEditedCopy.childComponents()
                                .stream()
                                .forEach(v ->
                                {
                                    TemporalAmount shiftAmount = DateTimeUtilities.temporalAmountBetween(startOriginalRecurrence, startRecurrence);
                                    Temporal newRecurreneId = v.getRecurrenceId().getValue().plus(shiftAmount);
                                    v.setRecurrenceId(newRecurreneId);
                                });
                    } else
                    {
                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
                    }
                    break;
                case CANCEL:
                    revisedVComponents.clear(); // remove vComponentEditedCopy
                case THIS_AND_FUTURE:
                    editThisAndFuture(vComponentEditedCopy, vComponentOriginalCopy);
                    revisedVComponents.add(0, vComponentOriginalCopy);
                    break;
                case ONE:
                    editOne(vComponentEditedCopy);
                    revisedVComponents.add(0, vComponentOriginalCopy);
                    break;
                default:
                    throw new RuntimeException("Unsupprted response:" + changeResponse);
                }
            }
        }

        if (incrementSequence)
        {
            vComponentEditedCopy.incrementSequence();
        }
        if (! vComponentEditedCopy.isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    vComponentEditedCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponentEditedCopy.toContent());
        }
        if (getVComponents() != null)
        {
            getVComponents().remove(getVComponentEdited());
            getVComponents().addAll(revisedVComponents);
        }
        return revisedVComponents;
//        getVComponents().remove(getVComponentEdited());
//        getVComponents().addAll(revisedVComponents);
    }
    
    /** If startRecurrence isn't valid due to a RRULE change, change startRecurrence and
     * endRecurrence to closest valid values
     */
     // TODO - VERITFY THIS WORKS - changed from old version
    void validateStartRecurrenceAndDTStart(U vComponentEditedCopy, Temporal startRecurrence)
    {
        if (vComponentEditedCopy.getRecurrenceRule() != null)
        {
            Temporal firstTemporal = vComponentEditedCopy.getRecurrenceRule().getValue()
                    .streamRecurrences(vComponentEditedCopy.getDateTimeStart().getValue())
                    .findFirst()
                    .get();
            if (! firstTemporal.equals(vComponentEditedCopy.getDateTimeStart().getValue()))
            {
                vComponentEditedCopy.setDateTimeStart(firstTemporal);
            }
        }
    }
    
    void becomeNonRecurring(U vComponentEditedCopy)
    {
        vComponentEditedCopy.setRecurrenceRule((RecurrenceRule2) null);
        vComponentEditedCopy.setRecurrenceDates(null);
        vComponentEditedCopy.setExceptionDates(null);
    }
    
    /** Adjust start date/time */
    @Deprecated
    void adjustDateTime(U vComponentEditedCopy)
    {
        // TODO - DescriptiveVBox needs to keep zone
        TemporalAmount shiftAmount = DateTimeUtilities.temporalAmountBetween(getStartOriginalRecurrence(), getStartRecurrence());
        TemporalAmount amountToStart = DateTimeUtilities.temporalAmountBetween(vComponentEditedCopy.getDateTimeStart().getValue(), getStartRecurrence());
        Temporal newStart = getStartRecurrence().minus(amountToStart).plus(shiftAmount);
        vComponentEditedCopy.setDateTimeStart(new DateTimeStart(newStart));
    }
    
    /**
     * Generates a list of iCalendar property names that have different values from the 
     * input parameter
     * 
     * equal checks are encapsulated inside the enum VComponentProperty
     * @param <T>
     * @param <U>
     */
    List<PropertyType> findChangedProperties(U vComponentEditedCopy, U vComponentOriginalCopy)

    {
        List<PropertyType> changedProperties = new ArrayList<>();

        vComponentEditedCopy.childrenUnmodifiable()
                .stream()
                .filter(c -> c instanceof Property<?>)
                .map(p -> ((Property<?>) p).propertyType())
                .forEach(t ->
                {
                    Object p1 = t.getProperty(vComponentEditedCopy);
                    Object p2 = t.getProperty(vComponentOriginalCopy);
//                    System.out.println("ppp:" + p1 + " " + p2);
                    if (! p1.equals(p2))
                    {
                        changedProperties.add(t);
                    }
                });
        
        /* Note:
         * time properties must be checked separately because changes are stored in startRecurrence and endRecurrence,
         * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
         * question is answered. */
        if (! startOriginalRecurrence.equals(startRecurrence))
        {
            changedProperties.add(PropertyType.DATE_TIME_START);
        }        
        return changedProperties;
    }
    
    /*
     * When one of these properties the change-scope dialog should be activated to determine if the changes should be applied
     * to ONE, ALL, or THIS_AND_FUTURE recurrences.  If other properties are modified the changes should be applied without a dialog.
     */
    public List<PropertyType> dialogRequiredProperties()
    {
        return new ArrayList<>(Arrays.asList(             
            PropertyType.ATTACHMENT,
            PropertyType.ATTENDEE,
            PropertyType.CATEGORIES,
            PropertyType.COMMENT,
            PropertyType.CONTACT,
            PropertyType.DATE_TIME_START,
            PropertyType.DATE_TIME_END,
            PropertyType.RECURRENCE_RULE,
            PropertyType.STATUS,
            PropertyType.SUMMARY,
            PropertyType.UNIFORM_RESOURCE_LOCATOR
            ));
    }
    
    /**
     * Changing this and future instances in VComponent is done by ending the previous
     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.  vEventNew has new settings, vEvent has former settings.
     * 
     */
    void editThisAndFuture(U vComponentEditedCopy, U vComponentOriginalCopy)
    {
        // Reset COUNT, set UNTIL
        if (vComponentOriginalCopy.getRecurrenceRule().getValue().getCount() != null)
        {
            vComponentOriginalCopy.getRecurrenceRule().getValue().setCount(null);
        }

        /*
         * Assigning UNTIL must be done before adjusting the start and end or the previousStreamValue
         * will not be valid.
         */
        final Temporal untilNew;
        if (vComponentEditedCopy.isWholeDay())
        {
            untilNew = vComponentEditedCopy.previousStreamValue(getStartOriginalRecurrence());
        } else
        {
            Temporal previousRecurrence = vComponentEditedCopy.previousStreamValue(getStartOriginalRecurrence());
            if (getStartOriginalRecurrence() instanceof LocalDateTime)
            {
                untilNew = LocalDateTime.from(previousRecurrence).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
            } else if (getStartOriginalRecurrence() instanceof ZonedDateTime)
            {
                untilNew = ((ZonedDateTime) previousRecurrence).withZoneSameInstant(ZoneId.of("Z"));
            } else
            {
                throw new DateTimeException("Unsupported Temporal type:" + previousRecurrence.getClass());
            }
            vComponentOriginalCopy.getRecurrenceRule().getValue().setUntil(untilNew);
        }
        
        // Adjust start and end - set recurrence temporal as start
        adjustStartAndEnd(vComponentEditedCopy, vComponentOriginalCopy);

        String relatedUID = (vComponentOriginalCopy.getRelatedTo() == null) ?
                vComponentOriginalCopy.getUniqueIdentifier().getValue() : vComponentOriginalCopy.getRelatedTo().get(0).getValue();
        vComponentEditedCopy.withRelatedTo(relatedUID);
        vComponentEditedCopy.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        
        // remove EXDATEs that are out of bounds
        if (vComponentEditedCopy.getExceptionDates() != null)
        {
            final Iterator<Temporal> exceptionDateIterator = vComponentEditedCopy.getExceptionDates()
                    .stream()
                    .flatMap(e -> e.getValue().stream())
                    .iterator();
            while (exceptionDateIterator.hasNext())
            {
                Temporal t = exceptionDateIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
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
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
                if (result > 0)
                {
                    exceptionDateIterator.remove();
                }
            }
        }
        
        // remove RDATEs that are out of bounds
        if (vComponentEditedCopy.getRecurrenceDates() != null)
        {
            final Iterator<Temporal> recurrenceDateIterator = vComponentEditedCopy.getRecurrenceDates()
                    .stream()
                    .flatMap(e -> e.getValue().stream())
                    .iterator();
            while (recurrenceDateIterator.hasNext())
            {
                Temporal t = recurrenceDateIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
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
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
                if (result > 0)
                {
                    recurrenceDateIterator.remove();
                }
            }
        }
        
        // remove RECURRENCE-ID components that are out of bounds
        if (vComponentEditedCopy.childComponents() != null)
        {
            final Iterator<Temporal> recurrenceIDIterator = vComponentEditedCopy.childComponents()
                    .stream()
                    .map(e -> e.getRecurrenceId().getValue())
                    .iterator();
            while (recurrenceIDIterator.hasNext())
            {
                Temporal t = recurrenceIDIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
                if (result < 0)
                {
                    recurrenceIDIterator.remove();
                }
            }
        }
        if (vComponentOriginalCopy.getRecurrenceDates() != null)
        {
            final Iterator<Temporal> recurrenceIDIterator = vComponentOriginalCopy.childComponents()
                    .stream()
                    .map(e -> e.getRecurrenceId().getValue())
                    .iterator();
            while (recurrenceIDIterator.hasNext())
            {
                Temporal t = recurrenceIDIterator.next();
                int result = DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t, getStartRecurrence());
                if (result > 0)
                {
                    recurrenceIDIterator.remove();
                }
            }
        }
        
        // Modify COUNT for the edited vEvent
        if (vComponentEditedCopy.getRecurrenceRule().getValue().getCount() != null)
        {
            int countInOrginal = (int) vComponentOriginalCopy.streamRecurrences().count();
            int countInNew = vComponentEditedCopy.getRecurrenceRule().getValue().getCount().getValue() - countInOrginal;
            // TODO - NEED TO CHECK IF COUNT IS LESS THAN 1 AND PROHIBIT THIS-AND-FUTURE EDIT
            vComponentEditedCopy.getRecurrenceRule().getValue().setCount(countInNew);
        }
        
        if (! vComponentOriginalCopy.isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    vComponentOriginalCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponentOriginalCopy.toContent());
        }
        
        vComponentEditedCopy.setUniqueIdentifier(); // TODO - NEED TO REGISTER CHANGE WITH VCALENDAR MAP
    }
    
    void adjustStartAndEnd(U vComponentEditedCopy, U vComponentOriginalCopy)
    {
        // no op hook, override in subclasses
    }
    
    /**
     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
     * 
     */
    void editOne(U vComponentEditedCopy)
    {
        vComponentEditedCopy.setRecurrenceRule((RecurrenceRule) null);
        vComponentEditedCopy.setDateTimeStart(new DateTimeStart(getStartRecurrence()));
        vComponentEditedCopy.setRecurrenceId(startOriginalRecurrence);
        vComponentEditedCopy.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
    }
    
   public enum RRuleStatus
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
}
