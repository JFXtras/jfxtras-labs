package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.FXCollections;
import jfxtras.labs.icalendar.properties.component.recurrence.Exceptions;

public class ExceptionsTest
{
    @Test
    public void canParseExceptions1()
    {
        String content = "EXDATE:20151112T100000,20151115T100000";
        Exceptions<LocalDateTime> madeProperty = new Exceptions<LocalDateTime>(content);
        assertEquals(content, madeProperty.toContentLine());
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
        Exceptions<ZonedDateTime> madeProperty = new Exceptions<ZonedDateTime>(content);
        assertEquals(content, madeProperty.toContentLine());
        Exceptions<ZonedDateTime> expectedProperty = new Exceptions<ZonedDateTime>(FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) ));
        assertEquals(expectedProperty, madeProperty);
        
        Set<ZonedDateTime> expectedValues = new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }
    
    @Test
    public void canParseExceptions3()
    {
        String content = "EXDATE;VALUE=DATE:20160402";
        Exceptions<LocalDate> madeProperty = new Exceptions<LocalDate>(content);
        assertEquals(content, madeProperty.toContentLine());
        Exceptions<LocalDate> expectedProperty = new Exceptions<LocalDate>(FXCollections.observableSet(
                LocalDate.of(2016, 4, 2) ));
        assertEquals(expectedProperty, madeProperty);
        
        Set<LocalDate> expectedValues = new HashSet<>(Arrays.asList( LocalDate.of(2016, 4, 2) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }
    
}
