package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities.ChangeDialogOption;

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
     * 
     * @param choicesAndDateRanges - map of ChangeDialogOption and matching string of the date/time range affected
     * @param resources
     */
    public EditChoiceDialog(Map<ChangeDialogOption, String> choicesAndDateRanges, ResourceBundle resources)
    {
        super(choicesAndDateRanges, resources);
        getDialogPane().setId("editChoiceDialog");
        setTitle(resources.getString("dialog.edit.title"));
//        setHeaderText(resources.getString("dialog.edit.header"));
    }
}
