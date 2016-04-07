package jfxtras.labs.icalendar.properties.component.descriptive.attachment;

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
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.PropertyAttachment;
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
 * @param <T> - URI or String (String is for BASE64 binary encoding
 *  
 *  The property can be specified in following components:
 *  @see VEvent
 *  @see VTodo
 *  @see VJournal
 *  @see VAlarm
 *  
 *  concrete classes
 *  @see AttachmentURI
 *  @see AttachmentBase64
 */
public abstract class AttachmentBase<T,U> extends PropertyBase<T,U> implements PropertyAttachment<U>
{
     /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    @Override
    public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
    @Override
    public ObjectProperty<FormatType> formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString());
        }
        return formatType;
    }
    private ObjectProperty<FormatType> formatType;
    @Override
    public void setFormatType(FormatType formatType)
    {
        if (formatType != null)
        {
            formatTypeProperty().set(formatType);
        }
    }
    public T withFormatType(FormatType format) { setFormatType(format); return (T) this; }
    public T withFormatType(String format) { setFormatType(new FormatType(format)); return (T) this; }
    
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
    @Override
    public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
    @Override
    public ObjectProperty<Encoding> encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return encoding;
    }
    private ObjectProperty<Encoding> encoding;
    @Override
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
    public T withEncoding(Encoding encoding) { setEncoding(encoding); return (T) this; }
    public T withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return (T) this; }

    
    /*
     * CONSTRUCTORS
     */
    
    AttachmentBase(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    AttachmentBase(AttachmentBase<T,U> source)
    {
        super(source);
    }
    
    AttachmentBase(U value)
    {
        super(value);
    }
    
    @Override
    public boolean isValid()
    {
        boolean isEncodingNull = getEncoding() == null;
        boolean isValueTypeNull = getValueParameter() == null;
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
        if (getValueParameter().getValue() != ValueType.BINARY)
        { // invalid ValueType
            return false;
        }
        return true && super.isValid();
    }
}
