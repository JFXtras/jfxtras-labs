package jfxtras.labs.icalendar.properties.component.descriptive.attachment;

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
public class AttachmentBase64 extends AttachmentBase<AttachmentBase64, String>
{    
    public AttachmentBase64(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public AttachmentBase64(AttachmentBase64 source)
    {
        super(source);
    }
}
