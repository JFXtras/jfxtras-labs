package jfxtras.labs.icalendarfx.calendar;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public class GeneralCalendarTest extends ICalendarTestAbstract2
{
    @Test
    public void canEscapeTest()
    {
        String contentLine = "DESCRIPTION:a dog\\nran\\, far\\;\\naway \\\\\\\\1";
        
        String expectedContent = 
                "BEGIN:VTIMEZONE" + System.lineSeparator() +
                "TZID:America/New_York" + System.lineSeparator() +
                "LAST-MODIFIED:20050809T050000Z" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19670430T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:STANDARD" + System.lineSeparator() +
                "DTSTART:19671029T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0400" + System.lineSeparator() +
                "TZOFFSETTO:-0500" + System.lineSeparator() +
                "TZNAME:EST" + System.lineSeparator() +
                "END:STANDARD" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19740106T020000" + System.lineSeparator() +
                "RDATE:19750223T020000" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19760425T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19870405T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:20070311T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:STANDARD" + System.lineSeparator() +
                "DTSTART:20071104T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU" + System.lineSeparator() +
                "TZOFFSETFROM:-0400" + System.lineSeparator() +
                "TZOFFSETTO:-0500" + System.lineSeparator() +
                "TZNAME:EST" + System.lineSeparator() +
                "END:STANDARD" + System.lineSeparator() +
                "END:VTIMEZONE";
            VTimeZone vTimeZone = VTimeZone.parse(expectedContent);
        
        VCalendar c = new VCalendar()
                .withVEvents(getYearly1())
                .withCalendarScale(new CalendarScale())
                .withCalendarScale(new CalendarScale())
                .withVersion(new Version())
                .withProductIdentifier(new ProductIdentifier())
                .withVTimeZones(vTimeZone)
                .withVTodos(new VTodo()
                        .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                        .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                        .withPercentComplete(35))
                .withVEvents(getMonthly6());
        System.out.println(c.toContentLines());
    }
}
