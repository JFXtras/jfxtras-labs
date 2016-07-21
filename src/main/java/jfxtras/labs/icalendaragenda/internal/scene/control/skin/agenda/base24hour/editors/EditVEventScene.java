package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * Scene for editing descriptive properties and a {@link RecurrenceRule} in a {@link VEvent}.
 * A {@link EditVEventTabPane} is set as the root node of the scene graph<br>
 * 
 * @author David Bal
 */
public class EditVEventScene extends EditDisplayableScene
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
 
