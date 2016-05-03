package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

/**
 * For single DESCRIPTION property
 * Note: Not for VJournal - allows multiple descriptions
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see VEvent
 * @see VTodoOld
 * @see VAlarm
 */
public interface VComponentDescribable2<T> extends VComponentDescribable<T>
{
    /**
     * DESCRIPTION:
     * RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     */
    public ObjectProperty<Description> descriptionProperty();
    Description getDescription();
    default void setDescription(String description) { setDescription(Description.parse(description)); }
    default void setDescription(Description description) { descriptionProperty().set(description); }
    default T withDescription(Description description)
    {
        if (getDescription() == null)
        {
            setDescription(description);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDescription(String description)
    {
        if (getDescription() == null)
        {
            setDescription(description);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
}
