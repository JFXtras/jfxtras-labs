package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.icalendar.mocks.InstanceMock;
import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.RRule;
import jfxtras.labs.icalendar.rrule.freq.Daily;

public class ICalendarDeleteTest extends ICalendarTestAbstract
{
    /**
     * Tests deleting one instance from recurrence set
     */
    @Test
    public void canDeleteOne()
    {
        VEventMock vEvent = getWeeklyZoned();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newinstances = vEvent.makeInstances(start, end);
        instances.addAll(newinstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances

        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        InstanceMock selectedInstance = instanceIterator.next();        
        Temporal startInstance = selectedInstance.getStartTemporal();

        vEvent.handleDelete(
                vComponents
              , startInstance
              , selectedInstance
              , instances
              , (m) -> ChangeDialogOption.ONE);

        assertEquals(1, vComponents.size());

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 10, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);

        VEventMock expectedVEvent = getWeeklyZoned()
                .withExDate(new ExDate().withTemporals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/Los_Angeles"))));
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    /**
     * Tests deleting all instances from recurrence set
     */
    @Test
    public void canDeleteAll()
    {
        VEventMock vEvent = getWeeklyZoned();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newinstances = vEvent.makeInstances(start, end);
        instances.addAll(newinstances);
        assertEquals(3, instances.size()); // check if there are only 3 instances

        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        InstanceMock selectedInstance = instanceIterator.next();        
        Temporal startInstance = selectedInstance.getStartTemporal();

        vEvent.handleDelete(
                vComponents
              , startInstance
              , selectedInstance
              , instances
              , (m) -> ChangeDialogOption.ALL);
        
        assertEquals(0, vComponents.size());
        assertEquals(0, instances.size());
    }
    
    /**
     * Tests deleting this and future instances from recurrence set
     */
    @Test
    public void canDeleteThisAndFuture()
    {
        VEventMock vEvent = getDailyUTC();
        List<VComponent<InstanceMock>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newinstances = vEvent.makeInstances(start, end);
        instances.addAll(newinstances);
        assertEquals(4, instances.size()); // check if there are only 3 instances

        // select InstanceMock (get recurrence date)
        Iterator<InstanceMock> instanceIterator = instances.iterator();
        instanceIterator.next(); // skip first
        InstanceMock selectedInstance = instanceIterator.next();        
        Temporal startInstance = selectedInstance.getStartTemporal();

        vEvent.handleDelete(
                vComponents
              , startInstance
              , selectedInstance
              , instances
              , (m) -> ChangeDialogOption.THIS_AND_FUTURE);

        assertEquals(1, vComponents.size());

        List<Temporal> madeDates = instances.stream()
                .map(a -> a.getStartTemporal())
                .sorted()
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<Temporal>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 10, 0), ZoneOffset.UTC)
                ));
        assertEquals(expectedDates, madeDates);

        VEventMock expectedVEvent = getDailyUTC()
                .withRRule(new RRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 10, 0), ZoneOffset.UTC))
                        .withFrequency(new Daily()
                                .withInterval(2)));
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
}
