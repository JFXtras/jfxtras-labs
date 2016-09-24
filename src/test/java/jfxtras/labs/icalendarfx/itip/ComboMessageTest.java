package jfxtras.labs.icalendarfx.itip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public class ComboMessageTest
{
    @Test
    public void canEditThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        mainVCalendar.getVEvents().add(vevent);

       String iTIPMessage =
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:REQUEST" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151109T100000" + System.lineSeparator() +
               "DTEND:20151109T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:Daily1 Summary" + System.lineSeparator() +
               "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
               "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "RRULE:FREQ=DAILY;UNTIL=20151110T180000Z" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "SEQUENCE:1" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR" + System.lineSeparator() +
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:PUBLISH" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151111T100000" + System.lineSeparator() +
               "DTEND:20151111T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:new summary" + System.lineSeparator() +
               "DTSTAMP:20160914T180627Z" + System.lineSeparator() +
               "UID:20160914T110627-0jfxtras.org" + System.lineSeparator() +
               "RRULE:FREQ=DAILY" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR";
//       System.out.println(iTIPMessage);
       mainVCalendar.processITIPMessage(iTIPMessage);
       
       // verify VComponent changes
       assertEquals(2, mainVCalendar.getVEvents().size());

       VEvent v0 = mainVCalendar.getVEvents().get(0);
       VEvent expectedV0 = ICalendarStaticComponents.getDaily1();
       ZonedDateTime until = ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
       expectedV0.getRecurrenceRule().getValue()
               .setUntil(until);
       assertEquals(expectedV0, v0);

       VEvent v1 = mainVCalendar.getVEvents().get(1);
       VEvent expectedV1 = ICalendarStaticComponents.getDaily1()
               .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
               .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 11, 0))
               .withDateTimeStamp(v1.getDateTimeStamp()) // time stamp is time-based so copy it to guarantee equality.
               .withUniqueIdentifier(v1.getUniqueIdentifier()) // uid is time-based so copy it to guarantee equality.
               .withSummary("new summary")
               .withRelatedTo("20150110T080000-004@jfxtras.org")
               .withSequence(1);
      assertEquals(expectedV1, v1);

//       // verify Appointment changes      
//       {
//           List<String> summaries = vComponentFactory.makeRecurrences(v0)
//                   .stream()
//                   .map(a -> a.getSummary())
//                   .collect(Collectors.toList());
//           List<String> summaries2 = vComponentFactory.makeRecurrences(v1)
//                   .stream()
//                   .map(a -> a.getSummary())
//                   .collect(Collectors.toList());
//           List<String> summariesAll = new ArrayList<>(summaries);
//           summariesAll.addAll(summaries2);
//           List<String> expectedSummaries = new ArrayList<>(Arrays.asList(
//                   "Daily1 Summary"
//                 , "Daily1 Summary"
//                 , "new summary"
//                 , "new summary"
//                 , "new summary"
//                 , "new summary"
//                   ));
//           assertEquals(expectedSummaries, summariesAll);
//       }
    }
}
