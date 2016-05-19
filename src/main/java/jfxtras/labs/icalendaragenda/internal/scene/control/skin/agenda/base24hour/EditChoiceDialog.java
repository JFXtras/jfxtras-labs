package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda.StartEndRange;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper.ChangeDialogOption;


/**
 * Dialog to allow user to choice between ChangeDialogOption (ONE, THIS_AND_FUTURE, and ALL) edit options.
 * The constructor requires a map of matching ChangeDialogOptions and a string of date/time range affected.
 * 
 * @author David Bal
 *
 */
public class EditChoiceDialog extends AppointmentChangeDialog
{
    /**
     * Callback to produce an edit choice dialog based on the options in the input argument choices.
     * Usually all or some of ONE, THIS_AND_FUTURE, and ALL.
     */
    final static public Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> EDIT_DIALOG_CALLBACK = (choices) ->
    {
        EditChoiceDialog dialog = new EditChoiceDialog(choices, Settings.resources);                
        Optional<ChangeDialogOption> result = dialog.showAndWait();
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    };
    /**
     * 
     * @param choicesAndDateRanges - list of ChangeDialogOption representing the date/time range to be affected
     * @param resources
     */
    public EditChoiceDialog(Map<ChangeDialogOption, StartEndRange> choiceList, ResourceBundle resources)
    {
        super(choiceList, resources);
        getDialogPane().setId("editChoiceDialog");
        setTitle(resources.getString("dialog.edit.title"));
//        setHeaderText(resources.getString("dialog.edit.header"));
    }
}
