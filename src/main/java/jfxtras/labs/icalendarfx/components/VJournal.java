package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public class VJournal extends VComponentDisplayableBase<VJournal> implements VJournalInt<VJournal>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VJOURNAL;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VJournal() { }
    
    public VJournal(String contentLines)
    {
        super(contentLines);
    }


    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObservableList<Description> getDescriptions()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDescriptions(ObservableList<Description> properties)
    {
        // TODO Auto-generated method stub
        
    }
}
