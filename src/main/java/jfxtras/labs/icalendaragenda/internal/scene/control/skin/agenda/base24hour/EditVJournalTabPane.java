package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VJournal;

public class EditVJournalTabPane extends EditDisplayableTabPane<VJournal>
{
    public EditVJournalTabPane( )
    {
        super();
        editDescriptiveVBox = new DescriptiveVJournalVBox();
        getDescriptiveAnchorPane().getChildren().add(editDescriptiveVBox);
        isFinished.bind(editDescriptiveVBox.isFinished);
    }
}
