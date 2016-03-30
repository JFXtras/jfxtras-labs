package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Attachment;

public class AttachmentText
{
    @Test
    public void canParseDescriptionSimple() throws URISyntaxException
    {
        Attachment property = new Attachment("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com");
        String expectedContentLine = "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseDescriptionComplex() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/reports/r-960812.ps";
        Attachment madeProperty = new Attachment(contentLine);
        Attachment expectedProperty = new Attachment("ftp://example.com/pub/reports/r-960812.ps")
                .withFormatType("application/postscript");
        System.out.println( madeProperty.toContentLine());
        System.out.println( expectedProperty.toContentLine());
        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContentLine());
    }
}
