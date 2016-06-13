package jfxtras.labs.icalendarfx.components.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public class VTodoReviser extends LocatableReviser<VTodoReviser, VTodo>
{
    public VTodoReviser(VTodo component)
    {
        super(component);
    }
    
    @Override
    public void adjustDateTime()
    {
        super.adjustDateTime();
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (getVComponentEdited().getDuration() != null)
        {
            getVComponentEdited().setDuration(duration);
        } else if (getVComponentEdited().getDateTimeDue() != null)
        {
            Temporal dtend = getVComponentEdited().getDateTimeStart().getValue().plus(duration);
            getVComponentEdited().setDateTimeDue(dtend);
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
}
