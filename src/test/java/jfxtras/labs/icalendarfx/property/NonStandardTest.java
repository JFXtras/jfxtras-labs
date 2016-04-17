package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

public class NonStandardTest
{
    @Test
    public void canParseNonStandard1()
    {
        String content = "X-MICROSOFT-CDO-ALLDAYEVENT;VALUE=BOOLEAN:FALSE";
        NonStandardProperty madeProperty = new NonStandardProperty(content);
        assertEquals(content, madeProperty.toContentLine());
        NonStandardProperty expectedProperty = new NonStandardProperty("FALSE")
                .withPropertyName("X-MICROSOFT-CDO-ALLDAYEVENT")
                .withValueParameter(ValueType.BOOLEAN);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(Boolean.FALSE, madeProperty.getValue());
    }
    
    @Test
    public void canParseNonStandard2() throws URISyntaxException
    {
        // MAINTAIN ORDER OF PROPERTIES?
        String content = "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au";
        NonStandardProperty madeProperty = new NonStandardProperty(content);
//        System.out.println(madeProperty.toContentLine());
        assertEquals(content, madeProperty.toContentLine());
        NonStandardProperty expectedProperty = new NonStandardProperty("http://www.example.org/mysubj.au")
                .withFormatType("audio/basic")
                .withValueParameter(ValueType.UNIFORM_RESOURCE_IDENTIFIER);
//        System.out.println(expectedProperty.toContentLine());

        assertEquals(expectedProperty, madeProperty);
        assertEquals(ValueType.UNIFORM_RESOURCE_IDENTIFIER, madeProperty.getValueParameter().getValue());
        assertEquals(new URI("http://www.example.org/mysubj.au"), madeProperty.getValue());
    }

}
