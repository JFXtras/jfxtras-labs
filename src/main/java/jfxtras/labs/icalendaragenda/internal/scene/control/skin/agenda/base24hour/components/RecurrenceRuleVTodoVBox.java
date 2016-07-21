package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VTodo;

public class RecurrenceRuleVTodoVBox extends RecurrenceRuleVBox<VTodo>
{
    @Override
    void synchStartDatePickerAndComponent(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDatePickerAndComponent(oldValue, newValue);
        Period shift = Period.between(oldValue, newValue);
        Temporal newEnd = vComponent.getDateTimeDue().getValue().plus(shift);
        vComponent.setDateTimeDue(newEnd);
    }
}
