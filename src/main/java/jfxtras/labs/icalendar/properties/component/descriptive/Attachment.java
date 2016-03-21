package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ICalendarParameter;
import jfxtras.labs.icalendar.properties.ComponentProperty;
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
 */
public class Attachment extends PropertyBase
{
    public URI getValue() { return value.get(); }
    public ObjectProperty<URI> valueProperty() { return value; }
    private ObjectProperty<URI> value = new SimpleObjectProperty<>(this, ComponentProperty.ATTACHMENT.toString() + "_URI");
    public void setValue(URI value) { this.value.set(value); }

    // Uses lazy initialization because its rarely used
    public String getEncoding() { return (encoding == null) ? _encoding : encoding.get(); }
    public String encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleStringProperty(this, ComponentProperty.ATTACHMENT.toString() + "_ENCODING", _encoding);
        }
        return encoding.get();
    }
    private String _encoding;
    private StringProperty encoding;
    public void setEncoding(String encoding)
    {
        if (this.encoding == null)
        {
            _encoding = encoding;
        } else
        {
            this.encoding.set(encoding);
        }
    }
    
    // Uses lazy initialization because its rarely used
    public FormatType getFormatType() { return (formatType == null) ? _formatType : formatType.get(); }
    public FormatType formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ComponentProperty.ATTACHMENT.toString() + "_" + ICalendarParameter.FORMAT_TYPE.toString(), _formatType);
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
    
    // Uses lazy initialization because its rarely used
    public String getValueParameter() { return (valueParameter == null) ? _valueParameter : valueParameter.get(); }
    public String valueParameterProperty()
    {
        if (valueParameter == null)
        {
            valueParameter = new SimpleStringProperty(this, ComponentProperty.ATTACHMENT.toString() + "_VALUE", _valueParameter);
        }
        return valueParameter.get();
    }
    private String _valueParameter;
    private StringProperty valueParameter;
    public void setValue(String valueParameter)
    {
        if (this.valueParameter == null)
        {
            _valueParameter = valueParameter;
        } else
        {
            this.valueParameter.set(valueParameter);
        }
    }
}
