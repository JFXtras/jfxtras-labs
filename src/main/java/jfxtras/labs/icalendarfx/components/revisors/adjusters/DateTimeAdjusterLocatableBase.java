package jfxtras.labs.icalendarfx.components.revisors.adjusters;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VComponentLocatable;

public abstract class DateTimeAdjusterLocatableBase<T, U extends VComponentLocatable<U>> extends DateTimeAdjusterDisplayableBase<T, U>
{
    public Temporal getEndRecurrence() { return endRecurrence; }
    private Temporal endRecurrence;
    public void setEndRecurrence(Temporal startRecurrence) { this.endRecurrence = startRecurrence; }
    public T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }

    public DateTimeAdjusterLocatableBase(U vComponent,
            Temporal startRecurrence,
            Temporal startOriginalRecurrence)
    {
        super(vComponent, startRecurrence, startOriginalRecurrence);
    }

}
