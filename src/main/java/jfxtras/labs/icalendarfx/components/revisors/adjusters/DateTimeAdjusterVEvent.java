package jfxtras.labs.icalendarfx.components.revisors.adjusters;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VEvent;

public class DateTimeAdjusterVEvent extends DateTimeAdjusterLocatableBase<DateTimeAdjusterVEvent, VEvent>
{
    public DateTimeAdjusterVEvent(VEvent vComponent,
            Temporal startRecurrence,
            Temporal startOriginalRecurrence)
    {
        super(vComponent, startRecurrence, startOriginalRecurrence);
    }
}
