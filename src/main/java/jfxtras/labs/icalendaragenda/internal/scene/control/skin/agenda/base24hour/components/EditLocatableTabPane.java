package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.Collection;

import javafx.fxml.FXML;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper;
import jfxtras.labs.icalendarfx.components.VComponentLocatableBase;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;

public abstract class EditLocatableTabPane<T extends VComponentLocatableBase<?>> extends EditDisplayableTabPane<T, DescriptiveLocatableVBox<T>>
{
    public EditLocatableTabPane( )
    {
        super();
    }
    
    @Override
    @FXML void handleSaveButton()
    {
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponent.setDescription((Description) null); 
        }
        if (editDescriptiveVBox.locationTextField.getText().isEmpty())
        {
            vComponent.setLocation((Location) null); 
        }
        super.handleSaveButton();

        Collection<T> newVComponents = ReviseComponentHelper.handleEdit(
                vComponentOriginalCopy,
                vComponent,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                editDescriptiveVBox.endNewRecurrence,
//                editDescriptiveVBox.shiftAmount,
                EditChoiceDialog.EDIT_DIALOG_CALLBACK
                );
        if (newVComponents != null)
        {
            vComponents.remove(vComponent);
            vComponents.addAll(newVComponents);
            // vComponentsTemp ensures listeners attached to vComponents to make recurrences are called only once
//            List<T> vComponentsTemp = new ArrayList<>(vComponents);
//            vComponentsTemp.remove(vComponent);
//            vComponentsTemp.addAll(newVComponents);
//            vComponents.clear();
//            vComponents.addAll(vComponentsTemp);
            isFinished.set(true);
        }
    }
}
