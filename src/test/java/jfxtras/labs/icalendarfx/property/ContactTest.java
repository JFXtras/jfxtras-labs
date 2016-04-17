package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;

public class ContactTest
{
    @Test
    public void canParseContact() throws URISyntaxException
    {
        String content = "CONTACT;ALTREP=\"CID:part3.msg970930T083000SILVER@example.com\";LANGUAGE=en-US:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234";
        Contact madeProperty = new Contact(content);
        assertEquals(content, madeProperty.toContentLine());
        Contact expectedProperty = new Contact("Jim Dolittle\\, ABC Industries\\, +1-919-555-1234")
                .withAlternateText(new URI("CID:part3.msg970930T083000SILVER@example.com"))
                .withLanguage("en-US");
        assertEquals(expectedProperty, madeProperty);
    }
}
