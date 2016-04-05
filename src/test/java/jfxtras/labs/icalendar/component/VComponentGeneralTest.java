package jfxtras.labs.icalendar.component;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.components.VComponentPrimaryBase;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;

public class VComponentGeneralTest
{
    @Test
    public void canParseAttendee1() throws URISyntaxException
    {
        VComponentPrimaryBase v = new VComponentPrimaryBase();
//        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        DTStartLocalDate dateTimeStart = new DTStartLocalDate("DTSTART;VALUE=DATE:20160322");
        v.setDateTimeStart(dateTimeStart);
        System.out.println(v.getDateTimeStart().getTimeZoneIdentifier());
        System.out.println(v.getDateTimeStart().propertyType().name());
    }
}
