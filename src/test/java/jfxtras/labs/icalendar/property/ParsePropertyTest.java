package jfxtras.labs.icalendar.property;

import java.util.Map;

import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities;

public class ParsePropertyTest
{
    @Test // TODO - MOVE THESE TESTS ELSEWHERE
    public void canParseRDate()
    {
//        String s = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
//        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap2(s);
//        String s2 = "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yahoo.com";
//        Map<String, String> map2 = ICalendarUtilities.propertyLineToParameterMap2(s2);
//        String s3 = "STATUS:CONFIRMED";
//        Map<String, String> map3 = ICalendarUtilities.propertyLineToParameterMap2(s3);
//        String s4 = "RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1";
//        Map<String, String> map4 = ICalendarUtilities.propertyLineToParameterMap2(s4);
//        String s4b = "RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1";
//        Map<String, String> map4b = ICalendarUtilities.propertyLineToParameterMap3(s4b);
//        String s4c = "FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1";
//        Map<String, String> map4c = ICalendarUtilities.propertyLineToParameterMap3(s4c);
        
        
//        String s5 = "GEO:37.386013;-122.082932";
//        Map<String, String> map5 = ICalendarUtilities.propertyLineToParameterMap2(s5);
//        String s6 = "ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org";
//        Map<String, String> map6 = ICalendarUtilities.propertyLineToParameterMap2(s6);
//        String s7 = "ORGANIZER;DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\":mailto:jimdo@example.com";
//        Map<String, String> map7 = ICalendarUtilities.propertyLineToParameterMap2(s7);
        String s8 = "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2ljaW5nIGVsaXQsIHNlZCBkbyBlaXVzbW9kIHRlbXBvciBpbmNpZGlkdW50IHV0IGxhYm9yZSBldCBkb2xvcmUgbWFnbmEgYWxpcXVhLiBVdCBlbmltIGFkIG1pbmltIHZlbmlhbSwgcXVpcyBub3N0cnVkIGV4ZXJjaXRhdGlvbiB1bGxhbWNvIGxhYm9yaXMgbmlzaSB1dCBhbGlxdWlwIGV4IGVhIGNvbW1vZG8gY29uc2VxdWF0LiBEdWlzIGF1dGUgaXJ1cmUgZG9sb3IgaW4gcmVwcmVoZW5kZXJpdCBpbiB2b2x1cHRhdGUgdmVsaXQgZXNzZSBjaWxsdW0gZG9sb3JlIGV1IGZ1Z2lhdCBudWxsYSBwYXJpYXR1ci4gRXhjZXB0ZXVyIHNpbnQgb2NjYWVjYXQgY3VwaWRhdGF0IG5vbiBwcm9pZGVudCwgc3VudCBpbiBjdWxwYSBxdWkgb2ZmaWNpYSBkZXNlcnVudCBtb2xsaXQgYW5pbSBpZCBlc3QgbGFib3J1bS4=";
        Map<String, String> map8 = ICalendarUtilities.propertyLineToParameterMap3(s8);

//        map.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map2.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map3.entrySet().stream().forEach(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map4.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map4b.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map4c.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map5.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map6.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
//        map7.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue()));
        map8.entrySet().stream().forEachOrdered(e -> System.out.println("key=" + e.getKey() + " value=" + e.getValue())); // WRONG - VALUE IN WRONG PLACE
    }
}
