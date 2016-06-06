package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVTodoPopupStage extends EditComponentPopupStage<VTodo>
{
    public EditVTodoPopupStage()
    {
        super(new EditVTodoTabPane());
    }

//    public EditVTodoPopupStage(VEvent vComponent, ObservableList<VEvent> vEvents)
//    {
//        super(new EditVEventTabPane());
//    }

    public EditVTodoPopupStage(
            VTodo vComponent,
            List<VTodo> vTodos,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super(new EditVTodoTabPane());
        getEditDisplayableTabPane().setupData(vComponent, vTodos, startRecurrence, endRecurrence, categories);
    }
}
 
