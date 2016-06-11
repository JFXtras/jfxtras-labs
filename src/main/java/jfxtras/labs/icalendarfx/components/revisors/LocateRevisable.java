package jfxtras.labs.icalendarfx.components.revisors;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

public interface LocateRevisable<T, U extends VComponentDisplayable<U>> extends DisplayRevisable<T, U>
{
    Temporal getEndRecurrence();
    void setEndRecurrence(Temporal endRecurrence);
    default T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
}
