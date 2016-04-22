package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;

/**
 * Test following components:
 * @see VEventNew
 * 
 * for the following properties:
 * @see TimeTransparency
 * @see DateTimeEnd
 * 
 * @author David Bal
 *
 */
public class VEventTest
{
    @Test
    public void canBuildVEvent()
    {
        VEventNew builtComponent = new VEventNew()
                .withTimeTransparency(TimeTransparencyType.OPAQUE);
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "TRANSP:OPAQUE" + System.lineSeparator() +
                "END:" + componentName;
                
        VEventNew madeComponent = new VEventNew(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }    
}