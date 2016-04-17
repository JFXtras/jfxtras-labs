package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Period;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

public class DurationTest
{
    @Test
    public void canParseDuration1()
    {
        String content = "DURATION:PT15M";
        DurationProp madeProperty = new DurationProp(content);
        assertEquals(content, madeProperty.toContentLine());
        DurationProp expectedProperty = new DurationProp("DURATION:PT15M");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Duration.ofMinutes(15), madeProperty.getValue());
    }
    
    @Test
    public void canParseDuration2()
    {
        String content = "DURATION:P2D";
        DurationProp madeProperty = new DurationProp(content);
        assertEquals(content, madeProperty.toContentLine());
        DurationProp expectedProperty = new DurationProp("DURATION:P2D");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Period.ofDays(2), madeProperty.getValue());
    }
}
