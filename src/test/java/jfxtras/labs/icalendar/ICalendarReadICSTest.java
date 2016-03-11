package jfxtras.labs.icalendar;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import javafx.util.Callback;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.VEventImpl;

public class ICalendarReadICSTest
{
    @Test
    public void canReadYahoo()
    {
      Callback<String, VEvent<?,?>> makeVEvent = (s) -> VEventImpl.parse(s, ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
      try
      {
          URI file = new URI("file:/home/david/Downloads/Calendar_David_Bal11.ics");
          ICalendarUtilities.parseICS(file, makeVEvent);
      } catch (URISyntaxException e)
      {
          e.printStackTrace();
      }
      System.exit(0);

    }
}
