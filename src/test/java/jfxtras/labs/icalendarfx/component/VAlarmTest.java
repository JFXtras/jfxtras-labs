package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;

public class VAlarmTest
{
    @Test
    public void canBuildBase()
    {        
        VAlarm builtComponent = new VAlarm()
                .withAction(ActionType.DISPLAY);
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "ACTION:DISPLAY" + System.lineSeparator() +
                "END:" + componentName;
                
        VAlarm madeComponent = new VAlarm(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
}
