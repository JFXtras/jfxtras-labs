package jfxtras.labs.icalendarfx.property.rrule;

import java.time.LocalDate;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;

public class RecurrenceRuleStreamTest
{
    @Test
    public void canStreamRRule1()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13"; //;BYDAY=SA";
        RecurrenceRule3 rRule = new RecurrenceRule3(s);
        Temporal dateTimeStart = LocalDate.of(2016, 6, 11);
        rRule.streamRecurrences(dateTimeStart).limit(20).forEach(System.out::println);
    }
}
