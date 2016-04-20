package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;

public interface VComponentDateTimeEnd<T> extends VComponentNew<T>
{
    /**
     * DTEND, Date-Time End.
     * RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * Can't be used if DURATION is used.  Must be one or the other.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    Temporal getDateTimeEnd();
    ObjectProperty<Temporal> dateTimeEndProperty();
    void setDateTimeEnd(Temporal dtEnd);
}
