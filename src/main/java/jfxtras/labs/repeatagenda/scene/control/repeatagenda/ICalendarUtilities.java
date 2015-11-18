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
     * If repeat criteria has changed display this alert to find out how to apply changes (one, all or future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static RepeatChange repeatChangeDialog(RepeatChange...choiceList)
    {
        ResourceBundle resources = Settings.resources;
        List<RepeatChange> choices;
        if (choiceList == null || choiceList.length == 0)
        { // use default choices
            choices = new ArrayList<RepeatChange>();
            choices.add(RepeatChange.ONE);
            choices.add(RepeatChange.ALL);
            choices.add(RepeatChange.FUTURE);
        } else { // use inputed choices
            choices = new ArrayList<RepeatChange>(Arrays.asList(choiceList));
        }
               
        ChoiceDialog<RepeatChange> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));

        Optional<RepeatChange> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : RepeatChange.CANCEL;
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
     * ALL: Change all appointments with repeat rule
     * FUTURE: Change future appointments with repeat rule
     * @author David Bal
     *
     */
    public enum RepeatChange {
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



}
