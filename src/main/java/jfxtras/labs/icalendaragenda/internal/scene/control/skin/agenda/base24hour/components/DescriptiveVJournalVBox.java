package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.List;

import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DescriptiveVJournalVBox extends DescriptiveVBox<VJournal>
{
    public DescriptiveVJournalVBox()
    {
        super();
        endLabel.setVisible(false);
        endLabel = null;
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VJournal vComponent,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, appointmentGroups);

        // Journal supports multiple descriptions, but this control only supports one description
        if (vComponent.getDescriptions() == null)
        {
            vComponent.withDescriptions("");
        }
        descriptionTextArea.textProperty().bind(vComponent.getDescriptions().get(0).valueProperty());
    }
}
