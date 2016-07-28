package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;


/**
 * Dialog that adds a CheckBox for selecting if special recurrences (VComponents with RECURRENCEID) should
 * be changed (when selected) or ignored (when not selected).
 * 
 * @author David Bal
 *
 */
public class EditWithRecurrencesChoiceDialog extends EditChoiceDialog
{
    /**
     * Callback to produce an edit choice dialog based on the options in the input argument choices.
     * Usually all or some of ONE, THIS_AND_FUTURE, and ALL.
     */
    final static public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> EDIT_DIALOG_CALLBACK = (choices) ->
    {
        EditWithRecurrencesChoiceDialog dialog = new EditWithRecurrencesChoiceDialog(choices, Settings.resources);                
        Optional<ChangeDialogOption> result = dialog.showAndWait();
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    };
    /**
     * 
     * @param choiceList list of ChangeDialogOption representing the date/time range to be affected
     * @param resources ResourceBundle for internationalization
     */
    public EditWithRecurrencesChoiceDialog(Map<ChangeDialogOption, Pair<Temporal,Temporal>> choiceList, ResourceBundle resources)
    {
        super(choiceList, resources);
        getDialogPane().setId("editWithRecurrencesChoiceDialog");
        setTitle(resources.getString("dialog.edit.title"));

        CheckBox recurrenceCheckBox = new CheckBox("change special recurrences");
        GridPane g = (GridPane) getDialogPane().getContent();
        g.add(recurrenceCheckBox, 0, 1);
        
        setResultConverter((dialogButton) ->
        {
            ButtonData data = (dialogButton == null) ? null : dialogButton.getButtonData();
            ChangeDialogOption selectedItem = (recurrenceCheckBox.isSelected()) ? comboBox.getSelectionModel().getSelectedItem();
            return data == ButtonData.OK_DONE ? selectedItem : null;
        });
    }
}
