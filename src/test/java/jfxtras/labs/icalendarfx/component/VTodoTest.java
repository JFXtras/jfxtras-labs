package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class VTodoTest
{
    @Test
    public void canBuildVTodo()
    {
        VTodo builtComponent = new VTodo()
                .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                .withPercentComplete(35);
        
        String componentName = builtComponent.componentType().toString();
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "COMPLETED:19960401T150000Z" + System.lineSeparator() +
                "DUE;TZID=America/Los_Angeles:19960401T050000" + System.lineSeparator() +
                "PERCENT-COMPLETE:35" + System.lineSeparator() +
                "END:" + componentName;
                
        VTodo madeComponent = new VTodo(content);
        assertEquals(madeComponent, builtComponent);
        System.out.println(builtComponent.toContent() );
        builtComponent.elementSortOrderMap().entrySet().forEach(System.out::println);
        System.out.println(madeComponent.toContent() );
        assertEquals(content, builtComponent.toContent());
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchBothDurationAndDue()
    {
       new VTodo()
                .withDateTimeDue(LocalDate.of(1997, 3, 1))
                .withDuration(Duration.ofMinutes(30));
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchBothDurationAndDue2()
    {
       new VTodo()
             .withDuration(Duration.ofMinutes(30))
             .withDateTimeDue(LocalDate.of(1997, 3, 1));
    }
    
    @Test
    public void canStreamWithDue()
    {
        VTodo e = new VTodo()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDateTimeDue(LocalDateTime.of(2015, 11, 10, 2, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withCount(6)
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
              , LocalDateTime.of(2015, 11, 24, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 15, 22, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canStreamWithRange()
    {
        VTodo e = new VTodo()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 20, 0))
                .withDuration(Duration.ofHours(6))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency("DAILY")
                        .withInterval(3));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 20, 0)
              , LocalDateTime.of(2015, 11, 18, 20, 0)
              , LocalDateTime.of(2015, 11, 21, 20, 0)
                ));
        List<Temporal> madeDates = e.streamRecurrences(LocalDateTime.of(2015, 11, 14, 20, 0), 
                                                           LocalDateTime.of(2015, 11, 22, 0, 0))
               .collect(Collectors.toList());
        assertEquals(expectedDates, madeDates);
    }
}
