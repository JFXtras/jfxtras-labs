package jfxtras.labs.icalendarfx.itip;

public class CancelRecurrenceTest
{
    
    // TODO - BELOW TEST ISN'T REALLY DELETE A RECURRENCE INSTANCE
   // IT JUST DELETES ONE ISNTANCE - I WANT A TEST THAT DELETES A SPECIAL INSTANCE 
    
//    @Test // makes sure when recurrence deleted the parent gets an EXDATE
//    public void canDeleteRecurrence()
//    {
//        VCalendar mainVCalendar = new VCalendar();
//        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
//        
//        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
//        vComponents.add(vComponentOriginal);
//        
//        String iTIPMessage =
//                "BEGIN:VCALENDAR" + System.lineSeparator() +
//                "METHOD:CANCEL" + System.lineSeparator() +
//                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
//                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160517T083000" + System.lineSeparator() +
//                "DTEND:20160517T093000" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:recurrence summary" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
////                "END:VCALENDAR" + System.lineSeparator() +
////                "BEGIN:VCALENDAR" + System.lineSeparator() +
////                "METHOD:REQUEST" + System.lineSeparator() +
////                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
////                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
////                "BEGIN:VEVENT" + System.lineSeparator() +
////                "CATEGORIES:group05" + System.lineSeparator() +
////                "DTSTART:20151109T100000" + System.lineSeparator() +
////                "DTEND:20151109T110000" + System.lineSeparator() +
////                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
////                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
////                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
////                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
////                "RRULE:FREQ=DAILY" + System.lineSeparator() +
////                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
////                "EXDATE:20160517T100000" + System.lineSeparator() +
////                "SEQUENCE:2" + System.lineSeparator() +
////                "END:VEVENT" + System.lineSeparator() +
//                "END:VCALENDAR";
//        mainVCalendar.processITIPMessage(iTIPMessage);
//        
//        assertEquals(1, vComponents.size());
//        VEvent myComponent1 = vComponents.get(0);
//        
//        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
//                .withExceptionDates(LocalDateTime.of(2016, 5, 17, 10, 0))
//                .withSequence(1);
//        assertEquals(expectedVComponent, myComponent1);
//    }
}
