package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.alarm.Action;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;

public class ActionTest
{
    @Test
    public void canParseAction()
    {
        Action madeProperty = Action.parse("ACTION:AUDIO");
        String expectedContent = "ACTION:AUDIO";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.AUDIO, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction2()
    {
        Action madeProperty = new Action(ActionType.DISPLAY);
        String expectedContent = "ACTION:DISPLAY";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.DISPLAY, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction3()
    {
        Action madeProperty = Action.parse("DANCE");
        String expectedContent = "ACTION:DANCE";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.UNKNOWN, madeProperty.getValue());
    }
    
    @Test
    public void canParseAction4()
    {
        Action madeProperty = Action.parse("EMAIL");
        String expectedContent = "ACTION:EMAIL";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.EMAIL, madeProperty.getValue());
        Action copiedProperty = new Action(madeProperty);
        assertEquals(madeProperty, copiedProperty);
        assertFalse(copiedProperty == madeProperty);
    }
}
