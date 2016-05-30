package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class EditVTodoTabPane extends EditLocatableTabPane<VTodo>
{
    public EditVTodoTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVTodoVBox();
        getDescriptiveAnchorPane().getChildren().add(editDescriptiveVBox);
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VTodo vComponent,
            List<VTodo> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
        vComponentOriginalCopy = new VTodo(vComponent);
    }
}