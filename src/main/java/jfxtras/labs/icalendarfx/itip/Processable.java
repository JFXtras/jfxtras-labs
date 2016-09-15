package jfxtras.labs.icalendarfx.itip;

import jfxtras.labs.icalendarfx.VCalendar;

public interface Processable
{
    void process(VCalendar mainVCalendar, VCalendar iTIPMessage);
}
