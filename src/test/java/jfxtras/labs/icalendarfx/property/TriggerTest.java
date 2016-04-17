package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;

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
    public void canBuildTrigger1()
    {
        Trigger<Duration> madeProperty = new Trigger<Duration>(Duration.ofMinutes(5))
                .withAlarmTrigger(AlarmTriggerRelationshipType.END);
        String expectedContent = "TRIGGER;RELATED=END:PT5M";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals(Duration.ofMinutes(5), madeProperty.getValue());
        assertEquals(AlarmTriggerRelationshipType.END, madeProperty.getAlarmTrigger().getValue());
    }

    @Test
    public void canBuildTrigger2()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z"));
        Trigger<ZonedDateTime> madeProperty = new Trigger<ZonedDateTime>(d);
        String expectedContent = "TRIGGER;VALUE=DATE-TIME:20160306T043000Z";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canCatchInvalidTypeChange()
    {
        new Trigger<Duration>(Duration.ofMinutes(5))
                .withAlarmTrigger(AlarmTriggerRelationshipType.END)
                .withValueParameter(ValueType.DATE_TIME); // invalid type
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canCatchInvalidRelationship()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("Z"));
        new Trigger<ZonedDateTime>(d)
                .withAlarmTrigger(AlarmTriggerRelationshipType.END); // not allowed for DATE-TIME value type
    }    
    
    @Test (expected=DateTimeException.class)
    public void canCatchNonUTCZone()
    {
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 4, 30), ZoneId.of("America/Los_Angeles"));
        new Trigger<ZonedDateTime>(d);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void canCatchInvalidParameterizedType()
    {
        new Trigger<>(Integer.class, "123"); // only TemporalAmount and ZonedDateTime accepted
    }
    
    @Test (expected=DateTimeException.class)
    public void canCatchTypeContentMismatch()
    {
        new Trigger<>(ZonedDateTime.class, "TRIGGER;RELATED=END:PT5M");
    }
    
    @Test (expected=DateTimeException.class)
    public void canCatchTypeContentMismatch2()
    {
        new Trigger<>(Duration.class, "TRIGGER;VALUE=DATE-TIME:20160306T043000Z");
    }
}
