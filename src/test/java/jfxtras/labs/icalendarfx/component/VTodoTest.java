package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.mocks.VTodoMock;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Daily;

public class VTodoTest
{
    @Test
    public void canBuildBase()
    {        
        ObjectProperty<String> s = new SimpleObjectProperty<>("start");
        s.set(null);
        
        VTodoMock builtComponent = new VTodoMock()
                .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                .withIANAProperty("TESTPROP2:CASUAL")
                .withNonStandardProperty("X-TEST-OBJ:testid");
        builtComponent.propertySortOrder().put("X-ABC-MMSUBJ", 0);
        builtComponent.propertySortOrder().put("TESTPROP2", 1);
        builtComponent.propertySortOrder().put("X-TEST-OBJ", 2);
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                "TESTPROP2:CASUAL" + System.lineSeparator() +
                "X-TEST-OBJ:testid" + System.lineSeparator() +
                "END:" + componentName;
                
        VTodoMock madeComponent = new VTodoMock(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
    
    @Test
    public void canBuildPrimary()
    {
        VTodoMock builtComponent = new VTodoMock()
                .withDateTimeStamp("20160306T080000Z")
                .withComments("This is a test comment", "Another comment")
                .withComments("COMMENT:My third comment");
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "DTSTAMP:20160306T080000Z" + System.lineSeparator() +
                "COMMENT:This is a test comment" + System.lineSeparator() +
                "COMMENT:Another comment" + System.lineSeparator() +
                "COMMENT:My third comment" + System.lineSeparator() +
                "END:" + componentName;
                
        VTodoMock madeComponent = new VTodoMock(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }

    @Test
    public void canBuildPersonal()
    {
        VTodoMock builtComponent = new VTodoMock()
                .withAttendees("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com")
                .withDateTimeStart(LocalDateTime.of(2016, 4, 15, 12, 0))
                .withOrganizer("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com")
                .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                .withRequestStatus("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy.")
                .withRequestStatus("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com")
                .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics");
        String componentName = builtComponent.componentType().toString();

        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "UID:19960401T080045Z-4000F192713-0052@example.com" + System.lineSeparator() +
                "URL:http://example.com/pub/calendars/jsmith/mytime.ics" + System.lineSeparator() +
                "DTSTART:20160415T120000" + System.lineSeparator() +
                "ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com" + System.lineSeparator() +
                "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com" + System.lineSeparator() +
                "REQUEST-STATUS:4.1;Event conflict.  Date-time is busy." + System.lineSeparator() +
                "REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com" + System.lineSeparator() +
                "END:" + componentName;

        VTodoMock madeComponent = new VTodoMock(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com", madeComponent.getOrganizer().toContentLine());
    }
    
    @Test
    public void canBuildRepeatable()
    {
        VTodoMock builtComponent = new VTodoMock()
                .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904")
                .withRecurrenceRule(new RecurrenceRuleParameter()
                    .withFrequency(new Daily()
                            .withInterval(4)));
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "END:" + componentName;

        VTodoMock madeComponent = new VTodoMock(content);
        assertEquals(madeComponent, builtComponent);
        
        // add another set of recurrences
        ObservableSet<LocalDate> expectedValues = FXCollections.observableSet(
                LocalDate.of(1996, 4, 2),
                LocalDate.of(1996, 4, 3),
                LocalDate.of(1996, 4, 4) );        
        builtComponent.getRecurrences().add(new Recurrences<LocalDate>(expectedValues));
        String content2 = "BEGIN:" + componentName + System.lineSeparator() +
                "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904" + System.lineSeparator() +
                "RDATE;VALUE=DATE:19960402,19960403,19960404" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=4" + System.lineSeparator() +
                "END:" + componentName;
        
        assertEquals(content2, builtComponent.toContentLines());
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause maybe the cause
    public void canCatchDifferentRepeatableTypes()
    {
        VTodoMock builtComponent = new VTodoMock()
                .withRecurrences("RDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        ObservableSet<ZonedDateTime> expectedValues = FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("Z")) );        
        builtComponent.getRecurrences().add(new Recurrences<ZonedDateTime>(expectedValues));
    }
    
    @Test
    public void canBuildDescribable()
    {
        VTodoMock builtComponent = new VTodoMock()
                .withAttachments("ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW",
                        "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com")
                .withSummary(new Summary("a test summary")
                        .withLanguage("en-USA"));
     
        String componentName = builtComponent.componentType().toString();
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW" + System.lineSeparator() +
                "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com" + System.lineSeparator() +
                "SUMMARY;LANGUAGE=en-USA:a test summary" + System.lineSeparator() +
                "END:" + componentName;

        VTodoMock madeComponent = new VTodoMock(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
}
