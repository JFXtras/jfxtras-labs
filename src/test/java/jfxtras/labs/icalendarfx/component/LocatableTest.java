package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * 
 * for the following properties:
 * @see Description
 * @see GeographicPosition
 * @see DurationProp
 * @see Location
 * @see Priority
 * @see Resources
 * 
 * @author David Bal
 *
 */
public class LocatableTest
{
    @Test
    public void canBuildLocatable() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentLocatable<?>> components = Arrays.asList(
                new VEventNew()
                    .withDescription("DESCRIPTION:A simple description")
                    .withGeographicPosition("37.386013;-122.082932"),
                new VTodo()
                    .withDescription("DESCRIPTION:A simple description")
                    .withGeographicPosition("37.386013;-122.082932")
                );
        
        for (VComponentLocatable<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DESCRIPTION:A simple description" + System.lineSeparator() +
                    "GEO:37.386013;-122.082932" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentLocatable<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
