package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

public class GeneralComponentTest
{
    @Test
    public void canEscapeTest()
    {
        String contentLine = "DESCRIPTION:a dog\\nran\\, far\\;\\naway \\\\\\\\1";
        Description d = Description.parse(contentLine);
        String expectedValue = "a dog" + System.lineSeparator() +
                               "ran, far;" + System.lineSeparator() +
                               "away \\\\1";
        assertEquals(expectedValue, d.getValue());
        assertEquals(contentLine, d.toContent());
    }

    
    @Test
    public void canFoldAndUnfoldLine()
    {
        String line = "Ek and Lorentzon said they would consider halting investment at th,eir headquarters in Stockholm. The pioneering music streaming company employs about 850 people in the city, and more than 1,000 in nearly 30 other offices around the world.";
        VEvent builtComponent = new VEvent()
                .withComments(line);
        String componentName = builtComponent.componentType().toString();
        String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                                 "COMMENT:Ek and Lorentzon said they would consider halting investment at th" + System.lineSeparator() +
                                 " \\,eir headquarters in Stockholm. The pioneering music streaming company em" + System.lineSeparator() +
                                 " ploys about 850 people in the city\\, and more than 1\\,000 in nearly 30 oth" + System.lineSeparator() +
                                 " er offices around the world." + System.lineSeparator() +
                                 "END:" + componentName;
        assertEquals(expectedContent, builtComponent.toContent());
        assertEquals(line, builtComponent.getComments().get(0).getValue());
    }
    
    @Test
    public void canGetProperties()
    {
        VEvent builtComponent = new VEvent()
                .withAttendees(Attendee.parse("ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com"))
                .withDateTimeStart(new DateTimeStart(LocalDateTime.of(2016, 4, 15, 12, 0)))
                .withOrganizer(Organizer.parse("ORGANIZER;CN=David Bal:mailto:ddbal1@yahoo.com"))
                .withAttendees(Attendee.parse("ATTENDEE;PARTSTAT=DECLINED:mailto:jsmith@example.com"))
                .withUniqueIdentifier("19960401T080045Z-4000F192713-0052@example.com");
        List<PropertyType> expectedPropertyEnums = Arrays.asList(PropertyType.ATTENDEE, PropertyType.DATE_TIME_START, PropertyType.ORGANIZER, PropertyType.UNIQUE_IDENTIFIER);
        assertEquals(expectedPropertyEnums, builtComponent.propertyEnums());
        List<Property<?>> expectedProperties = Arrays.asList(
                builtComponent.getAttendees().get(0),
                builtComponent.getAttendees().get(1),
                builtComponent.getDateTimeStart(),
                builtComponent.getOrganizer(),
                builtComponent.getUniqueIdentifier()
                );
        assertEquals(expectedProperties, builtComponent.properties());
//        builtComponent.properties().stream().forEach(p -> System.out.println(p.toContentLine()));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canCatchSecondAssignmentException()
    {
        String componentName = "VEVENT";
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "CLASS:PUBLIC" + System.lineSeparator() +
                "CLASS:PRIVATE" + System.lineSeparator() + // not allowed
                "END:" + componentName;
        VEvent.parse(content);
    }
    
    @Test
    public void canChangePropertyAllowedOnlyOnce()
    {
        String componentName = "VEVENT";
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "CLASS:PUBLIC" + System.lineSeparator() +
                "END:" + componentName;
        VEvent madeComponent = VEvent.parse(content);
        madeComponent.setClassification("PRIVATE");
        assertEquals(ClassificationType.PRIVATE, madeComponent.getClassification().getValue());
    }
    
    @Test
    public void canSetPropertyOrder()
    {
        String contentLines = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                + "DTSTART:20151109T100000Z" + System.lineSeparator()
                + "DTEND:20151109T110000Z" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "CATEGORIES:group03" + System.lineSeparator()
                + "SUMMARY:DailyUTC Summary" + System.lineSeparator()
                + "DESCRIPTION:DailyUTC Description" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z" + System.lineSeparator()
                + "END:VEVENT";
        VEvent builtComponent = VEvent.parse(contentLines);
        builtComponent.setDescription((Description) null);
        builtComponent.getCategories().add(Categories.parse("group05"));
        builtComponent.setClassification(ClassificationType.PRIVATE);
        builtComponent.setDateTimeStart("20151109T110000Z");
        
        String contentLines2 = "BEGIN:VEVENT" + System.lineSeparator()
                + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                + "DTSTART:20151109T110000Z" + System.lineSeparator()
                + "DTEND:20151109T110000Z" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "CATEGORIES:group03" + System.lineSeparator()
                + "CATEGORIES:group05" + System.lineSeparator()
                + "SUMMARY:DailyUTC Summary" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z" + System.lineSeparator()
                + "CLASS:PRIVATE" + System.lineSeparator()
                + "END:VEVENT";
        assertEquals(contentLines2, builtComponent.toContent());
    }
}
