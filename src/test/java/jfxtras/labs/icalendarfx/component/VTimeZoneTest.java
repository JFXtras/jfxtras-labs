package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VTimeZone;

public class VTimeZoneTest
{
    @Test
    public void canVTimeZone() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        VTimeZone builtComponent = new VTimeZone()
                .withTimeZoneIdentifier("America/Los_Angeles");

        String componentName = builtComponent.componentType().toString();            
        String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                "TZID:America/Los_Angeles" + System.lineSeparator() +
                "END:" + componentName;
                
        VTimeZone parsedComponent = builtComponent
                .getClass()
                .getConstructor(String.class)
                .newInstance(expectedContent);
        assertEquals(parsedComponent, builtComponent);
        assertEquals(expectedContent, builtComponent.toContentLines());            
    }
}
