package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities.ChangeDialogOption;

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
     * 
     * @param choicesAndDateRanges - map of ChangeDialogOption and matching string of the date/time range affected
     * @param resources
     */
    public DeleteChoiceDialog(Map<ChangeDialogOption, String> choicesAndDateRanges, ResourceBundle resources)
    {
        super(choicesAndDateRanges, resources);
        getDialogPane().setId("deleteChoiceDialog");
        setTitle(resources.getString("dialog.delete.title"));
//        setContentText(resources.getString("dialog.delete.content"));
//        setHeaderText(resources.getString("dialog.delete.header"));
    }
}
