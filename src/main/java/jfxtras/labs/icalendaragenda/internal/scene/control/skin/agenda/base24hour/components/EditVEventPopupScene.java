package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VEvent;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVEventPopupScene extends CreateEditComponentPopupScene
{
    public EditVEventPopupScene()
    {
        super(new EditVEventTabPane());
    }

    public EditVEventPopupScene(VEvent vComponent, ObservableList<VEvent> vEvents)
    {
        super(new EditVEventTabPane());
    }

    public EditVEventPopupScene(
            VEvent vComponent,
            List<VEvent> vEvents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super(new EditVEventTabPane());
        ((EditVEventTabPane) getRoot()).setupData(vComponent, vEvents, startRecurrence, endRecurrence, categories);
    }
}
 
