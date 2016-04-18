package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.Month;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;

public class RecurrenceRuleTest
{
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        RecurrenceRule madeProperty = new RecurrenceRule(content);
        assertEquals(content, madeProperty.toContentLine());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRuleParameter()
                .withFrequency(new Yearly()
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
        assertEquals(expectedProperty, madeProperty);
    }
}
