package jfxtras.labs.icalendar.properties.component.descriptive.attachment;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * ATTACH: Attachment
 * RFC 5545 iCalendar 3.8.1.1. page 80
 * This property provides the capability to associate a
 * document object with a calendar component.
 * 
 * Example:
 * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
 *  reports/r-960812.p
 */ 
public class AttachmentURI extends AttachmentBase<AttachmentURI, URI>
{
    public AttachmentURI(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public AttachmentURI(AttachmentURI source)
    {
        super(source);
    }
    
    public AttachmentURI(URI value)
    {
        super(value);
    }
    
    @Override
    protected URI valueFromString(String propertyValueString)
    {
        try
        {
            return new URI(propertyValueString);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
