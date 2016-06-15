package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVTodo;

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
            List<String> categories)
    {
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
        vComponentOriginalCopy = new VTodo(vComponent);
    }

    @Override
    Collection<VTodo> callRevisor()
    {
        ReviserVTodo reviser = ((ReviserVTodo) vComponentOriginalCopy.newRevisor())
                .withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
                .withEndRecurrence(editDescriptiveVBox.endNewRecurrence)
                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
                .withStartRecurrence(editDescriptiveVBox.startRecurrenceProperty.get())
                .withVComponentEdited(vComponent)
                .withVComponentOriginal(vComponentOriginalCopy);
        return reviser.revise();
    }
}