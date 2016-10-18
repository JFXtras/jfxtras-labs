package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/**
 * VBox containing controls to edit the {@link RecurrenceRule} in a {@link VEvent}.
 * 
 * @author David Bal
 */
public class EditRecurrenceRuleVEventVBox extends EditRecurrenceRuleVBox<VEvent>
{
    @Override
    void synchStartDatePickerAndComponent(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDatePickerAndComponent(oldValue, newValue);
        Period shift = Period.between(oldValue, newValue);
        Temporal newEnd = vComponent.getDateTimeEnd().getValue().plus(shift);
        vComponent.setDateTimeEnd(newEnd);
    }
}
