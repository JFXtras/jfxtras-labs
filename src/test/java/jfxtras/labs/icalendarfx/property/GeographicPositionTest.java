package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;

public class GeographicPositionTest
{
    @Test
    public void canParseGeographicPosition()
    {
        String content = "GEO:37.386013;-122.082932";
        GeographicPosition madeProperty = new GeographicPosition(content);
        assertEquals(content, madeProperty.toContentLine());
        GeographicPosition expectedProperty = new GeographicPosition()
                .withLatitude(37.386013)
                .withLongitude(-122.082932);

        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, madeProperty.toContentLine());
        assertEquals(content, expectedProperty.toContentLine());
    }
}
