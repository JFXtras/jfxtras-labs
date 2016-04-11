package jfxtras.labs.icalendar.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public interface VEventNew<I> extends VComponentLocatable<I>
{
    /**
     * DTEND, Date-Time End.
     * RFC 5545 iCalendar 3.8.2.2 page 95
     * Specifies the date and time that a calendar component ends.
     * Can't be used if DURATION is used.  Must be one or the other.
     * Must be same Temporal type as dateTimeStart (DTSTART)
     * 
     * 
     */
    Temporal getDateTimeEnd();
    ObjectProperty<Temporal> dateTimeEndProperty();
    void setDateTimeEnd(Temporal dtEnd);
    
    /**
     * TRANSP: Time Transparency
     * RFC 5545 iCalendar 3.8.2.7. page 101
     * This property defines whether or not an event is transparent to busy time searches.
     * "OPAQUE"- Blocks or opaque on busy time searches.
     * "TRANSPARENT" - Transparent on busy time searches.
     * Default value is OPAQUE
     * 
     * Example:
     * CLASS:PUBLIC
     */
    String getTimeTransparency();
    StringProperty timeTransparencyProperty();
    void setTimeTransparency(String transparency);

}
