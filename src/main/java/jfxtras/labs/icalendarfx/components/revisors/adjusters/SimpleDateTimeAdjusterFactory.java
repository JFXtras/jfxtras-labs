//package jfxtras.labs.icalendarfx.components.revisors.adjusters;
//
//import java.time.LocalDate;
//import java.time.temporal.Temporal;
//
//import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
//import jfxtras.labs.icalendarfx.components.StandardTime;
//import jfxtras.labs.icalendarfx.components.VAlarm;
//import jfxtras.labs.icalendarfx.components.VEvent;
//import jfxtras.labs.icalendarfx.components.VFreeBusy;
//import jfxtras.labs.icalendarfx.components.VJournal;
//import jfxtras.labs.icalendarfx.components.VTimeZone;
//import jfxtras.labs.icalendarfx.components.VTodo;
//import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
//import jfxtras.labs.icalendarfx.components.revisors.ReviserDisplayable;
//
//public class SimpleDateTimeAdjusterFactory
//{
//    public static <U> DateTimeAdjuster newDateTimeAdjuster(
//            U vComponent,
//            Temporal startRecurrence,
//            Temporal startOriginalRecurrence)
//    {
//        if (vComponent instanceof VEvent)
//        {
//            return new DateTimeAdjusterVEvent((VEvent) vComponent, startRecurrence, startOriginalRecurrence);
//        } else if (vComponent instanceof VTodo)
//        {
////            return new ReviserVTodo((VTodo) vComponent);            
//        } else if (vComponent instanceof VJournal)
//        {
////            return new ReviserVJournal((VJournal) vComponent);            
//        } else if (vComponent instanceof VFreeBusy)
//        {
////            return new ReviserVFreeBusy((VFreeBusy) vComponent);            
//        } else if (vComponent instanceof VTimeZone)
//        {
////            return new ReviserVTimeZone((VTimeZone) vComponent);            
//        } else if (vComponent instanceof VAlarm)
//        {
////            return new ReviserVAlarm((VAlarm) vComponent);            
//        } else if (vComponent instanceof StandardTime)
//        {
////            return new ReviserStandardTime((StandardTime) vComponent);            
//        } else if (vComponent instanceof DaylightSavingTime)
//        {
////            return new ReviserDaylightSavingTime((DaylightSavingTime) vComponent);            
//        } else
//        {
//            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
//        }
//        return null;
//    }
//    
//    public static <U> DateTimeAdjuster newDateTimeAdjuster(
//            ReviserDisplayable.RRuleStatus ruleStatus,
//            ChangeDialogOption changeDialogOption,
//            EditState editState)
//    {
//
//        return null;
//    }
//    
//    enum EditState
//    {
//        DATETIME_TO_DATETIME,
//        DATETIME_TO_DATE,
//        DATE_TO_DATETIME,
//        DATE_TO_DATE;
//        
//        public static EditState getEditState(Temporal startOriginal, Temporal startEdited)
//        {
//            boolean isStartWholeDay = startEdited instanceof LocalDate;
//            boolean isOriginalWholeDay = startOriginal instanceof LocalDate;
//            if (isStartWholeDay && isOriginalWholeDay)
//            {
//                return DATE_TO_DATE;
//            } else if (isStartWholeDay)
//            {
//                return DATETIME_TO_DATE;
//            } else if (isOriginalWholeDay)
//            {
//                return DATE_TO_DATETIME;                
//            } else
//            {
//                return DATETIME_TO_DATETIME;
//            }
//        }
//    }
//}
