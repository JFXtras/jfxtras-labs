package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VComponentPersonal;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

/**
 * Test following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * 
 * for the following properties:
 * @see Attendee
 * @see DateTimeStamp
 * @see Organizer
 * @see RequestStatus
 * @see UniqueIdentifier
 * @see UniformResourceLocator
 * 
 * @author David Bal
 *
 */
public class PersonalTest
{
    @Test
    public void canBuildPersonal() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        List<VComponentPersonal<?>> components = Arrays.asList(
                new VEventNew()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."), RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                new VTodo()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                    new VJournal()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."))
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics"),
                new VFreeBusy()
                    .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                    .withDateTimeStamp(DateTimeStamp.parse("20160415T120000Z"))
                    .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                    .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com")
                    .withRequestStatus(RequestStatus.parse("REQUEST-STATUS:4.1;Event conflict.  Date-time is busy."), RequestStatus.parse("REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com"))
                    .withUniformResourceLocator("http://example.com/pub/calendars/jsmith/mytime.ics")
                );
        
        for (VComponentPersonal<?> builtComponent : components)
        {
            String componentName = builtComponent.componentType().toString();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com" + System.lineSeparator() +
                    "DTSTAMP:20160415T120000Z" + System.lineSeparator() +
                    "ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com" + System.lineSeparator() +
                    "REQUEST-STATUS:4.1;Event conflict.  Date-time is busy." + System.lineSeparator() +
                    "REQUEST-STATUS:3.7;Invalid user;ATTENDEE:mailto:joecool@example.com" + System.lineSeparator() +
                    "UID:19960401T080045Z-4000F192713-0052@example.com" + System.lineSeparator() +
                    "URL:http://example.com/pub/calendars/jsmith/mytime.ics" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponentPersonal<?> parsedComponent = builtComponent
                    .getClass()
                    .getConstructor(String.class)
                    .newInstance(expectedContent);
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContentLines());            
        }
    }
}
