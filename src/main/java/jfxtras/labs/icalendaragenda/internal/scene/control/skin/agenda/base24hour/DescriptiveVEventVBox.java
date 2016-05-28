package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DescriptiveVEventVBox extends DescriptiveLocatableVBox<VEvent>
{
    public DescriptiveVEventVBox()
    {
        super();
        endLabel.setText(getResources().getString("end.time"));
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VEvent vComponent,
            List<VEvent> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        vComponentOriginalCopy = new VEvent(vComponent);
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
    }
}
