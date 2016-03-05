package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.util.Callback;
import jfxtras.labs.icalendar.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.icalendar.VComponent.StartEndRange;


/**
 * Dialog to allow user to choice between ChangeDialogOption (ONE, THIS_AND_FUTURE, and ALL) delete options.
 * The constructor requires a map of matching ChangeDialogOptions and a string of date/time range affected.
 * 
 * @author David Bal
 *
 */
public class DeleteChoiceDialog extends AppointmentChangeDialog
{
    /**
     * Callback to produce an edit choice dialog based on the options in the input argument choices.
     * Usually all or some of ONE, THIS_AND_FUTURE, and ALL.
     */
    final static public Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> DELETE_DIALOG_CALLBACK = (choices) ->
    {
        DeleteChoiceDialog dialog = new DeleteChoiceDialog(choices, Settings.resources);                
        Optional<ChangeDialogOption> result = dialog.showAndWait();
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    };
    
    /**
     * 
     * @param choiceList - list of ChangeDialogOption representing the date/time range to be affected
     * @param resources
     */
    public DeleteChoiceDialog(Map<ChangeDialogOption, StartEndRange> choiceList, ResourceBundle resources)
    {
        super(choiceList, resources);
        getDialogPane().setId("deleteChoiceDialog");
        setTitle(resources.getString("dialog.delete.title"));
//        setContentText(resources.getString("dialog.delete.content"));
//        setHeaderText(resources.getString("dialog.delete.header"));
    }
}
