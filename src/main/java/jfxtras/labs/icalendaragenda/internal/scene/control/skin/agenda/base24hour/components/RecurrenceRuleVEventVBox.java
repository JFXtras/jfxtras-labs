package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VEvent;

public class RecurrenceRuleVEventVBox extends RecurrenceRuleVBox<VEvent>
{
    public RecurrenceRuleVEventVBox( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("RecurrenceRule.fxml"), this);
    }
    
    @Override
    void synchStartDatePickerAndComponent(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDatePickerAndComponent(oldValue, newValue);
        Period shift = Period.between(oldValue, newValue);
        Temporal newEnd = vComponent.getDateTimeEnd().getValue().plus(shift);
        vComponent.setDateTimeEnd(newEnd);
    }
}
