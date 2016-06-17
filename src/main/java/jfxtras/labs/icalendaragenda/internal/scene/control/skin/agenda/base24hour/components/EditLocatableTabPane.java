package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.util.Collection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;

public abstract class EditLocatableTabPane<T extends VComponentLocatable<T>> extends EditDisplayableTabPane<T, DescriptiveLocatableVBox<T>>
{
    public EditLocatableTabPane( )
    {
        super();
    }
        
    @Override
    @FXML void handleSaveButton()
    {
        if (vComponent.isValid())
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
            Collection<T> newVComponents = callRevisor();
            if (newVComponents != null)
            {
                vComponents.remove(vComponent);
                vComponents.addAll(newVComponents);
                isFinished.set(true);
            }
        } else
        {
            System.out.println("vComponent not valid:");
            if (recurrenceRuleVBox.dayOfWeekList.isEmpty())
            {
                canNotHaveZeroDaysOfWeek();
            } else
            {
                throw new RuntimeException("Unhandled component error" + System.lineSeparator() + vComponent.errors());
            }
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
