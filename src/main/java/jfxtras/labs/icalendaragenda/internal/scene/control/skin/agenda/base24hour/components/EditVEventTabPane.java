package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
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
            VEvent vComponent,
            List<VEvent> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<AppointmentGroup> appointmentGroups)
    {
        vComponentOriginalCopy = new VEvent(vComponent);
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, appointmentGroups);
    }

}
