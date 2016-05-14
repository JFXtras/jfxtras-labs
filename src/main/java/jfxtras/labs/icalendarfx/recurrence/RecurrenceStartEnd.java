package jfxtras.labs.icalendarfx.recurrence;

import java.time.temporal.Temporal;

public class RecurrenceStartEnd extends RecurrenceStart
{
    @Override
    public RecurrenceStartEnd withDateTimeStart(Temporal dateTimeStart) { setDateTimeStart(dateTimeStart); return this; }
    
    public Temporal getDateTimeEnd() { return dateTimeEnd; }
    private Temporal dateTimeEnd;
    public void setDateTimeEnd(Temporal dateTimeEnd) { this.dateTimeEnd = dateTimeEnd; }
    public RecurrenceStartEnd withDateTimeEnd(Temporal dateTimeEnd) { this.dateTimeEnd = dateTimeEnd; return this; }
}
