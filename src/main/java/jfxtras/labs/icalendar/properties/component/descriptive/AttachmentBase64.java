package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.Encoding;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;

/**
 * ATTACH: Attachment
 * RFC 5545 iCalendar 3.8.1.1. page 80
 * This property provides the capability to associate a
 * document object with a calendar component.
 * 
 * For BASE64 binary encoding
 * 
 * Example:
 * ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW
 *  0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2ljaW
 *  5nIGVsaXQsIHNlZCBkbyBlaXVzbW9kIHRlbXBvciBpbmNpZGlkdW50IHV0IG
 *  xhYm9yZSBldCBkb2xvcmUgbWFnbmEgYWxpcXVhLiBVdCBlbmltIGFkIG1pbm
 *  ltIHZlbmlhbSwgcXVpcyBub3N0cnVkIGV4ZXJjaXRhdGlvbiB1bGxhbWNvIG
 *  xhYm9yaXMgbmlzaSB1dCBhbGlxdWlwIGV4IGVhIGNvbW1vZG8gY29uc2VxdW
 *  F0LiBEdWlzIGF1dGUgaXJ1cmUgZG9sb3IgaW4gcmVwcmVoZW5kZXJpdCBpbi
 *  B2b2x1cHRhdGUgdmVsaXQgZXNzZSBjaWxsdW0gZG9sb3JlIGV1IGZ1Z2lhdC
 *  BudWxsYSBwYXJpYXR1ci4gRXhjZXB0ZXVyIHNpbnQgb2NjYWVjYXQgY3VwaW
 *  RhdGF0IG5vbiBwcm9pZGVudCwgc3VudCBpbiBjdWxwYSBxdWkgb2ZmaWNpYS
 *  BkZXNlcnVudCBtb2xsaXQgYW5pbSBpZCBlc3QgbGFib3J1bS4=
 */ 
public class AttachmentBase64 extends Attachment<AttachmentBase64, String>
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
    public AttachmentBase64 withEncoding(Encoding encoding) { setEncoding(encoding); return this; }
    public AttachmentBase64 withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return this; }

    
    public AttachmentBase64(String content) throws URISyntaxException
    {
        super(content);
//        setValue(getPropertyValueString());
    }
    
    public AttachmentBase64(AttachmentBase64 source)
    {
        super(source);
    }
    
    public AttachmentBase64()
    {
        super();
    }
    
    @Override
    public boolean isValid()
    {
        super.isValid();
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
    
    @Override
    protected List<ValueEnum> allowedValueTypes()
    {
        return Arrays.asList(ValueEnum.BINARY);
    }
}
