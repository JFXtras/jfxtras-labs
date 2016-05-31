package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import jfxtras.labs.icalendarfx.components.VJournal;

public class RecurrenceRuleVJournalVBox extends RecurrenceRuleVBox<VJournal>
{
    public RecurrenceRuleVJournalVBox( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("RecurrenceRule.fxml"), this);
    }
}
