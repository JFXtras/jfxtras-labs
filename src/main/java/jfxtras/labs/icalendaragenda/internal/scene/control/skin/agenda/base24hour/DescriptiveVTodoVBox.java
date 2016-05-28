package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DescriptiveVTodoVBox extends DescriptiveLocatableVBox<VTodo>
{
    public DescriptiveVTodoVBox()
    {
        super();
        endLabel.setText( getResources().getString("due.time") );
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VTodo vComponent,
            List<VTodo> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        vComponentOriginalCopy = new VTodo(vComponent);
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
    }
}
