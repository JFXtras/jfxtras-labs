package jfxtras.labs.icalendarfx.components.revisors;

import jfxtras.labs.icalendarfx.components.VJournal;

public class ReviserVJournal extends ReviserDisplayable<ReviserVJournal, VJournal>
{    
    public ReviserVJournal(VJournal component)
    {
        super(component);
    }
    
//    @Override
//    public Collection<VJournal> revise()
//    {
//        Collection<VJournal> revisedVComponents = super.revise();
//        if (getVCalendar() != null)
//        {
//            getVCalendar().getVJournals().remove(getVComponentEdited());
//            getVCalendar().getVJournals().addAll(revisedVComponents);
//        }
//        return revisedVComponents;
//    }
}
