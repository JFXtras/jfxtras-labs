package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import javafx.fxml.FXML;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.revisors.VJournalReviser;

public class EditVJournalTabPane extends EditDisplayableTabPane<VJournal, DescriptiveVJournalVBox>
{
    public EditVJournalTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVJournalVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new RecurrenceRuleVJournalVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    @FXML void handleSaveButton()
    {
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponent.setDescriptions(null);
        }
        super.handleSaveButton();
        Collection<VJournal> newVComponents = callRevisor();
//        Collection<VJournal> newVComponents = ReviseComponentHelper.handleEdit(
//                vComponentOriginalCopy,
//                vComponent,
//                editDescriptiveVBox.startOriginalRecurrence,
//                editDescriptiveVBox.startRecurrenceProperty.get(),
//                null,
////                editDescriptiveVBox.shiftAmount,
//                EditChoiceDialog.EDIT_DIALOG_CALLBACK
//                );
        vComponents.remove(vComponent);
        vComponents.addAll(newVComponents);
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VJournal vComponent,
            List<VJournal> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
        vComponentOriginalCopy = new VJournal(vComponent);
    }

    @Override
    Collection<VJournal> callRevisor()
    {
        VJournalReviser reviser = ((VJournalReviser) vComponentOriginalCopy.newRevisor())
                .withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
                .withStartRecurrence(editDescriptiveVBox.startRecurrenceProperty.get())
                .withVComponentEdited(vComponent)
                .withVComponentOriginal(vComponentOriginalCopy);
        return reviser.revise();
    }
}
