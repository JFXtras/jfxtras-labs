package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

public class ICalendarUtilities {

    /**
     * This alert inquires how to apply changes (one, all or this-and-future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static ChangeDialogOptions repeatChangeDialog(ChangeDialogOptions...choiceList)
    {
        ResourceBundle resources = Settings.resources;
        List<ChangeDialogOptions> choices;
        if (choiceList == null || choiceList.length == 0)
        { // use default choices
            choices = new ArrayList<ChangeDialogOptions>();
            choices.add(ChangeDialogOptions.ONE);
            choices.add(ChangeDialogOptions.ALL);
            choices.add(ChangeDialogOptions.FUTURE);
        } else { // use inputed choices
            choices = new ArrayList<ChangeDialogOptions>(Arrays.asList(choiceList));
        }
               
        ChoiceDialog<ChangeDialogOptions> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));

        Optional<ChangeDialogOptions> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : ChangeDialogOptions.CANCEL;
    }

    /**
     * Alert to confirm delete appointments
     * 
     * @param resources
     * @param appointmentQuantity
     * @return
     */
    private static Boolean confirmDelete(String appointmentQuantity)
    {
        ResourceBundle resources = Settings.resources;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert.repeat.delete.title"));
        alert.setContentText(resources.getString("alert.repeat.delete.content"));
        alert.setHeaderText(appointmentQuantity + " " + resources.getString("alert.repeat.delete.header"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    /**
     * Options available when changing a repeatable appointment
     * ONE: Change only selected appointment
     * ALL: Change all appointments
     * FUTURE: Change this and future appointments
     * @author David Bal
     *
     */
    public enum ChangeDialogOptions
    {
        ONE, ALL, FUTURE, CANCEL;

        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
    public enum WindowCloseType
    {
        X, CANCEL, CLOSE_WITH_CHANGE, CLOSE_WITHOUT_CHANGE
    }

    public enum RRuleType
    {
        INDIVIDUAL
      , WITH_EXISTING_REPEAT
      , WITH_NEW_REPEAT
      , HAD_REPEAT_BECOMING_INDIVIDUAL
    }

}
