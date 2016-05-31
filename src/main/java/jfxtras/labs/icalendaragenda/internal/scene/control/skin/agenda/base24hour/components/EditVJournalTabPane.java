package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.Collection;
import java.util.List;

import javafx.fxml.FXML;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

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
        
        Collection<VJournal> newVComponents = ReviseComponentHelper.handleEdit(
                vComponent,
                vComponentOriginalCopy,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                null,
                EditChoiceDialog.EDIT_DIALOG_CALLBACK
                );
        vComponents.remove(vComponent);
        vComponents.addAll(newVComponents);
    }
    
    @Override
    public void setupData(
            Appointment appointment,
            VJournal vComponent,
            List<VJournal> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
        vComponentOriginalCopy = new VJournal(vComponent);
    }
}
