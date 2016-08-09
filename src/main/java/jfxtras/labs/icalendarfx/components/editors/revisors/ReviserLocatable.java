package jfxtras.labs.icalendarfx.components.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Handles revising a {@link VComponentLocatable}
 * 
 * @author David Bal
 *
 * @param <T> concrete implementation of this class
 * @param <U> concrete {@link VComponentLocatable} class
 */
public abstract class ReviserLocatable<T, U extends VComponentLocatable<U>> extends ReviserDisplayable<T, U>
{
    public ReviserLocatable(U component)
    {
        super(component);
    }

    /*
     * END RECURRENCE - NEW VALUE
     */
    /** Gets the value of the end of the selected recurrence after changes */
    public Temporal getEndRecurrence() { return endRecurrence; }
    private Temporal endRecurrence;
    /** Sets the value of the end of the selected recurrence after changes */
    public void setEndRecurrence(Temporal startRecurrence) { this.endRecurrence = startRecurrence; }
    /**
     * Sets the value of the end of the selected recurrence after changes
     * 
     * @return - this class for chaining
     */
    public T withEndRecurrence(Temporal endRecurrence) { setEndRecurrence(endRecurrence); return (T) this; }
    
    @Override
    boolean isValid()
    {
        if (getEndRecurrence() == null)
        {
//            System.out.println("endRecurrence must not be null");
            return false;
        }
        return super.isValid();
    }
    
    @Override
    void adjustStartAndEnd(U vComponentEditedCopy, U vComponentOriginalCopy)
    {
        // Adjust start and end - set recurrence temporal as start
        vComponentEditedCopy.setDateTimeStart(new DateTimeStart(getStartRecurrence()));
        vComponentEditedCopy.setEndOrDuration(getStartRecurrence(), getEndRecurrence());
    }
    
    @Override
    void becomeNonRecurring(U vComponentEditedCopy)
    {
        super.becomeNonRecurring(vComponentEditedCopy);
        if (getVComponentOriginal().getRecurrenceRule() != null)
        { // RRULE was removed, update DTSTART, DTEND or DURATION
            getVComponentEdited().setDateTimeStart(new DateTimeStart(getStartRecurrence()));
            if (getVComponentEdited().getDuration() != null)
            {
                TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
                getVComponentEdited().setDuration(duration);
            }
        }
    }
    
    @Override
    public List<PropertyType> dialogRequiredProperties()
    {
        List<PropertyType> list = super.dialogRequiredProperties();
        list.add(PropertyType.DATE_TIME_END);
        return list;
    }
}
