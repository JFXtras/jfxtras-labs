package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class ReadICSFileTest
{
    @Test
    public void canReadICSFile1() throws IOException
    {
        String fileName = "Yahoo_Sample_Calendar.ics";
        URL url = getClass().getResource(fileName);
        Path icsFilePath = Paths.get(url.getFile());
        VCalendar vCalendar = VCalendar.parseICalendarFile(icsFilePath);
        assertEquals(8584, vCalendar.toContent().length());
        assertEquals(7, vCalendar.getVEvents().size());
        assertEquals(1, vCalendar.getVTimeZones().size());
        int subcomponents = vCalendar.getVTimeZones().get(0).getStandardOrDaylight().size();
        assertEquals(9, subcomponents);
    }
    
    @Test
    public void canReadICSFile2() throws IOException
    {
        String fileName = "mathBirthdays.ics";       
        URL url = getClass().getResource(fileName);
        Path icsFilePath = Paths.get(url.getFile());
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> expectedLines = ICalendarUtilities.unfoldLines(br.lines().iterator());
        VCalendar vCalendar = VCalendar.parseICalendarFile(icsFilePath);
        List<String> lines = ICalendarUtilities.unfoldLines(vCalendar.toContent());
        assertEquals(expectedLines, lines);
    }
}
