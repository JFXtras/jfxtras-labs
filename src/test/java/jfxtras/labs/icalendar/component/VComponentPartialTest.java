package jfxtras.labs.icalendar.component;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.components.VComponentTest;

public class VComponentPartialTest
{
    @Test
    public void canBuildPrimary()
    {
        LocalDateTime dtStart = LocalDateTime.of(2016, 4, 15, 12, 0);
        VComponentTest madeProperty = new VComponentTest()
                .withDateTimeStart(dtStart)
                .withComments("This is a test comment", "Another comment");
        System.out.println(madeProperty.getDateTimeStart());
        System.out.println(madeProperty.toContentLines());
//        String expectedContent = "ACTION:AUDIO";
//        assertEquals(expectedContent, madeProperty.toContentLine());
//        assertEquals(ActionType.AUDIO, madeProperty.getValue());
    }
}
