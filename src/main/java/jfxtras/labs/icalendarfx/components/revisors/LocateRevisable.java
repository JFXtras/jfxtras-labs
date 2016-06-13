package jfxtras.labs.icalendarfx.components.revisors;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VComponentLocatable;

public interface LocateRevisable<T, U extends VComponentLocatable<U>> extends DisplayRevisable<T, U>
{
    Temporal getEndRecurrence();
    void setEndRecurrence(Temporal endRecurrence);
    default T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
}
