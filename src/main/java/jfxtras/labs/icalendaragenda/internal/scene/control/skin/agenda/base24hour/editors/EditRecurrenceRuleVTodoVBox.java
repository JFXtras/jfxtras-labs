package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/**
 * VBox containing controls to edit the {@link RecurrenceRule} in a {@link VTodo}.
 * 
 * @author David Bal
 */
public class EditRecurrenceRuleVTodoVBox extends EditRecurrenceRuleVBox<VTodo>
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
