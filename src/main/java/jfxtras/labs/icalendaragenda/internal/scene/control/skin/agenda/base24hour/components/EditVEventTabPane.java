package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class EditVEventTabPane extends EditLocatableTabPane<VEvent>
{
    public EditVEventTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVEventVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new RecurrenceRuleVEventVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VEvent vComponent,
            List<VEvent> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
        vComponentOriginalCopy = new VEvent(vComponent);
    }

}
