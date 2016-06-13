package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVTodoPopupScene extends EditComponentPopupScene
{
    public EditVTodoPopupScene()
    {
        super(new EditVTodoTabPane());
    }

//    public EditVTodoPopupStage(VEvent vComponent, ObservableList<VEvent> vEvents)
//    {
//        super(new EditVEventTabPane());
//    }

    public EditVTodoPopupScene(
            VTodo vComponent,
            List<VTodo> vTodos,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super(new EditVTodoTabPane());
        ((EditVTodoTabPane) getRoot()).setupData(vComponent, vTodos, startRecurrence, endRecurrence, categories);
    }
}
 
