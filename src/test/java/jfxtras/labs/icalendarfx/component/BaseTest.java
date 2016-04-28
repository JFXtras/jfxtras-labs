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
                new VEventNew()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VTodo()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VJournal()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VFreeBusy()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VAlarm()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new VTimeZone()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new DaylightSavingTime()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid")),
                new StandardTime()
                    .withNonStandardProperty(NonStandardProperty.parse("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au"))
                    .withIANAProperty(IANAProperty.parse("TESTPROP2:CASUAL"))
                    .withNonStandardProperty(NonStandardProperty.parse("X-TEST-OBJ:testid"))
                );
        
        for (VComponentBase<?> builtComponent : components)
        {
            // reorders properties to match expectedContent
            NonStandardProperty p0 = builtComponent.getNonStandardProperties()
                    .stream()
                    .filter(p -> p.getPropertyName().equals("X-ABC-MMSUBJ"))
                    .findFirst()
                    .get();
            IANAProperty p1 = builtComponent.getIANAProperties().get(0);
            NonStandardProperty p2 = builtComponent.getNonStandardProperties()
                    .stream()
                    .filter(p -> p.getPropertyName().equals("X-TEST-OBJ"))
                    .findFirst()
                    .get();

            builtComponent.propertySortOrder().put(p0, 0);
            builtComponent.propertySortOrder().put(p1, 1);
            builtComponent.propertySortOrder().put(p2, 2);
            String componentName = builtComponent.componentType().toString();
            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                    "TESTPROP2:CASUAL" + System.lineSeparator() +
                    "X-TEST-OBJ:testid" + System.lineSeparator() +
                    "END:" + componentName;
//            VComponentBase<?> parsedComponent = builtComponent
//                    .getClass()
//                    .getConstructor(String.class)
//                    .newInstance(expectedContent);
            VComponentBase<?> parsedComponent = builtComponent
                    .getClass()
                    .newInstance();
            parsedComponent.parseContent(expectedContent);
//            parsedComponent.parse

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
