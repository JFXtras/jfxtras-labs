package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Encoding.EncodingEnum;

/**
 * ENCODING
 * Inline Encoding
 * RFC 5545, 3.2.7, page 18
 * 
 * To specify an alternate inline encoding for the property value.
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
 * 
 * @author David Bal
 *
 */
public class Encoding extends ParameterBase<Encoding, EncodingEnum>
{
    public Encoding()
    {
        super();
    }
  
    public Encoding(String content)
    {
        super(content);
    }

    public Encoding(Encoding source)
    {
        super(source);
    }
    
    public enum EncodingEnum
    {
        EIGHT_BIT ("8BIT"),
        BASE64 ("BASE64");
        
        private String name;
        @Override public String toString() { return name; }
        EncodingEnum(String name)
        {
            this.name = name;
        }
    }
}