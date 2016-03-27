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
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyEnum;

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
public class Attachment extends PropertyBase
{
    /*
     * PROPERTY VALUE
     */
    @Override
    public URI getValue() { return value.get(); }
    public ObjectProperty<URI> valueProperty() { return value; }
    private ObjectProperty<URI> value = new SimpleObjectProperty<>(this, PropertyEnum.ATTACHMENT.toString() + "_URI");
    public void setValue(URI value) { this.value.set(value); }
//    @Override
//    public void parseAndSetValue(String value)
//    {
//        try
//        {
//            setValue(new URI(value));
//        } catch (URISyntaxException e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    /*
     * PROPERTY PARAMETERS
     */

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
    // Uses lazy initialization because its rarely used
    public Encoding getEncoding() { return (encoding == null) ? _encoding : encoding.get(); }
    public Encoding encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString(), _encoding);
        }
        return encoding.get();
    }
    private Encoding _encoding;
    private ObjectProperty<Encoding> encoding;
    public void setEncoding(Encoding encoding)
    {
        if (encoding != Encoding.BASE64)
        {
            throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + Encoding.BASE64);
        }

        if (this.encoding == null)
        {
            _encoding = encoding;
        } else
        {
            this.encoding.set(encoding);
        }
    }
    public Attachment withEncoding(Encoding encoding) { setEncoding(encoding); return this; }

    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    // Uses lazy initialization because its rarely used
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
        if (this.formatType == null)
        {
            _formatType = formatType;
        } else
        {
            this.formatType.set(formatType);
        }
    }
    
    /**
     * VALUE: Value Date Types
     * RFC 5545, 3.2.20. page 29
     * Explicitly specify the value type format for a property value.
     */
    // Uses lazy initialization because its rarely used
    public ValueType getValueParameter() { return (valueType == null) ? _valueType : valueType.get(); }
    public ValueType valueTypeProperty()
    {
        if (valueType == null)
        {
            valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString(), _valueType);
        }
        return valueType.get();
    }
    private ValueType _valueType;
    private ObjectProperty<ValueType> valueType;
    public void setValueParameter(ValueType valueType)
    {
        if (valueType != ValueType.BINARY)
        {
            throw new IllegalArgumentException("Attachment property only allows VALUE to be set to" + ValueType.BINARY);
        }
        if (this.valueType == null)
        {
            _valueType = valueType;
        } else
        {
            this.valueType.set(valueType);
        }
    }
    public Attachment withValueParameter(ValueType valueType) { setValueParameter(valueType); return this; }
    
    /*
     * CONSTRUCTORS
     */
    
    public Attachment(String propertyString) throws URISyntaxException
    {
        super(propertyString);
        setValue(new URI(getPropertyValueString()));
    }
    
    public Attachment(Attachment source)
    {
        super(source);
    }
    
    public Attachment()
    {
        super();
    }

}
