package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VJournal;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVJournalPopupScene extends EditComponentPopupScene
{
    public EditVJournalPopupScene()
    {
        super(new EditVJournalTabPane());
    }

//    public EditVJournalPopupStage(VEvent vComponent, ObservableList<VEvent> vEvents)
//    {
//        super(new EditVEventTabPane());
//    }

    public EditVJournalPopupScene(
            VJournal vComponent,
            List<VJournal> vJournals,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super(new EditVJournalTabPane());
        ((EditVJournalTabPane) getRoot()).setupData(vComponent, vJournals, startRecurrence, endRecurrence, categories);
    }
}
 
