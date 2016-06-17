//package jfxtras.labs.icalendaragenda.scene.control.agenda;
//
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.Temporal;
//import java.util.ArrayList;
//import java.util.List;
//
//import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
//import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
//
///**
// * Handles making recurrences
// * 
// * @author David Bal
// *
// * @param <R> - class of recurrence (Appointment for Agenda)
// */
//@Deprecated // consider putting inside a store
//public class RecurrenceHelper<R>
//{   
////    private final Collection<R> recurrences; // collection of recurrences
//    private final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, R> recurrenceCallBack;
////    public final Map<VComponentNew<?>, List<R>> vComponentRecurrencetMap;
////    public final Map<Integer, List<R>> vComponentRecurrencetMap;    
////    private final Map<Integer, VComponentDisplayable<?>> recurrenceVComponentMap; /* map matches appointment to VComponent that made it */
//    
//    private LocalDateTime startRange; // must be updated when range changes
//    public void setStartRange(LocalDateTime startRange) { this.startRange = startRange; } 
//
//    private LocalDateTime endRange; // must be updated when range changes
//    public void setEndRange(LocalDateTime endRange) { this.endRange = endRange; } 
//
//
//    public RecurrenceHelper(
////            Collection<R> recurrences,
//            CallbackTwoParameters<VComponentRepeatable<?>, Temporal, R>  recurrenceCallBack
////            Map<VComponentNew<?>, List<R>> vComponentRecurrencetMap,
////            Map<Integer, List<R>> vComponentRecurrencetMap,
////            Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap)
//            )
//    {
////        this.recurrences = recurrences;
//        this.recurrenceCallBack = recurrenceCallBack;
////        this.vComponentRecurrencetMap = vComponentRecurrencetMap;
////        this.recurrenceVComponentMap = appointmentVComponentMap;
//    }
//
//// TODO - SHOULD MAKE RECURRENCES GO IN ANOTHER CLASS???
//    /**
//     * Makes appointments from VEVENT or VTODO for Agenda
//     * Appointments are made between displayed range
//     * 
//     * @param vComponentEditedCopy - calendar component
//     * @return created appointments
//     */
//    public List<R> makeRecurrences(VComponentRepeatable<?> vComponentEditedCopy)
//    {
//        if ((startRange == null) || (endRange == null))
//        {
//            throw new DateTimeException("Both startRange and endRange must not be null (" + startRange + ", " + endRange + ")");
//        }
//        List<R> newRecurrences = new ArrayList<>();
//        Boolean isWholeDay = vComponentEditedCopy.getDateTimeStart().getValue() instanceof LocalDate;
//        
//        // Make start and end ranges in Temporal type that matches DTSTART
//        final Temporal startRange2;
//        final Temporal endRange2;
//        if (isWholeDay)
//        {
//            startRange2 = LocalDate.from(startRange);
//            endRange2 = LocalDate.from(endRange);            
//        } else
//        {
//            startRange2 = vComponentEditedCopy.getDateTimeStart().getValue().with(startRange);
//            endRange2 = vComponentEditedCopy.getDateTimeStart().getValue().with(endRange);            
//        }
//        vComponentEditedCopy.streamRecurrences(startRange2, endRange2)
//            .forEach(startTemporal -> 
//            {
//                R recurrence = recurrenceCallBack.call(vComponentEditedCopy, startTemporal);
////                recurrenceVComponentMap.put(System.identityHashCode(recurrence), vComponentEditedCopy);
//                newRecurrences.add(recurrence);
//            });
//        return newRecurrences;
//    }
//    
//    /**
//     * Makes appointments from VEVENT or VTODO for Agenda
//     * Appointments are made between displayed range
//     * 
//     * @param vComponentEditedCopy - calendar component
//     * @return created appointments
//     */
//    public List<R> makeRecurrences(VComponentDisplayable<?> vComponentEditedCopy)
//    {
//        if ((startRange == null) || (endRange == null))
//        {
//            throw new DateTimeException("Both startRange and endRange must not be null (" + startRange + ", " + endRange + ")");
//        }
//        List<R> newRecurrences = new ArrayList<>();
//        Boolean isWholeDay = vComponentEditedCopy.getDateTimeStart().getValue() instanceof LocalDate;
//        
//        // Make start and end ranges in Temporal type that matches DTSTART
//        final Temporal startRange2;
//        final Temporal endRange2;
//        if (isWholeDay)
//        {
//            startRange2 = LocalDate.from(startRange);
//            endRange2 = LocalDate.from(endRange);            
//        } else
//        {
//            startRange2 = vComponentEditedCopy.getDateTimeStart().getValue().with(startRange);
//            endRange2 = vComponentEditedCopy.getDateTimeStart().getValue().with(endRange);            
//        }
//        vComponentEditedCopy.streamRecurrences(startRange2, endRange2)
//            .forEach(startTemporal -> 
//            {
//                R recurrence = recurrenceCallBack.call(vComponentEditedCopy, startTemporal);
////                recurrenceVComponentMap.put(System.identityHashCode(recurrence), vComponentEditedCopy);
//                newRecurrences.add(recurrence);
//            });
//        return newRecurrences;
//    }
// 
//     /** Based on {@link Callback<P,R>} */
//     @FunctionalInterface
//     public interface CallbackTwoParameters<P1, P2, R> {
//         /**
//          * The <code>call</code> method is called when required, and is given 
//          * two arguments of type P1 and P2, with a requirement that an object of type R
//          * is returned.
//          *
//          * @param param1 The first argument upon which the returned value should be
//          *      determined.
//          * @param param1 The second argument upon which the returned value should be
//          *      determined.
//          * @return An object of type R that may be determined based on the provided
//          *      parameter value.
//          */
//         public R call(P1 param1, P2 param2);
//     }
//}
