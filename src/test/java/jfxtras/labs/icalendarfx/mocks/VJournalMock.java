package jfxtras.labs.icalendarfx.mocks;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.stream.Stream;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;

public class VJournalMock extends VJournal<VJournalMock, InstanceMock>
{
    /*
     * CONSTRUCTORS
     */
    public VJournalMock() { }
    
    public VJournalMock(String contentLines)
    {
        super(contentLines);
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

    @Override
    public String getStatus()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public StringProperty statusProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setStatus(String classification)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<Temporal> streamLimitedByRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Temporal getStartRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setStartRange(Temporal start)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Temporal getEndRange()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setEndRange(Temporal end)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<InstanceMock> makeInstances(Temporal start, Temporal end)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Collection<InstanceMock> makeInstances()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Collection<InstanceMock> instances()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }
}
