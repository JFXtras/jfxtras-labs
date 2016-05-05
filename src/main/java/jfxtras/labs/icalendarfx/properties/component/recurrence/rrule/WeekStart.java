package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.DayOfWeek;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ICalendarDayOfWeek;

/**
 * Week Start
 * WKST:
 * RFC 5545 iCalendar 3.3.10, page 42
 * 
 * The WKST rule part specifies the day on which the workweek starts.
 * Valid values are MO, TU, WE, TH, FR, SA, and SU.  This is
 * significant when a WEEKLY "RRULE" has an interval greater than 1,
 * and a BYDAY rule part is specified.  This is also significant when
 * in a YEARLY "RRULE" when a BYWEEKNO rule part is specified.  The
 * default value is MO.
 */
public class WeekStart extends RRuleElementBase<DayOfWeek, WeekStart>
{

    @Override
    public String toContent()
    {
        return ";" + elementType().toString() + "=" + getValue().toString().substring(0, 2);
    }

    @Override
    public void parseContent(String content)
    {
        setValue(ICalendarDayOfWeek.valueOf(content).getDayOfWeek());
    }
}
