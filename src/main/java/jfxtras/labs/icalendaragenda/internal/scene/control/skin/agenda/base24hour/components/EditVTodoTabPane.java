package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class EditVTodoTabPane extends EditLocatableTabPane<VTodo>
{
    public EditVTodoTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVTodoVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new RecurrenceRuleVTodoVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VTodo vComponent,
            List<VTodo> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, appointmentGroups);
        vComponentOriginalCopy = new VTodo(vComponent);
    }
}