//package jfxtras.labs.icalendarfx;
//
//import static org.junit.Assert.assertEquals;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.chrono.ThaiBuddhistDate;
//import java.time.temporal.Temporal;
//import java.time.temporal.TemporalAdjuster;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.junit.Ignore;
//import org.junit.Test;
//
//import jfxtras.labs.icalendarfx.mocks.VEventMock;
//import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
//import jfxtras.scene.control.agenda.TemporalUtilities;
//
///*
// * Tests subset of recurrence set
// */
//@Deprecated
//public class ICalendarDateTest extends ICalendarTestAbstract
//{
//   
//    /*
//     *  Tests for multi-part recurrence sets
//     *  Children have RECURRENCE-ID
//     */
//    @Test
//    @Ignore
//    public void canMakeRecurrenceSet1()
//    {
//        List<VEventMock> vComponents = getDailyWithRecurrence();
//        VEventMock parent = vComponents.get(0);
//        VEventMock recurrence = vComponents.get(1);
//        List<Temporal> madeDates = new ArrayList<>();
//        List<Temporal> madeDatesParent = parent
//                .stream(parent.getDateTimeStart())
//                .collect(Collectors.toList());
//        List<Temporal> madeDatesRecurrence = recurrence
//                .stream(recurrence.getDateTimeStart())
//                .collect(Collectors.toList());
//        madeDates.addAll(madeDatesParent);
//        madeDates.addAll(madeDatesRecurrence);
//        Collections.sort(madeDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
//        List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
//                LocalDateTime.of(2015, 11, 9, 10, 0)
//              , LocalDateTime.of(2015, 11, 11, 10, 0)
//              , LocalDateTime.of(2015, 11, 13, 10, 0)
//              , LocalDateTime.of(2015, 11, 15, 10, 0)
//              , LocalDateTime.of(2015, 11, 17, 10, 0)
//              , LocalDateTime.of(2015, 11, 19, 10, 0)
//              , LocalDateTime.of(2015, 11, 22, 16, 0)
//              , LocalDateTime.of(2015, 11, 23, 10, 0)
//              , LocalDateTime.of(2015, 11, 25, 10, 0)
//              , LocalDateTime.of(2015, 11, 27, 10, 0)
//              , LocalDateTime.of(2015, 11, 29, 10, 0)
//                ));
//        assertEquals(expectedDates, madeDates);
//    }
//        
//    @Test
//    @Ignore
//    public void canStreamDatesGoogleParts()
//    {
//        List<VEventMock> vComponents = getGoogleRepeatableParts();
//        VEventMock v0 = vComponents.get(0);
//        VEventMock v1 = vComponents.get(1);
//        VEventMock v2 = vComponents.get(2);
//        List<Temporal> startDates = new ArrayList<>();
//        List<Temporal> startDatesV0 = v0
//                .stream(v0.getDateTimeStart())
//                .collect(Collectors.toList());
//        List<Temporal> startDatesV1 = v1
//                .stream(v1.getDateTimeStart())
//                .limit(10)
//                .collect(Collectors.toList());
//        List<Temporal> startDatesV2 = v2
//                .stream(v2.getDateTimeStart())
//                .collect(Collectors.toList());
//        startDates.addAll(startDatesV0);
//        startDates.addAll(startDatesV1);
//        startDates.addAll(startDatesV2);
//        Collections.sort(startDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
//        List<ZonedDateTime> expectedStartDates = new ArrayList<>(Arrays.asList(
//                ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 15, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 7, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 17, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 19, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 20, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 22, 11, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 11, 0), ZoneId.of("America/Los_Angeles"))
//                ));
//        assertEquals(expectedStartDates, startDates);
//        
//        List<Temporal> endDates = new ArrayList<>();
//        Duration d0 = Duration.between(v0.getDateTimeStart(), v0.getDateTimeEnd());
//        List<Temporal> endDatesV0 = v0
//                .stream(v0.getDateTimeStart())
//                .map(t -> t.plus(d0))
//                .collect(Collectors.toList());
//        Duration d1 = Duration.between(v1.getDateTimeStart(), v1.getDateTimeEnd());
//        List<Temporal> endDatesV1 = v1
//                .stream(v1.getDateTimeStart())
//                .map(t -> t.plus(d1))
//                .limit(10)
//                .collect(Collectors.toList());
//        Duration d2 = Duration.between(v2.getDateTimeStart(), v2.getDateTimeEnd());
//        List<Temporal> endDatesV2 = v2
//                .stream(v2.getDateTimeStart())
//                .map(t -> t.plus(d2))
//                .collect(Collectors.toList());
//        endDates.addAll(endDatesV0);
//        endDates.addAll(endDatesV1);
//        endDates.addAll(endDatesV2);
//        Collections.sort(endDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
//        List<ZonedDateTime> expectedEndDates = new ArrayList<>(Arrays.asList(
//                ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 13, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 15, 13, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 9, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 17, 13, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 14, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 19, 14, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 20, 14, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 14, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 22, 14, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 14, 0), ZoneId.of("America/Los_Angeles"))
//                ));
//        assertEquals(expectedEndDates, endDates);
//    }
//
//    /*
//     * initialTemporal is Temporal from startTemporal or endTemporal
//     * adjuster is the argument passed to setStartLocalDateTime and setEndLocalDateTime.
//     * adjuster is modified to be LocalDate if Appointment isWholeDay and LocalDateTime otherwise.
//     * actual is the value to be put into startTemporal or endTemporal
//     */
//
//    @Test
//    public void canConvertLocalDateToLocalDate()
//    {
//        // LocalDate into LocalDate
//        {
//            Temporal initialTemporal = LocalDate.of(2015, 11, 18);
//            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            assertEquals(LocalDate.of(2015, 11, 19), actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToThaiBuddhistDate()
//    {
//        // LocalDate into ThaiBuddhistDate
//        {
//            Temporal initialTemporal = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
//            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 19));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToZonedDateTime()
//    {
//        // LocalDate into ZonedDateTime
//        {
//            Temporal initialTemporal = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
//            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 0), ZoneId.of("Japan"));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToLocalDateTime()
//    {
//        // LocalDate into LocalDateTime
//        {
//            Temporal initialTemporal = LocalDateTime.of(2015, 11, 19, 5, 30);
//            TemporalAdjuster adjuster = LocalDate.of(2015, 11, 18);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = LocalDateTime.of(2015, 11, 18, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToLocalDateTime()
//    {
//        // LocalDateTime into LocalDateTime
//        {
//            Temporal initialTemporal = LocalDateTime.of(2015, 11, 19, 5, 30);
//            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 22, 11, 30);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = LocalDateTime.of(2015, 11, 22, 11, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToLocalDate()
//    {
//        // LocalDateTime into LocalDate
//        {
//            Temporal initialTemporal = LocalDate.of(2015, 11, 18);
//            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToZonedDateTime()
//    {
//        // LocalDateTime into ZonedDateTime
//        {
//            Temporal initialTemporal = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
//            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 30), ZoneId.of("Japan"));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToThaiBuddhistDate()
//    {
//        // LocalDateTime into ThaiBuddhistDate
//        {
//            Temporal initialTemporal = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
//            TemporalAdjuster adjuster = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalUtilities.combine(initialTemporal, adjuster);
//            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
//
//}
