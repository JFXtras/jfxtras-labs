package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import javafx.fxml.FXML;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.revisors.SimpleRevisorFactory;

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
        super.handleSaveButton();        
        Object[] params = new Object[] {
                vComponentOriginalCopy,
                EditChoiceDialog.EDIT_DIALOG_CALLBACK,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                vComponent,
                vComponentOriginalCopy
        };
        boolean result = SimpleRevisorFactory.newReviser(vComponent, params).revise();
        isFinished.set(result);
    }
    
    @Override
    void removeEmptyProperties()
    {
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponent.setDescriptions(null);
        }
    }
    
    @Override
    public void setupData(
            VJournal vComponent,
            List<VJournal> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super.setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
        vComponentOriginalCopy = new VJournal(vComponent);
    }
}
