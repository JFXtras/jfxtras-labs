package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class MakeInstancesTest extends ICalendarTestAbstract
{

    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeInstanceMocksDailyTest1()
    {
        VEventMock vevent = getDaily1();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<InstanceMock> newInstanceMocks = vevent.makeInstances(start, end)
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
            assertTrue(InstanceMock.isEqualTo(expectedInstances.get(i), newInstanceMocks.get(i)));
        }
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void makeInstanceMocksWeeklyTest1()
    {
        VEventMock vevent = getWeekly2();
        LocalDateTime start = LocalDateTime.of(2015, 12, 20, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 12, 27, 0, 0);
        List<InstanceMock> InstanceMocks = new ArrayList<InstanceMock>();
        Collection<InstanceMock> newInstanceMocks = vevent.makeInstances(start, end);
        InstanceMocks.addAll(newInstanceMocks);

        Iterator<InstanceMock> InstanceMockIterator = InstanceMocks.iterator();

        InstanceMock madeInstanceMock1 = InstanceMockIterator.next();
        InstanceMock expectedInstanceMock1 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 21).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 21).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstanceMock1, madeInstanceMock1)); 
        
        InstanceMock madeInstanceMock2 = InstanceMockIterator.next();
        InstanceMock expectedInstanceMock2 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 23).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 23).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstanceMock2, madeInstanceMock2)); 

        InstanceMock madeInstanceMock3 = InstanceMockIterator.next();
        InstanceMock expectedInstanceMock3 = new InstanceMock()
                .withStartTemporal(LocalDate.of(2015, 12, 25).atTime(10, 0))
                .withEndTemporal(LocalDate.of(2015, 12, 25).atTime(10, 45))
                .withSummary("Weekly1 Summary");
        assertTrue(InstanceMock.isEqualTo(expectedInstanceMock3, madeInstanceMock3)); 
    }
    
    @Test
    public void makeInstanceMocksWholeDayTest1()
    {
        VEventMock vevent = getWholeDayDaily3();

        LocalDate start = LocalDate.of(2015, 11, 15);
        LocalDate end = LocalDate.of(2015, 11, 22);
        List<InstanceMock> newInstanceMocks = vevent.makeInstances(start, end)
                .stream()
                .sorted(InstanceMock.INSTANCE_MOCK_COMPARATOR)
                .collect(Collectors.toList());

        List<Temporal> dates = newInstanceMocks.stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
                ));        
        assertEquals(expectedDates, dates);
        
        assertTrue(newInstanceMocks.stream().map(a -> a.isWholeDay()).allMatch(a -> a == true)); // verify all InstanceMock are wholeDay
    }
    
}
