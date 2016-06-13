package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.VEventReviser;

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

    @Override
    Collection<VEvent> callRevisor()
    {
        VEventReviser reviser = ((VEventReviser) vComponentOriginalCopy.newRevisor())
                .withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
                .withEndRecurrence(editDescriptiveVBox.endNewRecurrence)
                .withStartOriginalRecurrence(editDescriptiveVBox.startOriginalRecurrence)
                .withStartRecurrence(editDescriptiveVBox.startRecurrenceProperty.get())
                .withVComponentEdited(vComponent)
                .withVComponentOriginal(vComponentOriginalCopy);
        return reviser.revise();
    }
}
