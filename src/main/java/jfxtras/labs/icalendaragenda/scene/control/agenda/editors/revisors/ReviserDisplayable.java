package jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VDisplayable;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Handles revising one or all recurrences of a {@link VDisplayable}
 * 
 * @author David Bal
 *
 * @param <T> concrete implementation of this class
 * @param <U> concrete {@link VDisplayable} class
 */
public abstract class ReviserDisplayable<T, U extends VDisplayable<U>> implements Reviser
{
    public ReviserDisplayable(U vComponent)
    {
        setVComponentEdited(vComponent);
    }

    /*
     * VCOMPONENT EDITED
     */
    /** Gets the value of the edited {@link VDisplayable} copy.  Note: don't pass original or
     * the changes will be instantaneous and cancel is not possible. */
    public U getVComponentEdited() { return vComponentEditedCopy; }
    private U vComponentEditedCopy;
    /** Sets the value of the edited {@link VDisplayable} copy.  Note: don't pass original or
     * the changes will be instantenous and cancel is not possible. */
    public void setVComponentEdited(U vComponentEdited) { this.vComponentEditedCopy = vComponentEdited; }
    /**
     * Sets the value of the edited {@link VDisplayable}
     * 
     * @return - this class for chaining
     * @see VCalendar
     */
    public T withVComponentEdited(U vComponentEdited) { setVComponentEdited(vComponentEdited); return (T) this; }

    /*
     * VCOMPONENT ORIGINAL
     */
    /** Gets the value of the original {@link VDisplayable} */
    public U getVComponentOriginal() { return vComponentOriginal; }
    private U vComponentOriginal;
    /** Sets the value of the original {@link VDisplayable} */
    public void setVComponentOriginal(U vComponentOriginal) { this.vComponentOriginal = vComponentOriginal; }
    /**
     * Sets the value of the edited {@link VDisplayable}
     * 
     * @return - this class for chaining
     * @see VCalendar
     */
    public T withVComponentOriginalCopy(U vComponentOriginal) { setVComponentOriginal(vComponentOriginal); return (T) this; }

//    // TODO - CHANGE TO VCALENDAR
//    /*
//     * VCOMPONENTS
//     */
//    /** Gets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * @see VCalendar */
//    public List<U> getVComponents() { return vComponents; }
//    private List<U> vComponents;
//    /** Sets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * @see VCalendar */
//    public void setVComponents(List<U> vComponents) { this.vComponents = vComponents; }
//    /**
//     * Sets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * 
//     * @return - this class for chaining
//     * @see VCalendar
//     */
//    public T withVComponents(List<U> vComponents) { setVComponents(vComponents); return (T) this; }

//    /*
//     * VCALENDAR
//     */
//    /** Gets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * @see VCalendar */
//    public VCalendar getVCalendar() { return vCalendar; }
//    private VCalendar vCalendar;
//    /** Sets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * @see VCalendar */
//    public void setVCalendar(VCalendar vCalendar) { this.vCalendar = vCalendar; }
//    /**
//     * Sets the value of the List of {@link VComponent} that contains the {@link VComponent} to be edited 
//     * 
//     * @return - this class for chaining
//     * @see VCalendar
//     */
//    public T withVCalendar(VCalendar vCalendar) { setVCalendar(vCalendar); return (T) this; }

    
    /*
     * START ORIGINAL RECURRENCE
     */
    /** Gets the value of the start of the selected recurrence, before changes */
    public Temporal getStartOriginalRecurrence() { return startOriginalRecurrence; }
    private Temporal startOriginalRecurrence;
    /** Sets the value of the start of the selected recurrence, before changes */
    public void setStartOriginalRecurrence(Temporal startOriginalRecurrence) { this.startOriginalRecurrence = startOriginalRecurrence; }
    /**
     * Sets the value of the start of the selected recurrence, before changes
     * 
     * @return - this class for chaining
     */
    public T withStartOriginalRecurrence(Temporal startOriginalRecurrence) { setStartOriginalRecurrence(startOriginalRecurrence); return (T) this; }

    /*
     * START RECURRENCE - NEW VALUE
     */
    /** Gets the value of the start of the selected recurrence, after changes */
    public Temporal getStartRecurrence() { return startRecurrence; }
    private Temporal startRecurrence;
    /** Sets the value of the start of the selected recurrence, after changes */
    public void setStartRecurrence(Temporal startRecurrence) { this.startRecurrence = startRecurrence; }
    /**
     * Sets the value of the start of the selected recurrence, after changes
     * 
     * @return - this class for chaining
     */
    public T withStartRecurrence(Temporal startRecurrence) { setStartRecurrence(startRecurrence); return (T) this; }
    
