package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;

/**
 * Test following components:
 * @see VEvent
 * 
 * for the following properties:
 * @see TimeTransparency
 * 
 * @author David Bal
 *
 */
public class VEventTest
{
    @Test
    public void canBuildVEvent()
    {
        VEvent builtComponent = new VEvent()
                .withTimeTransparency(TimeTransparencyType.OPAQUE)
                .withDateTimeEnd(LocalDate.of(1997, 3, 1));
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DTEND;VALUE=DATE:19970301" + System.lineSeparator() +
                "TRANSP:OPAQUE" + System.lineSeparator() +
                "END:" + componentName;
                
        VEvent madeComponent = VEvent.parse(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContent());
    }
        
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchBothDurationAndDTEnd()
    {
       new VEvent()
                .withDateTimeEnd(LocalDate.of(1997, 3, 1))
                .withDuration(Duration.ofMinutes(30));
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchBothDurationAndDTEnd2()
    {
       new VEvent()
             .withDuration(Duration.ofMinutes(30))
             .withDateTimeEnd(LocalDate.of(1997, 3, 1));
    }
    
    @Test
    public void canChangeDTEndToDuration()
    {
        VEvent builtComponent = new VEvent()
             .withDateTimeEnd(LocalDate.of(1997, 3, 1));
        assertEquals(LocalDate.of(1997, 3, 1), builtComponent.getDateTimeEnd().getValue());
        builtComponent.setDateTimeEnd((DateTimeEnd<?>) null);
        builtComponent.withDateTimeEnd((DateTimeEnd<? extends Temporal>) null).withDuration("PT15M");
        assertEquals(Duration.ofMinutes(15), builtComponent.getDuration().getValue());
        assertNull(builtComponent.getDateTimeEnd());
    }
}