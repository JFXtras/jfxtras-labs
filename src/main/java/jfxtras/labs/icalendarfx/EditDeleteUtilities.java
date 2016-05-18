//package jfxtras.labs.icalendarfx;
//
//import java.time.temporal.Temporal;
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
//import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;
//
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
//          , Collection<Object> recurrences
//          , Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> dialogCallback)
//    {
//        validateStartInstanceAndDTStart(vComponentEdited, startOriginalRecurrence, startRecurrence);
//        final RRuleStatus rruleType = RRuleStatus.getRRuleType(vComponentEdited.getRecurrenceRule(), vComponentOriginal.getRecurrenceRule());
//        System.out.println("rruleType:" + rruleType);
//        boolean incrementSequence = true;
//        Collection<Object> newInstances = null;
//        switch (rruleType)
//        {
//        case HAD_REPEAT_BECOMING_INDIVIDUAL:
//            vComponentEdited.becomeNonRecurring(vComponentOriginal, startRecurrence, endRecurrence);
//            // fall through
//        case WITH_NEW_REPEAT: // no dialog
//        case INDIVIDUAL:
//            adjustDateTime(startOriginalRecurrence, startRecurrence, endRecurrence);
//            if (! this.equals(vComponentOriginal)) { newInstances = updateInstances(recurrences); }
//            break;
//        case WITH_EXISTING_REPEAT:
//            // Find which properties changed
//            List<String> changedPropertyNames = findChangedProperties(vComponentOriginal);
//            /* Note:
//             * time properties must be checked separately because changes are stored in startInstance and endInstance,
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
//                        adjustDateTime(startOriginalRecurrence, startRecurrence, endRecurrence);
//                        if ((getRRule() != null) && (getRRule().recurrences().size() > 0))
//                        {
//                            getRRule().recurrences().forEach(v ->
//                            {
//                                Temporal newRecurreneId = adjustRecurrenceStart(v.getDateTimeRecurrence(), startOriginalInstance, startInstance, endInstance);
//                                v.setDateTimeRecurrence(newRecurreneId);
//                            });
//                        }
//                        newInstances = updateInstances(recurrences);
//                    } else
//                    {
//                        throw new RuntimeException("Only 1 relatedVComponents currently supported");
//                    }
//                    break;
//                case CANCEL:
//                    vComponentOriginal.copyTo(this); // return to original
//                    return false;
//                case THIS_AND_FUTURE:
//                    newInstances = editThisAndFuture(vComponentOriginal, vComponents, startOriginalRecurrence, startRecurrence, endRecurrence, recurrences);
//                    break;
//                case ONE:
//                    newInstances = editOne(vComponentOriginal, vComponents, startOriginalRecurrence, startRecurrence, endRecurrence, recurrences);
//                    break;
//                default:
//                    break;
//                }
//            }
//        }
//        if (! isValid()) throw new RuntimeException(errorString());
//        if (incrementSequence) { incrementSequence(); }
//        if (newInstances != null)
//        {
//            recurrences.clear();
//            recurrences.addAll(newInstances);
//        }
//        return true;
//    }
//    
//    /** If startInstance isn't valid due to a RRULE change, change startInstance and
//     * endInstance to closest valid values
//     */
//    // TODO - VERITFY THIS WORKS
//    static void validateStartInstanceAndDTStart(VComponentLocatable<?> vComponentEdited, Temporal startOriginalRecurrence, Temporal startRecurrence)
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
////            Iterator<Temporal> startInstanceIterator = stream(temporal).iterator();
////            while (startInstanceIterator.hasNext())
////            {
////                Temporal myStartInstance = startInstanceIterator.next();
////                if (myStartInstance.equals(temporal))
////                {
////                    isStreamedValue = true;
////                }
////                if (DateTimeUtilities.isAfter(myStartInstance, temporal))
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
//    /* Adjust DTEND by instance start and end date-time */
//    @Override
//    protected void adjustDateTime(Temporal startOriginalInstance
//            , Temporal startInstance
//            , Temporal endInstance)
//    {
//        super.adjustDateTime(startOriginalInstance, startInstance, endInstance);
//        endType().setDuration(this, startInstance, endInstance);
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
