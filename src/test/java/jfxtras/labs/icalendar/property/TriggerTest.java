package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.component.alarm.Trigger;

public class TriggerTest
{
    @Test
    public void canParseTrigger1()
    {
        String expectedContent = "TRIGGER:-PT15M";
        Trigger<Duration> madeProperty = new Trigger<Duration>(Duration.class, expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(Duration.ofMinutes(-15), madeProperty.getValue());
    }
    
    @Test
    public void canParseTrigger2()
    {
        Trigger<Duration> madeProperty = new Trigger<Duration>(Duration.ofMinutes(5))
                .withRelationship(AlarmTriggerRelationshipType.END);
        String expectedContent = "TRIGGER;RELATED=END:PT5M";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(Duration.ofMinutes(5), madeProperty.getValue());
        assertEquals(AlarmTriggerRelationshipType.END, madeProperty.getRelationship().getValue());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canParseTrigger3()
    {
        new Trigger<Duration>(Duration.ofMinutes(5))
                .withRelationship(AlarmTriggerRelationshipType.END)
                .withValueParameter(ValueType.DATE_TIME); // invalid type
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canParseTrigger4()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z"));
        new Trigger<ZonedDateTime>(d)
                .withRelationship(AlarmTriggerRelationshipType.END);
    }    
    
    @Test (expected=DateTimeException.class)
    public void canParseTrigger5()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles"));
        new Trigger<ZonedDateTime>(d);
    }
    
    @Test
    public void canParseTrigger6()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z"));
        Trigger<ZonedDateTime> madeProperty = new Trigger<ZonedDateTime>(d);
        String expectedContent = "TRIGGER;VALUE=DATE-TIME:20160306T043000Z";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
}
