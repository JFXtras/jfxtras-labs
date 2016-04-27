package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;

public class IANATest
{
    @Test
    public void canParseIANA1()
    {
        String content = "TESTPROP2:CASUAL";
        IANAProperty madeProperty = IANAProperty.parse(content);
        assertEquals(content, madeProperty.toContentLine());
        IANAProperty expectedProperty = IANAProperty.parse("CASUAL")
                .withPropertyName("TESTPROP2");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("CASUAL", madeProperty.getValue());
    }
    
    @Test
    public void canParseIANA2()
    {
        String content = "TESTPROP2;VALUE=INTEGER:12";
        IANAProperty madeProperty = IANAProperty.parse(content);
        assertEquals(content, madeProperty.toContentLine());
        IANAProperty expectedProperty = new IANAProperty(12)
                .withPropertyName("TESTPROP2")
                .withValueParameter("INTEGER");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(12, madeProperty.getValue());
        assertEquals("TESTPROP2", madeProperty.getPropertyName());
    }
}
