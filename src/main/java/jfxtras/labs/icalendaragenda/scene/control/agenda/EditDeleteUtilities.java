//package jfxtras.labs.icalendaragenda.scene.control.agenda;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.Period;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.temporal.Temporal;
//import java.time.temporal.TemporalAmount;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//import javafx.util.Callback;
//import jfxtras.labs.icalendarfx.components.VComponent;
//import jfxtras.labs.icalendarfx.components.VComponent.StartEndRange;
//import jfxtras.labs.icalendarfx.components.VComponentLocatable;
//import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
//import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
//import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;
//
///**
// * Handles edit and delete functionality of VEvents and VTodos
// * 
// * @author David Bal
// *
// */
//public final class EditDeleteUtilities
//{
//    private EditDeleteUtilities() { }
//    
//    public static <T> boolean handleEdit(
//            VComponentLocatable<T> vComponentEdited
//          , VComponentLocatable<T> vComponentOriginal
//          , Collection<VComponentLocatable<T>> vComponents
//          , Temporal startOriginalRecurrence
//          , Temporal startRecurrence
//          , Temporal endRecurrence
//          , Collection<Object> allRecurrences
//          , Collection<Object> componentRecurrences
//          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
//    {
//        validateStartRecurrenceAndDTStart(vComponentEdited, startOriginalRecurrence, startRecurrence);
//        final RRuleStatus rruleType = RRuleStatus.getRRuleType(vComponentEdited.getRecurrenceRule(), vComponentOriginal.getRecurrenceRule());
//        System.out.println("rruleType:" + rruleType);
//        boolean incrementSequence = true;
//        Collection<Object> newRecurrences = null;
//        switch (rruleType)
//        {
//        case HAD_REPEAT_BECOMING_INDIVIDUAL:
//            vComponentEdited.becomeNonRecurring(vComponentOriginal, startRecurrence, endRecurrence);
//            // fall through
//        case WITH_NEW_REPEAT: // no dialog
//        case INDIVIDUAL:
//            adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
//            if (! vComponentEdited.equals(vComponentOriginal))
//            {
//                newRecurrences = updateRecurrences(allRecurrences, componentRecurrences);
//            }
//            break;
//        case WITH_EXISTING_REPEAT:
//            // Find which properties changed
//            List<String> changedPropertyNames = findChangedProperties(vComponentOriginal);
//            /* Note:
//             * time properties must be checked separately because changes are stored in startRecurrence and endRecurrence,
//             * not the VComponents DTSTART and DTEND yet.  The changes to DTSTART and DTEND are made after the dialog
//             * question is answered. */
//            changedPropertyNames.addAll(changedStartAndEndDateTime(startOriginalRecurrence, startRecurrence, endRecurrence));
//            // determine if any changed properties warrant dialog
////            changedPropertyNames.stream().forEach(a -> System.out.println("changed property:" + a));
//            boolean provideDialog = requiresChangeDialog(changedPropertyNames);
//            if (changedPropertyNames.size() > 0) // if changes occurred
//            {
//                List<VComponent<I>> relatedVComponents = Arrays.asList(this); // TODO - support related components
//                final ChangeDialogOption changeResponse;
//                if (provideDialog)
//                {
//                    Map<ChangeDialogOption, StartEndRange> choices = ChangeDialogOption.makeDialogChoices(this, startOriginalRecurrence);
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
//                        adjustDateTime(vComponentEdited, startOriginalRecurrence, startRecurrence, endRecurrence);
//                        if ((getRRule() != null) && (getRRule().recurrences().size() > 0))
//                        {
//                            getRRule().recurrences().forEach(v ->
//                            {
//                                Temporal newRecurreneId = adjustRecurrenceStart(v.getDateTimeRecurrence(), startOriginalRecurrence, startRecurrence, endRecurrence);
//                                v.setDateTimeRecurrence(newRecurreneId);
//                            });
//                        }
//                        newRecurrences = updateRecurrences(allRecurrences);
//                    } else
//                    {
//                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
//                    }
//                    break;
//                case CANCEL:
//                    vComponentOriginal.copyTo(this); // return to original
//                    return false;
//                case THIS_AND_FUTURE:
//                    newRecurrences = editThisAndFuture(vComponentOriginal, vComponents, startOriginalRecurrence, startRecurrence, endRecurrence, allRecurrences);
//                    break;
//                case ONE:
//                    newRecurrences = editOne(vComponentOriginal, vComponents, startOriginalRecurrence, startRecurrence, endRecurrence, allRecurrences);
//                    break;
//                default:
//                    break;
//                }
//            }
//        }
//        if (! isValid()) throw new RuntimeException(errorString());
//        if (incrementSequence) { incrementSequence(); }
//        if (newRecurrences != null)
//        {
//            allRecurrences.clear();
//            allRecurrences.addAll(newRecurrences);
//        }
//        return true;
//    }
//    
//    /** If startRecurrence isn't valid due to a RRULE change, change startRecurrence and
//     * endRecurrence to closest valid values
//     */
//    // TODO - VERITFY THIS WORKS
//    static void validateStartRecurrenceAndDTStart(VComponentLocatable<?> vComponentEdited, Temporal startOriginalRecurrence, Temporal startRecurrence)
//    {
////        boolean isStreamedValue;
//        if (vComponentEdited.getRecurrenceRule() != null)
//        {
//            Temporal firstTemporal = vComponentEdited.getRecurrenceRule().getValue()
//                    .streamRecurrences(vComponentEdited.getDateTimeStart().getValue())
//                    .findFirst()
//                    .get();
//            if (! firstTemporal.equals(vComponentEdited.getDateTimeStart().getValue()))
//            {
//                vComponentEdited.setDateTimeStart(firstTemporal);
//            }
////            Iterator<Temporal> startRecurrenceIterator = stream(temporal).iterator();
////            while (startRecurrenceIterator.hasNext())
////            {
////                Temporal myStartRecurrence = startRecurrenceIterator.next();
////                if (myStartRecurrence.equals(temporal))
////                {
////                    isStreamedValue = true;
////                }
////                if (DateTimeUtilities.isAfter(myStartRecurrence, temporal))
////                {
////                    isStreamedValue = false;
////                }
////            }
////            isStreamedValue = false;
////            
////            if (! isStreamValue(vComponentEdited.getDateTimeStart()))
////            {            
////                Optional<Temporal> optionalAfter = vComponentEdited.streamRecurrences().findFirst();
////                final Temporal newTestedStart;
////                if (optionalAfter.isPresent())
////                {
////                    newTestedStart = optionalAfter.get();
////                } else
////                {
////                    throw new RuntimeException("No valid DTSTART in VComponent");
////                }
////                TemporalAmount duration = vComponentEdited.getActualDuration();
////    //            Temporal newTestedEnd = newTestedStart.plus(duration);
////                vComponentEdited.setDateTimeStart(newTestedStart);
////            }
////            endType().setDuration(this, duration);
////            setDateTimeEnd(newTestedEnd);
//        }
//    }
//    
//    /* Adjust DTSTART and DTEND, DUE, or DURATION by recurrence's start and end date-time */
//    private static void adjustDateTime(
//            VComponentLocatable<?> vComponentEdited,
//            Temporal startOriginalRecurrence,
//            Temporal startRecurrence,
//            Temporal endRecurrence)
//    {
//        Temporal newStart = adjustRecurrenceStart(
//                vComponentEdited.getDateTimeStart().getValue(),
//                startOriginalRecurrence,
//                startRecurrence,
//                endRecurrence);
//        vComponentEdited.setDateTimeStart(newStart);
//        System.out.println("new DTSTART:" + newStart);
//        vComponentEdited.setEndOrDuration(startRecurrence, endRecurrence);
////        endType().setDuration(this, startRecurrence, endRecurrence);
//    }
//    
//    /* Adjust DTSTART of RECURRENCE-ID */
//    private static Temporal adjustRecurrenceStart(Temporal initialStart
//            , Temporal startOriginalRecurrence
//            , Temporal startRecurrence
//            , Temporal endRecurrence)
//    {
//        DateTimeType newDateTimeType = DateTimeType.of(startRecurrence);
//        ZoneId zone = (startRecurrence instanceof ZonedDateTime) ? ZoneId.from(startRecurrence) : null;
//        Temporal startAdjusted = newDateTimeType.from(initialStart, zone);
//        Temporal startOriginalRecurrenceAdjusted = newDateTimeType.from(startOriginalRecurrence, zone);
//
//        // Calculate shift from startAdjusted to make new DTSTART
//        final TemporalAmount startShift;
//        if (newDateTimeType == DateTimeType.DATE)
//        {
//            startShift = Period.between(LocalDate.from(startOriginalRecurrence), LocalDate.from(startRecurrence));
//        } else
//        {
//            startShift = Duration.between(startOriginalRecurrenceAdjusted, startRecurrence);
//        }
//        return startAdjusted.plus(startShift);
//    }
//    
//    private static Collection<Object> updateRecurrences(Collection<Object> recurrences, Collection<Object> componentRecurrences)
//    {
//        Collection<Object> instancesTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
//        instancesTemp.addAll(recurrences);
//        instancesTemp.removeIf(a -> componentRecurrences.stream().anyMatch(a2 -> a2 == a));
////        instances().clear(); // clear VEvent of outdated appointments
//        instancesTemp.addAll(makeRecurrences()); // make new recurrences and add to main collection (added to VEvent's collection in makeAppointments)
//        return instancesTemp;
//    }
//
//    @Deprecated // replace with simple booleans
//    private enum RRuleStatus
//    {
//        INDIVIDUAL ,
//        WITH_EXISTING_REPEAT ,
//        WITH_NEW_REPEAT, 
//        HAD_REPEAT_BECOMING_INDIVIDUAL;
//      
//        public static RRuleStatus getRRuleType(RecurrenceRuleNew rruleNew, RecurrenceRuleNew rruleOld)
//        {
//            if (rruleNew == null)
//            {
//                if (rruleOld == null)
//                { // doesn't have repeat or have old repeat either
//                    return RRuleStatus.INDIVIDUAL;
//                } else {
//                    return RRuleStatus.HAD_REPEAT_BECOMING_INDIVIDUAL;
//                }
//            } else
//            { // RRule != null
//                if (rruleOld == null)
//                {
//                    return RRuleStatus.WITH_NEW_REPEAT;                
//                } else
//                {
//                    return RRuleStatus.WITH_EXISTING_REPEAT;
//                }
//            }
//        }
//    }
//}
