package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

/**
 * Creates recurrences from VComponents.
 * 
 * @author David Bal
 *
 * @param <R> - type of recurrences
 */
public interface RecurrenceFactory<R>
{
    /** Property for start of range to make recurrences */
    ObjectProperty<LocalDateTime> startRangeProperty();
    /** set start of range to make recurrences */
    void setStartRange(LocalDateTime startRange);
    /** get start of range to make recurrences */
    LocalDateTime getStartRange();

    /** Property for end of range to make recurrences */
    ObjectProperty<LocalDateTime> endRangeProperty();
    /** set end of range to make recurrences */
    void setEndRange(LocalDateTime endRange);
    /** get end of range to make recurrences */
    LocalDateTime getEndRange();
    
    /**
     * Makes recurrences for VEVENT, VTODO, and VJOURNAL
     * Appointments are made between displayed range
     * 
     * @param vComponent - calendar component
     * @return created appointments
     */
    List<R> makeRecurrences(VComponentDisplayable<?> vComponent);
}
