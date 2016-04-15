package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.misc.IANAProperty;

public class IANATest
{
    @Test
    public void canParseIANA1()
    {
        String content = "TESTPROP2:CASUAL";
        IANAProperty madeProperty = new IANAProperty(content);
        assertEquals(content, madeProperty.toContentLine());
        IANAProperty expectedProperty = new IANAProperty("CASUAL")
                .withPropertyName("TESTPROP2");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("CASUAL", madeProperty.getValue());
    }
}
