package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardOrSavings;
import jfxtras.labs.icalendarfx.components.StandardTime;

public class StandardOrDaylightTimeTest
{
    @Test
    public void canBuildStandardOrDaylight() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<StandardOrSavings<?>> components = Arrays.asList(
                new DaylightSavingTime()
                    .withTimeZoneName("TZNAME;LANGUAGE=fr-CA:HNE"),
                new StandardTime()
                    .withTimeZoneName("TZNAME;LANGUAGE=fr-CA:HNE")
                );
        
        for (StandardOrSavings<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "TZNAME;LANGUAGE=fr-CA:HNE" + System.lineSeparator() +
                    "END:" + componentName;
                    
            StandardOrSavings<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
