package jfxtras.labs.icalendarfx.components.editors;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

public interface LocateEditable<T, U extends VComponentDisplayable<U>> extends DisplayEditable<T, U>
{
    Temporal getEndRecurrence();
    void setEndRecurrence(Temporal endRecurrence);
    default T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
}
