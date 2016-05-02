package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ParameterType;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;

public class ContactTest
{
    @Test
    public void canParseContact() throws URISyntaxException
    {
        String content = "CONTACT;LANGUAGE=en-US;ALTREP=\"CID:part3.msg970930T083000SILVER@example.com\":Jim Dolittle\\, ABC Industries\\, +1-919-555-1234";
        Contact madeProperty = Contact.parse(content);
        assertEquals(content, madeProperty.toContentLines());
        Contact expectedProperty = new Contact()
                .withValue("Jim Dolittle, ABC Industries, +1-919-555-1234")
                .withAlternateText(new URI("CID:part3.msg970930T083000SILVER@example.com"))
                .withLanguage("en-US");
        expectedProperty.parameterSortOrder().put(ParameterType.LANGUAGE, 0);
        expectedProperty.parameterSortOrder().put(ParameterType.ALTERNATE_TEXT_REPRESENTATION, 1);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContentLines());
    }
}
