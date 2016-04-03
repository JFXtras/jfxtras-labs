package jfxtras.labs.icalendar.properties.component.descriptive.attachment;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
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
 *  @see VEvent
 *  @see VTodo
 *  @see VJournal
 *  @see VAlarm
 *  
 *  @see AttachmentURI
 *  @see AttachmentBase64
 */
public abstract class Attachment<T,U> extends PropertyBase<T,U>
{    
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
    public T withFormatType(FormatType format) { setFormatType(format); return (T) this; }
    public T withFormatType(String format) { setFormatType(new FormatType(format)); return (T) this; }
    
    /*
     * CONSTRUCTORS
     */
    
    Attachment(CharSequence content)
    {
        super(content);
    }
    
    Attachment(Attachment<T,U> source)
    {
        super(source);
//        setEncoding(source.getEncoding());
//        setFormatType(source.getFormatType());
    }
    
    Attachment(U value)
    {
        super(value);
    }
    
    Attachment()
    {
        super();
    }
    
//    @Override
//    protected List<ValueType> allowedValueTypes()
//    {
//        return Arrays.asList(ValueType.UNIFORM_RESOURCE_IDENTIFIER, ValueType.BINARY);
//    }
    
//    public static Attachment<?> parseContentLine(String content)
//    {
//        
//    }
}
