package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponentBase;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.mocks.VEventMockNew;
import jfxtras.labs.icalendarfx.mocks.VJournalMock;
import jfxtras.labs.icalendarfx.mocks.VTodoMock;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 * @see VFreeBusy
 * @see VTimeZone
 * @see StandardTime
 * @see DaylightSavingTime
 * 
 * for the following properties:
 * @see NonStandardProperty
 * @see IANAProperty
 * 
 * @author David Bal
 *
 */
public class BaseTest
{
    @Test
    public void canBuildBase() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentBase<?>> components = Arrays.asList(
                new VEventMockNew()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new VTodoMock()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new VJournalMock()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new VFreeBusy()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new VAlarm()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new VTimeZone()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new DaylightSavingTime()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid"),
                new StandardTime()
                    .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                    .withIANAProperty("TESTPROP2:CASUAL")
                    .withNonStandardProperty("X-TEST-OBJ:testid")
                );
        
        for (VComponentBase<?> builtComponent : components)
        {
            // reorders properties to match expectedContent
            builtComponent.propertySortOrder().put("X-ABC-MMSUBJ", 0);
            builtComponent.propertySortOrder().put("TESTPROP2", 1);
            builtComponent.propertySortOrder().put("X-TEST-OBJ", 2);
            String componentName = builtComponent.componentType().toString();
            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                    "TESTPROP2:CASUAL" + System.lineSeparator() +
                    "X-TEST-OBJ:testid" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentBase<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
