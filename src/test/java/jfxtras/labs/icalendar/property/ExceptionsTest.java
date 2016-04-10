package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

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
        assertEquals(2, madeProperty.getValue().size());
    }
//        return getDaily2()
//                .withExDate(new ExDate()
//                        .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
//                                     , LocalDateTime.of(2015, 11, 15, 10, 0)));
}