    /*
     * CHANGE DIALOG CALLBACK
     */
    /** Gets the value of the dialog callback to prompt the user to select revision option */
    public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> getDialogCallback() { return dialogCallback; }
    private Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback;    
    /** Sets the value of the dialog callback to prompt the user to select revision option */
    public void setDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback) { this.dialogCallback = dialogCallback; }
    /**
     * Sets the value of the dialog callback to prompt the user to select revision option
     * 
     * @return - this class for chaining
     */
    public T withDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> dialogCallback)
    {
        setDialogCallback(dialogCallback);
        return (T) this;
    }

    /** Tests the object state is valid and revision can proceed.  Returns true if valid, false otherwise */
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
//        if (getVCalendar() == null)
//        {
//            System.out.println("vCalendar must not be null");
//            return false;
//        }
        //        if (getVComponents() == null)
//        {
//            System.out.println("getVComponents must not be null");
//            return false;
//        }
        return true;  
    }
    
    @Override
    public List<VCalendar> revise()
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
//            vComponentEditedCopy.copyFrom(getVComponentEdited());
            getVComponentEdited().copyInto(vComponentEditedCopy);
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        // Copy original component for further changes
        U vComponentOriginalCopy = null;
        try
        {
            vComponentOriginalCopy = (U) getVComponentOriginal().getClass().newInstance();
//            vComponentOriginalCopy.copyFrom(getVComponentOriginal());
            getVComponentOriginal().copyInto(vComponentOriginalCopy);
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        Temporal startRecurrence = getStartRecurrence();
        Temporal startOriginalRecurrence = getStartOriginalRecurrence();

        if (! vComponentOriginalCopy.isValid())
        {
            throw new RuntimeException("Can't revise. Original component is invalid:" + System.lineSeparator() + 
                    vComponentEditedCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponentEditedCopy.toContent());
        }
        
        List<VCalendar> itipMessages = new ArrayList<>();
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
        {
            VCalendar message;
            if (vComponentEditedCopy.getParent() == null)
            {
                message = Reviser.emptyPublishiTIPMessage();
                incrementSequence = false;
            } else
            {
                message = Reviser.emptyRequestiTIPMessage();
            }
            adjustStartAndEnd(vComponentEditedCopy, vComponentOriginalCopy);
            message.addVComponent(vComponentEditedCopy);
            itipMessages.add(message);
            break;
        }
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
//                List<U> relatedVComponents = Arrays.asList(vComponentEditedCopy); // TODO - possibly support related components
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
                {
                    adjustDateTime(vComponentEditedCopy);
                    
                    VCalendar requestMessage = Reviser.emptyRequestiTIPMessage();
                    requestMessage.addVComponent(vComponentEditedCopy);
                    itipMessages.add(requestMessage);
                    // Note: Child recurrences become orphans and get deleted when the iTIP message is processed
                    break;
                }
                case ALL_IGNORE_RECURRENCES:
                {
                    adjustDateTime(vComponentEditedCopy);
                    
                    VCalendar requestMessage = Reviser.emptyRequestiTIPMessage();
                    requestMessage.addVComponent(vComponentEditedCopy);
                    itipMessages.add(requestMessage);
                    
                    // update child recurrences
                    VCalendar publishMessage = Reviser.emptyPublishiTIPMessage();
                    List<VDisplayable<?>> children = adjustRecurrenceChildren(startRecurrence, startOriginalRecurrence);
                    publishMessage.addAllVComponents(children);
                    itipMessages.add(publishMessage);
                    break;
                }
                case CANCEL:
                    break;
                case THIS_AND_FUTURE:
                {
                    editThisAndFuture(vComponentEditedCopy, vComponentOriginalCopy);
                    
                    // request message to change original component
                    VCalendar requestMessage = Reviser.emptyRequestiTIPMessage();
                    requestMessage.addVComponent(vComponentOriginalCopy);
                    itipMessages.add(requestMessage);

                    // publish message to add new the-and-future component
                    VCalendar publishMessage = Reviser.emptyPublishiTIPMessage();
                    publishMessage.addVComponent(vComponentEditedCopy);
                    vComponentOriginalCopy.incrementSequence();
                    incrementSequence = false;
                    itipMessages.add(publishMessage);
                    // Note: Child recurrences become orphans and get deleted when the iTIP message is processed
                    break;
                }
                case THIS_AND_FUTURE_IGNORE_RECURRENCES:
                {                   
                    // change edited and original copies
                    editThisAndFuture(vComponentEditedCopy, vComponentOriginalCopy);
//                    thisAndFutureIgnoreRecurrences(vComponentEditedCopy, vComponentEditedCopy);
                    
                    // request message to change original component
                    VCalendar requestMessage = Reviser.emptyRequestiTIPMessage();
                    requestMessage.addVComponent(vComponentOriginalCopy);
                    itipMessages.add(requestMessage);

                    // publish message to add new the-and-future component
                    VCalendar publishMessage = Reviser.emptyPublishiTIPMessage();
                    publishMessage.addVComponent(vComponentEditedCopy);
                    vComponentOriginalCopy.incrementSequence();
                    incrementSequence = false;
                    itipMessages.add(publishMessage);
                    
                    // process child recurrences (new recurrences made if after future edit)
                    String uid = vComponentEditedCopy.getUniqueIdentifier().getValue();
                    List<VDisplayable<?>> children = adjustRecurrenceChildren(startRecurrence, startOriginalRecurrence)
                            .stream()
                            .filter(c -> DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(c.getDateTimeStart().getValue(), startOriginalRecurrence) >= 0) // is after or equals
                            .peek(c ->
                            {
                                c.setUniqueIdentifier(uid);
                                c.setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
                            })
                            .collect(Collectors.toList());
                    publishMessage.addAllVComponents(children);
                    break;
                }
                case ONE:
                {
                    editOne(vComponentEditedCopy);
                    VCalendar message = Reviser.emptyPublishiTIPMessage();
                    message.addVComponent(vComponentEditedCopy);
                    itipMessages.add(message);
                    break;
                }
                default:
                    throw new RuntimeException("Unsupprted response:" + changeResponse);
                }
            }
        }

        if (incrementSequence)
        {
            vComponentEditedCopy.incrementSequence();
        }
