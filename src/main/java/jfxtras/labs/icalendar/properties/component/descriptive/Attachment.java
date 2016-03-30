package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Value.ValueType;
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
public class Attachment extends PropertyBase<Description, URI>
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
    public EncodingType getEncoding() { return (encoding == null) ? _encoding : encoding.get(); }
    public EncodingType encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString(), _encoding);
        }
        return encoding.get();
    }
    private EncodingType _encoding;
    private ObjectProperty<EncodingType> encoding;
    public void setEncoding(EncodingType encoding)
    {
        if (encoding != EncodingType.BASE64)
        {
            throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
        }
        
        if (encoding != null)
        {
            parametersModifiable().add(ParameterEnum.INLINE_ENCODING);
//            parameterMapModifiable().put(ParameterEnum.INLINE_ENCODING, encoding);
//            parameterMapInternal().add(ParameterEnum.INLINE_ENCODING);
        } else
        {
            parametersModifiable().remove(ParameterEnum.INLINE_ENCODING);
//            parameterMapModifiable().remove(ParameterEnum.INLINE_ENCODING);            
        }

        if (this.encoding == null)
        {
            _encoding = encoding;
        } else
        {
            this.encoding.set(encoding);
        }
    }
    public Attachment withEncoding(EncodingType encoding) { setEncoding(encoding); return this; }
//    public Attachment withEncoding(String encoding) { setEncoding(new Encoding(encoding)); return this; }

    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    public FormatType getFormatType() { return (formatType == null) ? _formatType : formatType.get(); }
    public FormatType formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString(), _formatType);
        }
        return formatType.get();
    }
    private FormatType _formatType;
    private ObjectProperty<FormatType> formatType;
    public void setFormatType(FormatType formatType)
    {
        if (formatType != null)
        {
            parameterMapModifiable().put(ParameterEnum.FORMAT_TYPE, formatType);
//            parametersModifiable().add(ParameterEnum.FORMAT_TYPE);
        } else
        {
//            parametersModifiable().remove(ParameterEnum.FORMAT_TYPE);
            parameterMapModifiable().remove(ParameterEnum.FORMAT_TYPE);
        }
        
        if (this.formatType == null)
        {
            _formatType = formatType;
        } else
        {
            this.formatType.set(formatType);
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
        if (isEncodingNull && isValueTypeNull)
        {
            return true;
        }
        if (isEncodingNull || isValueTypeNull)
        { // both ENCODING and VALUE must be set or not set, only one is not allowed
            return false;
        }
        if (getEncoding() != EncodingType.BASE64)
        { // invalid EncodingType
            return false;
        }
        if (getValueType() != ValueType.BINARY)
        { // invalid ValueType
            return false;
        }
        return true;
    }

}
