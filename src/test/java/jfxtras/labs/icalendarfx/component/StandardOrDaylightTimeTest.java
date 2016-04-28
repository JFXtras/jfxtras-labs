package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardOrDaylight;
import jfxtras.labs.icalendarfx.components.StandardTime;

public class StandardOrDaylightTimeTest
{
    @Test
    public void canBuildStandardOrDaylight() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<StandardOrDaylight<?>> components = Arrays.asList(
                new DaylightSavingTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE"),
                new StandardTime()
                    .withTimeZoneOffsetFrom(ZoneOffset.ofHours(-4))
                    .withTimeZoneOffsetTo(ZoneOffset.ofHours(-5))
                    .withTimeZoneNames("TZNAME;LANGUAGE=fr-CA:HNE")
                );
        
        for (StandardOrDaylight<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "TZNAME;LANGUAGE=fr-CA:HNE" + System.lineSeparator() +
                    "TZOFFSETFROM:-0400" + System.lineSeparator() +
                    "TZOFFSETTO:-0500" + System.lineSeparator() +
                    "END:" + componentName;

            StandardOrDaylight<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
