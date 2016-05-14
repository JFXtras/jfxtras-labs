package jfxtras.labs.icalendarfx.recurrence;

import java.time.temporal.Temporal;

public class RecurrenceStart
{
    public Temporal getDateTimeStart() { return dateTimeStart; }
    private Temporal dateTimeStart;
    public void setDateTimeStart(Temporal dateTimeStart) { this.dateTimeStart = dateTimeStart; }
    public RecurrenceStart withDateTimeStart(Temporal dateTimeStart) { setDateTimeStart(dateTimeStart); return this; }
}
