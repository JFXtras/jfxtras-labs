package jfxtras.labs.icalendar;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.components.VCalendarUtilities;
import jfxtras.labs.icalendar.mocks.VEventMock;

public class ICalendarReadICSTest
{
    @Test
    public void canReadYahoo()
    {
        VCalendar vCalendar = new VCalendar()
                .withVEventCallback((s) -> VEventMock.parse(s));
//      Callback<String, VEvent<?,?>> makeVEvent = (s) -> VEventImpl.parse(s, ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
      try
      {
          URI file = new URI("file:/home/david/Downloads/Calendar_David_Bal11.ics");
          VCalendarUtilities.parseICalendarFile(file, vCalendar);
      } catch (URISyntaxException e)
      {
          e.printStackTrace();
      }
    }
}
