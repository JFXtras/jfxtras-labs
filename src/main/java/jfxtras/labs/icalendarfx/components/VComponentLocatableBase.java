package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public abstract class VComponentLocatableBase<T, I> extends VComponentDisplayableBase<T, I> implements VComponentLocatable<T,I>
{
    /**
     * DESCRIPTION
     * RFC 5545 iCalendar 3.8.1.5. page 84
     * 
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * 
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *
     * Note: Only VJournal allows multiple instances of DESCRIPTION
     */
    @Override public ObjectProperty<Description> descriptionProperty()
    {
        if (description == null)
        {
            description = new SimpleObjectProperty<>(this, PropertyEnum.DESCRIPTION.toString());
        }
        return description;
    }
    private ObjectProperty<Description> description;
    
    /*
     * CONSTRUCTORS
     */
    public VComponentLocatableBase() { }
    
    public VComponentLocatableBase(String contentLines)
    {
        super(contentLines);
    }
}
