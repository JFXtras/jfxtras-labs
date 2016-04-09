package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.Month;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.RecurrenceImpl;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Yearly;

public class RecurrenceRuleTest
{
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        RecurrenceRule madeProperty = new RecurrenceRule(content);
        assertEquals(content, madeProperty.toContentLine());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceImpl()
                .withFrequency(new Yearly()
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
        assertEquals(expectedProperty, madeProperty);
    }
}
