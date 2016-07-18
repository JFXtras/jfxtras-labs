package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;

/** Makes new stage for popup window to edit VEvent with Agenda.Appointment recurrences
 * 
 * @author David Bal
 */
public class EditVEventScene extends EditVComponentScene
{
    public EditVEventScene()
    {
        super(new EditVEventTabPane());
    }

    /**
     * @param vComponent - component to edit
     * @param vComponents - collection of components that vComponent is a member
     * @param startRecurrence - start of selected recurrence
     * @param endRecurrence - end of selected recurrence
     * @param categories - available category names
     */
    public EditVEventScene(
            VEvent vComponent,
            List<VEvent> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        this();
        setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
    }
    
    EditVEventScene setupData(
            VEvent vComponent,
            List<VEvent> vComponents,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        ((EditVEventTabPane) getRoot()).setupData(vComponent, vComponents, startRecurrence, endRecurrence, categories);
        return this;
    }
}
 
