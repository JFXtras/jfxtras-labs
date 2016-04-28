package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Attachment;

public class AttachmentTest
{
    @Test
    public void canParseAttachement1()
    {
        Attachment<URI> property = new Attachment<URI>(URI.class, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com");
        String expectedContentLine = "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseAttachement2()
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> madeProperty = new Attachment<String>(String.class, contentLine);
        Attachment<String> expectedProperty = new Attachment<String>(String.class, "TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueParameter(ValueType.BINARY);
        assertEquals(expectedProperty, madeProperty);
        assertFalse(expectedProperty == madeProperty);
    }
    
    @Test
    public void canParseAttachementSimpleOld() throws URISyntaxException
    {
        Attachment<URI> property = new Attachment<URI>(URI.class, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com");
        String expectedContentLine = "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com";
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
    }
    
    @Test
    public void canParseAttachementComplex() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/reports/r-960812.ps";
        Attachment<URI> madeProperty = new Attachment<URI>(URI.class, contentLine);
        Attachment<URI> expectedProperty = new Attachment<URI>(URI.class, "ftp://example.com/pub/reports/r-960812.ps")
                .withFormatType("application/postscript");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContentLine());
    }
    
    @Test
    public void canParseAttachementComplex2() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> madeProperty = new Attachment<String>(String.class, contentLine);
        Attachment<String> expectedProperty = new Attachment<String>(String.class, "TG9yZW")
                .withFormatType("text/plain")
                .withEncoding(EncodingType.BASE64)
                .withValueParameter(ValueType.BINARY);
        expectedProperty.parameterSortOrder().put(expectedProperty.getFormatType(), 0);
        expectedProperty.parameterSortOrder().put(expectedProperty.getEncoding(), 1);
        expectedProperty.parameterSortOrder().put(expectedProperty.getValueParameter(), 2);

        assertEquals(expectedProperty, madeProperty);
        assertEquals(contentLine, expectedProperty.toContentLine());
    }
    
    @Test
    public void canCopyAttachement() throws URISyntaxException
    {
        String contentLine = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW";
        Attachment<String> expectedProperty = new Attachment<String>(String.class, contentLine);
        Attachment<String> madeProperty = new Attachment<String>(expectedProperty);
        assertEquals(expectedProperty, madeProperty);
        assertFalse(expectedProperty == madeProperty);
    }
}
