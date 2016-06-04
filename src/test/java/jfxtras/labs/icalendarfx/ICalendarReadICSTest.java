package jfxtras.labs.icalendarfx;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.utilities.VCalendarUtilities;

public class ICalendarReadICSTest
{
    @Test
    @Ignore
    public void canReadYahooICS()
    {
        VCalendar vCalendar = new VCalendar();
//                .withVEventCallback((s) -> VEventNew.parse(s));
        
        String fileName = "Yahoo_Sample_Calendar.ics";
        URL url = getClass().getResource(fileName);
        Path path = Paths.get(url.getFile());
        VCalendarUtilities.parseICalendarFile(path, vCalendar);
        
//        assertEquals(7, vCalendar.vEvents().size());
        
        // TODO - TEST EQUALITY ON THE VCOMPONENTS
    }
}
