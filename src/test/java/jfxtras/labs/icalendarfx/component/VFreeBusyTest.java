package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VFreeBusy;

public class VFreeBusyTest
{
    @Test
    public void canBuildVFreeBusy()
    {        
        VFreeBusy builtComponent = new VFreeBusy()
                .withContact("CONTACT:Harry Potter\\, Hogwarts\\, by owl");
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                "END:" + componentName;
                
        VFreeBusy madeComponent = new VFreeBusy(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
        
        builtComponent.setDateTimeStamp("20160210T100000Z");
        builtComponent.setUniqueIdentifier("66761d56-d248-4c12-a807-350e95abea66");
        assertTrue(builtComponent.isValid());
        builtComponent.setDateTimeStart(LocalDate.of(2016, 4, 25));
        builtComponent.setDateTimeEnd(LocalDate.of(2016, 4, 26));
        assertTrue(builtComponent.isValid());
    }
}
