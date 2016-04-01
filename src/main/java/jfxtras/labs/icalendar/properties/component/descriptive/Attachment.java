package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.parameters.Encoding;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
import jfxtras.labs.icalendar.properties.PropertyBase;

/**
 * ATTACH: Attachment
 * RFC 5545 iCalendar 3.8.1.1. page 80
 * This property provides the capability to associate a
 * document object with a calendar component.
 * 
 * Example:
 * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
 *  reports/r-960812.p
 *  
 *  @see VEvent
 *  @see VTodo
 *  @see VJournal
 *  @see VAlarm
 */
public class Attachment extends PropertyBase<Attachment, URI>
{    
    /**
     * ENCODING: Incline Encoding
     * RFC 5545, 3.2.7, page 18
     * 
     * Specify an alternate inline encoding for the property value.
     * Values can be "8BIT" text encoding defined in [RFC2045]
     *               "BASE64" binary encoding format defined in [RFC4648]
     *
     * If the value type parameter is ";VALUE=BINARY", then the inline
     * encoding parameter MUST be specified with the value" ;ENCODING=BASE64".
     */
    public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
    public ObjectProperty<Encoding> encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return encoding;
    }
    private ObjectProperty<Encoding> encoding;
    public void setEncoding(Encoding encoding)
    {
        if (encoding.getValue() != EncodingType.BASE64)
        {
            throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
        }

        if (encoding != null)
        {
            encodingProperty().set(encoding);
        }
    }
    public Attachment withEncoding(Encoding encoding) { setEncoding(encoding); return this; }
    public Attachment withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return this; }

    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
    public ObjectProperty<FormatType> formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString());
        }
        return formatType;
    }
    private ObjectProperty<FormatType> formatType;
    public void setFormatType(FormatType formatType)
    {
        if (formatType != null)
        {
            formatTypeProperty().set(formatType);
        }
    }
    public Attachment withFormatType(FormatType format) { setFormatType(format); return this; }
    public Attachment withFormatType(String format) { setFormatType(new FormatType(format)); return this; }
    
    /*
     * CONSTRUCTORS
     */
    
    public Attachment(String content) throws URISyntaxException
    {
        super(content);
        setValue(new URI(getPropertyValueString()));
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line");
        }
    }
    
    public Attachment(Attachment source)
    {
        super(source);
        setEncoding(source.getEncoding());
        setFormatType(source.getFormatType());
    }
    
    public Attachment()
    {
        super();
    }
    
    @Override
    public boolean isValid()
    {
        boolean isEncodingNull = getEncoding() == null;
        boolean isValueTypeNull = getValueType() == null;
//        System.out.println("null? encoding, value: " + isEncodingNull + " " + isValueTypeNull);
        if (isEncodingNull && isValueTypeNull)
        {
            return true;
        }
        if (isEncodingNull || isValueTypeNull)
        { // both ENCODING and VALUE must be set or not set, only one is not allowed
            return false;
        }
        if (getEncoding().getValue() != EncodingType.BASE64)
        { // invalid EncodingType
            return false;
        }
        if (getValueType().getValue() != ValueEnum.BINARY)
        { // invalid ValueType
            return false;
        }
        return true;
    }
}
