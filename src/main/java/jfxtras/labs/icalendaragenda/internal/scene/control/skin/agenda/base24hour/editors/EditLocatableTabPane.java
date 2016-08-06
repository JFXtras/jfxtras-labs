package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.editors.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VComponentLocatable}.
 * 
 * @author David Bal
 * 
 * @param <T> subclass of {@link VComponentLocatable}
 * @param <U> subclass of {@link EditDescriptiveVBox} associated with the subclass of {@link VComponentLocatable}
 */
public abstract class EditLocatableTabPane<T extends VComponentLocatable<T>> extends EditDisplayableTabPane<T, EditDescriptiveLocatableVBox<T>>
{
    public EditLocatableTabPane( )
    {
        super();
    }
        
    @Override
    @FXML void handleSaveButton()
    {
        super.handleSaveButton();
        if (vComponent.getRecurrenceRule() != null)
        {
            RecurrenceRule2 rrule = vComponent.getRecurrenceRule().getValue();
            if (rrule.getFrequency().getValue() == FrequencyType.WEEKLY)
            {
                if (recurrenceRuleVBox.dayOfWeekList.isEmpty())
                {
                    canNotHaveZeroDaysOfWeek();
                    return; // skip other operations, allow user to make changes and try again
                }
            }
        }
        Object[] params = new Object[] {
                EditChoiceDialog.EDIT_DIALOG_CALLBACK,
                editDescriptiveVBox.endNewRecurrence,
                editDescriptiveVBox.startOriginalRecurrence,
                editDescriptiveVBox.startRecurrenceProperty.get(),
                vComponents,
                vComponent,
                vComponentOriginalCopy
        };
//        boolean result = SimpleRevisorFactory.newReviser(vComponent, params).revise();
        List<T> result = (List<T>) SimpleRevisorFactory.newReviser(vComponent, params).revise();
        newVComponentsProperty().set(result);
//        isFinished.set(result);
    }
    
    @Override
    void removeEmptyProperties()
    {
        super.removeEmptyProperties();
        if (editDescriptiveVBox.descriptionTextArea.getText().isEmpty())
        {
            vComponent.setDescription((Description) null); 
        }
        if (editDescriptiveVBox.locationTextField.getText().isEmpty())
        {
            vComponent.setLocation((Location) null); 
        }
    }
    
    // Displays an alert notifying at least one day of week must be present for weekly frequency
    private static void canNotHaveZeroDaysOfWeek()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Please select at least one day of the week.");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("zero_day_of_week_alert");
        alert.getDialogPane().lookupButton(buttonTypeOk).setId("zero_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
}
