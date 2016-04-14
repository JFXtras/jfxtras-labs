package jfxtras.labs.icalendar.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.components.VComponentTest;

public class VComponentPartialTest
{
    @Test
    public void canBuildPrimary()
    {
        VComponentTest builtComponent = new VComponentTest()
                .withDateTimeStart(LocalDateTime.of(2016, 4, 15, 12, 0))
                .withComments("This is a test comment", "Another comment")
                .withOrganizer("ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yahoo.com");
        System.out.println(builtComponent.toContentLines());
        
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160415T120000" + System.lineSeparator() +
                "COMMENT:This is a test comment" + System.lineSeparator() +
                "COMMENT:Another comment" + System.lineSeparator() +
                "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
                " o.com" + System.lineSeparator() +
                "END:VEVENT";
                
        VComponentTest madeComponent = new VComponentTest(content);
        System.out.println(madeComponent.toContentLines());
        assertEquals(madeComponent, builtComponent);
//        String expectedContent = "ACTION:AUDIO";
//        assertEquals(expectedContent, madeProperty.toContentLine());
//        assertEquals(ActionType.AUDIO, madeProperty.getValue());
    }
    
    @Test
    public void canParse()
    {
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTART:20160415T120000" + System.lineSeparator() +
                "COMMENT:This is a test comment" + System.lineSeparator() +
                "COMMENT:Another comment" + System.lineSeparator() +
                "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
                " o.com" + System.lineSeparator() +
                "END:VEVENT";
        
        // TODO - do I need to maintain order?
        VComponentTest madeComponent = new VComponentTest(content);
        System.out.println(madeComponent.toContentLines());
        System.out.println(content);
        assertEquals(content, madeComponent.toContentLines());
    }
    
//    @Test
//    public void canFold()
//    {
//        String s = "Note: It is possible for very simple implementations to generate improperly folded lines in the middle of a UTF-8 multi-octet sequence.  For this reason, implementations need to unfold lines in such a way to properly restore the original sequence.";
//        CharSequence s2 = VComponentBase.foldLine(s);
//        System.out.println(s2);
//    }

}
