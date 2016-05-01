package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Exceptions;

public class ExceptionsTest
{
    @Test
    public void canParseExceptions1()
    {
        String content = "EXDATE:20151112T100000,20151115T100000";
        Exceptions<LocalDateTime> madeProperty = Exceptions.parse(LocalDateTime.class, content);
        assertEquals(content, madeProperty.toContentLines());
        Exceptions<LocalDateTime> expectedProperty = new Exceptions<LocalDateTime>(FXCollections.observableSet(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0)));
        assertEquals(expectedProperty, madeProperty);
        
        List<LocalDateTime> expectedValues = new ArrayList<>(Arrays.asList(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0)));
        List<LocalDateTime> madeValues =  madeProperty.getValue().stream().sorted().collect(Collectors.toList());
        assertEquals(expectedValues, madeValues);
    }

    @Test
    public void canParseExceptions2()
    {
        String content = "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z";
        Exceptions<ZonedDateTime> madeProperty = Exceptions.parse(content);
        assertEquals(content, madeProperty.toContentLines());
        ObservableSet<ZonedDateTime> observableSet = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );
        Exceptions<ZonedDateTime> expectedProperty = new Exceptions<ZonedDateTime>(observableSet);
        assertEquals(expectedProperty, madeProperty);
        
        Set<ZonedDateTime> expectedValues = new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) ));
        assertEquals(expectedValues, madeProperty.getValue());
        
        observableSet.add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 5, 1, 0), ZoneId.of("Z")));
        assertEquals(4, expectedProperty.getValue().size());
    }
    
    @Test
    public void canParseExceptions3()
    {
        String content = "EXDATE;VALUE=DATE:20160402";
        Exceptions<LocalDate> madeProperty = Exceptions.parse(LocalDate.class, content);
        assertEquals(content, madeProperty.toContentLines());
        Exceptions<LocalDate> expectedProperty = new Exceptions<LocalDate>(FXCollections.observableSet(
                LocalDate.of(2016, 4, 2) ));
        assertEquals(expectedProperty, madeProperty);
        
        Set<LocalDate> expectedValues = new HashSet<>(Arrays.asList( LocalDate.of(2016, 4, 2) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }

    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongTypeExceptions1()
    {
        Exceptions<Temporal> e = Exceptions.parse("20160228T093000");
        e.getValue().add(LocalDateTime.of(2016, 4, 25, 1, 0));
        e.getValue().add(LocalDate.of(2016, 4, 25));
        assertEquals(2, e.getValue().size());
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongTypeExceptions2()
    {
        Exceptions<Temporal> e = new Exceptions<>();
        e.setValue(FXCollections.observableSet(ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles"))));
        e.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")));
        e.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 5, 1, 0), ZoneId.of("America/New_York")));
        assertEquals(2, e.getValue().size());
    }
    
    @Test
    public void canCopyExceptions()
    {
        String content = "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z";
        Exceptions<ZonedDateTime> property1 = Exceptions.parse(content);
        Exceptions<ZonedDateTime> property2 = new Exceptions<>(property1);
        assertEquals(property1, property2);
        assertFalse(property1 == property2);
    }

}
