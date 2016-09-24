package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * TabPane for editing descriptive properties and a {@link RecurrenceRule} for a {@link VEvent}.
 * 
 * @author David Bal
 */
public class EditVEventTabPane extends EditLocatableTabPane<VEvent>
{
    public EditVEventTabPane( )
    {
        super();
        editDescriptiveVBox = new EditDescriptiveVEventVBox();
        descriptiveAnchorPane.getChildren().add(0, editDescriptiveVBox);
        recurrenceRuleVBox = new EditRecurrenceRuleVEventVBox();
        recurrenceRuleAnchorPane.getChildren().add(0, recurrenceRuleVBox);
    }
    
    @Override
    public void setupData(
            VEvent vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        vComponentOriginalCopy = new VEvent(vComponent);
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
}
