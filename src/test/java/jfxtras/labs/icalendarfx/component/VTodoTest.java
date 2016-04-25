package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public class VTodoTest
{
    @Test
    public void canBuildVTodo()
    {
        String string = "DTEND;TZID=America/Los_Angeles:20160307T053000";
        Temporal t = DateTimeUtilities.temporalFromString(string);
        VTodo builtComponent = new VTodo()
                .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                .withPercentComplete(35);
        
        String componentName = builtComponent.componentType().toString();
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "COMPLETED:19960401T150000Z" + System.lineSeparator() +
                "DUE;TZID=America/Los_Angeles:19960401T050000" + System.lineSeparator() +
                "PERCENT-COMPLETE:35" + System.lineSeparator() +
                "END:" + componentName;
                
        VTodo madeComponent = new VTodo(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
}
