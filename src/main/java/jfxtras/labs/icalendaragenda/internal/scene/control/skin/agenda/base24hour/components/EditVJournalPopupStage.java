package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVJournalPopupStage extends EditComponentPopupStage<VJournal>
{
    public EditVJournalPopupStage()
    {
        super(new EditVJournalTabPane());
    }

//    public EditVJournalPopupStage(VEvent vComponent, ObservableList<VEvent> vEvents)
//    {
//        super(new EditVEventTabPane());
//    }

    public EditVJournalPopupStage(
            VJournal vComponent,
            List<VJournal> vJournals,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<AppointmentGroup> appointmentGroups)
    {
        super(new EditVJournalTabPane());
        getEditDisplayableTabPane().setupData(vComponent, vJournals, startRecurrence, endRecurrence, appointmentGroups);
    }
}
 
