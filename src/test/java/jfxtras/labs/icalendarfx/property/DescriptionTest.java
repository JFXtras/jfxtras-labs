package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public class DescriptionTest
{
    @Test
    public void canParseDescriptionSimple() throws URISyntaxException
    {
        Description description = Description.parse("this is a simple description without parameters");
        String expectedContentLine = "DESCRIPTION:this is a simple description without parameters";
        String madeContentLine = description.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseDescriptionComplex() throws URISyntaxException
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management")
                .withAlternateText("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en");
        
        expectedDescription.parameterSortOrder().put(expectedDescription.getAlternateText(), 0);
        expectedDescription.parameterSortOrder().put(expectedDescription.getLanguage(), 1);
        assertEquals(expectedDescription, madeDescription);
        assertEquals(contentLine, expectedDescription.toContentLine());
    }
    
    @Test
    public void canParseDescriptionWithOtherParameters()
    {
        String contentLine = "DESCRIPTION;MYPARAMETER=some value;IGNORE ME;PARAMETER2=other value:Example description";
        Description madeDescription = Description.parse(contentLine);
        Description expectedDescription = Description.parse("Example description")
                .withOtherParameters("MYPARAMETER=some value", "PARAMETER2=other value");
        assertEquals(expectedDescription, madeDescription);
        assertEquals("DESCRIPTION;MYPARAMETER=some value;PARAMETER2=other value:Example description", expectedDescription.toContentLine());
    }
}
