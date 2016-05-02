package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VComponentLastModified;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VTimeZone
 * 
 * for the following properties:
 * @see LastModified
 * 
 * @author David Bal
 *
 */
public class LastModifiedTest
{
    @Test
    public void canBuildLastModified() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentLastModified<?>> components = Arrays.asList(
                new VEventNew()
                        .withDateTimeLastModified("20160306T080000Z"),
                new VTodo()
                        .withDateTimeLastModified("20160306T080000Z"),
                new VJournal()
                        .withDateTimeLastModified("20160306T080000Z"),
                new VTimeZone()
                        .withDateTimeLastModified("20160306T080000Z")
                );
        
        for (VComponentLastModified<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "LAST-MODIFIED:20160306T080000Z" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentLastModified<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());            
        }
    }
}
