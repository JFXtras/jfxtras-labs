package jfxtras.labs.icalendar.properties.component.misc;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.Encoding;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.FreeBusyType;
import jfxtras.labs.icalendar.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.properties.PropertyAttendeeAbstract;

/**
 * 
 * contains all parameters
 * 
 * @author David Bal
 *
 */
// TODO - DO I WANT TO MAKE INTERFACES FOR PARAMETERS?
public abstract class UnknownProperty<T, U> extends PropertyAttendeeAbstract<T, U>
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
    public T withEncoding(Encoding encoding) { setEncoding(encoding); return (T) this; }
    public T withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return (T) this; }

    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    public FreeBusyType getFreeBusyType() { return (freeBusyType == null) ? null : freeBusyType.get(); }
    public ObjectProperty<FreeBusyType> freeBusyTypeProperty()
    {
        if (freeBusyType == null)
        {
            freeBusyType = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
        }
        return freeBusyType;
    }
    private ObjectProperty<FreeBusyType> freeBusyType;
    public void setFreeBusyType(FreeBusyType freeBusyType)
    {
        if (freeBusyType != null)
        {
            freeBusyTypeProperty().set(freeBusyType);
        }
    }
    public void setFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(new FreeBusyType(type)); }
    public T withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return (T)this; }
    public T withFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(type); return (T) this; }
    public T withFreeBusyType(String freeBusyType) { setFreeBusyType(new FreeBusyType(freeBusyType)); return (T) this; }

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
    
    /**
     * TZID
     * Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * 
     * Examples:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    public ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString());
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifier;
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        if (timeZoneIdentifier != null)
        {
            timeZoneIdentifierProperty().set(timeZoneIdentifier);
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value)); }
    public T withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (T) this; }
    public T withTimeZoneIdentifier(String content) { ParameterEnum.TIME_ZONE_IDENTIFIER.parse(this, content); return (T) this; }        

    /*
     * CONSTRUCTORS
     */
    
    public UnknownProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public UnknownProperty(U value)
    {
        super(value);
    }
    
    public UnknownProperty(UnknownProperty source)
    {
        super(source);
    }
}
