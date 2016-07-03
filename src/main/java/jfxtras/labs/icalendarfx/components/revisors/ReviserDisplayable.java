package jfxtras.labs.icalendarfx.components.revisors;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class ReviserDisplayable<T, U extends VComponentDisplayable<U>> extends ReviserBase<T, U>
{ 
    public ReviserDisplayable(U component)
    {
        setVComponentEdited(component);
    }

    public U getVComponentEdited() { return vComponentEdited; }
    private U vComponentEdited;
    public void setVComponentEdited(U vComponentEdited) { this.vComponentEdited = vComponentEdited; }
    public T withVComponentEdited(U vComponentEdited) { setVComponentEdited(vComponentEdited); return (T) this; }

    public U getVComponentOriginal() { return vComponentOriginal; }
    private U vComponentOriginal;
    public void setVComponentOriginal(U vComponentOriginal) { this.vComponentOriginal = vComponentOriginal; }
    public T withVComponentOriginal(U vComponentOriginal) { setVComponentOriginal(vComponentOriginal); return (T) this; }

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
    
    /** Edit VEvent or VTodo or VJournal */
    @Override
    public Collection<U> revise()
    {
        if (! isValid())
        {
            throw new RuntimeException("Invalid parameters for component revision:");
        }
        
        U vComponentEdited = getVComponentEdited();
        U vComponentOriginal = getVComponentOriginal();
        Temporal startRecurrence = getStartRecurrence();
        Temporal startOriginalRecurrence = getStartOriginalRecurrence();
        if (! getVComponentEdited().isValid())
        {
            throw new RuntimeException("Can't revise. Edited component is invalid:" + System.lineSeparator() + 
                    getVComponentEdited().errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    getVComponentEdited().toContent());
        }
        if (! getVComponentOriginal().isValid())
        {
            throw new RuntimeException("Can't revise. Original component is invalid:" + System.lineSeparator() + 
                    getVComponentEdited().errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    getVComponentEdited().toContent());
        }
        
        Collection<U> vComponents = new ArrayList<>(); // new components that should be added to main list
        validateStartRecurrenceAndDTStart(getStartRecurrence());
        final RRuleStatus rruleType = RRuleStatus.getRRuleType(getVComponentOriginal().getRecurrenceRule(), getVComponentEdited().getRecurrenceRule());
        System.out.println("rruleType:" + rruleType);
        boolean incrementSequence = true;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            becomeNonRecurring();
            // fall through
        case WITH_NEW_REPEAT: // no dialog
        case INDIVIDUAL:
            adjustDateTime();
            break;
        case WITH_EXISTING_REPEAT:
            // Find which properties changed
            List<PropertyType> changedProperties = findChangedProperties();
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
                List<U> relatedVComponents = Arrays.asList(vComponentEdited); // TODO - support related components
                final ChangeDialogOption changeResponse;
                if (provideDialog)
                {
                    Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = ChangeDialogOption.makeDialogChoices(
                            vComponentOriginal,
                            vComponentEdited,
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
                        adjustDateTime();
                        // Adjust children components with RecurrenceIDs
                        getVComponentEdited().childComponents()
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
                    return null;
                case THIS_AND_FUTURE:
                    editThisAndFuture();
                    vComponents.add(vComponentOriginal);
                    break;
                case ONE:
                    editOne();
                    vComponents.add(vComponentOriginal);
                    break;
                default:
                    throw new RuntimeException("Unknown response:" + changeResponse);
                }
            }
        }
        
        if (! getVComponentEdited().isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    getVComponentEdited().errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    getVComponentEdited().toContent());
        }
        if (incrementSequence)
        {
            getVComponentEdited().incrementSequence();
        }
        vComponents.add(vComponentEdited);
        return vComponents;
    }
    
    /** If startRecurrence isn't valid due to a RRULE change, change startRecurrence and
     * endRecurrence to closest valid values
     */
     // TODO - VERITFY THIS WORKS - changed from old version
    void validateStartRecurrenceAndDTStart(Temporal startRecurrence)
    {
        if (getVComponentEdited().getRecurrenceRule() != null)
        {
            Temporal firstTemporal = getVComponentEdited().getRecurrenceRule().getValue()
                    .streamRecurrences(getVComponentEdited().getDateTimeStart().getValue())
                    .findFirst()
                    .get();
            if (! firstTemporal.equals(getVComponentEdited().getDateTimeStart().getValue()))
            {
                getVComponentEdited().setDateTimeStart(firstTemporal);
            }
        }
    }
    
    void becomeNonRecurring()
    {
        getVComponentEdited().setRecurrenceRule((RecurrenceRule2) null);
        getVComponentEdited().setRecurrenceDates(null);
        getVComponentEdited().setExceptionDates(null);
    }
    
    /** Adjust start date/time */
    void adjustDateTime()
    {
        TemporalAmount amount = DateTimeUtilities.temporalAmountBetween(getStartOriginalRecurrence(), getStartRecurrence());
        Temporal newStart = getVComponentEdited().getDateTimeStart().getValue().plus(amount);
        getVComponentEdited().setDateTimeStart(newStart);
    }
    
    /**
     * Generates a list of iCalendar property names that have different values from the 
     * input parameter
     * 
     * equal checks are encapsulated inside the enum VComponentProperty
     * @param <T>
     * @param <U>
     */
    List<PropertyType> findChangedProperties()

    {
        List<PropertyType> changedProperties = new ArrayList<>();

        getVComponentEdited().childrenUnmodifiable()
                .stream()
                .filter(c -> c instanceof Property<?>)
                .map(p -> ((Property<?>) p).propertyType())
                .forEach(t ->
                {
                    Object p1 = t.getProperty(getVComponentEdited());
                    Object p2 = t.getProperty(getVComponentOriginal());
//                    System.out.println("prop:" + p1 + " " + p2);
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
    void editThisAndFuture()
    {
        // Reset COUNT, set UNTIL
        if (getVComponentOriginal().getRecurrenceRule().getValue().getCount() != null)
        {
            getVComponentOriginal().getRecurrenceRule().getValue().setCount(null);
        }
        
        final Temporal untilNew;
        if (getVComponentEdited().isWholeDay())
        {
            untilNew = getVComponentEdited().previousStreamValue(getStartRecurrence());
        } else
        {
            System.out.println("getStartRecurrence():" + getStartRecurrence() + " " + getVComponentEdited().previousStreamValue(getStartRecurrence()));
            Temporal previousRecurrence = getVComponentEdited().previousStreamValue(getStartRecurrence());
            if (getStartRecurrence() instanceof LocalDateTime)
            {
                untilNew = LocalDateTime.from(previousRecurrence).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
            } else if (getStartRecurrence() instanceof ZonedDateTime)
            {
                untilNew = ((ZonedDateTime) previousRecurrence).withZoneSameInstant(ZoneId.of("Z"));
            } else
            {
                throw new DateTimeException("Unsupported Temporal type:" + previousRecurrence.getClass());
            }
            getVComponentOriginal().getRecurrenceRule().getValue().setUntil(untilNew);
        }

        // Adjust start and end
        getVComponentEdited().setUniqueIdentifier();
        getVComponentEdited().setDateTimeStart(getStartRecurrence());

        String relatedUID = (getVComponentOriginal().getRelatedTo() == null) ?
                getVComponentOriginal().getUniqueIdentifier().getValue() : getVComponentOriginal().getRelatedTo().get(0).getValue();
        getVComponentEdited().withRelatedTo(relatedUID);
        getVComponentEdited().setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        
        // remove EXDATEs that are out of bounds
        if (getVComponentEdited().getExceptionDates() != null)
        {
            final Iterator<Temporal> exceptionDateIterator = getVComponentEdited().getExceptionDates()
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
        if (getVComponentOriginal().getExceptionDates() != null)
        {
            final Iterator<Temporal> exceptionDateIterator = getVComponentOriginal().getExceptionDates()
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
        if (getVComponentEdited().getRecurrenceDates() != null)
        {
            final Iterator<Temporal> recurrenceDateIterator = getVComponentEdited().getRecurrenceDates()
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
        if (getVComponentOriginal().getRecurrenceDates() != null)
        {
            final Iterator<Temporal> recurrenceDateIterator = getVComponentOriginal().getRecurrenceDates()
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
        if (getVComponentEdited().childComponents() != null)
        {
            final Iterator<Temporal> recurrenceIDIterator = getVComponentEdited().childComponents()
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
        if (getVComponentOriginal().getRecurrenceDates() != null)
        {
            final Iterator<Temporal> recurrenceIDIterator = getVComponentOriginal().childComponents()
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
//        System.out.println(getVComponentOriginal().getRecurrenceRule().getValue());
//        System.out.println(getVComponentEdited().getRecurrenceRule().getValue());
        if (getVComponentEdited().getRecurrenceRule().getValue().getCount() != null)
        {
            int countInOrginal = (int) getVComponentOriginal().streamRecurrences().count();
            int countInNew = getVComponentEdited().getRecurrenceRule().getValue().getCount().getValue() - countInOrginal;
            getVComponentEdited().getRecurrenceRule().getValue().setCount(countInNew);
        }
        
        if (! getVComponentOriginal().isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    getVComponentOriginal().errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    getVComponentOriginal().toContent());
        }
    }
    
    /**
     * Edit one instance of a VEvent with a RRule.  The instance becomes a new VEvent without a RRule
     * as with the same UID as the parent and a recurrence-id for the replaced date or date/time.
     * 
     */
    void editOne()
    {
        getVComponentEdited().setRecurrenceRule((RecurrenceRule) null);
        getVComponentEdited().setDateTimeStart(getStartRecurrence());
        getVComponentEdited().setRecurrenceId(startOriginalRecurrence);
        getVComponentEdited().setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
    }
    
    private enum RRuleStatus
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
