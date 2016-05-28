package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

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
            List<VJournal> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        vComponentOriginalCopy = new VJournal(vComponent);
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
    }
}
