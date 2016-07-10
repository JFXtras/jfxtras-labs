package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

/**
 * Creates VComponent calendar objects from a recurrence and makes recurrences from VComponents.
 * 
 * @author David Bal
 *
 * @param <R> - type of recurrences
 */
public interface VComponentFactory<R>
{
    /** Create VComponent from recurrence.  The recurrence is tested to determine which type of VComponent should
     * be created, such as VEVENT or VTODO
     * 
     * @param recurrence - recurrence as basis for VComponent
     * @return - new VComponent
     */
    VComponentDisplayable<?> createVComponent(R recurrence);
    
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
    
    // TODO - DIVIDE INTO TWO FACTORIES - ONE MAKES COMPONENTS, ONE MAKES RECURRENCES
    // ONE CLASS  - ONE RESPONSIBILITY
    /**
     * Makes recurrences for VEVENT, VTODO, and VJOURNAL
     * Appointments are made between displayed range
     * 
     * @param vComponent - calendar component
     * @return created appointments
     */
    List<R> makeRecurrences(VComponentDisplayable<?> vComponent);

}
