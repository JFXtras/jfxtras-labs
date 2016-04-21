package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Daily;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see Recurrences
 * @see RecurrenceRule
 * 
 * @author David Bal
 *
 */
public class RepeatableTest
{
    @Test
    public void canBuildRepeatable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentRepeatable<?>> components = Arrays.asList(
                new VEventNew()
                    .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                    .withRecurrences(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Daily()
                                .withInterval(4))),
                new VTodo()
                    .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                    .withRecurrences(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Daily()
                                .withInterval(4))),
                new VJournal()
                    .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                    .withRecurrences(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Daily()
                                .withInterval(4))),
                new DaylightSavingTime()
                    .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                    .withRecurrences(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Daily()
                                .withInterval(4))),
                new StandardTime()
                    .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                    .withRecurrences(LocalDate.of(2016, 4, 15), LocalDate.of(2016, 4, 16), LocalDate.of(2016, 4, 17))
                    .withRecurrenceRule(new RecurrenceRuleParameter()
                        .withFrequency(new Daily()
                                .withInterval(4)))
                );
        
        for (VComponentRepeatable<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                    "RDATE;VALUE=DATE:20160415,20160416,20160417" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentRepeatable<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