//        if (! vComponentEditedCopy.isValid())
//        {
//            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
//                    vComponentEditedCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
//                    vComponentEditedCopy.toContent());
//        }

        return itipMessages;
    }

    // Make a copy of child recurrences, apply time shift, and return them as a List
    private List<VDisplayable<?>> adjustRecurrenceChildren(Temporal startRecurrence, Temporal startOriginalRecurrence)
    {
        return getVComponentOriginal().recurrenceChildren()
                .stream()
                .map(v ->
                {
                    Period dayShift = Period.between(LocalDate.from(startOriginalRecurrence), (LocalDate.from(v.getRecurrenceId().getValue())));
                    Temporal newRecurreneId = startRecurrence.plus(dayShift);
                    try
                    {
                        VDisplayable<?> vCopy = v.getClass().newInstance();
                        v.copyInto(vCopy);
//                        vCopy.copyFrom(v);
                        vCopy.setRecurrenceId(new RecurrenceId(newRecurreneId));
                        return vCopy;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
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
    
    /** Make changes necessary for making a repeating component into a non-repeating component */
    void becomeNonRecurring(U vComponentEditedCopy)
    {
        vComponentEditedCopy.setRecurrenceRule((RecurrenceRule2) null);
        vComponentEditedCopy.setRecurrenceDates(null);
        vComponentEditedCopy.setExceptionDates(null);
    }
    
    /** Adjust start date/time according to changes in selected recurrence */
    void adjustDateTime(U vComponentEditedCopy)
    {
        // TODO - DescriptiveVBox needs to keep zone - what does this mean?
        TemporalAmount shiftAmount = DateTimeUtilities.temporalAmountBetween(getStartOriginalRecurrence(), getStartRecurrence());
        TemporalAmount amountToStart = DateTimeUtilities.temporalAmountBetween(vComponentEditedCopy.getDateTimeStart().getValue(), getStartRecurrence());
        Temporal newStart = getStartRecurrence().minus(amountToStart).plus(shiftAmount);
        // handle WEEKLY day of week change
        if (vComponentEditedCopy.getRecurrenceRule() != null)
        {
            ByDay byDay = (ByDay) vComponentEditedCopy.getRecurrenceRule().getValue().lookupByRule(ByDay.class);
            if (byDay != null)
            {
                DayOfWeek originalDayOfWeek = DayOfWeek.from(vComponentEditedCopy.getDateTimeStart().getValue());
                DayOfWeek replacemenDayOfWeekt = DayOfWeek.from(newStart);
                if (originalDayOfWeek != replacemenDayOfWeekt)
                { // day shift occurred - change ByDay rule
                    byDay.replaceDayOfWeek(originalDayOfWeek, replacemenDayOfWeekt);
                }
            }
        }
        vComponentEditedCopy.setDateTimeStart(new DateTimeStart(newStart));
    }
    
    /**
     * Generates a list of {@link PropertyType} that have changed between the original
     * and edited VComponents.
     * 
     * @param vComponentEditedCopy edited VComponent
     * @param vComponentOriginalCopy original VComponent
     * @return list of changed {@link PropertyType}
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

    /**
     * Returned list of {@link PropertyType} values, that when changed, necessitate a user dialog to determine scope of change.
     * If changes do not contain ANY {@link PropertyType} in the returned list then changes can proceed automatically
     * without a user dialog.
     * 
     * @return {@code List<PropertyType>} that when any are changed require a user dialog to request scope of change
     * (e.g. ONE, ALL or THIS_AND_FUTURE)
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
            PropertyType.RECURRENCE_RULE,
            PropertyType.STATUS,
            PropertyType.SUMMARY,
            PropertyType.UNIFORM_RESOURCE_LOCATOR
            ));
    }
    
    /**
     * ONE
     * 
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
    
    /**
     * THIS AND FUTURE
     * 
     * Handles changing this-and-future recurrences in a VComponent by ending the original
     * VComponent with a UNTIL date or date/time, then starting a new VComponent from 
     * the selected recurrence.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.
     * 
     * @param vComponentEditedCopy VComponent with changes
     * @param vComponentOriginalCopy Unchanged VComponent, except for addition of the UNTIL element.
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
        }
        vComponentOriginalCopy.getRecurrenceRule().getValue().setUntil(untilNew);
        
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

        vComponentEditedCopy.setUniqueIdentifier(); // assign new UID
        
        // Modify COUNT for the edited vEvent
        if (vComponentEditedCopy.getRecurrenceRule().getValue().getCount() != null)
        {
            int countInOrginal = (int) vComponentOriginalCopy.streamRecurrences().count();
            int countInNew = vComponentEditedCopy.getRecurrenceRule().getValue().getCount().getValue() - countInOrginal;
            vComponentEditedCopy.getRecurrenceRule().getValue().setCount(countInNew);
        }
        
        if (! vComponentOriginalCopy.isValid())
        {
            throw new RuntimeException("Invalid component:" + System.lineSeparator() + 
                    vComponentOriginalCopy.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                    vComponentOriginalCopy.toContent());
        }
    }
    
    /** Handle changing the {@link UniqueIdentifier} of recurrence children, if any exist,
     * to match the {@link UniqueIdentifier} of the revised VComponent */
    @Deprecated
    private void thisAndFutureIgnoreRecurrences(List<U> revisedVComponents, U vComponentEditedCopy)
    {
        List<VDisplayable<?>> recurrenceChildren = getVComponentEdited().recurrenceChildren();
        if (! recurrenceChildren.isEmpty())
        {
            recurrenceChildren.stream().forEach(c -> 
            {
                Temporal t = c.getRecurrenceId().getValue();
                boolean isAfterStartRecurrence = DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(t, getStartRecurrence()) > 0;
                if (isAfterStartRecurrence)
                { // change UID to match vComponentEditedCopy
//                    getVComponents().remove(c);
                    String uniqueIdentifier = vComponentEditedCopy.getUniqueIdentifier().getValue();
                    c.setUniqueIdentifier(uniqueIdentifier);
                    revisedVComponents.add((U) c);
                }
           });
        }
    }
    
    void adjustStartAndEnd(U vComponentEditedCopy, U vComponentOriginalCopy)
    {
    	// TODO - SHOULD THIS BE MERGED WITH adjustDateTime(U vComponentEditedCopy)?
        // no op hook, override in subclasses
    }
   
   /**
    * Enum that identifies the VComponent's recurrence rule status
    * 
    * @author David Bal
    *
    */
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
}
