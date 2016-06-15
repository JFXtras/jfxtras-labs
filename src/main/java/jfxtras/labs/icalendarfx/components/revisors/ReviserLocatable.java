package jfxtras.labs.icalendarfx.components.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class ReviserLocatable<T, U extends VComponentLocatable<U>> extends ReviserDisplayable<T, U>
{
    public ReviserLocatable(U component)
    {
        super(component);
    }

    public Temporal getEndRecurrence() { return endRecurrence; }
    private Temporal endRecurrence;
    public void setEndRecurrence(Temporal startRecurrence) { this.endRecurrence = startRecurrence; }
    public T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
    
    @Override
    boolean isValid()
    {
        if (getEndRecurrence() == null)
        {
            System.out.println("endRecurrence must not be null");
            return false;
        }
        return super.isValid();
    }
    
    @Override
    void becomeNonRecurring()
    {
        super.becomeNonRecurring();
        if (getVComponentOriginal().getRecurrenceRule() != null)
        { // RRULE was removed, update DTSTART, DTEND or DURATION
            getVComponentEdited().setDateTimeStart(getStartRecurrence());
            if (getVComponentEdited().getDuration() != null)
            {
                TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
                getVComponentEdited().setDuration(duration);
            }
        }
    }
}
