package jfxtras.labs.icalendarfx.method;

import jfxtras.labs.icalendarfx.VCalendar;

public interface Processable
{
    VCalendar process(VCalendar mainVCalendar, VCalendar inputVCalendar);
}
