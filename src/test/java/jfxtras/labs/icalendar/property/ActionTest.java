package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.alarm.Action;
import jfxtras.labs.icalendar.properties.component.alarm.Action.ActionType;

public class ActionTest
{
    @Test
    public void canParseClassification()
    {
        Action madeProperty = new Action("ACTION:AUDIO");
        String expectedContent = "ACTION:AUDIO";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.AUDIO, madeProperty.getValue());
    }
    
    @Test
    public void canParseClassification2()
    {
        Action madeProperty = new Action(ActionType.DISPLAY);
        String expectedContent = "ACTION:DISPLAY";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.DISPLAY, madeProperty.getValue());
    }
    
    @Test
    public void canParseClassification3()
    {
        Action madeProperty = new Action("DANCE");
        String expectedContent = "ACTION:DANCE";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.UNKNOWN, madeProperty.getValue());
    }
    
    @Test
    public void canParseClassification4()
    {
        Action madeProperty = new Action("EMAIL");
        String expectedContent = "ACTION:EMAIL";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(ActionType.EMAIL, madeProperty.getValue());
        Action copiedProperty = new Action(madeProperty);
        assertEquals(madeProperty, copiedProperty);
        assertFalse(copiedProperty == madeProperty);
    }
}
