package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;

public class EditVEventTabPane extends EditLocatableTabPane<VEvent>
{
    public EditVEventTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVEventVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new RecurrenceRuleVEventVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
            VEvent vComponent,
            List<VEvent> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        vComponentOriginalCopy = new VEvent(vComponent);
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
    }

//    @Override
//    Collection<VEvent> callRevisor()
//    {
//        return ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginalCopy))
//                .withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
//                .withEndRecurrence(editDescriptiveVBox.endNewRecurrence)
//                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
//                .withStartRecurrence(editDescriptiveVBox.startRecurrenceProperty.get())
//                .withVComponentEdited(vComponent)
//                .withVComponentOriginal(vComponentOriginalCopy)
//                .revise();
//    }
    
//    @Override
//    VEvent callDeleter()
//    {
////        SimpleBehaviorFactory.newBehavior(vComponent)
////            .callDeleter(agenda, appointment);
//        return new DeleterVEvent(vComponent)
//                .withDialogCallback(DeleteChoiceDialog.DELETE_DIALOG_CALLBACK)
//                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
//                .delete();
//    }
}
