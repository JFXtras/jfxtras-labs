package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities.ChangeDialogOption;

/**
 * Dialog that can be either an edit or a delete choice dialog.
 * Choice options include ONE, THIS_AND_FUTURE and ALL
 * 
 * @author David Bal
 *
 */
public class AppointmentChangeDialog extends Dialog<ChangeDialogOption>
{
    private final ChangeDialogOption initialSelection = ChangeDialogOption.ONE;

    /**
     * 
     * @param choicesAndDateRanges - map of ChangeDialogOption and matching string of the date/time range affected
     * @param resources
     */
    public AppointmentChangeDialog(Map<ChangeDialogOption, String> choicesAndDateRanges, ResourceBundle resources)
    {
        if (! choicesAndDateRanges.containsKey(initialSelection)) throw new RuntimeException("choicesAndDateRanges map must contain: ChangeDialogOption." + initialSelection);
        getDialogPane().getStyleClass().add("choice-dialog");

        // Buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).setId("changeDialogOkButton");
        getDialogPane().lookupButton(ButtonType.CANCEL).setId("changeDialogCancelButton");
        
        // new Label bound to Dialog's contentTextProperty 
        Label label = new Label();
        setContentText(resources.getString("dialog.content"));
        label.textProperty().bind(getDialogPane().contentTextProperty());
        
        // Choices
        List<ChangeDialogOption> choiceList = new ArrayList<>(choicesAndDateRanges.keySet());
        ComboBox<ChangeDialogOption> comboBox = new ComboBox<>();       
        comboBox.setId("changeDialogComboBox");
        comboBox.getItems().addAll(choiceList);
        comboBox.getSelectionModel().select(initialSelection);

        // grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.add(comboBox, 1, 0);
        grid.add(label, 0, 0);
        getDialogPane().setContent(grid);
        
        // Match header with range string
        setHeaderText(initialSelection + ":" + System.lineSeparator() + choicesAndDateRanges.get(initialSelection)); // initial header text
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
        {
            setHeaderText(newSelection + ":" + System.lineSeparator() + choicesAndDateRanges.get(newSelection));
        });
        
        setResultConverter((dialogButton) -> {
            ButtonData data = (dialogButton == null) ? null : dialogButton.getButtonData();
            return data == ButtonData.OK_DONE ? comboBox.getSelectionModel().getSelectedItem() : null;
        });
    }
}
