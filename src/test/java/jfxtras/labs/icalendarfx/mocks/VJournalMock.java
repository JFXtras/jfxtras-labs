package jfxtras.labs.icalendarfx.mocks;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.relationship.Contact;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;

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
    public String getClassification()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public StringProperty classificationProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setClassification(String classification)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Contact getContact()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Contact> contactProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setContact(Contact contact)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ZonedDateTime getDateTimeCreated()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<ZonedDateTime> dateTimeCreatedProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeCreated(ZonedDateTime dtCreated)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ExDate getExDate()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<ExDate> exDateProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setExDate(ExDate exDate)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Temporal getDateTimeRecurrence()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Temporal> dateTimeRecurrenceProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeRecurrence(Temporal recurrence)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public RelatedTo getRelatedTo()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<RelatedTo> relatedToProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setRelatedTo(RelatedTo relatedTo)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getSequence()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IntegerProperty sequenceProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setSequence(int value)
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
