package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Description;

public class DescriptionTest
{
    @Test
    public void canParseDescriptionSimple() throws URISyntaxException
    {
        Description description = new Description("this is a simple description without parameters");
        String expectedContentLine = "DESCRIPTION:this is a simple description without parameters";
        String madeContentLine = description.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseDescriptionComplex() throws URISyntaxException
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        Description madeDescription = new Description(contentLine);
        Description expectedDescription = new Description()
                .withAlternateTextRepresentation(new URI("CID:part3.msg.970415T083000@example.com"))
                .withLanguage("en")
                .withValue("Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management");
        assertEquals(expectedDescription, madeDescription);
        assertEquals(contentLine, expectedDescription.toContentLine());
    }
}
