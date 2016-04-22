package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public class GeneralTest
{
    @Test
    public void canEscapeTest()
    {
        String contentLine = "DESCRIPTION:a dog\\nran\\, far\\;\\naway \\\\\\\\1";
        Description d = new Description(contentLine);
        String expectedValue = "a dog" + System.lineSeparator() +
                               "ran, far;" + System.lineSeparator() +
                               "away \\\\1";
        assertEquals(expectedValue, d.getValue());
        assertEquals(contentLine, d.toContentLine());
    }

    
    @Test
    public void canFoldAndUnfoldLine()
    {
        String line = "Ek and Lorentzon said they would consider halting investment at th,eir headquarters in Stockholm. The pioneering music streaming company employs about 850 people in the city, and more than 1,000 in nearly 30 other offices around the world.";
        VEventNew builtComponent = new VEventNew()
                .withComments(line);
        String componentName = builtComponent.componentType().toString();
        String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                                 "COMMENT:Ek and Lorentzon said they would consider halting investment at th" + System.lineSeparator() +
                                 " \\,eir headquarters in Stockholm. The pioneering music streaming company em" + System.lineSeparator() +
                                 " ploys about 850 people in the city\\, and more than 1\\,000 in nearly 30 oth" + System.lineSeparator() +
                                 " er offices around the world." + System.lineSeparator() +
                                 "END:" + componentName;
        assertEquals(expectedContent, builtComponent.toContentLines());
        assertEquals(line, builtComponent.getComments().get(0).getValue());
    }
    
    @Test
    public void canGetProperties()
    {
        VEventNew builtComponent = new VEventNew()
                .withAttendees("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com")
                .withDateTimeStart(LocalDateTime.of(2016, 4, 15, 12, 0))
                .withOrganizer("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com")
                .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com");
        VEventNew builtComponent2 = new VEventNew();
        System.out.println("here:" + builtComponent2.getRecurrences());
        List<PropertyEnum> expectedProperties = Arrays.asList(PropertyEnum.ATTENDEE, PropertyEnum.DATE_TIME_START, PropertyEnum.ORGANIZER, PropertyEnum.UNIQUE_IDENTIFIER);
        assertEquals(expectedProperties, builtComponent.properties());
    }
}
