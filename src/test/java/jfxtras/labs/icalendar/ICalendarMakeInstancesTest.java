package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendar.mocks.InstanceMock;
import jfxtras.labs.icalendar.mocks.VEventMock;

public class ICalendarMakeInstancesTest extends ICalendarTestAbstract
{

    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeInstancesDailyTest1()
    {
        VEventMock vevent = getDaily1();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> newInstances = vevent.makeInstances(start, end)
                .stream()
                .sorted(InstanceMock.INSTANCE_MOCK_COMPARATOR)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));

        List<InstanceMock> expectedInstances = expectedDates
                .stream()
                .map(d -> {
                    return new InstanceMock()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(1, ChronoUnit.HOURS))
                            .withSummary("Daily1 Summary");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedInstances.size(); i++)
        {
            assertTrue(InstanceMock.isEqualTo(expectedInstances.get(i), newInstances.get(i)));
        }
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void makeInstancesWeeklyTest1()
    {
        VEventMock vevent = getWeekly2();
        LocalDateTime start = LocalDateTime.of(2015, 12, 20, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 12, 27, 0, 0);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
        instances.addAll(newInstances);

        Iterator<InstanceMock> instanceIterator = instances.iterator();

        InstanceMock madeInstances1 = instanceIterator.next();
        InstanceMock expectedInstances1 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 21).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 21).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstances1, madeInstances1)); 
        
        InstanceMock madeInstances2 = instanceIterator.next();
        InstanceMock expectedInstances2 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 23).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 23).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstances2, madeInstances2)); 

        InstanceMock madeInstances3 = instanceIterator.next();
        InstanceMock expectedInstances3 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 25).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 25).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstances3, madeInstances3)); 
    }
    
    @Test
    public void makeInstancesJapanZone()
    {
        VEventMock vevent = getDailyJapanZone();
        Temporal start = ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 0, 0), ZoneOffset.UTC);
        Temporal end = ZonedDateTime.of(LocalDateTime.of(2015, 11, 22, 0, 0), ZoneOffset.UTC);
        List<InstanceMock> instances = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstances = vevent.makeInstances(start, end);
        instances.addAll(newInstances);

        Iterator<InstanceMock> instanceIterator = instances.iterator();
        int dayCounter = 0;
        while (instanceIterator.hasNext())
        {
            InstanceMock madeInstances = instanceIterator.next();            
            InstanceMock expectedInstances = new InstanceMock()
                    .withStartTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 0), ZoneId.of("Japan")).plus(dayCounter, ChronoUnit.DAYS))
                    .withEndTemporal(ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 9, 0), ZoneId.of("Japan")).plus(dayCounter, ChronoUnit.DAYS))
                    .withSummary("Japan Summary");
            assertTrue(InstanceMock.isEqualTo(expectedInstances, madeInstances)); 
            dayCounter++;
        }
    }
    
    @Test
    public void makeInstancesWholeDayTest1()
    {
        VEventMock vevent = getWholeDayDaily3();

        LocalDate start = LocalDate.of(2015, 11, 15);
        LocalDate end = LocalDate.of(2015, 11, 22);
        List<InstanceMock> newInstances = vevent.makeInstances(start, end)
                .stream()
                .sorted(InstanceMock.INSTANCE_MOCK_COMPARATOR)
                .collect(Collectors.toList());

        List<Temporal> dates = newInstances.stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
                ));        
        assertEquals(expectedDates, dates);
        
        assertTrue(newInstances.stream().map(a -> a.isWholeDay()).allMatch(a -> a == true)); // verify all instances are wholeDay
    }
    
}
