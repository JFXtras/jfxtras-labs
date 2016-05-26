package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VJournal;

public class EditVJournalTabPane extends EditDisplayableTabPane<VJournal>
{
    public EditVJournalTabPane( )
    {
        super();
        setDescriptiveVBox(new DescriptiveVJournalVBox());
        getDescriptiveAnchorPane().getChildren().add(getDescriptiveVBox());
//        this.setId("editVJournalTabPane");
    }
}
