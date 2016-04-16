package jfxtras.labs.icalendar.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.components.VComponentBase;
import jfxtras.labs.icalendar.components.VComponentTest;
import jfxtras.labs.icalendar.properties.component.descriptive.Description;

public class ParsePropertiesTest
{
    
    @Test
    public void canBuildBase()
    {        
        ObjectProperty<String> s = new SimpleObjectProperty<>("start");
        s.set(null);
        
        VComponentTest builtComponent = new VComponentTest()
                .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                .withIANAProperty("TESTPROP2:CASUAL")
                .withNonStandardProperty("X-TEST-OBJ:testid");
        builtComponent.propertySortOrder().put("X-ABC-MMSUBJ", 0);
        builtComponent.propertySortOrder().put("TESTPROP2", 1);
        builtComponent.propertySortOrder().put("X-TEST-OBJ", 2);
        
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
                "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                "TESTPROP2:CASUAL" + System.lineSeparator() +
                "X-TEST-OBJ:testid" + System.lineSeparator() +
                "END:VEVENT";
                
        VComponentTest madeComponent = new VComponentTest(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
    
    @Test
    public void canBuildPrimary()
    {
        VComponentTest builtComponent = new VComponentTest()
                .withDateTimeStamp("20160306T080000Z")
                .withComments("This is a test comment", "Another comment")
                .withComments("COMMENT:My third comment");
        
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
                "DTSTAMP:20160306T080000Z" + System.lineSeparator() +
                "COMMENT:This is a test comment" + System.lineSeparator() +
                "COMMENT:Another comment" + System.lineSeparator() +
                "COMMENT:My third comment" + System.lineSeparator() +
                "END:VEVENT";
                
        VComponentTest madeComponent = new VComponentTest(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
    
    @Test
    public void canFoldingLine()
    {
        Description d = new Description("a dog\\nran\\, far\\;\\naway \\\\\\\\1");
        System.out.println(d.toContentLine());
        System.out.println(d.getValue());
        
        String line = "Ek and Lorentzon said they would consider halting investment at their head\\,quarters in Stockholm. The pioneering music streaming company employs about 850 people in the city, and more than 1,000 in nearly 30 other offices around the world.";
        String f = VComponentBase.foldLine(line).toString();
        String line2 = VComponentBase.unfoldLines(f).get(0);
        
        assertEquals(line, line2);
//        System.out.println(VComponentBase.foldLine(line));
//        String s1 = "away \\\\\\\\1";
//        System.out.println(VComponentBase.unfoldLines(s1));
    }

    @Test
    public void canBuildPersonal()
    {
        VComponentTest builtComponent = new VComponentTest()
                .withAttendees("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com")
                .withDateTimeStart(LocalDateTime.of(2016, 4, 15, 12, 0))
                .withOrganizer("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com")
                .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                .withRequestStatus("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy.")
                .withRequestStatus("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com")
                .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics");
        
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
                "UID:19960401T080045Z-4000F192713-0052@example.com" + System.lineSeparator() +
                "URL:http://example.com/pub/calendars/jsmith/mytime.ics" + System.lineSeparator() +
                "DTSTART:20160415T120000" + System.lineSeparator() +
                "ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com" + System.lineSeparator() +
                "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com" + System.lineSeparator() +
                "REQUEST-STATUS:4.1;Event conflict.  Date-time is busy." + System.lineSeparator() +
                "REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com" + System.lineSeparator() +
                "END:VEVENT";
                
        VComponentTest madeComponent = new VComponentTest(content);
        System.out.println(madeComponent.toContentLines());
        assertEquals(madeComponent, builtComponent);
        assertEquals("ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yahoo.com", madeComponent.getOrganizer().toContentLine());
    }
}