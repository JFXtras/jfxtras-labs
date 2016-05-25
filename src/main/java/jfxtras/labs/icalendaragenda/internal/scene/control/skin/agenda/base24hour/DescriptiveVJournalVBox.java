package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VJournal;

public class DescriptiveVJournalVBox extends DescriptiveVBox<VJournal>
{
    public DescriptiveVJournalVBox()
    {
        super();
        getEndLabel().setVisible(false);
        getEndTextField().setVisible(false);
        setEndLabel(null);
        setEndTextField(null);
    }
}
