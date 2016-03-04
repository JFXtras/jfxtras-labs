package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.icalendar.mocks.InstanceMock;
import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.RRule;
import jfxtras.labs.icalendar.rrule.freq.Daily;

public class ICalendarEditTest extends ICalendarTestAbstract
{
    /**
     * Tests editing start and end time of ALL events
     */
    @Test
    public void canEditAll1()
    {
        VEventMock vEvent = getDaily2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vComponentOriginal = new VEventMock(vEvent);
               
        // select InstanceMock and apply changes
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        InstanceMock selectedInstance = instanceIterator.next();
        Temporal startOriginalInstance = selectedInstance.getStartTemporal();
        Temporal startInstance = selectedInstance.getStartTemporal().with(LocalTime.of(9, 45));
        Temporal endInstance = selectedInstance.getEndTemporal().with(LocalTime.of(11, 00));
        
        vEvent.handleEdit(
                  vComponentOriginal
                , vComponents
                , startOriginalInstance
                , startInstance
                , endInstance
                , instances
                , (m) -> ChangeDialogOption.ALL);
        
        // Check start date/times
        List<Temporal> madeDates = instances.stream().map(a -> a.getStartTemporal()).collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 9, 45)
              , LocalDateTime.of(2015, 11, 21, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);

        // Check edited VEvent
        VEventMock expectedVEvent = getDaily2()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45))
                .withDuration(Duration.ofMinutes(75))
                .withSequence(1);

        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent)); // check to see if repeat rule changed correctly
        assertEquals(3, instances.size()); // check if there are only two instances
    }

    /**
     * Tests ONE event of a daily repeat event changing date and time
     */
    @Test
    public void canEditOne1()
    {
        // Individual InstanceMock
        VEventMock vEvent = getDaily2();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);
        
        // select InstanceMock and apply changes
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        InstanceMock selectedInstance = instanceIterator.next();
        Temporal startOriginalInstance = selectedInstance.getStartTemporal();
        LocalDate date = LocalDate.from(selectedInstance.getStartTemporal()).plus(1, ChronoUnit.DAYS);
        selectedInstance.setStartTemporal(date.atTime(9, 45)); // change start time
        selectedInstance.setEndTemporal(date.atTime(11, 0)); // change end time
        Temporal startInstance = selectedInstance.getStartTemporal();
        Temporal endInstance = selectedInstance.getEndTemporal();
        
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);

        assertEquals(2, vComponents.size());

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                LocalDateTime.of(2015, 11, 16, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);

        Collections.sort(vComponents, VComponent.VCOMPONENT_COMPARATOR);
        VEventMock vEvent0 = (VEventMock) vComponents.get(0);
        VEventMock vEvent1 = (VEventMock) vComponents.get(1);
        VEventMock expectedVEvent0 = getDaily2()
                .withRRule(new RRule()
                        .withCount(6)
                        .withFrequency(new Daily().withInterval(3))
                        .withRecurrences(vEvent1));
        assertTrue(VEventMock.isEqualTo(expectedVEvent0, vEvent0));
        
        VEventMock expectedVEvent1 = getDaily2()
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 15, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 16, 9, 45))
                .withDateTimeStamp(vEvent1.getDateTimeStamp())
                .withDuration(Duration.ofMinutes(75))
                .withRRule(null)
                .withSequence(1);
        assertTrue(VEventMock.isEqualTo(expectedVEvent1, vEvent1));
    }

    @Test
    public void canEditThisAndFuture1()
    {
        VEventMock vEvent = getDaily1();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(7, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);
        
        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        instanceIterator.next(); // skip second
        InstanceMock selectedInstance = instanceIterator.next();
        Temporal startOriginalInstance = selectedInstance.getStartTemporal();
        
        // apply changes
        LocalDate newDate = LocalDate.from(selectedInstance.getStartTemporal());
        selectedInstance.setStartTemporal(newDate.atTime(9, 45)); // change start time
        selectedInstance.setEndTemporal(newDate.atTime(10, 30)); // change end time
        Temporal startInstance = selectedInstance.getStartTemporal();
        Temporal endInstance = selectedInstance.getEndTemporal();
        long startShift = ChronoUnit.NANOS.between(startOriginalInstance, startInstance);
        Temporal dtStart = vEvent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedInstance.getStartTemporal(), selectedInstance.getEndTemporal());
        Temporal dtEnd = dtStart.plus(duration, ChronoUnit.NANOS);
        vEvent.setDateTimeStart(dtStart);
        vEvent.setDateTimeEnd(dtEnd);

        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.THIS_AND_FUTURE);
        
        assertEquals(2, vComponents.size());
        
        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 9, 45)
              , LocalDateTime.of(2015, 11, 19, 9, 45)
              , LocalDateTime.of(2015, 11, 20, 9, 45)
              , LocalDateTime.of(2015, 11, 21, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);
        
        Collections.sort(vComponents, VComponent.VCOMPONENT_COMPARATOR);
        VEventMock vEvent0 = (VEventMock) vComponents.get(0);
        VEventMock vEvent1 = (VEventMock) vComponents.get(1);
        VEventMock expectedVEvent0 = getDaily1()
                .withRRule(new RRule()
                        .withFrequency(new Daily())
                        .withUntil(LocalDateTime.of(2015, 11, 16, 23, 59, 59)));
        assertTrue(VEventMock.isEqualTo(expectedVEvent0, vEvent0));
        
        VEventMock expectedVEvent1 = getDaily1()
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 17, 10, 30))
                .withDateTimeStamp(vEvent1.getDateTimeStamp())
                .withDateTimeStart(LocalDateTime.of(2015, 11, 17, 9, 45))
                .withRelatedTo("20150110T080000-0@jfxtras.org")
                .withSequence(1)
                .withUniqueIdentifier(vEvent1.getUniqueIdentifier());
        assertTrue(VEventMock.isEqualTo(expectedVEvent1, vEvent1));
    }

    /**
     * Tests changing a repeating event to an individual one
     */
    @Test
    public void canEditAll2()
    {
        // Individual InstanceMock
        VEventMock vEvent = getDaily2();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);

        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        InstanceMock selectedInstance = instanceIterator.next();
        
        // apply changes
        vEvent.setRRule(null);
        Temporal startOriginalInstance = selectedInstance.getStartTemporal();
        Temporal startInstance = selectedInstance.getStartTemporal();
        Temporal endInstance = selectedInstance.getEndTemporal();

        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ALL);

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                LocalDateTime.of(2015, 11, 18, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        VEventMock expectedVEvent = getDaily2()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 18, 10, 0))
                .withRRule(null)
                .withSequence(1);
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    /**
     * Tests changing a ZonedDateTime repeating event
     */
    @Test
    public void canEditOne2()
    {
        // Individual InstanceMock
        VEventMock vEvent = getWeeklyZoned();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);

        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        InstanceMock selectedInstance = instanceIterator.next();
        
        // apply changes
        Temporal startOriginalInstance = selectedInstance.getStartTemporal();
        Temporal startInstance = selectedInstance.getStartTemporal().with(LocalTime.of(9, 45)).minus(1, ChronoUnit.DAYS);
        Temporal endInstance = selectedInstance.getEndTemporal().with(LocalTime.of(10, 30)).minus(1, ChronoUnit.DAYS);

        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());

        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 9, 45), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 10, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);

        VEventMock expectedVEvent = getWeeklyZoned()
                .withRRule(null)
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 9, 45), ZoneId.of("America/Los_Angeles")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 10, 30), ZoneId.of("America/Los_Angeles")))
                .withDateTimeRecurrence(ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withSequence(1);
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));

        VEventMock expectedVEvent2 = getWeeklyZoned();
        expectedVEvent2.getRRule().recurrences().add(vEvent);
        assertTrue(VEventMock.isEqualTo(expectedVEvent2, vEventOriginal));
    }
    
    /**
     * Changing a event with an exception to wholeday
     */
    @Test
    public void canChangeToWholeDay()
    {
        // Individual InstanceMock
        VEventMock vEvent = getGoogleWithExDates();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2016, 2, 7, 0, 0);
        LocalDateTime end = LocalDateTime.of(2016, 2, 14, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(4, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);

        // apply changes
        Temporal startOriginalInstance = ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 12, 30), ZoneId.of("America/Los_Angeles"));
        Temporal startInstance = LocalDate.of(2016, 2, 8);
        Temporal endInstance = LocalDate.of(2016, 2, 9);

        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted(VComponent.TEMPORAL_COMPARATOR)
                .collect(Collectors.toList());

        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles"))
              , LocalDate.of(2016, 2, 8)
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 11, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 12, 30), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);

        VEventMock expectedVEvent = getGoogleWithExDates()
                .withRRule(null)
                .withDateTimeStart(LocalDate.of(2016, 2, 8))
                .withDateTimeEnd(LocalDate.of(2016, 2, 9))
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withDateTimeRecurrence(ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withSequence(1);
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));

        VEventMock expectedVEvent2 = getGoogleWithExDates()
                .withDateTimeCreated(vEventOriginal.getDateTimeCreated()); // set by system time, so need to copy
        expectedVEvent2.getRRule().recurrences().add(vEvent);
        assertTrue(VEventMock.isEqualTo(expectedVEvent2, vEventOriginal));
    }
    
    /**
     * Changing two instances to wholeday
     * Tests keeping RECURRENCE-ID as parent DateTimeType
     */
    @Test
    public void canChangeTwoWholeDay()
    {
        VEventMock vEvent = getGoogleRepeatable();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        Temporal start = ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 0, 0), ZoneId.of("America/Los_Angeles"));
        Temporal end = ZonedDateTime.of(LocalDateTime.of(2016, 2, 28, 0, 0), ZoneId.of("America/Los_Angeles"));
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);
        
        // apply changes
        Temporal startOriginalInstance = ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 8, 0), ZoneId.of("America/Los_Angeles"));
        Temporal startInstance = LocalDate.of(2016, 2, 22);
        Temporal endInstance = LocalDate.of(2016, 2, 23);

        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted(VComponent.TEMPORAL_COMPARATOR)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 8, 0), ZoneId.of("America/Los_Angeles"))
              , LocalDate.of(2016, 2, 22)
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 26, 8, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);

        VEventMock expectedVEvent = getGoogleRepeatable()
                .withRRule(null)
                .withDateTimeStart(LocalDate.of(2016, 2, 22))
                .withDateTimeEnd(LocalDate.of(2016, 2, 23))
                .withDateTimeRecurrence(ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 8, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withSequence(1);
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));

        VEventMock expectedVEvent2 = getGoogleRepeatable();
        expectedVEvent2.getRRule().recurrences().add(vEvent);
        assertTrue(VEventMock.isEqualTo(expectedVEvent2, vEventOriginal));
        
        // apply changes
        VEventMock vEventOriginal2 = new VEventMock(vEventOriginal);
        Temporal startOriginalInstance2 = ZonedDateTime.of(LocalDateTime.of(2016, 2, 26, 8, 0), ZoneId.of("America/Los_Angeles"));
        Temporal startInstance2 = LocalDate.of(2016, 2, 26);
        Temporal endInstance2 = LocalDate.of(2016, 2, 27);       
        
        // Edit
        vEventOriginal.handleEdit(
                vEventOriginal2
              , vComponents
              , startOriginalInstance2
              , startInstance2
              , endInstance2
              , instances
              , (m) -> ChangeDialogOption.ONE);
        
        List<Temporal> madeDates2 = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted(VComponent.TEMPORAL_COMPARATOR)
                .collect(Collectors.toList());
        List<Temporal> expectedDates2 = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 8, 0), ZoneId.of("America/Los_Angeles"))
              , LocalDate.of(2016, 2, 22)
              , LocalDate.of(2016, 2, 26)
                ));
        
        vComponents.stream().forEach(System.out::println);
        assertEquals(expectedDates2, madeDates2);
    }
    
    
    /**
     * Changing two instances to wholeday
     * Tests keeping RECURRENCE-ID as parent DateTimeType
     */
    @Test
    public void canChangeOneWholeDayToDateTime()
    {
        VEventMock vEvent = getWholeDayDaily2();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        Temporal start = LocalDate.of(2015, 11, 15);
        Temporal end = LocalDate.of(2015, 11, 22);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vEvent.makeInstances(start, end);
        instances.addAll(newInstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances
        VEventMock vEventOriginal = new VEventMock(vEvent);
        
        // apply changes
        Temporal startOriginalInstance = LocalDate.of(2015, 11, 18);
        Temporal startInstance = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 0), ZoneId.of("America/New_York"));
        Temporal endInstance = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/New_York"));
        
        instances.stream().forEach(a -> System.out.println("instance:" + a));
        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);
        
        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted(VComponent.TEMPORAL_COMPARATOR)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                  LocalDate.of(2015, 11, 15)
                , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 0), ZoneId.of("America/New_York"))
                , LocalDate.of(2015, 11, 21)
                ));
        assertEquals(expectedDates, madeDates);
    }


    
    
