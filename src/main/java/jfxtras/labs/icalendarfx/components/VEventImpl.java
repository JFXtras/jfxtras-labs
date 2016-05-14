//package jfxtras.labs.icalendarfx.components;
//
//import java.time.temporal.Temporal;
//import java.time.temporal.TemporalAmount;
//import java.util.ArrayList;
//import java.util.List;
//
//import jfxtras.labs.icalendarfx.recurrence.RecurrenceStartEnd;
//
///**
// * An example VEVENT class that uses a Recurrence type containing only a start and end date/time.
// * 
// * @author David Bal
// *
// */
//@Deprecated
//public class VEventImpl extends VEvent<RecurrenceStartEnd>
//{
//    @Override
//    public List<RecurrenceStartEnd> makeRecurrences(Temporal startRange, Temporal endRange)
//    {
//        
//        List<RecurrenceStartEnd> madeInstances = new ArrayList<>();
////        streamRecurrenceDates(getStartRange())
//        streamRecurrenceDates(startRange, endRange).forEach(temporalStart ->
//        {
//            TemporalAmount duration = getDuration().getValue();
//            Temporal temporalEnd = temporalStart.plus(duration);
//            RecurrenceStartEnd instance = new RecurrenceStartEnd();
////                .withStartTemporal(temporalStart)
////                .withEndTemporal(temporalEnd)
////                .withSummary(getSummary());
//            madeInstances.add(instance);
//            recurrences().add(instance);
//      });
//      return madeInstances;
//    }
//    
//    /** Parse content lines into calendar component object */
//    public static VEvent<?> parse(String contentLines)
//    {
//        VEvent<?> component = new VEventImpl();
//        component.parseContent(contentLines);
//        return component;
//    }
//}
