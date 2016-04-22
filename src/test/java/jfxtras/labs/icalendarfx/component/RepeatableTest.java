package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.stage.Stage;
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
public class RepeatableTest extends Application
{
    // Below Application code inserted as an attempt to catch listener-thrown exceptions - not successful
    @Override
    public void start(Stage primaryStage) throws Exception {
        // noop
    }

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread")
        {
            @Override
            public void run() {
                Application.launch(RepeatableTest.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }


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
     
    @Test (expected = DateTimeException.class)
    @Ignore // can't catch exception in listener
    public void canHandleDTStartTypeChange()
    {
        VEventNew component = new VEventNew()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
//        Platform.runLater(() -> component.setDateTimeStart("20160302T223316Z"));      
        component.setDateTimeStart("20160302T223316Z"); // invalid
    }

    @Test (expected = DateTimeException.class)
    public void canCatchWrongRDateType()
    {
        VEventNew component = new VEventNew()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<Recurrences<? extends Temporal>> recurrences = FXCollections.observableArrayList();
        recurrences.add(new Recurrences<LocalDateTime>("20160228T093000"));
        component.setRecurrences(recurrences); // invalid        
    }

    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause maybe the cause
    public void canCatchDifferentRepeatableTypes()
    {
        VEventNew builtComponent = new VEventNew()
                .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        ObservableSet<ZonedDateTime> expectedValues = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );        
        builtComponent.getRecurrences().add(new Recurrences<ZonedDateTime>(expectedValues));
    }
}
