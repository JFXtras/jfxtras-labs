package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VTodo;

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

//    @Override
//    Collection<VTodo> callRevisor()
//    {
//        return ((ReviserVTodo) SimpleRevisorFactory.newReviser(vComponentOriginalCopy))
//                .withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
//                .withEndRecurrence(editDescriptiveVBox.endNewRecurrence)
//                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
//                .withStartRecurrence(editDescriptiveVBox.startRecurrenceProperty.get())
//                .withVComponentEdited(vComponent)
//                .withVComponentOriginal(vComponentOriginalCopy)
//                .revise();
//    }
    
//    @Override
//    VTodo callDeleter()
//    {
////        return new DeleterVTodo(vComponent)
////                .withDialogCallback(DeleteChoiceDialog.DELETE_DIALOG_CALLBACK)
////                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
////                .delete();
//    }
}