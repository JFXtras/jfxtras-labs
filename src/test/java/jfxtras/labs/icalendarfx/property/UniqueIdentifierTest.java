package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class UniqueIdentifierTest
{
    @Test
    public void canParseUniqueIdentifier()
    {
        String expectedContentLine = "UID:19960401T080045Z-4000F192713-0052@example.com";
        UniqueIdentifier property = UniqueIdentifier.parse(expectedContentLine);
        String madeContentLine = property.toContentLines();
        assertEquals(expectedContentLine, madeContentLine);
        assertEquals("19960401T080045Z-4000F192713-0052@example.com", property.getValue());
    }
}
