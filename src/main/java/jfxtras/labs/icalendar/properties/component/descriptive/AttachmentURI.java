package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;

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
public class AttachmentURI extends Attachment<AttachmentURI, URI>
{
    public AttachmentURI(String content) throws URISyntaxException
    {
        super(content);
//        setValue(new URI(getPropertyValueString()));
    }
    
    public AttachmentURI(AttachmentURI source)
    {
        super(source);
    }
    
    public AttachmentURI()
    {
        super();
    }
    
    @Override
    protected List<ValueEnum> allowedValueTypes()
    {
        return Arrays.asList(ValueEnum.UNIFORM_RESOURCE_IDENTIFIER);
    }
}
