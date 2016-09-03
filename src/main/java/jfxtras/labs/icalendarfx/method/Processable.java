package jfxtras.labs.icalendarfx.method;

import jfxtras.labs.icalendarfx.VCalendar;

public interface Processable
{
    void process(VCalendar mainVCalendar, VCalendar inputVCalendar);
}