// /**
//  * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
//  * FREQ=DAILY;INVERVAL=3;COUNT=6
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editFutureTimeAndDateDaily2()
// {
//     VEventMock vevent = getDaily2();
//     List<VComponent<InstanceMock>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<InstanceMock> instances = new ArrayList<InstanceMock>();
//     Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
//     instances.addAll(newInstances);
//     assertEquals(3, instances.size()); // check if there are only 3 instances
//     VEventMock veventOld = new VEventMock(vevent);
//     
//     // select InstanceMock and apply changes
//     Iterator<InstanceMock> instanceIterator = instances.iterator();
//     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
//     LocalDateTime dateTimeOriginal = selectedInstance.getStartTemporal();
//     LocalDate date = selectedInstance.getStartTemporal().toLocalDate();
//     selectedInstance.setStartTemporal(date.atTime(9, 45)); // change start time
//     selectedInstance.setEndTemporal(date.atTime(11, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
//     long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.NANOS.between(selectedInstance.getStartTemporal(), selectedInstance.getEndTemporal());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDurationInNanos(duration);
//     vevent.setSummary("Edited Summary");
//     vevent.setDescription("Edited Description");
//     vevent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     
////     // select InstanceMock (get recurrence date)
////     Iterator<InstanceMock> instanceIterator = instances.iterator();
////     instanceIterator.next(); // skip first
////     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
////     LocalDateTime dateTimeOriginal = selectedInstance.getStartTemporal();
////     
////     // apply changes
////     LocalDate newDate = selectedInstance.getStartTemporal().toLocalDate().minusDays(1); // shift InstanceMock 1 day backward
////     selectedInstance.setStartTemporal(newDate.atTime(9, 45)); // change start time
////     selectedInstance.setEndTemporal(newDate.atTime(11, 0)); // change end time
//////     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , instances            // collection of all instances
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.THIS_AND_FUTURE                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     instances.stream().forEach(a -> System.out.println(a.getStartTemporal() + " " + a.getEndTemporal()));
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     List<Temporal> madeDates = instances.stream().map(a -> a.getStartTemporal()).collect(Collectors.toList());
//     List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 9, 45)
//           , LocalDateTime.of(2015, 11, 20, 9, 45)
//             ));
//     assertEquals(expectedDates, madeDates);
//
//     // Check instances
//     Iterator<InstanceMock> instanceIteratorNew = instances.iterator();
//     InstanceMock editedInstanceMock1 = (InstanceMock) instanceIteratorNew.next();
//
//     InstanceMock expectedInstanceMock1 = InstanceMockFactory.newInstanceMock(getClazz())
//         .withStartTemporal(LocalDateTime.of(2015, 11, 15, 10, 0))
//         .withEndTemporal(LocalDateTime.of(2015, 11, 15, 11, 30))
//         .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(3))
//         .withDescription("Daily2 Description")
//         .withSummary("Daily2 Summary");
//     assertEquals(expectedInstanceMock1, editedInstanceMock1); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock2 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock2 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 17, 9, 45))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 17, 11, 0))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedInstanceMock2, editedInstanceMock2); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock3 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock3 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 20, 9, 45))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 20, 11, 0))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedInstanceMock3, editedInstanceMock3); // Check to see if repeat-generated InstanceMock changed correctly
// }
//
// 
// /**
//  * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
//  * FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editFutureTimeAndDateDaily6()
// {
//     // Individual InstanceMock
//     VEventMock vevent = getDaily6();
//     List<VComponent<InstanceMock>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     Set<InstanceMock> instances = new TreeSet<InstanceMock>((a,b) -> a.getStartTemporal().compareTo(b.getStartTemporal()));
//     Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
//     instances.addAll(newInstances);
//     assertEquals(4, instances.size()); // check if there are only 3 instances
//     VEventMock veventOld = new VEventMock(vevent);
//     
//     // select InstanceMock and apply changes
//     Iterator<InstanceMock> instanceIterator = instances.iterator();
//     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
//     LocalDateTime dateTimeOriginal = selectedInstance.getStartTemporal();
//     LocalDate date = selectedInstance.getStartTemporal().toLocalDate().minusDays(2);
//     selectedInstance.setStartTemporal(date.atTime(6, 0)); // change start time
//     selectedInstance.setEndTemporal(date.atTime(7, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
//     long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.NANOS.between(selectedInstance.getStartTemporal(), selectedInstance.getEndTemporal());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDurationInNanos(duration);
//     vevent.setSummary("Edited Summary");
//     vevent.setDescription("Edited Description");
//     vevent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     
////     // select InstanceMock (get recurrence date)
////     Iterator<InstanceMock> instanceIterator = instances.iterator();
////     instanceIterator.next(); // skip first
////     instanceIterator.next(); // skip second
////     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
////     LocalDateTime dateTimeOriginal = selectedInstance.getStartTemporal();
////     
////     // apply changes
////     LocalDate newDate = selectedInstance.getStartTemporal().toLocalDate().minusDays(2); // shift InstanceMock 2 day backward
////     selectedInstance.setStartTemporal(newDate.atTime(6, 0)); // change start time
////     selectedInstance.setEndTemporal(newDate.atTime(7, 0)); // change end time
//////     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , instances            // collection of all instances
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.THIS_AND_FUTURE                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     instances.stream().forEach(a -> System.out.println(a.getStartTemporal() + " " + a.getEndTemporal()));
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     List<Temporal> madeDates = instances
//             .stream()
//             .map(a -> a.getStartTemporal())
//             .collect(Collectors.toList());
//     List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 6, 0)
//           , LocalDateTime.of(2015, 11, 17, 10, 0)
//           , LocalDateTime.of(2015, 11, 19, 6, 0)
//           , LocalDateTime.of(2015, 11, 21, 6, 0)
//             ));
//     assertEquals(expectedDates, madeDates);
//
//     // Check instances
//     Iterator<InstanceMock> instanceIteratorNew = instances.iterator();
//     InstanceMock editedInstanceMock1 = (InstanceMock) instanceIteratorNew.next();
//
//     InstanceMock expectedInstanceMock1 = InstanceMockFactory.newInstanceMock(getClazz())
//         .withStartTemporal(LocalDateTime.of(2015, 11, 15, 10, 0))
//         .withEndTemporal(LocalDateTime.of(2015, 11, 15, 11, 30))
//         .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(3))
//         .withDescription("Daily6 Description")
//         .withSummary("Daily6 Summary");
//     assertEquals(expectedInstanceMock1, editedInstanceMock1); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock2 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock2 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 17, 6, 0))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 17, 7, 0))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedInstanceMock2, editedInstanceMock2); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock3 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock3 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 17, 10, 0))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 17, 11, 30))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(3))
//             .withDescription("Daily6 Description")
//             .withSummary("Daily6 Summary");
//         assertEquals(expectedInstanceMock3, editedInstanceMock3); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock4 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock4 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 19, 6, 0))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 19, 7, 0))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedInstanceMock4, editedInstanceMock4); // Check to see if repeat-generated InstanceMock changed correctly
//
//     InstanceMock editedInstanceMock5 = (InstanceMock) instanceIteratorNew.next();
//     InstanceMock expectedInstanceMock5 = InstanceMockFactory.newInstanceMock(getClazz())
//             .withStartTemporal(LocalDateTime.of(2015, 11, 21, 6, 0))
//             .withEndTemporal(LocalDateTime.of(2015, 11, 21, 7, 0))
//             .withInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedInstanceMock5, editedInstanceMock5); // Check to see if repeat-generated InstanceMock changed correctly
//
//     // All dates check
//     LocalDateTime start2 = LocalDateTime.of(2015, 11, 1, 0, 0);
//     LocalDateTime end2 = LocalDateTime.of(2015, 12, 31, 0, 0);
//     instances.clear();
//     instances.addAll(vevent.makeInstances(start2, end2));
//     
//     VEventMock veventNew = (VEventMock) vevents.get(1);
//     instances.addAll(veventNew.makeInstances(start2, end2));
//     List<Temporal> madeDates2 = instances.stream().map(a -> a.getStartTemporal()).collect(Collectors.toList());
//     
//     List<Temporal> expectedDates2 = new ArrayList<Temporal>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 9, 10, 0)
//           , LocalDateTime.of(2015, 11, 11, 10, 0)
//           , LocalDateTime.of(2015, 11, 13, 10, 0)
//           , LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 6, 0)
//           , LocalDateTime.of(2015, 11, 17, 10, 0)
//           , LocalDateTime.of(2015, 11, 19, 6, 0)
//           , LocalDateTime.of(2015, 11, 21, 6, 0)
//           , LocalDateTime.of(2015, 11, 23, 6, 0)
//           , LocalDateTime.of(2015, 11, 25, 6, 0)
//           , LocalDateTime.of(2015, 11, 27, 6, 0)
//           , LocalDateTime.of(2015, 11, 29, 6, 0)
//           ));
//     assertEquals(expectedDates2, madeDates2);
//
// }
// 
// /**
//  * Tests changing a daily repeat rule to an individual InstanceMock
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editToIndividualDaily2()
// {
//     // Individual InstanceMock
//     VEventMock vevent = getDaily2();
//     List<VComponent<InstanceMock>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<InstanceMock> instances = new ArrayList<InstanceMock>();
//     Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
//     instances.addAll(newInstances);
//     assertEquals(3, instances.size()); // check if there are only 3 instances
//     VEventMock veventOld = new VEventMock(vevent);
//     assertEquals(veventOld, vevent); // check to see if repeat rule changed correctly
//
//     // select InstanceMock (get recurrence date)
//     Iterator<InstanceMock> instanceIterator = instances.iterator(); // skip first
//     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
//     
//     // apply changes
//     vevent.setRRule(null);
//
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             selectedInstance.getStartTemporal()
//           , selectedInstance.getStartTemporal()
//           , veventOld               // original VEvent
//           , instances            // collection of all instances
//           , vevents                 // collection of all VEvents
//           , null                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     VEventMock expectedVEvent = new VEventMock(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS);
//     expectedVEvent.setInstanceMockClass(getClazz());
//     expectedVEvent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(3));
//     expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
//     expectedVEvent.setDescription("Daily2 Description");
//     expectedVEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
//     expectedVEvent.setSummary("Daily2 Summary");
//     expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
//     expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly       
// }
// 
// /**
//  * Tests editing start and end time of ALL events
//  */
// @Test
// @Ignore
// public void editCancelDaily2()
// {
//     VEventMock vevent = getDaily2();
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<VComponent<InstanceMock>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     List<InstanceMock> instances = new ArrayList<>();
//     Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
//     instances.addAll(newInstances);
//     VEventMock veventOld = new VEventMock(vevent);
//     
//     // select InstanceMock and apply changes
//     Iterator<InstanceMock> instanceIterator = instances.iterator(); // skip first
//     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
//     LocalDateTime dateTimeOld = selectedInstance.getStartTemporal();
//     LocalDate dateOld = dateTimeOld.toLocalDate();
//     selectedInstance.setStartTemporal(dateOld.atTime(9, 45)); // change start time
//     selectedInstance.setEndTemporal(dateOld.atTime(11, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
//
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOld
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , instances            // collection of all instances
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.CANCEL   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITHOUT_CHANGE, windowCloseType); // check to see if close type is correct
//
//     // Check edited VEvent
//     VEventMock expectedVEvent = veventOld;
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
// }
// 
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editWholeDay1()
// {
//     VEventMock vevent = getWholeDayDaily3();
//     LocalDate start = LocalDate.of(2015, 11, 15);
//     LocalDate end = LocalDate.of(2015, 11, 22);
//     List<VComponent<InstanceMock>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     List<InstanceMock> instances = new ArrayList<>();
//     Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
//     instances.addAll(newInstances);
//     assertEquals(3, instances.size()); // check if there are 3 instances
//     VEventMock veventOld = new VEventMock(vevent);
//
//     // select InstanceMock and apply changes
//     Iterator<InstanceMock> instanceIterator = instances.iterator();
//     instanceIterator.next(); // skip first
//     instanceIterator.next(); // skip second
//     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
//     LocalDateTime dateTimeOriginal = selectedInstance.getStartTemporal();
//     selectedInstance.setStartTemporal(selectedInstance.getStartTemporal().plusDays(1)); // change start time
//     selectedInstance.setEndTemporal(selectedInstance.getEndTemporal().plusDays(1)); // change end time
//     LocalDateTime dateTimeNew = selectedInstance.getStartTemporal();
//     long startShift = ChronoUnit.DAYS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.DAYS.between(selectedInstance.getStartTemporal(), selectedInstance.getEndTemporal());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDateTimeEnd(dtStart.plus(duration, ChronoUnit.DAYS));
////     
////     // select InstanceMock (get recurrence date) // TODO - WHAT DOES InstanceMock DO?  APPEARS USELESS.
////     Iterator<InstanceMock> instanceIterator = instances.iterator();
////     instanceIterator.next(); // skip first
////     instanceIterator.next(); // skip second
////     InstanceMock selectedInstance = (InstanceMock) instanceIterator.next();
////     
////     // apply changes
////     vevent.setDateTimeStart(LocalDate.of(2015, 11, 10)); // shift forward one day
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , instances            // collection of all instances
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.ALL   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//     
//     // Check edited VEvent
//     VEventMock expectedVEvent = new VEventMock(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS);
//     expectedVEvent.setInstanceMockGroup(ICalendarAgenda.DEFAULT_InstanceMock_GROUPS.get(7));
//     expectedVEvent.setDateTimeStart(LocalDate.of(2015, 11, 10));
//     expectedVEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
//     expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
//     expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
//     expectedVEvent.setDescription("Edited Description");
//     expectedVEvent.setSummary("Edited Summary");
//     expectedVEvent.setInstanceMockClass(getClazz());
//     RRule rule = new RRule()
//             .withUntil(LocalDate.of(2015, 11, 24));
//     expectedVEvent.setRRule(rule);
//     Frequency daily = new Daily()
//             .withInterval(3);
//     rule.setFrequency(daily);
//
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
//     assertEquals(3, instances.size()); // check if there are only two instances
// }

}
