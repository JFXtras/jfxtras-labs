package jfxtras.labs.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class RRuleErrorTest
{
    @Test
    public void canDetectIntervalError()
    {
        RecurrenceRule2 rrule = new RecurrenceRule2()
                .withFrequency(FrequencyType.YEARLY)
                .withInterval(0); // invalid
        assertEquals(1, rrule.errors().size());
    }

}
