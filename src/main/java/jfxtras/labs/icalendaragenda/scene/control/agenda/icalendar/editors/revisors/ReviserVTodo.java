package jfxtras.labs.icalendaragenda.scene.control.agenda.icalendar.editors.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeDue;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Reviser for {@link VTodo}
 * 
 * @author David Bal
 *
 */
public class ReviserVTodo extends ReviserLocatable<ReviserVTodo, VTodo>
{
    public ReviserVTodo(VTodo component)
    {
        super(component);
    }
    
    @Override
    public void adjustDateTime(VTodo vComponentEditedCopy)
    {
        super.adjustDateTime(vComponentEditedCopy);
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (vComponentEditedCopy.getDuration() != null)
        {
            vComponentEditedCopy.setDuration(duration);
        } else if (vComponentEditedCopy.getDateTimeDue() != null)
        {
            Temporal due = vComponentEditedCopy.getDateTimeStart().getValue().plus(duration);
            vComponentEditedCopy.setDateTimeDue(new DateTimeDue(due));
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
}
