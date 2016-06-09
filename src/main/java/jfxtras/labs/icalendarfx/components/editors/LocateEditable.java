package jfxtras.labs.icalendarfx.components.editors;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

public interface LocateEditable<T extends VComponentDisplayable<T>, U, V extends Temporal> extends DisplayEditable<T, U, V>
{
    V getEndRecurrence();
    void setEndRecurrence(V endRecurrence);
    default T withEndRecurrence(V endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
}
